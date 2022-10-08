package org.zeith.hammerlib.proxy;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.inv.IScreenContainer;
import org.zeith.hammerlib.api.lighting.ColoredLight;
import org.zeith.hammerlib.api.lighting.HandleLightOverrideEvent;
import org.zeith.hammerlib.api.lighting.impl.IGlowingEntity;
import org.zeith.hammerlib.client.render.tile.IBESR;
import org.zeith.hammerlib.client.render.tile.TESRBase;
import org.zeith.hammerlib.core.adapter.ConfigAdapter;
import org.zeith.hammerlib.event.client.ClientLoadedInEvent;
import org.zeith.hammerlib.mixins.client.ParticleEngineAccessor;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.PacketPlayerReady;
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
	public static KeyMapping RENDER_GUI_ITEM = new KeyMapping("key.hammerlib.render_item", KeyConflictContext.GUI, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "key.categories.ui");
	
	public static Map<ParticleRenderType, Queue<Particle>> PARTICLE_MAP;
	private Map<String, String> languageList;
	int pingTimer;
	
	@Override
	public void construct(IEventBus modBus)
	{
		modBus.addListener(this::registerKeybinds);
	}
	
	private void registerKeybinds(RegisterKeyMappingsEvent e)
	{
		e.register(RENDER_GUI_ITEM);
	}
	
	@Override
	public void clientSetup()
	{
		MenuScreens.register(ContainerAPI.TILE_CONTAINER, (MenuScreens.ScreenConstructor) (ctr, inv, txt) -> Cast
				.optionally(ctr, IScreenContainer.class)
				.map(c -> c.openScreen(inv, txt))
				.orElse(null));
		
		PARTICLE_MAP = ((ParticleEngineAccessor) Minecraft.getInstance().particleEngine).getParticles();
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
	public Consumer<FMLClientSetupEvent> addTESR(Type owner, String member, Type tesr)
	{
		return e ->
		{
			ReflectionUtil.<BlockEntityType<?>> getStaticFinalField(ReflectionUtil.fetchClass(owner), member)
					.ifPresent(type ->
					{
						ResourceLocation name = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(type);
						
						if(name == null)
						{
							HammerLib.LOG.info("Skipping TESR for tile " + type + " as it is not registered.");
							return;
						}
						
						HammerLib.LOG.info("Registering TESR for tile " + name);
						
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
							throw new RuntimeException("Unable to find a valid constructor for " + name + "'s TESR " + anyTesr);
						
						Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<?>> finalTheTesr = theTesr;
						BlockEntityRenderers.register(type, (BlockEntityRendererProvider<BlockEntity>) ctx -> Cast.cast(finalTheTesr.apply(ctx)));
					});
		};
	}
	
	boolean renderedWorld = false;
	
	@SubscribeEvent
	public void renderWorldLast(RenderLevelStageEvent e)
	{
		if(!renderedWorld)
		{
			Network.sendToServer(new PacketPlayerReady());
			MinecraftForge.EVENT_BUS.post(new ClientLoadedInEvent());
			renderedWorld = true;
		}
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent e)
	{
		if(Minecraft.getInstance().level != null)
		{
			if(!Minecraft.getInstance().isPaused())
			{
				pingTimer--;
				if(pingTimer <= 0)
				{
					pingTimer += 40;
					Network.sendToServer(new PingServerPacket(System.currentTimeMillis()));
				}
			}
		} else if(renderedWorld)
		{
			renderedWorld = false;
			ConfigAdapter.resetClientsideSync();
		}
	}
	
	@SubscribeEvent
	public void addF3Info(CustomizeGuiOverlayEvent.DebugText f3)
	{
		if(Minecraft.getInstance().options.renderDebug)
		{
			List<String> tip = f3.getLeft();
			tip.add(ChatFormatting.GOLD + "[HammerLib]" + ChatFormatting.RESET + " Ping: ~" + PingServerPacket.lastPingTime + " ms.");
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