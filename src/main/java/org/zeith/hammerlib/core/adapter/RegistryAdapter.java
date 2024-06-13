package org.zeith.hammerlib.core.adapter;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.annotations.ap.AnnotationProcessorRegistry;
import org.zeith.hammerlib.annotations.ap.IAPContext;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.api.blocks.*;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import org.zeith.hammerlib.util.mcf.Resources;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class RegistryAdapter
{
	private static final Set<Class<?>> SCANNED_CLASSES = ConcurrentHashMap.newKeySet();
	
	public static <T> Optional<BiConsumer<ResourceLocation, T>> createRegisterer(RegisterEvent event, ResourceKey<? extends Registry<T>> registryType, String prefix)
	{
		Registry<T> reg = event.getRegistry(registryType);
		if(prefix == null) prefix = "";
		if(reg != null)
			return Optional.of(createRegisterer(reg, prefix));
		
		return Optional.empty();
	}
	
	public static <T> BiConsumer<ResourceLocation, T> createRegisterer(Registry<T> registry, String prefix)
	{
		return (name, entry) ->
		{
			name = Resources.location(name.getNamespace(), prefix + name.getPath());
			IRegisterListener l = Cast.cast(entry, IRegisterListener.class);
			if(l != null) l.onPreRegistered(name);
			Registry.register(registry, name, entry);
			if(l != null) l.onPostRegistered(name);
		};
	}
	
	private static final Map<Class<?>, List<Tuple2<Block, ResourceLocation>>> blocks = new ConcurrentHashMap<>();
	
	/**
	 * Should be called within the RegisterEvent on your mod, letting HL grab the active mod container and deal with the namespaces and everything else.
	 * Returns -1 if HL is unable to determine the active mod.
	 */
	public static int registerCurrentMod(RegisterEvent event, Class<?> source, String prefix)
	{
		var mc = ModLoadingContext.get().getActiveContainer();
		if(!(mc instanceof FMLModContainer fmlmc)) return -1;
		return register(event, source, fmlmc, prefix);
	}
	
	public static int register(RegisterEvent event, Class<?> source, FMLModContainer mod, String prefix)
	{
		var reg = event.getRegistry();
		
		//<editor-fold desc="Scan phase for all classes, performs AP setup.">
		if(SCANNED_CLASSES.add(source))
		{
			var modid = mod.getModId();
			var superType = RegistryMapping.getSuperType(reg);
			var regKey = event.getRegistryKey();
			
			for(Field field : source.getDeclaredFields())
			{
				if(!Modifier.isStatic(field.getModifiers())) continue;
				try
				{
					var ctxb = IAPContext.builder().owner(mod);
					
					field.setAccessible(true);
					var val = field.get(null);
					
					var name = field.getAnnotation(RegistryName.class);
					if(name != null)
					{
						var rl = Resources.location(modid, prefix + name.value());
						ctxb.id(rl);
						
						var onlyIf = field.getAnnotation(OnlyIf.class); // Bring back OnlyIf, for registries that are non-intrusive. (Mostly, for custom registry types)
						boolean register = !RegistryMapping.isNonIntrusive(regKey)
										   || OnlyIfAdapter.checkCondition(onlyIf, source.toString(),
								superType != null ? superType.getSimpleName() : field.getType().getSimpleName(), val, rl
						);
						
						ctxb.shouldRegister(register);
					}
					
					AnnotationProcessorRegistry.scan(ctxb.build(), field, val);
				} catch(ReflectiveOperationException roe)
				{
					HammerLib.LOG.error("Failed to scan field {}", field);
				}
			}
			
			for(Method method : source.getDeclaredMethods())
			{
				AnnotationProcessorRegistry.scan(IAPContext.DUMMY, method);
			}
		}
		//</editor-fold>
		
		try
		{
			return RegistryAdapter.register(event, reg, source, mod, prefix);
		} catch(Throwable e)
		{
			throw new RuntimeException("Failed to perform registration for registry " + event.getRegistryKey(), e);
		}
	}
	
	/**
	 * Registers all static fields (from source) with the matching registry type, and methods that accept Consumer<T>
	 */
	public static <T> int register(RegisterEvent event, Registry<T> registry, Class<?> source, FMLModContainer mod, String prefix)
	{
		var modid = mod.getModId();
		var superType = RegistryMapping.getSuperType(registry);
		var regKey = event.getRegistryKey();
		
		List<Tuple2<Block, ResourceLocation>> blockList = blocks.computeIfAbsent(source, s -> new ArrayList<>());
		
		var registrar = source.getAnnotation(SimplyRegister.class);
		
		List<CreativeTab> tabs =
				registrar != null
				? Ref.Resolver.resolveFields(CreativeTab.class, registrar.creativeTabs())
				: List.of();
		
		BiConsumer<ResourceLocation, T> grabber = createRegisterer(registry, prefix).andThen((key, handler) ->
		{
			if(handler instanceof Block b)
				blockList.add(Tuples.immutable(b, key));
			
			if(handler instanceof ItemLike item && !tabs.isEmpty())
				CreativeTabAdapter.bindTab(item, tabs.toArray(CreativeTab[]::new));
		});
		
		if(Item.class.equals(superType)) for(var e : blockList)
		{
			Block blk = e.a();
			if(blk instanceof INoItemBlock) continue;
			BlockItem item;
			IItemPropertySupplier gen = Cast.cast(blk, IItemPropertySupplier.class);
			if(blk instanceof ICustomBlockItem) item = ((ICustomBlockItem) blk).createBlockItem();
			else
			{
				Item.Properties def = new Item.Properties();
				Item.Properties props = gen != null ? gen.createItemProperties(def) : def;
				item = new BlockItem(blk, props);
				if(blk instanceof ICreativeTabBlock t)
					t.getCreativeTab().add(item);
			}
			
			grabber.accept(e.b(), Cast.cast(item));
		}
		
		int prevSize = registry != null ? registry.size() : 0;
		
		// ICustomRegistrar hook!
		Arrays
				.stream(source.getDeclaredFields())
				.filter(f -> ICustomRegistrar.class.isAssignableFrom(f.getType()))
				.filter(field -> Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
				.forEach(field ->
				{
					try
					{
						field.setAccessible(true);
						var name = field.getAnnotation(RegistryName.class);
						if(name == null) throw new RuntimeException("Field " + field + " is missing @RegistryName!");
						var rl = Resources.location(modid, prefix + name.value());
						
						var val = field.get(null);
						var onlyIf = field.getAnnotation(OnlyIf.class); // Bring back OnlyIf, for registries that are non-intrusive. (Mostly, for custom registry types)
						boolean register = !RegistryMapping.isNonIntrusive(regKey)
										   || OnlyIfAdapter.checkCondition(onlyIf, source.toString(),
								superType != null ? superType.getSimpleName() : field.getType().getSimpleName(), val, rl
						);
						
						var ctx = IAPContext.builder().owner(mod).id(rl).shouldRegister(register).build();
						AnnotationProcessorRegistry.scanReg(ctx, field, val, false);
						
						if(register)
						{
							if(val instanceof ICustomRegistrar cr)
							{
								cr.performRegister(event, rl);
								AnnotationProcessorRegistry.scanReg(ctx, field, val, true);
							}
						}
					} catch(IllegalArgumentException | IllegalAccessException e)
					{
						LogManager.getLogger(modid + "/" + source.getSimpleName())
								.error("Failed to register field {}", field.getName(), e);
					}
				});
		
		if(superType == null)
			return registry == null ? 0 : registry.size() - prevSize;
		
		Arrays
				.stream(source.getDeclaredMethods())
				.filter(m -> m.getAnnotation(SimplyRegister.class) != null && m.getParameterCount() == 1 &&
							 BiConsumer.class.isAssignableFrom(m.getParameterTypes()[0]) &&
							 ReflectionUtil.doesParameterTypeArgsMatch(m.getParameters()[0], ResourceLocation.class, superType))
				.filter(method -> Modifier.isStatic(method.getModifiers()))
				.forEach(method ->
				{
					final String prefix2 = Optional.ofNullable(method.getAnnotation(SimplyRegister.class))
							.map(SimplyRegister::prefix).orElse("");
					
					try
					{
						method.setAccessible(true);
						
						BiConsumer<ResourceLocation, T> grabber2 = (id, obj) ->
						{
							id = Resources.location(id.getNamespace(), prefix2 + id.getPath());
							grabber.accept(id, obj);
						};
						
						method.invoke(null, grabber2);
					} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						LogManager.getLogger(modid + "/" + source.getSimpleName())
								.error("Failed to register method {}", method.getName(), e);
					}
				});
		
		Arrays
				.stream(source.getDeclaredFields())
				.filter(f -> superType.isAssignableFrom(f.getType())
							 &&
							 !ICustomRegistrar.class.isAssignableFrom(f.getType())) // Custom registrars have been called by now.
				.filter(field -> Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
				.forEach(field ->
				{
					try
					{
						field.setAccessible(true);
						var name = field.getAnnotation(RegistryName.class);
						var rl = Resources.location(modid, name.value());
						
						var val = field.get(null);
						
						var onlyIf = field.getAnnotation(OnlyIf.class); // Bring back OnlyIf, for registries that are non-intrusive. (Mostly, for custom registry types)
						var register = !RegistryMapping.isNonIntrusive(regKey) || OnlyIfAdapter.checkCondition(onlyIf, source.toString(), superType.getSimpleName(), val, rl);
						
						var ctx = IAPContext.builder().owner(mod).id(rl).shouldRegister(register).build();
						AnnotationProcessorRegistry.scanReg(ctx, field, val, false);
						
						if(register)
						{
							var fval = superType.cast(val);
							grabber.accept(rl, fval);
							
							AnnotationProcessorRegistry.scanReg(ctx, field, val, true);
						}
					} catch(IllegalArgumentException | IllegalAccessException e)
					{
						LogManager.getLogger(modid + "/" + source.getSimpleName()).error("Failed to register field {}", field.getName(), e);
					}
				});
		
		return registry.size() - prevSize;
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