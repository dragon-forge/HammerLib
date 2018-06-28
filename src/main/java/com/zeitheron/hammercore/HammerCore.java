package com.zeitheron.hammercore;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import com.zeitheron.hammercore.annotations.MCFBus;
import com.zeitheron.hammercore.api.GameRules;
import com.zeitheron.hammercore.api.GameRules.GameRuleEntry;
import com.zeitheron.hammercore.api.GameRules.ValueType;
import com.zeitheron.hammercore.api.HammerCoreAPI;
import com.zeitheron.hammercore.api.IHammerCoreAPI;
import com.zeitheron.hammercore.api.IJavaCode;
import com.zeitheron.hammercore.api.IProcess;
import com.zeitheron.hammercore.api.RequiredDeps;
import com.zeitheron.hammercore.api.WrappedFMLLog;
import com.zeitheron.hammercore.api.mhb.RaytracePlugin;
import com.zeitheron.hammercore.api.mhb.IRayRegistry;
import com.zeitheron.hammercore.cfg.ConfigHolder;
import com.zeitheron.hammercore.cfg.HCModConfigurations;
import com.zeitheron.hammercore.cfg.IConfigReloadListener;
import com.zeitheron.hammercore.command.CommandBuildStructure;
import com.zeitheron.hammercore.command.CommandExportStructure;
import com.zeitheron.hammercore.command.CommandLoadChunk;
import com.zeitheron.hammercore.command.CommandLyingItem;
import com.zeitheron.hammercore.command.CommandPosToLong;
import com.zeitheron.hammercore.command.CommandSetEnchantmentColor;
import com.zeitheron.hammercore.command.CommandSetLootTable;
import com.zeitheron.hammercore.command.CommandTPX;
import com.zeitheron.hammercore.command.CommandTimeToTicks;
import com.zeitheron.hammercore.event.GetAllRequiredApisEvent;
import com.zeitheron.hammercore.fluiddict.FluidDictionary;
import com.zeitheron.hammercore.internal.GuiManager;
import com.zeitheron.hammercore.internal.SimpleRegistration;
import com.zeitheron.hammercore.internal.TeslaAPI;
import com.zeitheron.hammercore.internal.capabilities.CapabilityEJ;
import com.zeitheron.hammercore.internal.chunk.ChunkLoaderHC;
import com.zeitheron.hammercore.internal.init.BlocksHC;
import com.zeitheron.hammercore.internal.init.ItemsHC;
import com.zeitheron.hammercore.internal.init.ManualHC;
import com.zeitheron.hammercore.lib.zlib.database.SafeStore;
import com.zeitheron.hammercore.lib.zlib.utils.Threading;
import com.zeitheron.hammercore.lib.zlib.weupnp.AttuneResult;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.opts.PacketReqOpts;
import com.zeitheron.hammercore.proxy.AudioProxy_Common;
import com.zeitheron.hammercore.proxy.BookProxy_Common;
import com.zeitheron.hammercore.proxy.NativeProxy_Common;
import com.zeitheron.hammercore.proxy.ParticleProxy_Common;
import com.zeitheron.hammercore.proxy.PipelineProxy_Common;
import com.zeitheron.hammercore.proxy.RenderProxy_Common;
import com.zeitheron.hammercore.utils.AnnotatedInstanceUtil;
import com.zeitheron.hammercore.utils.HammerCoreUtils;
import com.zeitheron.hammercore.utils.UpdateChecker;
import com.zeitheron.hammercore.utils.charging.ItemChargeHelper;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import com.zeitheron.hammercore.utils.recipes.BrewingRecipe;
import com.zeitheron.hammercore.utils.recipes.helper.RecipeRegistry;
import com.zeitheron.hammercore.utils.recipes.helper.RegisterRecipes;
import com.zeitheron.hammercore.utils.structure.StructureAPI;
import com.zeitheron.hammercore.world.WorldGenHammerCore;
import com.zeitheron.hammercore.world.WorldGenHelper;
import com.zeitheron.hammercore.world.data.PerChunkDataManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
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
import net.minecraftforge.registries.IForgeRegistry;

/**
 * The core of Hammer Core. <br>
 * <span style="text-decoration: underline;"> <em>This really sounds weird
 * :/</em></span>
 **/
