package org.zeith.hammerlib.core.adapter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringUtil;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLPaths;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.SetupConfigs;
import org.zeith.hammerlib.api.config.*;
import org.zeith.hammerlib.event.player.PlayerLoadedInEvent;
import org.zeith.hammerlib.net.lft.NetTransport;
import org.zeith.hammerlib.net.packets.PacketSyncConfigs;
import org.zeith.hammerlib.util.SidedLocal;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class ConfigAdapter
{
	private static final Map<String, ConfigFile> FILE_HASH = new HashMap<>();
	private static final Map<Class<? extends IConfigRoot>, CompoundTag> CLIENT_STATE = new HashMap<>();
	private static final SidedLocal<Map<Class<? extends IConfigRoot>, IConfigRoot>> STRUCTURES = new SidedLocal<>(side -> new HashMap<>());
	
	@SubscribeEvent
	public static void syncConfigs(PlayerLoadedInEvent event)
	{
		var map = new HashMap<>(STRUCTURES.get(LogicalSide.SERVER));
		map.values().removeIf(IConfigRoot::avoidSync);
		NetTransport.wrap(new PacketSyncConfigs(map)).sendTo(event.getEntity());
	}
	
	public static void handleClientsideSync(PacketSyncConfigs packet)
	{
		var map = STRUCTURES.get(LogicalSide.CLIENT);
		CLIENT_STATE.clear();
		
		packet.data().forEach((type, tag) ->
		{
			IConfigRoot configRoot = map.get(type);
			if(configRoot != null)
			{
				// We backup the client state so that it can be restored later.
				{
					CompoundTag sub = new CompoundTag();
					configRoot.toNetwork(sub);
					CLIENT_STATE.put(type, sub);
				}
				
				configRoot.fromNetwork(tag);
				configRoot.updateInstance(LogicalSide.CLIENT);
			}
		});
		
		HammerLib.LOG.info("Applied " + CLIENT_STATE.size() + " configs from server.");
	}
	
	public static void resetClientsideSync()
	{
		var map = STRUCTURES.get(LogicalSide.CLIENT);
		
		CLIENT_STATE.forEach((type, tag) ->
		{
			IConfigRoot configRoot = map.get(type);
			if(configRoot != null)
			{
				configRoot.fromNetwork(tag);
				configRoot.updateInstance(LogicalSide.CLIENT);
			}
		});
		
		HammerLib.LOG.info("Reset " + CLIENT_STATE.size() + " configs to their client-side state.");
		
		CLIENT_STATE.clear();
	}
	
	public static void setup()
	{
		ScanDataHelper.lookupAnnotatedObjects(SetupConfigs.class).forEach(data ->
		{
			Class<?> registerer = data.getOwnerClass();
			if(data.getTargetType() == ElementType.METHOD)
			{
				HammerLib.LOG.info("Injecting config setup into " + registerer);
				data.getOwnerMod()
						.ifPresent(b -> configSetup(b, registerer, data.getMemberName()));
			}
		});
		
		for(var ad : ScanDataHelper.lookupAnnotatedObjects(Config.class, e -> IConfigRoot.class.isAssignableFrom(e.getOwnerClass())))
		{
			try
			{
				var cfg = ad.getOwnerClass().getAnnotation(Config.class);
				var inst = ad.getOwnerClass().asSubclass(IConfigRoot.class).getDeclaredConstructor().newInstance();
				var module = cfg.module();
				
				ad.getOwnerMod().ifPresentOrElse(mod ->
				{
					apply(mod.getModId(), StringUtil.isNullOrEmpty(module) ? Optional.empty() : Optional.of(module), inst::load);
					inst.updateInstance(LogicalSide.SERVER);
					inst.updateInstance(LogicalSide.CLIENT);
					STRUCTURES.applyForAllSides(map ->
					{
						map.put(inst.getClass(), inst);
						return map;
					});
				}, () ->
				{
					HammerLib.LOG.warn("Failed to find out the mod for " + ad.getOwnerClass() + ". Why though?");
				});
			} catch(ReflectiveOperationException e)
			{
				HammerLib.LOG.fatal("Failed to create new config instance for " + ad.getOwnerClass(), e);
			} catch(ConfigException e)
			{
				HammerLib.LOG.error("Failed to load config file in " + ad.getOwnerClass() + " correctly.", e);
			}
		}
	}
	
	public static <T extends IConfigRoot> Optional<T> getConfigForSide(LogicalSide side, Class<T> type)
	{
		return Cast.optionally(STRUCTURES.get(side).get(type), type);
	}
	
	public static ConfigFile getConfigFile(String mod, Optional<String> module)
	{
		Path path = module
				.map(s -> FMLPaths.CONFIGDIR.get().resolve(mod).resolve(s + ".cfg"))
				.orElseGet(() -> FMLPaths.CONFIGDIR.get().resolve(mod + ".cfg"));
		
		return FILE_HASH.computeIfAbsent(path.toFile().getAbsolutePath(), p ->
		{
			try
			{
				Files.createDirectories(path.getParent());
			} catch(IOException e)
			{
				e.printStackTrace();
			}
			return new ConfigFile(path.toFile());
		});
	}
	
	public static void apply(String mod, Optional<String> module, Consumer<ConfigFile> handler)
	{
		ConfigFile file = getConfigFile(mod, module);
		if(file != null)
		{
			handler.accept(file);
			if(file.hasChanged())
				file.save();
		}
	}
	
	public static void configSetup(FMLModContainer mod, Class<?> source, String memberName)
	{
		String methodName = memberName.substring(0, memberName.indexOf('('));
		
		Arrays
				.stream(source.getDeclaredMethods())
				.filter(m -> m.getAnnotation(SetupConfigs.class) != null && m.getName().equals(methodName))
				.forEach(method ->
				{
					SetupConfigs cfgs = method.getAnnotation(SetupConfigs.class);
					if(Modifier.isStatic(method.getModifiers()))
						try
						{
							OnlyIf onlyIf = method.getAnnotation(OnlyIf.class);
							if(!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), "ConfigSetup", null, null)) return;
							method.setAccessible(true);
							if(method.getParameterCount() == 0)
								method.invoke(null);
							else if(method.getParameterCount() == 1 && method.getParameterTypes()[0] == ConfigFile.class)
							{
								ConfigFile file = getConfigFile(mod.getModId(),
										cfgs.module().isEmpty()
												? Optional.empty()
												: Optional.of(cfgs.module())
								);
								method.invoke(null, file);
								if(file.hasChanged())
									file.save();
							}
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
						} catch(Throwable e)
						{
							HammerLib.LOG.error("Mod " + mod.getModId() + " has failed to set up it's configs:", e);
							e.printStackTrace();
						}
				});
	}
}