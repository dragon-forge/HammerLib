package org.zeith.hammerlib.proxy;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.ClientLanguageMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.LanguageHelper;
import org.zeith.hammerlib.api.LanguageHelper.LangMap;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.inv.IScreenContainer;
import org.zeith.hammerlib.api.lighting.ColoredLight;
import org.zeith.hammerlib.api.lighting.HandleLightOverrideEvent;
import org.zeith.hammerlib.api.lighting.impl.IGlowingEntity;
import org.zeith.hammerlib.client.render.tile.ITESR;
import org.zeith.hammerlib.client.render.tile.TESRBase;
import org.zeith.hammerlib.event.ResourceManagerReloadEvent;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.PingServerPacket;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HLClientProxy
		extends HLCommonProxy
{
	public static Map<IParticleRenderType, Queue<Particle>> PARTICLE_MAP;

	private LanguageMap langMap;
	private Map<String, String> languageList;

	@Override
	public void applyLang(LangMap lmap)
	{
		langMap = I18n.language;

		if(langMap instanceof ClientLanguageMap)
		{
			ClientLanguageMap clm = (ClientLanguageMap) langMap;
			languageList = clm.storage;
		}

		lmap.apply(languageList);
	}

	int pingTimer;

	@Override
	public void clientSetup()
	{
		ScreenManager.register(ContainerAPI.TILE_CONTAINER, (ScreenManager.IScreenFactory) (ctr, inv, txt) -> Cast
				.optionally(ctr, IScreenContainer.class)
				.map(c -> c.openScreen(inv, txt))
				.orElse(null));

		getResourceManager().registerReloadListener((ISelectiveResourceReloadListener) (resourceManager, resourcePredicate) ->
		{
			if(resourcePredicate.test(VanillaResourceType.LANGUAGES)) LanguageHelper.reloadLanguage();
			MinecraftForge.EVENT_BUS.post(new ResourceManagerReloadEvent(resourcePredicate, resourceManager));
		});

		PARTICLE_MAP = ReflectionUtil.getField(Minecraft.getInstance().particleEngine, 2, Map.class).orElseGet(Collections::emptyMap);
	}

	public static Stream<Particle> streamParticles()
	{
		return PARTICLE_MAP.values().stream().flatMap(Collection::stream);
	}

	@Override
	public Stream<ColoredLight> getGlowingParticles(float partialTicks)
	{
		return streamParticles().map(particle ->
		{
			ColoredLight l = null;
			IGlowingEntity ent = Cast.cast(partialTicks, IGlowingEntity.class);
			if(ent != null) l = ent.produceColoredLight(partialTicks);
			HandleLightOverrideEvent<Particle> evt = new HandleLightOverrideEvent<>(particle, partialTicks, l);
			MinecraftForge.EVENT_BUS.post(evt);
			return evt.getNewLight();
		}).filter(Predicates.notNull());
	}

	@Override
	public Consumer<FMLClientSetupEvent> addTESR(Class<?> owner, String member, Type tesr)
	{
		return e ->
		{
			ReflectionUtil.<TileEntityType<?>> getStaticFinalField(owner, member)
					.ifPresent(type ->
					{
						if(type.getRegistryName() == null)
						{
							HammerLib.LOG.info("Skipping TESR for tile " + type.getRegistryName() + " as it is not registered.");
							return;
						}

						HammerLib.LOG.info("Registering TESR for tile " + type.getRegistryName());

						Class<?> anyTesr = ReflectionUtil.fetchClass(tesr);

						TileEntityRendererDispatcher terd = TileEntityRendererDispatcher.instance;

						TileEntityRenderer<?> theTesr = null;

						if(ITESR.class.isAssignableFrom(anyTesr))
						{
							try
							{
								Constructor<?> ctor = anyTesr.getDeclaredConstructor();
								ctor.setAccessible(true);
								theTesr = new TESRBase<>(terd, (ITESR<?>) ctor.newInstance());
							} catch(ReflectiveOperationException err)
							{
								err.printStackTrace();
							}
						}

						if(theTesr == null)
						{
							for(Constructor<?> ctr : anyTesr.getDeclaredConstructors())
							{
								try
								{
									if(ctr.getParameterCount() == 0)
										theTesr = (TileEntityRenderer<?>) ctr.newInstance();
									else if(ctr.getParameterCount() == 1 && ctr.getParameterTypes()[0] == TileEntityRendererDispatcher.class)
										theTesr = (TileEntityRenderer<?>) ctr.newInstance(terd);
								} catch(ReflectiveOperationException err)
								{
									err.printStackTrace();
								}
							}
						}

						if(theTesr == null)
							throw new RuntimeException("Unable to find a valid constructor for " + type.getRegistryName() + "'s TESR " + anyTesr);

						terd.setSpecialRendererInternal(type, Cast.cast(theTesr));
					});
		};
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent e)
	{
		if(e.phase == Phase.START)
			LanguageHelper.update();
		if(Minecraft.getInstance().level != null && !Minecraft.getInstance().isPaused())
		{
			pingTimer--;
			if(pingTimer <= 0)
			{
				pingTimer += 40;
				Network.sendToServer(new PingServerPacket(System.currentTimeMillis()));
			}
		}
	}

	private boolean renderF3;

	@SubscribeEvent
	public void addF3Info(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() == RenderGameOverlayEvent.ElementType.DEBUG)
			renderF3 = true;
	}

	@SubscribeEvent
	public void addF3Info(RenderGameOverlayEvent.Text f3)
	{
		if(renderF3)
		{
			List<String> tip = f3.getLeft();
			tip.add(TextFormatting.GOLD + "[HammerLib]" + TextFormatting.RESET + " Ping: ~" + PingServerPacket.lastPingTime + " ms.");
			renderF3 = false;
		}
	}

	@Override
	public String getLanguage()
	{
		return Minecraft.getInstance().options.languageCode;
	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}

	@Override
	public IReloadableResourceManager getResourceManager()
	{
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.CLIENT)
			return (IReloadableResourceManager) Minecraft.getInstance().getResourceManager();
		return super.getResourceManager();
	}
}