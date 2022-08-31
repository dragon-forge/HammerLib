package org.zeith.hammerlib.core.adapter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.blocks.ICreativeTabBlock;
import org.zeith.hammerlib.api.blocks.IItemPropertySupplier;
import org.zeith.hammerlib.api.blocks.INoItemBlock;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RegistryAdapter
{
	public static <T> BiConsumer<ResourceLocation, T> createRegisterer(IForgeRegistry<T> registry)
	{
		return (name, entry) ->
		{
			IRegisterListener l = Cast.cast(entry, IRegisterListener.class);
			if(l != null)
				l.onPreRegistered();
			registry.register(name, entry);
			if(l != null)
				l.onPostRegistered();
		};
	}
	
	private static final Map<Class<?>, List<Tuple<Block, ResourceLocation>>> blocks = new ConcurrentHashMap<>();
	
	/**
	 * Registers all static fields (from source) with the matching registry type, and methods that accept Consumer<T>
	 */
	public static <T> int register(IForgeRegistry<T> registry, Class<?> source, String modid)
	{
		var superType = RegistryMapping.getSuperType(registry);
		
		if(superType == null)
		{
			// Unknown registry.
			return 0;
		}
		
		List<Tuple<Block, ResourceLocation>> blockList = blocks.computeIfAbsent(source, s -> new ArrayList<>());
		
		BiConsumer<ResourceLocation, T> grabber = createRegisterer(registry).andThen((key, handler) ->
		{
			if(handler instanceof Block b)
				blockList.add(new Tuple<>(b, key));
		});
		
		if(Item.class.equals(superType)) for(Tuple<Block, ResourceLocation> e : blockList)
		{
			Block blk = e.getA();
			if(blk instanceof INoItemBlock) continue;
			BlockItem item;
			IItemPropertySupplier gen = Cast.cast(blk, IItemPropertySupplier.class);
			if(blk instanceof ICustomBlockItem) item = ((ICustomBlockItem) blk).createBlockItem();
			else
			{
				Item.Properties props = gen != null ? gen.createItemProperties(new Item.Properties()) : new Item.Properties();
				if(blk instanceof ICreativeTabBlock) props = props.tab(((ICreativeTabBlock) blk).getCreativeTab());
				item = new BlockItem(blk, props);
			}
			grabber.accept(e.getB(), Cast.cast(item));
		}
		
		int prevSize = registry.getValues().size();
		
		Arrays
				.stream(source.getDeclaredMethods())
				.filter(m -> m.getAnnotation(SimplyRegister.class) != null && m.getParameterCount() == 1 && BiConsumer.class.isAssignableFrom(m.getParameterTypes()[0]) && ReflectionUtil.doesParameterTypeArgsMatch(m.getParameters()[0], ResourceLocation.class, superType))
				.forEach(method ->
				{
					if(Modifier.isStatic(method.getModifiers()))
						try
						{
							method.setAccessible(true);
							method.invoke(null, grabber);
						} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							e.printStackTrace();
						}
				});
		
		Arrays
				.stream(source.getDeclaredFields())
				.filter(f -> superType.isAssignableFrom(f.getType()))
				.forEach(field ->
				{
					if(Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
						try
						{
							field.setAccessible(true);
							var name = field.getAnnotation(RegistryName.class);
							
							var t = superType.cast(field.get(null));
							var rl = new ResourceLocation(modid, name.value());
							
							grabber.accept(rl, t);
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