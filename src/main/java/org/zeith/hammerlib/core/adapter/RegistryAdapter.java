package org.zeith.hammerlib.core.adapter;

import lombok.var;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.api.blocks.*;
import org.zeith.hammerlib.api.fml.*;
import org.zeith.hammerlib.util.java.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class RegistryAdapter
{
	public static <T extends IForgeRegistryEntry<T>> BiConsumer<ResourceLocation, T> createRegisterer(IForgeRegistry<T> registry, String prefix)
	{
		return (name, entry) ->
		{
			name = new ResourceLocation(name.getNamespace(), prefix + name.getPath());
			IRegisterListener l = Cast.cast(entry, IRegisterListener.class);
			if(l != null)
				l.onPreRegistered();
			registry.register(entry.setRegistryName(name));
			if(l != null)
				l.onPostRegistered();
		};
	}
	
	private static final Map<Class<?>, List<Tuple<Block, ResourceLocation>>> blocks = new ConcurrentHashMap<>();
	
	/**
	 * Registers all static fields (from source) with the matching registry type, and methods that accept Consumer<T>
	 */
	public static <T extends IForgeRegistryEntry<T>> int register(RegistryEvent.Register<T> event, Class<?> source, String modid, String prefix)
	{
		var registry = event.getRegistry();
		var superType = registry.getRegistrySuperType();
		
		if(superType == null)
		{
			// Unknown registry.
			return 0;
		}
		
		List<Tuple<Block, ResourceLocation>> blockList = blocks.computeIfAbsent(source, s -> new ArrayList<>());
		
		BiConsumer<ResourceLocation, T> grabber = createRegisterer(registry, prefix).andThen((key, handler) ->
		{
			if(handler instanceof Block)
				blockList.add(new Tuple<>((Block) handler, key));
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
				if(blk instanceof IItemGroupBlock) props = props.tab(((IItemGroupBlock) blk).getItemGroup());
				item = new BlockItem(blk, props);
			}
			grabber.accept(e.getB(), Cast.cast(item));
		}
		
		int prevSize = registry.getValues().size();
		
		
		// ICustomRegistrar hook!
		Arrays
				.stream(source.getDeclaredFields())
				.filter(f -> ICustomRegistrar.class.isAssignableFrom(f.getType()))
				.forEach(field ->
				{
					if(Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
						try
						{
							field.setAccessible(true);
							var name = field.getAnnotation(RegistryName.class);
							var rl = new ResourceLocation(modid, prefix + name.value());
							
							var val = field.get(null);
							
							var onlyIf = field.getAnnotation(OnlyIf.class); // Bring back OnlyIf, for registries that are non-intrusive. (Mostly, for custom registry types)
							if(OnlyIfAdapter.checkCondition(onlyIf, source.toString(), superType != null ? superType.getSimpleName() : field.getType().getSimpleName(), val))
							{
								if(val instanceof ICustomRegistrar)
									((ICustomRegistrar) val).performRegister(event, rl);
							}
						} catch(IllegalArgumentException | IllegalAccessException e)
						{
							LogManager.getLogger(modid + "/" + source.getSimpleName()).error("Failed to register field {}", field.getName(), e);
						}
				});
		
		
		Arrays
				.stream(source.getDeclaredMethods())
				.filter(m -> m.getAnnotation(SimplyRegister.class) != null && m.getParameterCount() == 1 && BiConsumer.class.isAssignableFrom(m.getParameterTypes()[0]) && ReflectionUtil.doesParameterTypeArgsMatch(m.getParameters()[0], ResourceLocation.class, superType))
				.forEach(method ->
				{
					final String prefix2 = Optional.ofNullable(method.getAnnotation(SimplyRegister.class)).map(SimplyRegister::prefix).orElse("");
					
					if(Modifier.isStatic(method.getModifiers()))
						try
						{
							method.setAccessible(true);
							
							BiConsumer<ResourceLocation, T> grabber2 = (id, obj) ->
							{
								id = new ResourceLocation(id.getNamespace(), prefix2 + id.getPath());
								grabber.accept(id, obj);
							};
							
							method.invoke(null, grabber2);
						} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							e.printStackTrace();
						}
				});
		
		Arrays
				.stream(source.getDeclaredFields())
				.filter(f -> superType.isAssignableFrom(f.getType())
							 && !ICustomRegistrar.class.isAssignableFrom(f.getType())) // Custom registrars have been called by now.
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