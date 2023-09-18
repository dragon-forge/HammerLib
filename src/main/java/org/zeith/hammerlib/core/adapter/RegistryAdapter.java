package org.zeith.hammerlib.core.adapter;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.annotations.client.*;
import org.zeith.hammerlib.api.blocks.*;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.java.tuples.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class RegistryAdapter
{
	public static <T> Optional<BiConsumer<ResourceLocation, T>> createRegisterer(RegisterEvent event, ResourceKey<Registry<T>> registryType, String prefix)
	{
		if(event.getRegistryKey().equals(registryType))
		{
			Registry<T> reg = event.getVanillaRegistry();
			IForgeRegistry<T> freg = event.getForgeRegistry();
			
			if(prefix == null) prefix = "";
			
			if(freg != null)
				return Optional.of(createRegisterer(freg, prefix));
			else if(reg != null)
				return Optional.of(createRegisterer(reg, prefix));
		}
		
		return Optional.empty();
	}
	
	public static <T> BiConsumer<ResourceLocation, T> createRegisterer(IForgeRegistry<T> registry, String prefix)
	{
		return (name, entry) ->
		{
			name = new ResourceLocation(name.getNamespace(), prefix + name.getPath());
			IRegisterListener l = Cast.cast(entry, IRegisterListener.class);
			if(l != null) l.onPreRegistered(name);
			registry.register(name, entry);
			if(l != null) l.onPostRegistered(name);
		};
	}
	
	public static <T> BiConsumer<ResourceLocation, T> createRegisterer(Registry<T> registry, String prefix)
	{
		return (name, entry) ->
		{
			name = new ResourceLocation(name.getNamespace(), prefix + name.getPath());
			IRegisterListener l = Cast.cast(entry, IRegisterListener.class);
			if(l != null) l.onPreRegistered(name);
			Registry.register(registry, name, entry);
			if(l != null) l.onPostRegistered(name);
		};
	}
	
	private static final Map<Class<?>, List<Tuple2<Block, ResourceLocation>>> blocks = new ConcurrentHashMap<>();
	
	public static int register(RegisterEvent event, Class<?> source, String modid, String prefix)
	{
		IForgeRegistry<?> reg = event.getForgeRegistry();
		if(reg == null)
			reg = RegistryMapping.getRegistryByType(RegistryMapping.getSuperType(event.getRegistryKey()));
		return RegistryAdapter.register(event, reg, source, modid, prefix);
	}
	
	/**
	 * Registers all static fields (from source) with the matching registry type, and methods that accept Consumer<T>
	 */
	public static <T> int register(RegisterEvent event, IForgeRegistry<T> registry, Class<?> source, String modid, String prefix)
	{
		var superType = RegistryMapping.getSuperType(registry);
		var regKey = event.getRegistryKey();
		
		List<Tuple2<Block, ResourceLocation>> blockList = blocks.computeIfAbsent(source, s -> new ArrayList<>());
		
		BiConsumer<ResourceLocation, T> grabber = createRegisterer(registry, prefix).andThen((key, handler) ->
		{
			if(handler instanceof Block b)
				blockList.add(Tuples.immutable(b, key));
		});
		
		if(Item.class.equals(superType)) for(var e : blockList)
		{
			Block blk = e.a();
			if(blk instanceof INoItemBlock) continue;
			BlockItem item;
			IItemPropertySupplier gen = Cast.cast(blk, IItemPropertySupplier.class);
			if(blk instanceof ICustomBlockItem icbi) item = icbi.createBlockItem();
			else
			{
				Item.Properties props = gen != null ? gen.createItemProperties(new Item.Properties()) : new Item.Properties();
				if(blk instanceof ICreativeTabBlock t) props = props.tab(t.getCreativeTab());
				item = new BlockItem(blk, props);
			}
			grabber.accept(e.b(), Cast.cast(item));
		}
		
		boolean tileRegistryOnClient = BlockEntityType.class.equals(superType) && FMLEnvironment.dist == Dist.CLIENT;
		boolean particleRegistryOnClient = ParticleType.class.equals(superType) && FMLEnvironment.dist == Dist.CLIENT;
		
		int prevSize = registry != null ? registry.getValues().size() : 0;
		
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
							if(!RegistryMapping.isNonIntrusive(regKey)
									|| OnlyIfAdapter.checkCondition(onlyIf, source.toString(),
									superType != null ? superType.getSimpleName() : field.getType().getSimpleName(), val, rl))
							{
								if(val instanceof ICustomRegistrar cr)
									cr.performRegister(event, rl);
							}
						} catch(IllegalArgumentException | IllegalAccessException e)
						{
							LogManager.getLogger(modid + "/" + source.getSimpleName()).error("Failed to register field {}", field.getName(), e);
						}
				});
		
		if(superType == null)
			return registry == null ? 0 : registry.getValues().size() - prevSize;
		
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
							LogManager.getLogger(modid + "/" + source.getSimpleName()).error("Failed to register method {}", method.getName(), e);
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
							var rl = new ResourceLocation(modid, name.value());
							
							var val = field.get(null);
							
							var onlyIf = field.getAnnotation(OnlyIf.class); // Bring back OnlyIf, for registries that are non-intrusive. (Mostly, for custom registry types)
							if(!RegistryMapping.isNonIntrusive(regKey)
									|| OnlyIfAdapter.checkCondition(onlyIf, source.toString(), superType.getSimpleName(), val, rl))
							{
								var fval = superType.cast(val);
								grabber.accept(rl, fval);
								
								if(tileRegistryOnClient)
								{
									var tesr = TileRenderer.RendererTarget.get(source, field.getName());
									if(tesr != null)
									{
										tesr.apply();
										HammerLib.LOG.debug("Applied TESR registration for " + field.getType().getSimpleName() + "[" + registry.getKey(fval) + "] " + source.getSimpleName() + '.' + field.getName());
									}
								}
								
								if(particleRegistryOnClient)
								{
									var provider = Particles.Target.get(source, field.getName());
									if(provider != null)
									{
										provider.apply();
										HammerLib.LOG.debug(
												"Applied ParticleProvider for " + field.getType().getSimpleName() +
														"[" + registry.getKey(fval) + "] " + source.getSimpleName() +
														'.' + field.getName());
									}
								}
							}
						} catch(IllegalArgumentException | IllegalAccessException e)
						{
							LogManager.getLogger(modid + "/" + source.getSimpleName()).error("Failed to register field {}", field.getName(), e);
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
							if(!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), "Setup", null, null)) return;
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
							if(!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), "ClientSetup", null, null)) return;
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