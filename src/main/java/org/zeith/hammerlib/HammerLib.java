package org.zeith.hammerlib;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.annotations.client.TileRenderer;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;
import org.zeith.hammerlib.api.multipart.MultipartBlock;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.core.adapter.ConfigAdapter;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.core.adapter.RegistryAdapter;
import org.zeith.hammerlib.core.command.CommandHammerLib;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.mixins.RegistryManagerAccessor;
import org.zeith.hammerlib.proxy.HLClientProxy;
import org.zeith.hammerlib.proxy.HLCommonProxy;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.charging.ItemChargeHelper;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod(HLConstants.MOD_ID)
public class HammerLib
{
	public static final Logger LOG = LogManager.getLogger("HammerLib");
	public static final HLCommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> HLClientProxy::new, () -> HLCommonProxy::new);

	public static final IEventBus EVENT_BUS = BusBuilder.builder().build();

	{
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		MinecraftForge.EVENT_BUS.register(PROXY);
		MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

		LanguageAdapter.registerMod(HLConstants.MOD_ID);

		TagsHL.init();

		ItemChargeHelper.setup();

		// Register all recipe providers
		ScanDataHelper.lookupAnnotatedObjects(ProvideRecipes.class).forEach(data ->
		{
			Class<?> c = data.getOwnerClass();
			if(IRecipeProvider.class.isAssignableFrom(c))
			{
				IRecipeProvider provider = (IRecipeProvider) UnsafeHacks.newInstance(c);
				if(provider != null) HammerLib.EVENT_BUS.addListener(provider::provideRecipes);
			}
		});

		if(Environment.get().getDist() == Dist.CLIENT)
			ScanDataHelper.lookupAnnotatedObjects(TileRenderer.class).forEach(data ->
			{
				if(data.getTargetType() == ElementType.FIELD)
					data.getOwnerMod()
							.ifPresent(mc ->
							{
								mc.getEventBus().addListener(PROXY.addTESR(data.clazz(), data.getMemberName(), data.getProperty("value").map(Type.class::cast).orElse(null)));
							});
			});

		if(RegistryManager.ACTIVE instanceof RegistryManagerAccessor activeRegistries)
		{
			// Register all content providers
			ScanDataHelper.lookupAnnotatedObjects(SimplyRegister.class).forEach(data ->
			{
				if(data.getTargetType() == ElementType.TYPE)
					data.getOwnerMod()
							.ifPresent(mc ->
							{
								for(var registry : activeRegistries.getRegistries().values())
									mc.getEventBus()
											.addGenericListener(registry.getRegistrySuperType(), (Consumer<RegistryEvent.Register>) event ->
													RegistryAdapter.register(event.getRegistry(), data.getOwnerClass(), mc.getModId())
											);
							});
			});
		} else
			throw new RuntimeException("Unable to cast RegistryManager to RegistryManagerAccessor. Mixin apply failed?");

		// Prepare configs
		ConfigAdapter.setup();

		List<ModAnnotation.EnumHolder> bothSides = Stream.of(Dist.values())
				.map(dst -> new ModAnnotation.EnumHolder("Lnet/minecraftforge/api/distmarker/Dist;", dst.name()))
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
						.ifPresent(b -> b.addListener((Consumer<FMLClientSetupEvent>) event -> RegistryAdapter.clientSetup(event, data.getOwnerClass(), data.getMemberName())));
			}
		});

		NBTSerializationHelper.construct();
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void clientSetup(FMLClientSetupEvent e)
	{
		PROXY.clientSetup();
	}

	@SubscribeEvent
	public void finish(FMLLoadCompleteEvent e)
	{
		PROXY.finishLoading();
	}

	@SubscribeEvent
	public void newRegistries(NewRegistryEvent e)
	{
		e.create(new RegistryBuilder<MultipartBlock>()
				.setType(MultipartBlock.class)
				.setName(new ResourceLocation(HLConstants.MOD_ID, "multiparts")));
	}

	public void registerCommands(RegisterCommandsEvent e)
	{
		CommandHammerLib.register(e.getDispatcher());
	}

	public static boolean logHLEvents = String.valueOf(System.getProperty("hammerlib.logevents")).toLowerCase(Locale.ROOT).contains("true");

	public static boolean postEvent(Event evt)
	{
		if(logHLEvents || ConfigHL.INSTANCE.getCurrent().internal.logHLBusEvents)
			HammerLib.LOG.info("[HammerLib.postEvent] " + evt);
		return HammerLib.EVENT_BUS.post(evt);
	}
}