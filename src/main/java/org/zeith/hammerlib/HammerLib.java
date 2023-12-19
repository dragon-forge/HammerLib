package org.zeith.hammerlib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.fml.javafmlmod.*;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.moddiscovery.ModAnnotation;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.*;
import org.zeith.hammerlib.annotations.*;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.compat.base.CompatList;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.core.adapter.*;
import org.zeith.hammerlib.core.command.CommandHammerLib;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.proxy.*;
import org.zeith.hammerlib.tiles.tooltip.own.impl.TooltipRenderEngine;
import org.zeith.hammerlib.util.*;
import org.zeith.hammerlib.util.charging.ItemChargeHelper;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

@Mod(HLConstants.MOD_ID)
public class HammerLib
{
	public static final Logger LOG = LogManager.getLogger("HammerLib");
	public static final HLCommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> HLClientProxy::new, () -> HLCommonProxy::new);
	public static final IEventBus EVENT_BUS = BusBuilder.builder().build();
	
	public static final CompatList<BaseHLCompat> HL_COMPAT_LIST = CompatList.gather(BaseHLCompat.class);
	
	public HammerLib()
	{
		CommonMessages.printMessageOnIllegalRedistribution(HammerLib.class,
				LOG, "HammerLib", "https://www.curseforge.com/minecraft/mc-mods/hammer-lib"
		);
		
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		PROXY.construct(FMLJavaModLoadingContext.get().getModEventBus());
		NeoForge.EVENT_BUS.register(PROXY);
		NeoForge.EVENT_BUS.addListener(this::registerCommands);
		
		LanguageAdapter.registerMod(HLConstants.MOD_ID);
		
		TagsHL.init();
		ZeithLinkRepository.initialize(); // Ask to initialize the link repository offthread somewhere.
		
		ItemChargeHelper.setup();
		
		// Register all recipe providers
		ScanDataHelper.lookupAnnotatedObjects(ProvideRecipes.class).forEach(data ->
		{
			var ow = data.getOwnerMod().orElse(null);
			if(ow == null)
			{
				LOG.info("Skipping mod-less @ProvideRecipes annotation in " + data.getOwnerClass());
				return;
			}
			
			Class<?> c = data.getOwnerClass();
			if(IRecipeProvider.class.isAssignableFrom(c))
			{
				IRecipeProvider provider = (IRecipeProvider) UnsafeHacks.newInstance(c);
				if(provider != null)
				{
					var bus = ow.getEventBus();
					bus.addListener(provider::provideRecipes);
					bus.addListener(provider::spoofRecipes);
				}
			}
		});
		
		ScanDataHelper.lookupAnnotatedObjects(CreativeTab.RegisterTab.class).forEach(data ->
		{
			if(data.getTargetType() == ElementType.FIELD)
				data.getOwnerMod().ifPresent(mc -> mc.getEventBus().addListener((Consumer<RegisterEvent>) e ->
				{
					var registrar = RegistryAdapter.createRegisterer(e, Registries.CREATIVE_MODE_TAB, null);
					
					registrar.ifPresent(register ->
					{
						Optional<CreativeTab> tab = ReflectionUtil.getStaticFinalField(data.getOwnerClass(), data.getMemberName());
						tab.ifPresent(t0 -> t0.register(t ->
						{
							var tabBuilder = CreativeModeTab.builder();
							t.factory().accept(tabBuilder);
							var ct = tabBuilder.build();
							register.accept(t.id(), ct);
							return ct;
						}));
					});
				}));
		});
		
		// Register all content providers
		ScanDataHelper.lookupAnnotatedObjects(SimplyRegister.class).forEach(data ->
		{
			if(data.getTargetType() == ElementType.TYPE)
				data.getOwnerMod()
						.ifPresent(mc ->
						{
							LOG.info("Hooked " + data.clazz() + " from " + mc.getModId() + " to register it's stuff.");
							mc.getEventBus()
									.addListener((Consumer<RegisterEvent>) event ->
											RegistryAdapter.register(event, data.getOwnerClass(), mc.getModId(), data.getProperty("prefix").map(Objects::toString).orElse(""))
									);
						});
		});
		
		// Prepare configs
		ConfigAdapter.setup();
		
		List<ModAnnotation.EnumHolder> bothSides = Stream.of(Dist.values())
				.map(dst -> new ModAnnotation.EnumHolder("Lnet/minecraftforge/itf/distmarker/Dist;", dst.name()))
				.collect(Collectors.toList());
		
		// Register all setups
		ScanDataHelper.lookupAnnotatedObjects(Setup.class).forEach(data ->
		{
			Object side = data.getProperty("side")
					.orElse(bothSides);
			
			if(side instanceof List<?> lst && !lst.isEmpty())
			{
				for(Object o : lst)
				{
					if(o instanceof ModAnnotation.EnumHolder h && FMLEnvironment.dist.name().equals(h.getValue()))
					{
						if(data.getTargetType() == ElementType.METHOD)
						{
							HammerLib.LOG.info("Injecting setup into " + data.clazz().getClassName());
							data.getOwnerMod()
									.map(FMLModContainer::getEventBus)
									.ifPresent(b -> b.addListener((Consumer<FMLCommonSetupEvent>) event -> RegistryAdapter.setup(event, data.getOwnerClass(), data.getMemberName())));
						}
						
						break;
					}
				}
			} else
				HammerLib.LOG.warn("What the hell is this? " + data.parent.clazz() + "->" + data.getMemberName());
		});
		
		ScanDataHelper.lookupAnnotatedObjects(ClientSetup.class).forEach(data ->
		{
			if(data.getTargetType() == ElementType.METHOD)
			{
				HammerLib.LOG.info("Injecting client-setup into " + data.clazz().getClassName());
				data.getOwnerMod()
						.map(FMLModContainer::getEventBus)
						.ifPresent(b -> b.addListener((Consumer<FMLClientSetupEvent>) event ->
								RegistryAdapter.clientSetup(event, data.getOwnerClass(), data.getMemberName())
						));
			}
		});
		
		NBTSerializationHelper.construct();
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
			{
				var bus = ctr.getEventBus();
				bus.addListener((FMLCommonSetupEvent e) ->
						ctr.getEventBus().post(new FMLFingerprintCheckEvent(ctr))
				);
			}
		});
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void clientSetup(FMLClientSetupEvent e)
	{
		PROXY.clientSetup();
	}
	
	@SubscribeEvent
	public void checkFingerprint(FMLFingerprintCheckEvent e)
	{
		CommonMessages.printMessageOnFingerprintViolation(e, "97e852e9b3f01b83574e8315f7e77651c6605f2b455919a7319e9869564f013c",
				LOG, "HammerLib", "https://www.curseforge.com/minecraft/mc-mods/hammer-lib"
		);
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
	
	public static <T extends Event> T postEvent(T evt)
	{
		ConfigHL cfgs = ConfigHL.INSTANCE.getCurrent();
		if(logHLEvents || (cfgs != null && cfgs.internal.logHLBusEvents))
			HammerLib.LOG.info("[HammerLib.postEvent] " + evt);
		return HammerLib.EVENT_BUS.post(evt);
	}
}