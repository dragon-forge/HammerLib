package org.zeith.hammerlib.net;

import com.google.common.base.Predicates;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SAnimateHandPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

public class Network
{
	public static final ResourceLocation MAIN_CHANNEL = new ResourceLocation("hammerlib", "main");
	private static SimpleChannel channel;

	@Setup
	private static void initialize()
	{
		HammerLib.LOG.info("Setup HammerLib networking!");

		channel = NetworkRegistry.newSimpleChannel(MAIN_CHANNEL, () -> "1", Predicates.alwaysTrue(), Predicates.alwaysTrue());
		channel.registerMessage(1, PlainHLMessage.class, PlainHLMessage::write, PlainHLMessage::new, PlainHLMessage::handle);
	}

	///

	public static void sendTo(PlayerEntity player, IPacket packet)
	{
		sendTo(packet, player);
	}

	public static void sendTo(ServerPlayerEntity player, IPacket packet)
	{
		sendTo(packet, player);
	}

	public static void sendTo(IPacket packet, PlayerEntity player)
	{
		if(player == null || packet == null)
			return;
		if(ServerPlayerEntity.class.isAssignableFrom(player.getClass()))
			channel.send(PacketDistributor.PLAYER.with(Cast.supply(player, ServerPlayerEntity.class)), toPlain(packet));
	}

	public static void sendTo(IPacket packet, ServerPlayerEntity player)
	{
		if(player == null || packet == null) return;
		channel.send(PacketDistributor.PLAYER.with(Cast.supply(player, ServerPlayerEntity.class)), toPlain(packet));
	}

	public static void sendToDimension(World dim, IPacket packet)
	{
		sendToDimension(packet, dim.dimension());
	}

	public static void sendToDimension(RegistryKey<World> dim, IPacket packet)
	{
		sendToDimension(packet, dim);
	}

	public static void sendToDimension(IPacket packet, RegistryKey<World> dim)
	{
		if(dim == null || packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			channel.send(PacketDistributor.DIMENSION.with(Cast.staticValue(dim)), toPlain(packet));
	}

	public static void sendToAll(IPacket packet)
	{
		if(packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			channel.send(PacketDistributor.ALL.noArg(), toPlain(packet));
	}

	public static void sendToArea(HLTargetPoint point, IPacket packet)
	{
		sendToArea(point.toForge().get(), packet);
	}

	public static void sendToArea(TargetPoint point, IPacket packet)
	{
		if(point == null || packet == null) return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			channel.send(PacketDistributor.NEAR.with(Cast.staticValue(point)), toPlain(packet));
	}

	public static void sendToServer(IPacket packet)
	{
		if(packet == null)
			return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.CLIENT)
			channel.sendToServer(toPlain(packet));
	}

	public static void send(PacketDistributor.PacketTarget target, IPacket packet)
	{
		if(target == null || packet == null) return;
		if(LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER)
			channel.send(target, toPlain(packet));
	}

	///

	public static PlainHLMessage toPlain(IPacket packet)
	{
		return new PlainHLMessage(packet);
	}

	public static PacketBuffer toBuffer(PlainHLMessage msg)
	{
		final PacketBuffer bufIn = new PacketBuffer(Unpooled.buffer());
		channel.encodeMessage(msg, bufIn);
		return bufIn;
	}

	///

	public static void swingHand(PlayerEntity player, Hand hand)
	{
		ServerPlayerEntity spe = Cast.cast(player, ServerPlayerEntity.class);
		if(spe != null)
			spe.getLevel().getChunkSource().broadcastAndSend(spe, new SAnimateHandPacket(player, hand == Hand.MAIN_HAND ? 0 : 3));
	}
}