package org.zeith.hammerlib;

import com.google.common.collect.BiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.annotations.TileRenderer;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.api.multipart.MultipartBlock;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.core.adapter.RegistryAdapter;
import org.zeith.hammerlib.core.command.CommandHammerLib;
import org.zeith.hammerlib.core.init.TagsHL;
import org.zeith.hammerlib.proxy.HLClientProxy;
import org.zeith.hammerlib.proxy.HLCommonProxy;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.charging.ItemChargeHelper;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.function.Consumer;

@Mod(HLConstants.MOD_ID)
public class HammerLib
{
	public static final Logger LOG = LogManager.getLogger("HammerLib");
	public static final HLCommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> HLClientProxy::new, () -> HLCommonProxy::new);

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
			Class<?> c = ReflectionUtil.fetchClass(data.getClassType());
			if(IRecipeProvider.class.isAssignableFrom(c))
			{
				IRecipeProvider provider = (IRecipeProvider) UnsafeHacks.newInstance(c);
				if(provider != null) MinecraftForge.EVENT_BUS.addListener(provider::provideRecipes);
			}
		});

		if(Environment.get().getDist() == Dist.CLIENT)
			ScanDataHelper.lookupAnnotatedObjects(TileRenderer.class).forEach(data ->
			{
				if(data.getTargetType() == ElementType.FIELD)
					data.getOwnerMod()
							.ifPresent(mc ->
							{
								mc.getEventBus().addListener(PROXY.addTESR(data.getOwnerClass(), data.getMemberName(), data.getProperty("value").map(Type.class::cast).orElse(null)));
							});
			});

		// Register all content providers
		ScanDataHelper.lookupAnnotatedObjects(SimplyRegister.class).forEach(data ->
		{
			Class<?> registerer = data.getOwnerClass();
			if(data.getTargetType() == ElementType.TYPE)
				data.getOwnerMod()
						.ifPresent(mc ->
						{
							try
							{
								Field f = ReflectionUtil.lookupField(RegistryManager.class, "registries");
								BiMap<ResourceLocation, ForgeRegistry<? extends IForgeRegistryEntry<?>>> registries = Cast.cast(f.get(RegistryManager.ACTIVE));

								registries.values().forEach(registry ->
								{
									mc.getEventBus().addGenericListener(registry.getRegistrySuperType(), (Consumer<RegistryEvent.Register>) event -> RegistryAdapter.register(event.getRegistry(), registerer, mc.getModId()));
								});
							} catch(IllegalAccessException e)
							{
								throw new RuntimeException(e);
							}
						});
		});

		// Register all setups
		ScanDataHelper.lookupAnnotatedObjects(Setup.class).forEach(data ->
		{
			Class<?> registerer = data.getOwnerClass();
			if(data.getTargetType() == ElementType.METHOD)
			{
				HammerLib.LOG.info("Injecting setup into " + registerer);
				data.getOwnerMod()
						.map(FMLModContainer::getEventBus)
						.ifPresent(b -> b.addListener((Consumer<FMLCommonSetupEvent>) event -> RegistryAdapter.setup(event, registerer, data.getMemberName())));
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
	public void finish(FMLLoadCompleteEvent e)
	{
		PROXY.finishLoading();
	}

	@SubscribeEvent
	public void newRegistries(RegistryEvent.NewRegistry e)
	{
		new RegistryBuilder<MultipartBlock>()
				.setType(MultipartBlock.class)
				.setName(new ResourceLocation(HLConstants.MOD_ID, "multiparts"))
				.create();
	}

	public void registerCommands(RegisterCommandsEvent e)
	{
		CommandHammerLib.register(e.getDispatcher());
	}
}