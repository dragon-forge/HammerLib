package org.zeith.hammerlib.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;
import org.zeith.hammerlib.util.mcf.ModHelper;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class Network
{
	public static final CustomPacketPayload.Type<PlainHLMessage> MAIN_CHANNEL = new CustomPacketPayload.Type<>(new ResourceLocation("hammerlib", "main"));
	
	@SubscribeEvent
	private static void initialize(RegisterPayloadHandlersEvent event)
	{
		HammerLib.LOG.info("Setup HammerLib networking!");
		var reg = event.registrar(HLConstants.MOD_ID)
				.versioned(ModHelper.getModVersion(HLConstants.MOD_ID));
		
		reg.commonBidirectional(MAIN_CHANNEL, StreamCodec.ofMember(PlainHLMessage::write, PlainHLMessage::new), PlainHLMessage::handle);
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
			PacketDistributor.sendToPlayer(sp, toPlain(packet));
	}
	
	public static void sendTo(IPacket packet, ServerPlayer player)
	{
		if(player != null && packet != null)
			PacketDistributor.sendToPlayer(player, toPlain(packet));
	}
	
	public static void sendToTracking(LevelChunk chunk, IPacket packet)
	{
		sendToTracking(packet, chunk);
	}
	
	public static void sendToTracking(IPacket packet, LevelChunk chunk)
	{
		if(packet != null && chunk != null && chunk.getLevel() instanceof ServerLevel sl)
			PacketDistributor.sendToPlayersTrackingChunk(sl, chunk.getPos(), toPlain(packet));
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
			PacketDistributor.sendToPlayersTrackingEntity(entity, toPlain(packet));
	}
	
	public static void sendToTrackingAndSelf(Entity entity, IPacket packet)
	{
		sendToTrackingAndSelf(packet, entity);
	}
	
	public static void sendToTrackingAndSelf(IPacket packet, Entity entity)
	{
		if(packet != null && entity != null)
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, toPlain(packet));
	}
	
	public static void sendToDimension(Level dim, IPacket packet)
	{
		if(!(dim instanceof ServerLevel sl) || packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			PacketDistributor.sendToPlayersInDimension(sl, toPlain(packet));
	}
	
	public static void sendToAll(IPacket packet)
	{
		if(packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			PacketDistributor.sendToAllPlayers(toPlain(packet));
	}
	
	public static void sendToArea(HLTargetPoint point, IPacket packet)
	{
		if(point == null || point.dim == null || packet == null) return;
		sendToArea(point.dim, point.excluded, point, point.range, packet);
	}
	
	public static void sendToArea(ServerLevel level, @Nullable ServerPlayer excluded, Vec3 pos, double radius, IPacket packet)
	{
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			PacketDistributor.sendToPlayersNear(level, excluded, pos.x, pos.y, pos.z, radius, toPlain(packet));
	}
	
	public static void sendToServer(IPacket packet)
	{
		if(packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.CLIENT)
			PacketDistributor.sendToServer(toPlain(packet));
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