package org.zeith.hammerlib.net;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;
import org.zeith.hammerlib.util.mcf.ModHelper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Network
{
	public static final ResourceLocation MAIN_CHANNEL = HLConstants.id("main");
	
	@SubscribeEvent
	private static void initialize(RegisterPayloadHandlerEvent event)
	{
		HammerLib.LOG.info("Setup HammerLib networking!");
		var reg = event.registrar(HLConstants.MOD_ID)
				.versioned(ModHelper.getModVersion(HLConstants.MOD_ID));
		reg.common(MAIN_CHANNEL, PlainHLMessage::new, PlainHLMessage::handle);
	}
	
	///
	
	public static void sendTo(Player player, IPacket packet)
	{
		sendTo(packet, player);
	}
	
	public static void sendTo(ServerPlayer player, IPacket packet)
	{
		sendTo(packet, player);
	}
	
	public static void sendTo(IPacket packet, Player player)
	{
		if(packet != null && player instanceof ServerPlayer sp)
			PacketDistributor.PLAYER.with(sp).send(toPlain(packet));
	}
	
	public static void sendTo(IPacket packet, ServerPlayer player)
	{
		if(player != null && packet != null)
			PacketDistributor.PLAYER.with(player).send(toPlain(packet));
	}
	
	public static void sendToTracking(LevelChunk chunk, IPacket packet)
	{
		sendToTracking(packet, chunk);
	}
	
	public static void sendToTracking(IPacket packet, LevelChunk chunk)
	{
		if(packet != null && chunk != null)
			PacketDistributor.TRACKING_CHUNK.with(chunk).send(toPlain(packet));
	}
	
	public static void sendToTracking(BlockEntity tile, IPacket packet)
	{
		sendToTracking(packet, tile);
	}
	
	public static void sendToTracking(IPacket packet, BlockEntity tile)
	{
		if(packet != null && tile != null && tile.hasLevel() && !tile.getLevel().isClientSide)
			sendToTracking(packet, tile.getLevel().getChunkAt(tile.getBlockPos()));
	}
	
	public static void sendToTracking(Entity entity, IPacket packet)
	{
		sendToTracking(packet, entity);
	}
	
	public static void sendToTracking(IPacket packet, Entity entity)
	{
		if(packet != null && entity != null)
			PacketDistributor.TRACKING_ENTITY.with(entity).send(toPlain(packet));
	}
	
	public static void sendToTrackingAndSelf(Entity entity, IPacket packet)
	{
		sendToTrackingAndSelf(packet, entity);
	}
	
	public static void sendToTrackingAndSelf(IPacket packet, Entity entity)
	{
		if(packet != null && entity != null)
			PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity).send(toPlain(packet));
	}
	
	public static void sendToDimension(Level dim, IPacket packet)
	{
		sendToDimension(packet, dim.dimension());
	}
	
	public static void sendToDimension(ResourceKey<Level> dim, IPacket packet)
	{
		sendToDimension(packet, dim);
	}
	
	public static void sendToDimension(IPacket packet, ResourceKey<Level> dim)
	{
		if(dim == null || packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			PacketDistributor.DIMENSION.with(dim).send(toPlain(packet));
	}
	
	public static void sendToAll(IPacket packet)
	{
		if(packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			PacketDistributor.ALL.noArg().send(toPlain(packet));
	}
	
	public static void sendToArea(HLTargetPoint point, IPacket packet)
	{
		sendToArea(point.toForge().get(), packet);
	}
	
	public static void sendToArea(PacketDistributor.TargetPoint point, IPacket packet)
	{
		if(point == null || packet == null) return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			PacketDistributor.NEAR.with(point).send(toPlain(packet));
	}
	
	public static void sendToServer(IPacket packet)
	{
		if(packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.CLIENT)
			PacketDistributor.SERVER.noArg().send(toPlain(packet));
	}
	
	///
	
	public static PlainHLMessage toPlain(IPacket packet)
	{
		return new PlainHLMessage(packet);
	}
	
	///
	
	public static void swingHand(Player player, InteractionHand hand)
	{
		ServerPlayer spe = Cast.cast(player, ServerPlayer.class);
		if(spe != null)
			spe.serverLevel().getChunkSource().broadcastAndSend(spe, new ClientboundAnimatePacket(player, hand == InteractionHand.MAIN_HAND ? 0 : 3));
	}
}