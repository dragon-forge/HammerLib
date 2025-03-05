package org.zeith.hammerlib;

import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.*;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.compat.base.*;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.core.adapter.*;
import org.zeith.hammerlib.core.command.CommandHammerLib;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.core.scans.*;
import org.zeith.hammerlib.core.scans.base.DataScanner;
import org.zeith.hammerlib.proxy.*;
import org.zeith.hammerlib.tiles.tooltip.own.impl.TooltipRenderEngine;
import org.zeith.hammerlib.util.*;
import org.zeith.hammerlib.util.charging.ItemChargeHelper;

import java.util.Locale;

@Mod(HLConstants.MOD_ID)
public class HammerLib
{
	public static final Logger LOG = LoggerFactory.getLogger(HammerLib.class);
	public static final HLCommonProxy PROXY = IProxy.create(() -> HLClientProxy::new, () -> HLCommonProxy::new);
	public static final IEventBus EVENT_BUS = BusBuilder.builder().build();
	
	private static CompatList<BaseHLCompat> hlCompatList;
	
	public HammerLib(FMLModContainer container, IEventBus modEventBus, Dist dist)
	{
		CommonMessages.printMessageOnIllegalRedistribution(HammerLib.class,
				LOG, "HammerLib", "https://www.curseforge.com/minecraft/mc-mods/hammer-lib"
		);
		
		hlCompatList = CompatList.gather(BaseHLCompat.class, CompatContext.builder(modEventBus).build());
		
		modEventBus.register(this);
		PROXY.construct(modEventBus);
		NeoForge.EVENT_BUS.addListener(this::registerCommands);
		
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
	
	// Fixme: Need to figure out why jar signing results in a null fingerprint (corrupted jdks?!)
//	@SubscribeEvent
//	public void checkFingerprint(FMLFingerprintCheckEvent e)
//	{
//		CommonMessages.printMessageOnFingerprintViolation(e, "97e852e9b3f01b83574e8315f7e77651c6605f2b455919a7319e9869564f013c",
//				LOG, "HammerLib", "https://www.curseforge.com/minecraft/mc-mods/hammer-lib"
//		);
//	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void clientSetup(RegisterGuiLayersEvent e)
	{
		e.registerAboveAll(HLConstants.id("tooltip_engine"), new TooltipRenderEngine());
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
	
	public static <T extends Event> T postEvent(T evt)
	{
		ConfigHL cfgs = ConfigHL.INSTANCE.getCurrent();
		if(logHLEvents || (cfgs != null && cfgs.internal.logHLBusEvents))
			HammerLib.LOG.info("[HammerLib.postEvent] {}", evt);
		return HammerLib.EVENT_BUS.post(evt);
	}
	
	public static <T extends Event> T postNeoEvent(T evt)
	{
		ConfigHL cfgs = ConfigHL.INSTANCE.getCurrent();
		if(logHLEvents || (cfgs != null && cfgs.internal.logNFBusEvents))
			HammerLib.LOG.info("[HammerLib.postNeoEvent] {}", evt);
		return NeoForge.EVENT_BUS.post(evt);
	}
	
	public static CompatList<BaseHLCompat> getHLCompats()
	{
		return hlCompatList;
	}
}