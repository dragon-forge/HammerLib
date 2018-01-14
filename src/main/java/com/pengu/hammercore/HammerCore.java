package com.pengu.hammercore;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.xml.sax.SAXException;

import com.endie.lib.weupnp.AttuneResult;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.api.HammerCoreAPI;
import com.pengu.hammercore.api.RequiredDeps;
import com.pengu.hammercore.api.WrappedFMLLog;
import com.pengu.hammercore.api.iHammerCoreAPI;
import com.pengu.hammercore.api.iJavaCode;
import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.api.mhb.RaytracePlugin;
import com.pengu.hammercore.api.mhb.iRayRegistry;
import com.pengu.hammercore.cfg.ConfigHolder;
import com.pengu.hammercore.cfg.HCModConfigurations;
import com.pengu.hammercore.cfg.iConfigReloadListener;
import com.pengu.hammercore.command.CommandBuildStructure;
import com.pengu.hammercore.command.CommandLoadChunk;
import com.pengu.hammercore.command.CommandPosToLong;
import com.pengu.hammercore.command.CommandSetEnchantmentColor;
import com.pengu.hammercore.command.CommandTPX;
import com.pengu.hammercore.command.CommandTimeToTicks;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.common.chunk.ChunkLoaderHC;
import com.pengu.hammercore.common.structure.StructureAPI;
import com.pengu.hammercore.common.utils.AnnotatedInstanceUtil;
import com.pengu.hammercore.common.utils.HammerCoreUtils;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.common.utils.WrappedLog;
import com.pengu.hammercore.core.ext.TeslaAPI;
import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.core.init.BlocksHC;
import com.pengu.hammercore.core.init.ItemsHC;
import com.pengu.hammercore.core.init.ManualHC;
import com.pengu.hammercore.event.GetAllRequiredApisEvent;
import com.pengu.hammercore.fluiddict.FluidDictionary;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.PacketReloadRaytracePlugins;
import com.pengu.hammercore.net.pkt.script.PacketSendGlobalRecipeScriptsWithRemoval;
import com.pengu.hammercore.proxy.AudioProxy_Common;
import com.pengu.hammercore.proxy.BookProxy_Common;
import com.pengu.hammercore.proxy.LightProxy_Common;
import com.pengu.hammercore.proxy.ParticleProxy_Common;
import com.pengu.hammercore.proxy.PipelineProxy_Common;
import com.pengu.hammercore.proxy.RenderProxy_Common;
import com.pengu.hammercore.recipeAPI.BrewingRecipe;
import com.pengu.hammercore.recipeAPI.GlobalRecipeScript;
import com.pengu.hammercore.recipeAPI.RecipePlugin;
import com.pengu.hammercore.recipeAPI.RecipeTypeRegistry;
import com.pengu.hammercore.recipeAPI.SimpleRecipeScript;
import com.pengu.hammercore.recipeAPI.iRecipePlugin;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.hammercore.world.WorldGenHammerCore;
import com.pengu.hammercore.world.WorldGenHelper;
import com.pengu.hammercore.world.data.PerChunkDataManager;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

/**
 * The core of Hammer Core. <br>
 * <span style="text-decoration: underline;"> <em>This really sounds weird
 * :/</em></span>
 **/