@Mod(modid = "hammercore", version = "@VERSION@", name = "Hammer Core", guiFactory = "com.zeitheron.hammercore.cfg.gui.GuiConfigFactory", certificateFingerprint = "4d7b29cd19124e986da685107d16ce4b49bc0a97")
public class HammerCore
{
	public static final List<String> initHCChannels = new ArrayList<>();
	public static final List<AttuneResult> closeAfterLogoff = new ArrayList<>();
	public static boolean invalidCertificate = false;
	/**
	 * Contains all mods that require HammerCore and have invalid certificates
	 */
	public static Map<String, String> invalidCertificates = new HashMap<>();
	public static final List<IProcess> updatables = new ArrayList<>(16);
	
	/**
	 * Render proxy for HC used to handle complicated rendering codes in a
	 * simple way.
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.zeitheron.hammercore.proxy.RenderProxy_Client", serverSide = "com.zeitheron.hammercore.proxy.RenderProxy_Common")
	public static RenderProxy_Common renderProxy;
	
	// /**
	// * All sources compiled from 'javacode' dir
	// */
	// public static ClassLoader javaLoader;
	
	/**
	 * Audio proxy for HC used to interact with audio in any way
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.zeitheron.hammercore.proxy.AudioProxy_Client", serverSide = "com.zeitheron.hammercore.proxy.AudioProxy_Common")
	public static AudioProxy_Common audioProxy;
	
	/**
	 * Particle proxy for HC used to interact with particles from both sides.
	 */
	@SidedProxy(modId = "hammercore", clientSide = "com.zeitheron.hammercore.proxy.ParticleProxy_Client", serverSide = "com.zeitheron.hammercore.proxy.ParticleProxy_Common")
	public static ParticleProxy_Common particleProxy;
	
	@SidedProxy(modId = "hammercore", clientSide = "com.zeitheron.hammercore.proxy.BookProxy_Client", serverSide = "com.zeitheron.hammercore.proxy.BookProxy_Common")
	public static BookProxy_Common bookProxy;
	
	@SidedProxy(modId = "hammercore", clientSide = "com.zeitheron.hammercore.proxy.PipelineProxy_Client", serverSide = "com.zeitheron.hammercore.proxy.PipelineProxy_Common")
	public static PipelineProxy_Common pipelineProxy;
	
	@SidedProxy(modId = "hammercore", clientSide = "com.zeitheron.hammercore.proxy.NativeProxy_Client", serverSide = "com.zeitheron.hammercore.proxy.NativeProxy_Common")
	public static NativeProxy_Common nativeProxy;
	
	/**
	 * An instance of {@link HammerCore} class
	 **/
	@Instance("hammercore")
	public static HammerCore instance;
	
	/** Creative tab of HammerCore */
	public static final CreativeTabs tab = HammerCoreUtils.createDynamicCreativeTab("hammercore", 60);
	
	public static final Map<IHammerCoreAPI, HammerCoreAPI> APIS = new HashMap<>();
	
	public static final Set<IJavaCode> COMPILED_CODES = new HashSet<>();
	
	public static final Logger LOG = LogManager.getLogger("HammerCore");
	
	// public static final CSVFile FIELD_CSV, METHODS_CSV;
	
	private List<IRayRegistry> raytracePlugins;
	private List<ConfigHolder> configListeners;
	private List<RecipeRegistry> recipeRegistries;
	
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
		
		Class c = ForgeHooksClient.class;
		
