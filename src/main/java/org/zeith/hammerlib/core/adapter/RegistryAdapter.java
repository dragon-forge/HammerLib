package org.zeith.hammerlib.core.adapter;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.blocks.IItemGroupBlock;
import org.zeith.hammerlib.api.blocks.IItemPropertySupplier;
import org.zeith.hammerlib.api.blocks.INoItemBlock;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
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

	private static final Map<Class<?>, List<Block>> blocks = new HashMap<>();

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
				.stream(source.getMethods())
				.filter(m -> m.getAnnotation(SimplyRegister.class) != null && m.getParameterCount() == 1 && Consumer.class.isAssignableFrom(m.getParameterTypes()[0]) && ReflectionUtil.doesParameterTypeArgsMatch(m.getParameters()[0], registry.getRegistrySuperType()))
				.forEach(method ->
				{
					if(Modifier.isStatic(method.getModifiers()))
						try
						{
							OnlyIf onlyIf = method.getAnnotation(OnlyIf.class);
							if(!checkCondition(onlyIf, source.toString(), registry.getRegistrySuperType().getSimpleName()))
								return;

							method.setAccessible(true);
							method.invoke(null, grabber);
						} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							e.printStackTrace();
						}
				});

		Arrays
				.stream(source.getFields())
				.filter(f -> registry.getRegistrySuperType().isAssignableFrom(f.getType()))
				.forEach(field ->
				{
					if(Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
						try
						{
							field.setAccessible(true);
							RegistryName name = field.getAnnotation(RegistryName.class);
							OnlyIf onlyIf = field.getAnnotation(OnlyIf.class);
							if(!checkCondition(onlyIf, source.toString(), registry.getRegistrySuperType().getSimpleName()))
								return;

							T t = registry.getRegistrySuperType().cast(field.get(null));
							if(name != null) t.setRegistryName(new ResourceLocation(modid, name.value()));
							grabber.accept(t);
						} catch(IllegalArgumentException | IllegalAccessException e)
						{
							e.printStackTrace();
						}
				});

		return registry.getValues().size() - prevSize;
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
							if(!checkCondition(onlyIf, source.toString(), "Setup")) return;
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

	public static boolean checkCondition(OnlyIf onlyIf, String source, String type)
	{
		boolean add = true;

		if(onlyIf != null)
		{
			String member = onlyIf.member();
			try
			{
				int i;
				Class<?> c = onlyIf.owner();
				try
				{
					Field f = c.getDeclaredField(member);
					f.setAccessible(true);
					add = f.getBoolean(null);
				} catch(NoSuchFieldException e)
				{
					Method m = c.getDeclaredMethod(member);
					m.setAccessible(true);
					add = (Boolean) m.invoke(null);
				} catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
			{
				HammerLib.LOG.warn("Failed to parse @OnlyIf({}) in {}! {} will" + (onlyIf.invert() ? " not be registered due to inversion." : " be registered anyway."), JSONObject.quote(member), source, type);
				e.printStackTrace();
			}

			if(onlyIf.invert()) add = !add;
		}

		return add;
	}
}