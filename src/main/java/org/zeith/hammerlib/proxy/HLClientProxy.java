package org.zeith.hammerlib.proxy;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import net.minecraft.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.client.ext.*;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.inv.IScreenContainer;
import org.zeith.hammerlib.api.items.tooltip.TooltipColoredLine;
import org.zeith.hammerlib.api.items.tooltip.TooltipMulti;
import org.zeith.hammerlib.api.lighting.ColoredLight;
import org.zeith.hammerlib.api.lighting.HandleLightOverrideEvent;
import org.zeith.hammerlib.api.lighting.impl.IGlowingEntity;
import org.zeith.hammerlib.api.proxy.IClientProxy;
import org.zeith.hammerlib.client.adapter.ChatMessageAdapter;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.client.model.SimpleModelGenerator;
import org.zeith.hammerlib.client.render.tile.IBESR;
import org.zeith.hammerlib.client.render.tile.TESRBase;
import org.zeith.hammerlib.client.utils.TexturePixelGetter;
import org.zeith.hammerlib.core.adapter.*;
import org.zeith.hammerlib.core.items.tooltip.ClientTooltipColoredLine;
import org.zeith.hammerlib.core.items.tooltip.ClientTooltipMulti;
import org.zeith.hammerlib.core.scans.ScanRequireStencil;
import org.zeith.hammerlib.core.scans.base.DataScanner;
import org.zeith.hammerlib.core.scans.base.IAnnotationScanListener;
import org.zeith.hammerlib.event.client.ClientLoadedInEvent;
import org.zeith.hammerlib.mixins.client.ParticleEngineAccessor;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.PacketPlayerReady;
import org.zeith.hammerlib.net.packets.PingServerPacket;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;
import org.zeith.hammerlib.util.mcf.RunnableReloader;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
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
		NeoForge.EVENT_BUS.addListener(this::clientTick);
	}
	
	@Override
	public void queueTask(Level level, int delay, Runnable task)
	{
		super.queueTask(level, delay, task);
		if(level.isClientSide)
			clientTickTasks.add(new QueuedTask(delay, task));
	}
	
	@Override
	public void appendScans(DataScanner data)
	{
		data.add(IAnnotationScanListener.forAnnotation(FlowguiReader.class, ElementType.TYPE, FlowguiRegistry::handleReader));
		data.add(IAnnotationScanListener.forAnnotation(XmlFlowgui.class, ElementType.TYPE, FlowguiRegistry::handleXml));
		data.add(ScanRequireStencil.create());
	}
	
	@Override
	public void construct(IEventBus modBus)
	{
		modBus.addListener(this::registerKeybinds);
		modBus.addListener(this::modelBake);
		modBus.addListener(this::registerClientTooltips);
		modBus.addListener(this::loadComplete);
		modBus.addListener(this::registerGuis);
		modBus.addListener(this::registerClientExtensions);
		modBus.addListener(TexturePixelGetter::reloadTexture);
		modBus.addListener(this::clientSetup);
		modBus.addListener(this::registerReloadListeners);
		SimpleModelGenerator.setup();
		
		NeoForge.EVENT_BUS.register(this);
	}
	
	private void registerReloadListeners(AddClientReloadListenersEvent e)
	{
		e.addListener(HLConstants.id("flowgui"), RunnableReloader.of(FlowguiRegistry::reload));
	}
	
	private void registerClientExtensions(RegisterClientExtensionsEvent e)
	{
		for(var c : BuiltInRegistries.BLOCK)
			if(c instanceof IClientBlockExtensionHolder h)
				h.initializeClient(ext -> e.registerBlock(ext, c));
		
		for(var c : NeoForgeRegistries.FLUID_TYPES)
			if(c instanceof IClientFluidExtensionHolder h)
				h.initializeClient(ext -> e.registerFluidType(ext, c));
		
		for(var c : BuiltInRegistries.ITEM)
			if(c instanceof IClientItemExtensionHolder h)
				h.initializeClient(ext -> e.registerItem(ext, c));
		
		for(var c : BuiltInRegistries.MOB_EFFECT)
			if(c instanceof IClientMobEffectExtensionHolder h)
				h.initializeClient(ext -> e.registerMobEffect(ext, c));
	}
	
	private void loadComplete(FMLLoadCompleteEvent e)
	{
		FingerprintCheckAdapter.loadComplete();
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
	
	@SuppressWarnings({ "rawtypes", "unchecked", "DataFlowIssue" })
	public void registerGuis(RegisterMenuScreensEvent e)
	{
		e.register(ContainerAPI.TILE_CONTAINER, (MenuScreens.ScreenConstructor) (ctr, inv, txt) -> Cast
				.optionally(ctr, IScreenContainer.class)
				.map(c -> c.openScreen(inv, txt))
				.orElse(null)
		);
	}
	
	private void clientSetup(FMLClientSetupEvent e)
	{
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
			HandleLightOverrideEvent evt = new HandleLightOverrideEvent(particle, partialTicks, l);
			HammerLib.postEvent(evt);
			return evt.getNewLight();
		}).filter(Objects::nonNull);
	}
	
	@Override
	public Consumer<FMLClientSetupEvent> addTESR(BlockEntityType<?> type, Class<?> anyTesr)
	{
		return e ->
		{
			ResourceLocation name = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type);
			
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
											"Unable to create BlockEntityRenderer(no-args) for BlockEntityType " +
											name, err
									));
								}
							};
						}
					} catch(ReflectiveOperationException err)
					{
						throw new ReportedException(new CrashReport(
								"Unable to create BlockEntityRenderer(no-args) for BlockEntityType " +
								name, err
						));
					}
				}
			}
			
			if(theTesr == null)
				throw new RuntimeException("Unable to find a valid constructor for " + name + "'s TESR " + anyTesr);
			
			Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<?>> finalTheTesr = theTesr;
			BlockEntityRenderers.register(type, (BlockEntityRendererProvider<BlockEntity>) ctx -> Cast.cast(finalTheTesr.apply(ctx)));
		};
	}
	
	@Override
	public Consumer<RegisterParticleProvidersEvent> addParticleTypeProvider(ParticleType<?> type, Class<?> providerCls)
	{
		return e ->
		{
			ResourceLocation name = BuiltInRegistries.PARTICLE_TYPE.getKey(type);
			
			if(name == null)
			{
				HammerLib.LOG.info(
						"Skipping Particles for particle type " + type + " as it is not registered.");
				return;
			}
			
			HammerLib.LOG.info("Registering ParticleProvider for particle type " + name);
			
			if(ParticleProvider.Sprite.class.isAssignableFrom(providerCls))
			{
				try
				{
					var spc = providerCls.asSubclass(ParticleProvider.Sprite.class)
							.getDeclaredConstructor();
					spc.setAccessible(true);
					e.registerSprite(type, spc.newInstance());
					return;
				} catch(ReflectiveOperationException ex)
				{
					throw new ReportedException(new CrashReport(
							"Unable to create ParticleProvider.Sprite(no-args) for ParticleType " +
							name, ex
					));
				}
			}
			
			if(ParticleEngine.SpriteParticleRegistration.class.isAssignableFrom(providerCls))
			{
				try
				{
					var spc = providerCls.asSubclass(ParticleEngine.SpriteParticleRegistration.class)
							.getDeclaredConstructor();
					spc.setAccessible(true);
					e.registerSpriteSet(type, spc.newInstance());
					return;
				} catch(ReflectiveOperationException ex)
				{
					throw new ReportedException(new CrashReport(
							"Unable to create ParticleProvider.Sprite(no-args) for ParticleType " +
							name, ex
					));
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
						e.registerSpecial(type, Cast.cast(ctor.newInstance()));
					} catch(ReflectiveOperationException ex)
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
						e.registerSpriteSet(type, set ->
								{
									try
									{
										return Cast.cast(ctor.newInstance(set));
									} catch(ReflectiveOperationException ex)
									{
										throw new ReportedException(new CrashReport(
												"Unable to create ParticleProvider(no-args) for ParticleType " +
												name, ex
										));
									}
								}
						);
						return;
					}
				}
			}
		};
	}
	
	boolean renderedWorld = false;
	
	@SubscribeEvent
	public void renderWorldLast(RenderLevelStageEvent e)
	{
		if(!renderedWorld)
		{
			Network.sendToServer(new PacketPlayerReady());
			HammerLib.postNeoEvent(new ClientLoadedInEvent());
			renderedWorld = true;
		}
	}
	
	private void clientTick(ClientTickEvent.Pre e)
	{
		var mc = Minecraft.getInstance();
		
		if(mc.level != null)
		{
			Component c;
			while((c = ChatMessageAdapter.clientTick()) != null)
				mc.chatListener.handleSystemMessage(c, false);
		}
		
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
		List<String> tip = f3.getLeft();
		tip.add(ChatFormatting.GOLD + "[HammerLib]" + ChatFormatting.RESET + " Ping: ~" +
				PingServerPacket.lastPingTime + " ms.");
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