@Mod(modid = "hammercore", version = "@VERSION@", name = "Hammer Core", guiFactory = "com.pengu.hammercore.cfg.gui.GuiConfigFactory", certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97")
public class HammerCore
{
	public static final List<String> initHCChannels = new ArrayList<>();
	public static final List<AttuneResult> closeAfterLogoff = new ArrayList<>();
	public static boolean invalidCertificate = false;
	public static final List<iProcess> updatables = new ArrayList<>(16);
	
	/**
	 * Render proxy for HC used to handle complicated rendering codes in a
	 * simple way.
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.pengu.hammercore.proxy.RenderProxy_Client", serverSide = "com.pengu.hammercore.proxy.RenderProxy_Common")
	public static RenderProxy_Common renderProxy;
	
	// /**
	// * All sources compiled from 'javacode' dir
	// */
	// public static ClassLoader javaLoader;
	
	/**
	 * Audio proxy for HC used to interact with audio in any way
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.pengu.hammercore.proxy.AudioProxy_Client", serverSide = "com.pengu.hammercore.proxy.AudioProxy_Common")
	public static AudioProxy_Common audioProxy;
	
	/**
	 * Particle proxy for HC used to interact with particles from both sides.
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.pengu.hammercore.proxy.ParticleProxy_Client", serverSide = "com.pengu.hammercore.proxy.ParticleProxy_Common")
	public static ParticleProxy_Common particleProxy;
	
	@SidedProxy(modId = "hammercore", clientSide = "com.pengu.hammercore.proxy.LightProxy_Client", serverSide = "com.pengu.hammercore.proxy.LightProxy_Common")
	public static LightProxy_Common lightProxy;
	
	@SidedProxy(modId = "hammercore", clientSide = "com.pengu.hammercore.proxy.BookProxy_Client", serverSide = "com.pengu.hammercore.proxy.BookProxy_Common")
	public static BookProxy_Common bookProxy;
	
	@SidedProxy(modId = "hammercore", clientSide = "com.pengu.hammercore.proxy.PipelineProxy_Client", serverSide = "com.pengu.hammercore.proxy.PipelineProxy_Common")
	public static PipelineProxy_Common pipelineProxy;
	
	/**
	 * An instance of {@link HammerCore} class
	 **/
	@Instance("hammercore")
	public static HammerCore instance;
	
	/** Creative tab of HammerCore */
	public static final CreativeTabs tab = HammerCoreUtils.createDynamicCreativeTab("hammercore", 60);
	
	public static final Map<iHammerCoreAPI, HammerCoreAPI> APIS = new HashMap<>();
	
	public static final Set<iJavaCode> COMPILED_CODES = new HashSet<>();
	
	public static final WrappedLog LOG = new WrappedLog("Hammer Core");
	
	// public static final CSVFile FIELD_CSV, METHODS_CSV;
	
	private List<iRayRegistry> raytracePlugins;
	private List<iRecipePlugin> recipePlugins;
	private List<ConfigHolder> configListeners;
	
	static
	{
		// CSVFile f = null, m = null;
		//
		// f = new CSVFile(HammerCore.class.getResourceAsStream("/fields.csv"));
		// m = new
		// CSVFile(HammerCore.class.getResourceAsStream("/methods.csv"));
		//
		// FIELD_CSV = f;
		// METHODS_CSV = m;
	}
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		LOG.warn("*****************************");
		LOG.warn("WARNING: Somebody has been tampering with HammerCore jar!");
		LOG.warn("It is highly recommended that you redownload mod from https://minecraft.curseforge.com/projects/247401 !");
		LOG.warn("*****************************");
		invalidCertificate = true;
	}
	
	/**
	 * This method is used to construct proxies
	 */
	@EventHandler
	public void construct(FMLConstructionEvent e)
	{
		renderProxy.construct();
		audioProxy.construct();
		
		if(!FluidRegistry.isUniversalBucketEnabled())
			FluidRegistry.enableUniversalBucket();
		
		new FluidDictionary();
		
		initHCChannels.add("particles");
		
		// File javacode = new File(".", "javacode");
		// if(!javacode.isDirectory()) javacode.mkdir();
		// try
		// {
		// Map<String, byte[]> classes = JavaCodeLoader.compileRoot(javacode);
		// javaLoader = JavaCodeLoader.toLoader(classes);
		// for(String clas : classes.keySet())
		// {
		// try
		// {
		// // GameRegistry.makeItemStack(itemName, meta, stackSize, nbtString)
		// Class cls = javaLoader.loadClass(clas);
		// IJavaCode code = null;
		// if(IJavaCode.class.isAssignableFrom(cls)) code = (IJavaCode)
		// cls.newInstance();
		// else code = new IJavaCode.IJavaCode_IMPL(cls.newInstance());
		// COMPILED_CODES.add(code);
		// LOG.info("Added new JavaCode: " + code + " for " + clas);
		// }
		// catch(ClassNotFoundException cnfe)
		// {
		// LOG.error("Error: unexpected class " + clas +
		// ". Perharps it has different package?");
		// }
		// catch(Throwable err) { err.printStackTrace(); }
		// }
		// } catch(Exception e1) { e1.printStackTrace(); }
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		List<iHammerCoreAPI> apis = AnnotatedInstanceUtil.getInstances(e.getAsmData(), HammerCoreAPI.class, iHammerCoreAPI.class);
		List<Object> toRegister = AnnotatedInstanceUtil.getInstances(e.getAsmData(), MCFBus.class, Object.class);
		List<iConfigReloadListener> listeners = AnnotatedInstanceUtil.getInstances(e.getAsmData(), HCModConfigurations.class, iConfigReloadListener.class);
		
		renderProxy.preInit(e.getAsmData());
		
		toRegister.add(this);
		
		for(iJavaCode code : COMPILED_CODES)
			// Add compiled codes
			code.addMCFObjects(toRegister);
		
		ProgressBar bar = ProgressManager.push("Loading", 3 + apis.size() + toRegister.size() + listeners.size());
		
		bar.step("Registering EJ");
		CapabilityEJ.register();
		
		configListeners = new ArrayList<>();
		int i = 0;
		for(iConfigReloadListener listener : listeners)
		{
			i++;
			bar.step("Registering Custom Configs (" + i + "/" + listeners.size() + ")");
			ConfigHolder h = new ConfigHolder(listener, new Configuration(listener.getSuggestedConfigurationFile()));
			h.reload();
			configListeners.add(h);
			LOG.info("Added \"" + h.listener.getClass().getName() + "\" to Hammer Core Simple Configs.");
		}
		
		raytracePlugins = AnnotatedInstanceUtil.getInstances(e.getAsmData(), RaytracePlugin.class, iRayRegistry.class);
		recipePlugins = AnnotatedInstanceUtil.getInstances(e.getAsmData(), RecipePlugin.class, iRecipePlugin.class);
		
		i = 0;
		for(Object o : toRegister)
		{
			i++;
			bar.step("Registering Objects to Event Bus (" + i + "/" + toRegister.size() + ")");
			MinecraftForge.EVENT_BUS.register(o);
			LOG.info("Added \"" + o + "\" to MCF Event Bus.");
		}
		
		LOG.info("Added " + toRegister.size() + " object to MCF Event Bus.");
		
		{
			GetAllRequiredApisEvent evt = new GetAllRequiredApisEvent();
			MinecraftForge.EVENT_BUS.post(evt);
			RequiredDeps.addRequests(evt);
		}
		
		i = 0;
		for(iHammerCoreAPI api : apis)
		{
			i++;
			bar.step("Registering external libraries (" + i + "/" + apis.size() + ")");
			HammerCoreAPI apia = api.getClass().getAnnotation(HammerCoreAPI.class);
			if(apia != null)
			{
				WrappedFMLLog log = new WrappedFMLLog(apia.name());
				api.init(log, apia.version());
				APIS.put(api, apia);
			}
		}
		
		bar.step("Registering Blocks");
		SimpleRegistration.registerFieldBlocksFrom(BlocksHC.class, "hammercore", HammerCore.tab);
		
		bar.step("Registering Items");
		SimpleRegistration.registerFieldItemsFrom(ItemsHC.class, "hammercore", HammerCore.tab);
		
		OreDictionary.registerOre("gearIron", ItemsHC.iron_gear);
		
		ModMetadata meta = e.getModMetadata();
		meta.autogenerated = false;
		meta.version = "@VERSION@";
		
		meta.authorList = new ArrayList<>();
		Arrays.asList(HCAUTHORS).forEach(a -> meta.authorList.add(a.getUsername()));
		meta.authorList = Collections.unmodifiableList(meta.authorList);
		
		for(iJavaCode code : COMPILED_CODES)
			code.preInit();
		
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		renderProxy.init();
		bookProxy.init();
		HCNetwork.clinit();
		ManualHC.register();
		
		for(iJavaCode code : COMPILED_CODES)
			code.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiManager());
		
		BrewingRecipeRegistry.addRecipe(BrewingRecipe.INSTANCE);
		
		GameRegistry.registerWorldGenerator(new WorldGenHammerCore(), 0);
		
		StructureAPI.registerSpawnableStructure(new ResourceLocation("hammercore", "well"));
		
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.pengu.hammercore.intr.top.GetTOP");
		FMLInterModComms.sendMessage("waila", "register", "com.pengu.hammercore.intr.waila.GetWaila.register");
	}
	
	public static final RecipeTypeRegistry registry = new RecipeTypeRegistry();
	private GlobalRecipeScript recipeScript;
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		renderProxy.postInit();
		for(iJavaCode code : COMPILED_CODES)
			code.postInit();
		for(iRecipePlugin plugin : recipePlugins)
		{
			LOG.info("Registering recipe plugin: " + plugin.getClass().getName() + " ...");
			long start = System.currentTimeMillis();
			plugin.registerTypes(registry);
			LOG.info("Registered recipe  plugin: " + plugin.getClass().getName() + " in " + (System.currentTimeMillis() - start) + " ms");
		}
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CommandTPX());
		e.registerServerCommand(new CommandBuildStructure());
		e.registerServerCommand(new CommandTimeToTicks());
		e.registerServerCommand(new CommandLoadChunk());
		e.registerServerCommand(new CommandSetEnchantmentColor());
		e.registerServerCommand(new CommandPosToLong());
		
		File hc_recipes_global = new File("hc-recipes");
		MinecraftServer server = e.getServer();
		File worldFolder = new File((server.isDedicatedServer() ? "" : "saves" + File.separator) + server.getFolderName(), "hc-recipes");
		worldFolder.mkdirs();
		hc_recipes_global.mkdirs();
		
		if(recipeScript != null)
			recipeScript.remove();
		List<SimpleRecipeScript> scripts = new ArrayList<>();
		scripts.addAll(Arrays.asList(parse(worldFolder).scripts));
		scripts.addAll(Arrays.asList(parse(hc_recipes_global).scripts));
		recipeScript = new GlobalRecipeScript(scripts.toArray(new SimpleRecipeScript[scripts.size()]));
		GRCProvider.reloadScript();
		
		reloadRaytracePlugins();
	}
	
	public static void registerDebugCommand(ICommandManager mgr, ICommand cmd)
	{
		CommandHandler ch = (CommandHandler) mgr;
		
		ch.registerCommand(cmd);
		ch.getCommands().remove(cmd.getName());
		ch.getCommands().put(TextFormatting.DARK_GRAY.toString() + cmd.getName(), cmd);
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppingEvent evt)
	{
		if(recipeScript != null)
			recipeScript.remove();
		recipeScript = null;
		ChunkLoaderHC.INSTANCE.isAlive();
		
		closeAfterLogoff.forEach(r ->
		{
			try
			{
				LOG.info("Closing port " + r.getInternalPort() + " on " + r.getProtocol());
				r.undo();
			} catch(IOException e)
			{
				e.printStackTrace();
			} catch(SAXException e)
			{
				e.printStackTrace();
			}
		});
		
		closeAfterLogoff.clear();
	}
	
	@EventHandler
	public void serverStopped(FMLServerStoppedEvent e)
	{
		PerChunkDataManager.cleanup();
		WorldGenHelper.CHUNKLOADERS.clear();
	}
	
	@SubscribeEvent
	public void getApis(GetAllRequiredApisEvent evt)
	{
		
	}
	
	@SubscribeEvent
	public void playerConnected(PlayerLoggedInEvent evt)
	{
		EntityPlayer client = HammerCore.renderProxy.getClientPlayer();
		if(client != null && client.getGameProfile().getId().equals(evt.player.getGameProfile().getId()))
			return;
		
		if(evt.player instanceof EntityPlayerMP)
			try
			{
				EntityPlayerMP mp = (EntityPlayerMP) evt.player;
				
				HCNetwork.manager.sendTo(new PacketReloadRaytracePlugins(), mp);
				
				if(!evt.player.world.isRemote && GRCProvider.getScriptCount() > 0)
					HCNetwork.manager.sendTo(new PacketSendGlobalRecipeScriptsWithRemoval(0, GRCProvider.getScript(0)), mp);
			} catch(Throwable err)
			{
			}
	}
	
	@SubscribeEvent
	public void serverTick(ServerTickEvent evt)
	{
		if(evt.side == Side.SERVER)
		{
			ChunkLoaderHC.INSTANCE.update();
			for(int i = 0; evt.phase == Phase.START && i < updatables.size(); ++i)
			{
				try
				{
					iProcess upd = updatables.get(i);
					upd.update();
					if(!upd.isAlive())
					{
						upd.onKill();
						updatables.remove(i);
					}
				} catch(Throwable err)
				{
				}
			}
		}
	}
	
	@Override
	public int hashCode()
	{
		return 0x666666;
	}
	
	@SubscribeEvent
	public void configReloaded(ConfigChangedEvent evt)
	{
		String mid = evt.getModID();
		for(ConfigHolder holder : configListeners)
			if(holder.listener.getModid().equals(mid))
				holder.reload();
	}
	
	public void reloadRaytracePlugins()
	{
		TeslaAPI.refreshTeslaClassData();
		
		RayCubeRegistry.instance.cubes.clear();
		RayCubeRegistry.instance.mgrs.clear();
		
		for(iRayRegistry reg : raytracePlugins)
		{
			LOG.info("Registering raytrace plugin: " + reg.getClass().getName() + " ...");
			long start = System.currentTimeMillis();
			reg.registerCubes(RayCubeRegistry.instance);
			LOG.info("Registered raytrace  plugin: " + reg.getClass().getName() + " in " + (System.currentTimeMillis() - start) + " ms");
		}
	}
	
	private GlobalRecipeScript parse(File path)
	{
		if(path.isDirectory())
		{
			List<SimpleRecipeScript> jsons = new ArrayList<>();
			for(File json : path.listFiles(new FileFilter()
			{
				@Override
				public boolean accept(File pathname)
				{
					return pathname.isFile() && pathname.getName().endsWith(".json");
				}
			}))
			{
				try
				{
					jsons.add(registry.parse(new String(IOUtils.pipeOut(new FileInputStream(json)))));
				} catch(Throwable err)
				{
					LOG.warn("Failed to parse HammerCoreRecipeJson File:");
					err.printStackTrace();
				}
			}
			
			return new GlobalRecipeScript(jsons.toArray(new SimpleRecipeScript[jsons.size()]));
		} else if(path.isFile())
		{
			try
			{
				return new GlobalRecipeScript(registry.parse(new String(IOUtils.pipeOut(new FileInputStream(path)))));
			} catch(Throwable err)
			{
				LOG.warn("Failed to parse HammerCoreRecipeJson File:");
				err.printStackTrace();
			}
		}
		return new GlobalRecipeScript();
	}
	
	public static class GRCProvider
	{
		public static int getScriptCount()
		{
			return instance.recipeScript.scripts.length;
		}
		
		public static void setScriptCount(int amt)
		{
			if(amt == 0)
				if(instance.recipeScript != null)
				{
					instance.recipeScript.remove();
					instance.recipeScript = null;
					return;
				}
			
			if(instance.recipeScript == null)
				instance.recipeScript = new GlobalRecipeScript();
			instance.recipeScript.remove();
			SimpleRecipeScript[] old = instance.recipeScript.scripts;
			if(old.length == amt)
				return;
			instance.recipeScript.scripts = new SimpleRecipeScript[amt];
			for(int i = 0; i < Math.min(old.length, amt); ++i)
				instance.recipeScript.scripts[i] = old[i];
		}
		
		public static NBTTagList getScript(int id)
		{
			if(instance.recipeScript == null)
				return new NBTTagList();
			return id >= instance.recipeScript.scripts.length && instance.recipeScript.scripts[id].makeTag != null ? null : instance.recipeScript.scripts[id].makeTag.copy();
		}
		
		public static void setScript(int id, NBTTagList list)
		{
			if(instance.recipeScript == null)
				instance.recipeScript = new GlobalRecipeScript();
			instance.recipeScript.remove();
			setScriptCount(Math.max(instance.recipeScript.scripts.length, id + 1));
			instance.recipeScript.scripts[id] = registry.parse(list);
		}
		
		public static void reloadScript()
		{
			if(instance.recipeScript != null)
			{
				instance.recipeScript.remove();
				instance.recipeScript.add();
			}
		}
	}
	
	public static int client_ticks = 0;
	
	private static final HCAuthor[] HCAUTHORS = //
	        { //
	                new HCAuthor("APengu", TextFormatting.BLUE + "" + TextFormatting.ITALIC + "       " + TextFormatting.RESET + "  ", () ->
	                {
		                float sine = .5F * ((float) Math.sin(Math.toRadians(16 * client_ticks)) + 1);
		                
		                int r = 16;
		                int g = 16;
		                int b = 150 + (int) (80F * sine);
		                
		                return ColorHelper.packRGB(r / 255F, g / 255F, b / 255F);
	                }, true), //
	                new HCAuthor("EndieDargon", TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + "              " + TextFormatting.RESET + "  ", () ->
	                {
		                float sine = .5F * ((float) Math.sin(Math.toRadians(16 * client_ticks)) + 1);
		                
		                int r = 140 + (int) (sine * 60);
		                int g = 0;
		                int b = 255;
		                
		                return ColorHelper.packRGB(r / 255F, g / 255F, b / 255F);
	                }, true) //
			};
			
	public static HCAuthor[] getHCAuthors()
	{
		return HCAUTHORS.clone();
	}
	
	public static List<String> getHCAuthorsArray()
	{
		List<String> a = new ArrayList<>();
		for(HCAuthor h : HCAUTHORS)
			if(h.isAuthor())
				a.add(h.getUsername());
		return Collections.unmodifiableList(a);
	}
	
	public static String getHCMainDev()
	{
		return HCAUTHORS[0].getUsername();
	}
	
	public static class HCAuthor
	{
		private final String username, dname;
		private final Supplier<Integer> color;
		private final boolean isAuthor;
		
		private HCAuthor(String username, String dname, Supplier<Integer> color, boolean isAuthor)
		{
			this.username = username;
			this.dname = dname;
			this.color = color;
			this.isAuthor = isAuthor;
		}
		
		public boolean isAuthor()
		{
			return isAuthor;
		}
		
		public String getUsername()
		{
			return username;
		}
		
		public String getDisplayName()
		{
			return dname;
		}
		
		public Supplier<Integer> getColor()
		{
			return color;
		}
	}
}