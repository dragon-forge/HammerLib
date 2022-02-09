package org.zeith.hammerlib.proxy;

import com.google.common.base.Predicates;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.inv.IScreenContainer;
import org.zeith.hammerlib.api.lighting.ColoredLight;
import org.zeith.hammerlib.api.lighting.HandleLightOverrideEvent;
import org.zeith.hammerlib.api.lighting.impl.IGlowingEntity;
import org.zeith.hammerlib.client.render.tile.IBESR;
import org.zeith.hammerlib.client.render.tile.TESRBase;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.PingServerPacket;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class HLClientProxy
		extends HLCommonProxy
{
	public static Map<ParticleRenderType, Queue<Particle>> PARTICLE_MAP;

	private Map<String, String> languageList;

	int pingTimer;

	@Override
	public void clientSetup()
	{
		MenuScreens.register(ContainerAPI.TILE_CONTAINER, (MenuScreens.ScreenConstructor) (ctr, inv, txt) -> Cast
				.optionally(ctr, IScreenContainer.class)
				.map(c -> c.openScreen(inv, txt))
				.orElse(null));

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
			HammerLib.postEvent(evt);
			return evt.getNewLight();
		}).filter(Predicates.notNull());
	}

	@Override
	public Consumer<FMLClientSetupEvent> addTESR(Class<?> owner, String member, Type tesr)
	{
		return e ->
		{
			ReflectionUtil.<BlockEntityType<?>> getStaticFinalField(owner, member)
					.ifPresent(type ->
					{
						if(type.getRegistryName() == null)
						{
							HammerLib.LOG.info("Skipping TESR for tile " + type.getRegistryName() + " as it is not registered.");
							return;
						}

						HammerLib.LOG.info("Registering TESR for tile " + type.getRegistryName());

						Class<?> anyTesr = ReflectionUtil.fetchClass(tesr);

						Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<?>> theTesr = null;

						if(IBESR.class.isAssignableFrom(anyTesr))
						{
							try
							{
								Constructor<?> ctor = anyTesr.getDeclaredConstructor();
								ctor.setAccessible(true);
								TESRBase<?> base = new TESRBase<>((IBESR<?>) ctor.newInstance());
								theTesr = ctx -> base;
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
									{
										BlockEntityRenderer<?> r = (BlockEntityRenderer<?>) ctr.newInstance();
										theTesr = c -> r;
									} else if(ctr.getParameterCount() == 1 && ctr.getParameterTypes()[0] == BlockEntityRendererProvider.Context.class)
									{
										theTesr = ctx ->
										{
											try
											{
												return Cast.cast(ctr.newInstance(ctx));
											} catch(ReflectiveOperationException err)
											{
												err.printStackTrace();
											}
											return null;
										};
									}
								} catch(ReflectiveOperationException err)
								{
									err.printStackTrace();
								}
							}
						}

						if(theTesr == null)
							throw new RuntimeException("Unable to find a valid constructor for " + type.getRegistryName() + "'s TESR " + anyTesr);

						Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<?>> finalTheTesr = theTesr;
						BlockEntityRenderers.register(type, (BlockEntityRendererProvider<BlockEntity>) ctx -> Cast.cast(finalTheTesr.apply(ctx)));
					});
		};
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent e)
	{
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
			tip.add(ChatFormatting.GOLD + "[HammerLib]" + ChatFormatting.RESET + " Ping: ~" + PingServerPacket.lastPingTime + " ms.");
			renderF3 = false;
		}
	}

	@Override
	public String getLanguage()
	{
		return Minecraft.getInstance().options.languageCode;
	}

	@Override
	public Player getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}

	@Override
	public ReloadableResourceManager getResourceManager()
	{
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.CLIENT)
			return (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
		return super.getResourceManager();
	}
}