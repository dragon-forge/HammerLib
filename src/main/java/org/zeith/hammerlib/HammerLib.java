package org.zeith.hammerlib;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.compat.base.CompatList;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.core.adapter.*;
import org.zeith.hammerlib.core.command.CommandHammerLib;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.core.scans.*;
import org.zeith.hammerlib.core.scans.base.DataScanner;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.mixins.RegistryManagerAccessor;
import org.zeith.hammerlib.proxy.*;
import org.zeith.hammerlib.tiles.tooltip.own.impl.TooltipRenderEngine;
import org.zeith.hammerlib.util.CommonMessages;
import org.zeith.hammerlib.util.ZeithLinkRepository;
import org.zeith.hammerlib.util.charging.ItemChargeHelper;

import java.util.Locale;

@Mod(HLConstants.MOD_ID)
public class HammerLib
{
	public static final Logger LOG = LogManager.getLogger("HammerLib");
	public static final HLCommonProxy PROXY = IProxy.create(() -> HLClientProxy::new, () -> HLCommonProxy::new);
	public static final IEventBus EVENT_BUS = BusBuilder.builder().build();
	
	public static final CompatList<BaseHLCompat> HL_COMPAT_LIST = CompatList.gather(BaseHLCompat.class);
	
	public HammerLib()
	{
		CommonMessages.printMessageOnIllegalRedistribution(HammerLib.class,
				LOG, "HammerLib", "https://www.curseforge.com/minecraft/mc-mods/hammer-lib");
		
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		PROXY.construct(FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(PROXY);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
		
		LanguageAdapter.registerMod(HLConstants.MOD_ID);
		
		TagsHL.init();
		ZeithLinkRepository.initialize(); // Ask to initialize the link repository offthread somewhere.
		
		DataScanner data = DataScanner.start();
		data.add(ItemChargeHelper.create());
		data.add(ScanRecipes.create());
		data.add(ScanTabs.create());
		data.add(ScanRegisters.create());
		data.add(ScanSetups.create());
		data.add(ConfigAdapter.create());
		data.add(NBTSerializationHelper.create());
		PROXY.appendScans(data);
		DataScanner.finish(data);
		
		if(RegistryManager.ACTIVE instanceof RegistryManagerAccessor activeRegistries)
		{
			for(var registry : activeRegistries.getRegistries().values())
			{
				var superType = RegistryMapping.getSuperType(registry);
				if(superType == null)
					LOG.error("Found registry without defined super type: {}", registry.getRegistryKey());
			}
		} else
			throw new RuntimeException("Unable to cast RegistryManager to RegistryManagerAccessor. Mixin apply failed?");
	}
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(HLConstants.MOD_ID, path);
	}
	
	@SubscribeEvent
	public void constructMod(FMLConstructModEvent e0)
	{
		ModList.get().forEachModContainer((modid, container) ->
		{
			if(container instanceof FMLModContainer ctr)
				FingerprintCheckAdapter.service(ctr);
		});
	}
	
	@SubscribeEvent
	public void checkFingerprint(FMLFingerprintCheckEvent e)
	{
		CommonMessages.printMessageOnFingerprintViolation(e, "97e852e9b3f01b83574e8315f7e77651c6605f2b455919a7319e9869564f013c",
				LOG, "HammerLib", "https://www.curseforge.com/minecraft/mc-mods/hammer-lib");
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void clientSetup(RegisterGuiOverlaysEvent e)
	{
		e.registerAboveAll("tooltip_engine", new TooltipRenderEngine());
	}
	
	@SubscribeEvent
	public void finish(FMLLoadCompleteEvent e)
	{
		PROXY.finishLoading();
		CreativeTabAdapter.deque();
	}
	
	public void registerCommands(RegisterCommandsEvent e)
	{
		CommandHammerLib.register(e.getDispatcher());
	}
	
	public static boolean logHLEvents = String.valueOf(System.getProperty("hammerlib.logevents")).toLowerCase(Locale.ROOT).contains("true");
	
	public static boolean postEvent(Event evt)
	{
		ConfigHL cfgs = ConfigHL.INSTANCE.getCurrent();
		if(logHLEvents || (cfgs != null && cfgs.internal.logHLBusEvents))
			HammerLib.LOG.info("[HammerLib.postEvent] " + evt);
		return HammerLib.EVENT_BUS.post(evt);
	}
	
	public static boolean postEvent(Event evt, IEventBusInvokeDispatcher dispatcher)
	{
		ConfigHL cfgs = ConfigHL.INSTANCE.getCurrent();
		if(logHLEvents || (cfgs != null && cfgs.internal.logHLBusEvents))
			HammerLib.LOG.info("[HammerLib.postEvent] " + evt);
		return HammerLib.EVENT_BUS.post(evt, dispatcher);
	}
}