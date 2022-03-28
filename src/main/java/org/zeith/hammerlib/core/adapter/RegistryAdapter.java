package org.zeith.hammerlib.core.adapter;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.*;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.blocks.IItemGroupBlock;
import org.zeith.hammerlib.api.blocks.IItemPropertySupplier;
import org.zeith.hammerlib.api.blocks.INoItemBlock;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RegistryAdapter
{
	public static <T extends IForgeRegistryEntry<T>> Consumer<T> createRegisterer(IForgeRegistry<T> registry)
	{
		return entry ->
		{
			IRegisterListener l = Cast.cast(entry, IRegisterListener.class);
			if(l != null)
				l.onPreRegistered();
			registry.register(entry);
			if(l != null)
				l.onPostRegistered();
		};
	}

	private static final Map<Class<?>, List<Block>> blocks = new ConcurrentHashMap<>();

	/**
	 * Registers all static fields (from source) with the matching registry type, and methods that accept Consumer<T>
	 */
	public static <T extends IForgeRegistryEntry<T>> int register(IForgeRegistry<T> registry, Class<?> source, String modid)
	{
		List<Block> blockList = blocks.computeIfAbsent(source, s -> new ArrayList<>());

		Consumer<T> grabber = createRegisterer(registry).andThen(handler ->
		{
			if(handler instanceof Block)
				blockList.add((Block) handler);
		});

		if(Item.class.equals(registry.getRegistrySuperType())) for(Block blk : blockList)
		{
			if(blk instanceof INoItemBlock) continue;
			BlockItem item;
			IItemPropertySupplier gen = Cast.cast(blk, IItemPropertySupplier.class);
			if(blk instanceof ICustomBlockItem) item = ((ICustomBlockItem) blk).createBlockItem();
			else
			{
				Item.Properties props = gen != null ? gen.createItemProperties(new Item.Properties()) : new Item.Properties();
				if(blk instanceof IItemGroupBlock) props = props.tab(((IItemGroupBlock) blk).getItemGroup());
				item = new BlockItem(blk, props);
			}
			if(item.getRegistryName() == null) item.setRegistryName(blk.getRegistryName());
			ForgeRegistries.ITEMS.register(item);
		}

		int prevSize = registry.getValues().size();

		Arrays
				.stream(source.getDeclaredMethods())
				.filter(m -> m.getAnnotation(SimplyRegister.class) != null && m.getParameterCount() == 1 && Consumer.class.isAssignableFrom(m.getParameterTypes()[0]) && ReflectionUtil.doesParameterTypeArgsMatch(m.getParameters()[0], registry.getRegistrySuperType()))
				.forEach(method ->
				{
					if(Modifier.isStatic(method.getModifiers()))
						try
						{
							OnlyIf onlyIf = method.getAnnotation(OnlyIf.class);
							if(!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), registry.getRegistrySuperType().getSimpleName(), null))
								return;

							method.setAccessible(true);
							method.invoke(null, grabber);
						} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							e.printStackTrace();
						}
				});

		Arrays
				.stream(source.getDeclaredFields())
				.filter(f -> registry.getRegistrySuperType().isAssignableFrom(f.getType()))
				.forEach(field ->
				{
					if(Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
						try
						{
							field.setAccessible(true);
							var name = field.getAnnotation(RegistryName.class);
							var onlyIf = field.getAnnotation(OnlyIf.class);

							var t = registry.getRegistrySuperType().cast(field.get(null));
							var rl = name != null ? new ResourceLocation(modid, name.value()) : null;
							if(rl != null) t.setRegistryName(rl);
							if(!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), registry.getRegistrySuperType().getSimpleName(), t))
							{
								if(t instanceof ForgeRegistryEntry<?> e) try
								{
									Method refl = e.delegate.getClass().getDeclaredMethod("setName", ResourceLocation.class);
									refl.setAccessible(true);
									if(rl != null) refl.invoke(e.delegate, rl);

									refl = e.delegate.getClass().getDeclaredMethod("changeReference", Object.class);
									refl.setAccessible(true);
									if(rl != null) refl.invoke(e.delegate, e);
								} catch(ReflectiveOperationException err)
								{
									HammerLib.LOG.fatal("Forge sucks big pp", err);
								}
								fuckForgeOff(t);
								return;
							}
							grabber.accept(t);
						} catch(IllegalArgumentException | IllegalAccessException e)
						{
							e.printStackTrace();
						}
				});

		return registry.getValues().size() - prevSize;
	}

	public static <T extends IForgeRegistryEntry<T>> Holder.Reference<T> findReference(T t)
	{
		if(t instanceof EntityType i)
			return i.builtInRegistryHolder();
		else if(t instanceof Item i)
			return Cast.cast(i.builtInRegistryHolder());
		else if(t instanceof Block i)
			return Cast.cast(i.builtInRegistryHolder());
		else if(t instanceof GameEvent i)
			return Cast.cast(i.builtInRegistryHolder());
		else if(t instanceof Fluid i)
			return Cast.cast(i.builtInRegistryHolder());
		else
		{
			Field refF = ReflectionUtil.lookupField(t.getClass(), Holder.Reference.class);
			if(refF != null)
			{
				try
				{
					return (Holder.Reference<T>) refF.get(t);
				} catch(ReflectiveOperationException e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private static <T extends IForgeRegistryEntry<T>> void fuckForgeOff(T t)
	{
		if(t instanceof EntityType i)
			i.builtInRegistryHolder().bind(ResourceKey.create(Registry.ENTITY_TYPE_REGISTRY, t.getRegistryName()), i);
		else if(t instanceof Item i)
			i.builtInRegistryHolder().bind(ResourceKey.create(Registry.ITEM_REGISTRY, t.getRegistryName()), i);
		else if(t instanceof Block i)
			i.builtInRegistryHolder().bind(ResourceKey.create(Registry.BLOCK_REGISTRY, t.getRegistryName()), i);
		else if(t instanceof GameEvent i)
			i.builtInRegistryHolder().bind(ResourceKey.create(Registry.GAME_EVENT_REGISTRY, t.getRegistryName()), i);
		else if(t instanceof Fluid i)
			i.builtInRegistryHolder().bind(ResourceKey.create(Registry.FLUID_REGISTRY, t.getRegistryName()), i);
		else
		{
			Field refF = ReflectionUtil.lookupField(t.getClass(), Holder.Reference.class);
			if(refF != null)
			{
				try
				{
					var o = (Holder.Reference<T>) refF.get(t);
					IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(t.getRegistryType());
					o.bind(ResourceKey.create(registry.getRegistryKey(), t.getRegistryName()), t);
				} catch(ReflectiveOperationException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void setup(FMLCommonSetupEvent event, Class<?> source, String memberName)
	{
		String methodName = memberName.substring(0, memberName.indexOf('('));

		Arrays
				.stream(source.getDeclaredMethods())
				.filter(m -> m.getAnnotation(Setup.class) != null && m.getName().equals(methodName))
				.forEach(method ->
				{
					if(Modifier.isStatic(method.getModifiers()))
						try
						{
							OnlyIf onlyIf = method.getAnnotation(OnlyIf.class);
							if(!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), "Setup", null)) return;
							method.setAccessible(true);
							if(method.getParameterCount() == 0)
								method.invoke(null);
							else if(method.getParameterCount() == 1 && method.getParameterTypes()[0] == FMLCommonSetupEvent.class)
								method.invoke(null, event);
						} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							RuntimeException re = null;
							if(e instanceof InvocationTargetException && e.getCause() instanceof RuntimeException)
								re = (RuntimeException) e.getCause();
							if(e instanceof RuntimeException)
								re = (RuntimeException) e;
							if(re != null)
								throw re;
							e.printStackTrace();
						}
				});
	}

	public static void clientSetup(FMLClientSetupEvent event, Class<?> source, String memberName)
	{
		String methodName = memberName.substring(0, memberName.indexOf('('));

		Arrays
				.stream(source.getDeclaredMethods())
				.filter(m -> m.getAnnotation(ClientSetup.class) != null && m.getName().equals(methodName))
				.forEach(method ->
				{
					if(Modifier.isStatic(method.getModifiers()))
						try
						{
							OnlyIf onlyIf = method.getAnnotation(OnlyIf.class);
							if(!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), "ClientSetup", null)) return;
							method.setAccessible(true);
							if(method.getParameterCount() == 0)
								method.invoke(null);
							else if(method.getParameterCount() == 1 && method.getParameterTypes()[0] == FMLClientSetupEvent.class)
								method.invoke(null, event);
						} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							RuntimeException re = null;
							if(e instanceof InvocationTargetException && e.getCause() instanceof RuntimeException)
								re = (RuntimeException) e.getCause();
							if(e instanceof RuntimeException)
								re = (RuntimeException) e;
							if(re != null)
								throw re;
							e.printStackTrace();
						}
				});
	}
}