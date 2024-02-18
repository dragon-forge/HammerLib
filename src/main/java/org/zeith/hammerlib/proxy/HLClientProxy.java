package org.zeith.hammerlib.proxy;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.recipes.*;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.inv.IScreenContainer;
import org.zeith.hammerlib.api.items.tooltip.*;
import org.zeith.hammerlib.api.lighting.*;
import org.zeith.hammerlib.api.lighting.impl.IGlowingEntity;
import org.zeith.hammerlib.api.proxy.IClientProxy;
import org.zeith.hammerlib.client.model.SimpleModelGenerator;
import org.zeith.hammerlib.client.render.tile.*;
import org.zeith.hammerlib.client.utils.TexturePixelGetter;
import org.zeith.hammerlib.core.adapter.ConfigAdapter;
import org.zeith.hammerlib.core.items.tooltip.*;
import org.zeith.hammerlib.event.client.ClientLoadedInEvent;
import org.zeith.hammerlib.mixins.client.ParticleEngineAccessor;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.*;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Stream;

public class HLClientProxy
		extends HLCommonProxy
		implements IClientProxy
{
	protected List<QueuedTask> clientTickTasks = new ArrayList<>();
	
	public static final KeyMapping RENDER_GUI_ITEM = new KeyMapping("key.hammerlib.render_item", KeyConflictContext.GUI, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "key.categories.ui");
	
	public static Map<ParticleRenderType, Queue<Particle>> PARTICLE_MAP;
	int pingTimer;
	
	public HLClientProxy()
	{
		MinecraftForge.EVENT_BUS.addListener(this::clientTick);
	}
	
	@Override
	public void queueTask(Level level, int delay, Runnable task)
	{
		if(level.isClientSide)
			clientTickTasks.add(new QueuedTask(delay, task));
		else
			super.queueTask(level, delay, task);
	}
	
	@Override
	public void construct(IEventBus modBus)
	{
		modBus.addListener(this::registerKeybinds);
		modBus.addListener(this::modelBake);
		modBus.addListener(this::registerClientTooltips);
		modBus.addListener(this::loadComplete);
		modBus.addListener(TexturePixelGetter::reloadTexture);
		SimpleModelGenerator.setup();

//		MinecraftForge.EVENT_BUS.addListener(this::alterTooltip);
	}
	
	private void loadComplete(FMLLoadCompleteEvent e)
	{
		for(RecipeType<?> type : ForgeRegistries.RECIPE_TYPES.getValues())
		{
			if(type instanceof IVisualizedRecipeType<?> visual)
			{
				AtomicReference<IRecipeVisualizer<?, ?>> visualizer = new AtomicReference<>();
				visual.initVisuals(visualizer::set);
				var vis = visualizer.get();
				if(vis != null)
					RecipeVisualizationRegistry.register(type, Cast.cast(vis));
			}
		}
	}
	
	private void alterTooltip(RenderTooltipEvent.GatherComponents e)
	{
		int[] colors = TexturePixelGetter.getAllColors(e.getItemStack());
		e.getTooltipElements().add(Either.right(new TooltipColoredLine(colors)));
	}
	
	private void registerClientTooltips(RegisterClientTooltipComponentFactoriesEvent e)
	{
		e.register(TooltipMulti.class, ClientTooltipMulti::new);
		e.register(TooltipColoredLine.class, ClientTooltipColoredLine::new);
	}
	
	private void registerKeybinds(RegisterKeyMappingsEvent e)
	{
		e.register(RENDER_GUI_ITEM);
	}
	
	private void modelBake(ModelEvent.BakingCompleted e)
	{
//		BlockState state = null;
//		e.getModels().put(BlockModelShaper.stateToModelLocation(state), null);
	}
	
	@Override
	public void clientSetup()
	{
		//noinspection DataFlowIssue,rawtypes
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
		}).filter(Objects::nonNull);
	}
	
	@Override
	public Consumer<FMLClientSetupEvent> addTESR(BlockEntityType<?> type, Class<?> anyTesr)
	{
		return e ->
		{
			ResourceLocation name = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(type);
			
			if(name == null)
			{
				HammerLib.LOG.info("Skipping TESR for tile " + type + " as it is not registered.");
				return;
			}
			
			HammerLib.LOG.info("Registering TESR for tile " + name);
			
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
					throw new ReportedException(new CrashReport(
							"Unable to create IBESR(no-args) for BlockEntityType " + name, err));
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
						} else if(ctr.getParameterCount() == 1 &&
								ctr.getParameterTypes()[0] == BlockEntityRendererProvider.Context.class)
						{
							theTesr = ctx ->
							{
								try
								{
									return Cast.cast(ctr.newInstance(ctx));
								} catch(ReflectiveOperationException err)
								{
									throw new ReportedException(new CrashReport(
											"Unable to create BlockEntityRenderer(BlockEntityRendererProvider.Context) for BlockEntityType " +
													name, err));
								}
							};
						}
					} catch(ReflectiveOperationException err)
					{
						throw new ReportedException(new CrashReport(
								"Unable to create BlockEntityRenderer(no-args) for BlockEntityType " +
										name, err));
					}
				}
			}
			
			if(theTesr == null)
				throw new RuntimeException(
						"Unable to find a valid constructor for " + name + "'s TESR " + anyTesr);
			
			Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<?>> finalTheTesr = theTesr;
			BlockEntityRenderers.register(type, (BlockEntityRendererProvider<BlockEntity>) ctx -> Cast.cast(finalTheTesr.apply(ctx)));
		};
	}
	
	@Override
	public Consumer<RegisterParticleProvidersEvent> addParticleTypeProvider(ParticleType<?> type, Class<?> providerCls)
	{
		return e ->
		{
			ResourceLocation name = ForgeRegistries.PARTICLE_TYPES.getKey(type);
			
			if(name == null)
			{
				HammerLib.LOG.info(
						"Skipping Particles for particle type " + type + " as it is not registered.");
				return;
			}
			
			HammerLib.LOG.info("Registering ParticleProvider for particle type " + name);
			
			if(ParticleEngine.SpriteParticleRegistration.class.isAssignableFrom(providerCls))
			{
				try
				{
					var spc = providerCls.asSubclass(ParticleEngine.SpriteParticleRegistration.class)
							.getDeclaredConstructor();
					spc.setAccessible(true);
					e.register(type, spc.newInstance());
					return;
				} catch(ReflectiveOperationException ex)
				{
					throw new ReportedException(new CrashReport(
							"Unable to create ParticleProvider.Sprite(no-args) for ParticleType " +
									name, ex));
				}
			}
			
			var ctors = providerCls.getConstructors();
			for(var ctor : ctors)
			{
				if(ctor.getParameterCount() == 0)
				{
					ctor.setAccessible(true);
					try
					{
						e.register(type, Objects.requireNonNull(Cast.cast(ctor.newInstance(), ParticleProvider.class)));
					} catch(Exception ex)
					{
						throw new ReportedException(new CrashReport(
								"Unable to create ParticleProvider(no-args) for ParticleType " + name, ex));
					}
					return;
				} else if(ctor.getParameterCount() == 1)
				{
					if(SpriteSet.class.isAssignableFrom(ctor.getParameterTypes()[0]))
					{
						ctor.setAccessible(true);
						e.register(type, set ->
						{
							try
							{
								return Cast.cast(ctor.newInstance(set));
							} catch(ReflectiveOperationException ex)
							{
								throw new ReportedException(new CrashReport(
										"Unable to create ParticleProvider(no-args) for ParticleType " +
												name, ex));
							}
						});
						return;
					}
				}
			}
			;
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
	
	private void clientTick(ClientTickEvent e)
	{
		var mc = Minecraft.getInstance();
		
		if(e.phase == TickEvent.Phase.START)
		{
			if(mc.level != null)
			{
				if(!mc.isPaused())
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
			
			return;
		}
		
		if(mc.level == null)
			clientTickTasks.clear();
		
		for(int i = 0; i < clientTickTasks.size(); i++)
		{
			if(clientTickTasks.get(i).shouldRemove())
			{
				clientTickTasks.remove(i);
				--i;
			}
		}
	}
	
	@SubscribeEvent
	public void addF3Info(CustomizeGuiOverlayEvent.DebugText f3)
	{
		if(Minecraft.getInstance().options.renderDebug)
		{
			List<String> tip = f3.getLeft();
			tip.add(ChatFormatting.GOLD + "[HammerLib]" + ChatFormatting.RESET + " Ping: ~" +
					PingServerPacket.lastPingTime + " ms.");
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