		FMLCommonHandler.instance().registerCrashCallable(new CrashUtil());
	}
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		LOG.warn("*****************************");
		LOG.warn("WARNING: Somebody has been tampering with HammerCore jar!");
		LOG.warn("It is highly recommended that you redownload mod from https://minecraft.curseforge.com/projects/247401 !");
		LOG.warn("*****************************");
		invalidCertificate = true;
		invalidCertificates.put("hammercore", "https://minecraft.curseforge.com/projects/247401");
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
	
	public List<Object> MCFBusObjects;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		List<IHammerCoreAPI> apis = AnnotatedInstanceUtil.getInstances(e.getAsmData(), HammerCoreAPI.class, IHammerCoreAPI.class);
		List<Object> toRegister = MCFBusObjects = AnnotatedInstanceUtil.getInstances(e.getAsmData(), MCFBus.class, Object.class);
		List<IConfigReloadListener> listeners = AnnotatedInstanceUtil.getInstances(e.getAsmData(), HCModConfigurations.class, IConfigReloadListener.class);
		
		renderProxy.preInit(e.getAsmData());
		
		toRegister.add(this);
		
		for(IJavaCode code : COMPILED_CODES)
			// Add compiled codes
			code.addMCFObjects(toRegister);
		
		ProgressBar bar = ProgressManager.push("Loading", 7 + apis.size() + toRegister.size() + listeners.size());
		
		bar.step("Registering EJ");
		CapabilityEJ.register();
		
		bar.step("Registering Chunk Storage");
		PerChunkDataManager.register();
		
		bar.step("Loading ItemChargeHelper modules");
		ItemChargeHelper.preInit(e.getAsmData());
		
		configListeners = new ArrayList<>();
		int i = 0;
		for(IConfigReloadListener listener : listeners)
		{
			i++;
			bar.step("Registering Custom Configs (" + i + "/" + listeners.size() + ")");
			ConfigHolder h = new ConfigHolder(listener, new Configuration(listener.getSuggestedConfigurationFile()));
			h.reload();
			configListeners.add(h);
			LOG.info("Added \"" + h.listener.getClass().getName() + "\" to Hammer Core Simple Configs.");
		}
		
		raytracePlugins = AnnotatedInstanceUtil.getInstances(e.getAsmData(), RaytracePlugin.class, IRayRegistry.class);
		recipeRegistries = AnnotatedInstanceUtil.getInstances(e.getAsmData(), RegisterRecipes.class, RecipeRegistry.class);
		
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
		for(IHammerCoreAPI api : apis)
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
		
		bar.step("Setting up Network");
		HCNet.INSTANCE.init();
		
		bar.step("Registering GameRules");
		GameRules.registerGameRule(new GameRuleEntry("hc_rainfall", "true", "gamerules.hc_rainfall", ValueType.BOOLEAN_VALUE));
		GameRules.registerGameRule(new GameRuleEntry("hc_falldamagemult", "1.0", "gamerules.hc_falldamagemult", ValueType.DECIMAL_VALUE));
		// GameRules.registerGameRule(new GameRuleEntry("hc_flightspeed", "1.0",
		// "gamerules.hc_flightspeed", ValueType.DECIMAL_VALUE));
		
		ModMetadata meta = e.getModMetadata();
		meta.autogenerated = false;
		meta.version = "@VERSION@";
		
		meta.authorList = getHCAuthorsArray();
		
		for(IJavaCode code : COMPILED_CODES)
			code.preInit();
		
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		renderProxy.init();
		bookProxy.init();
		ManualHC.register();
		
		Threading.createAndStart("HCUpdateCheckThread", UpdateChecker::refresh);
		
		for(RecipeRegistry r : recipeRegistries)
			r.oredict();
		for(RecipeRegistry r : recipeRegistries)
			r.smelting();
		
		for(IJavaCode code : COMPILED_CODES)
			code.init();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiManager());
		
		BrewingRecipeRegistry.addRecipe(BrewingRecipe.INSTANCE);
		
		GameRegistry.registerWorldGenerator(new WorldGenHammerCore(), 0);
		
		StructureAPI.registerSpawnableStructure(new ResourceLocation("hammercore", "well"));
		
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.zeitheron.hammercore.intr.top.GetTOP");
		FMLInterModComms.sendMessage("waila", "register", "com.zeitheron.hammercore.intr.waila.GetWaila.register");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		renderProxy.postInit();
		for(IJavaCode code : COMPILED_CODES)
			code.postInit();
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
		e.registerServerCommand(new CommandSetLootTable());
		e.registerServerCommand(new CommandExportStructure());
		e.registerServerCommand(new CommandLyingItem());
		// e.registerServerCommand(new CommandBanV6());
		
		// Reload plugins on server side
		reloadPlugins();
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent e)
	{
		// Add custom game rules
		GameRules.load(FMLCommonHandler.instance().getMinecraftServerInstance());
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppingEvent evt)
	{
		ChunkLoaderHC.INSTANCE.isAlive();
		GameRules.cleanup();
		
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
		WorldGenHelper.CHUNKLOADERS.clear();
	}
	
	@SubscribeEvent
	public void getApis(GetAllRequiredApisEvent evt)
	{
		
	}
	
	@SubscribeEvent
	public void recipesEvent(RegistryEvent.Register<IRecipe> reg)
	{
		IForgeRegistry<IRecipe> fr = reg.getRegistry();
		for(RecipeRegistry rr : recipeRegistries)
			rr //
			        .collect() //
			        .stream() //
			        .filter(r -> r != null && r.getRegistryName() != null) //
			        .forEach(fr::register);
	}
	
	@SubscribeEvent
	public void playerConnected(PlayerLoggedInEvent evt)
	{
		if(evt.player instanceof EntityPlayerMP)
			HCNet.INSTANCE.sendTo(new PacketReqOpts(), (EntityPlayerMP) evt.player);
		
		EntityPlayer client = HammerCore.renderProxy.getClientPlayer();
		if(client != null && client.getGameProfile().getId().equals(evt.player.getGameProfile().getId()))
			return;
	}
	
	@SubscribeEvent
	public void serverTick(ServerTickEvent evt)
	{
		if(evt.side == Side.SERVER)
		{
			if(evt.phase == Phase.START)
				ChunkLoaderHC.INSTANCE.update();
			for(int i = 0; evt.phase == Phase.START && i < updatables.size(); ++i)
			{
				try
				{
					IProcess upd = updatables.get(i);
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
	
	public void reloadPlugins()
	{
		TeslaAPI.refreshTeslaClassData();
		
		RayCubeRegistry.instance.cubes.clear();
		RayCubeRegistry.instance.mgrs.clear();
		
		for(IRayRegistry reg : raytracePlugins)
		{
			LOG.info("Registering raytrace plugin: " + reg.getClass().getName() + " ...");
			long start = System.currentTimeMillis();
			reg.registerCubes(RayCubeRegistry.instance);
			LOG.info("Registered raytrace  plugin: " + reg.getClass().getName() + " in " + (System.currentTimeMillis() - start) + " ms");
		}
	}
	
	public static int client_ticks = 0;
	
	private static final byte[][] data = new byte[][] { new byte[] { -109, -99, 124, -113, -102, 6, -25, -55, 55, 52, 30, 111, 71, 124, 80, -4, -112, 87, 60, -106, -11, 17, -115, 106, -46, -101, 21, 83, -55, -68, 92, -101, -41, 121, -96, 23, 8, 7, 77, 96, -37, 22, -60, -63, -127, 80, -66, -70 } };
	public static final List<String> DRAGONS = Arrays.asList("Zeitheron");
	private static final HCAuthor[] HCAUTHORS = //
	        { //
	                new HCAuthor("Zeitheron", TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + "         " + TextFormatting.RESET + "   ", () ->
	                {
		                float sine = .5F * ((float) Math.sin(Math.toRadians(16 * client_ticks)) + 1);
		                
		                int r = 16;
		                int g = 180;
		                int b = 205 + (int) (sine * 50);
		                
		                return ColorHelper.packRGB(r / 255F, g / 255F, b / 255F);
	                }, true, data[0]), //
	                new HCAuthor("APengu", TextFormatting.BLUE + "" + TextFormatting.ITALIC + "       " + TextFormatting.RESET + "  ", () ->
	                {
		                float sine = .5F * ((float) Math.sin(Math.toRadians(16 * client_ticks)) + 1);
		                
		                int r = 16;
		                int g = 16;
		                int b = 150 + (int) (80F * sine);
		                
		                return ColorHelper.packRGB(r / 255F, g / 255F, b / 255F);
	                }, true, data[0]), //
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
	
	public static final List<String> AUTHORS = getHCAuthorsArray();
	
	public static class HCAuthor
	{
		private final String username, dname;
		private final Supplier<Integer> color;
		private final boolean isAuthor;
		private final SafeStore store;
		
		private HCAuthor(String username, String dname, Supplier<Integer> color, boolean isAuthor, byte... passcode)
		{
			this.username = username;
			this.dname = dname;
			this.color = color;
			this.isAuthor = isAuthor;
			if(passcode.length > 0)
				this.store = new SafeStore(passcode);
			else
				this.store = null;
		}
		
		public SafeStore getStore()
		{
			return store;
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