package com.zeitheron.hammercore.net;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.internal.PacketParticle;
import com.zeitheron.hammercore.net.internal.PacketSyncMouseStack;
import com.zeitheron.hammercore.utils.java.itf.BooleanConsumer;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum HCNet
{
	INSTANCE;
	
	public final String ch_name = "hammercore2";
	final FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(ch_name);
	
	@SideOnly(Side.CLIENT)
	public static void c_sendToServer(Packet<?> packet)
	{
		HammerCore.pipelineProxy.sendToServer(packet);
	}
	
	public static void s_sendTo(Packet<?> packet, EntityPlayerMP player)
	{
		HammerCore.pipelineProxy.sendTo(packet, player);
	}
	
	public static void s_sendToDimension(Packet<?> packet, int dim)
	{
		MinecraftServer mcs = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(mcs != null)
			mcs.getWorld(dim)
					.getPlayers(EntityPlayerMP.class, p -> !(p instanceof FakePlayer))
					.forEach(player -> s_sendTo(packet, player));
	}
	
	public static void setMouseStack(EntityPlayer player, ItemStack stack)
	{
		if(player != null)
		{
			if(!player.world.isRemote)
				INSTANCE.sendTo(new PacketSyncMouseStack(stack), (EntityPlayerMP) player);
			player.inventory.setItemStack(stack);
		}
	}
	
	public static ItemStack getMouseStack(EntityPlayer player)
	{
		return player != null ? player.inventory.getItemStack() : ItemStack.EMPTY;
	}
	
	public static void spawnParticle(World world, EnumParticleTypes particle, double x, double y, double z, double motionX, double motionY, double motionZ, int... args)
	{
		PacketParticle pp = new PacketParticle(world, particle, new Vec3d(x, y, z), new Vec3d(motionX, motionY, motionZ), args);
		if(!world.isRemote)
			INSTANCE.sendToAllAround(pp, new TargetPoint(world.provider.getDimension(), x, y, z, 100));
		else
			try
			{
				pp.executeOnClient(null);
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
	}
	
	/**
	 * Swings the player's arms on server AND client if called from server.
	 *
	 * @param player
	 * 		The player
	 * @param hand
	 * 		The hand to swing
	 */
	public static void swingArm(EntityPlayer player, EnumHand hand)
	{
		player.swingArm(hand);
		if(player instanceof EntityPlayerMP && !player.world.isRemote)
			s_sendTo(new SPacketAnimation(player, hand == EnumHand.MAIN_HAND ? 0 : 3), (EntityPlayerMP) player);
	}
	
	public void init()
	{
		channel.register(this);
	}
	
	private final Map<Class<? extends IPacket>, Supplier<? extends IPacket>> reconstruction = new HashMap<>();
	
	public <T extends IPacket> void handle(Class<T> t, Supplier<T> nev)
	{
		reconstruction.put(t, nev);
	}
	
	public <T extends IPacket> T newPacket(Class<T> t)
	{
		Supplier sup = reconstruction.get(t);
		if(sup != null)
			return (T) sup.get();
		try
		{
			Constructor<T> c = t.getDeclaredConstructor();
			c.setAccessible(true);
			return c.newInstance();
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	public void sendToAll(IPacket packet)
	{
		channel.sendToAll(wrap(new PacketHolder(packet), null));
	}
	
	public void sendTo(IPacket packet, EntityPlayerMP player)
	{
		channel.sendTo(wrap(new PacketHolder(packet), null), player);
	}
	
	public void sendToAllAround(IPacket packet, TargetPoint point)
	{
		channel.sendToAllAround(wrap(new PacketHolder(packet), null), point);
	}
	
	public void sendToAllAroundTracking(IPacket packet, TargetPoint point)
	{
		channel.sendToAllTracking(wrap(new PacketHolder(packet), null), point);
	}
	
	public void sendToAllAroundTracking(IPacket packet, Entity point)
	{
		channel.sendToAllTracking(wrap(new PacketHolder(packet), null), point);
	}
	
	public void sendToDimension(IPacket packet, int dimensionId)
	{
		channel.sendToDimension(wrap(new PacketHolder(packet), null), dimensionId);
	}
	
	@SideOnly(Side.CLIENT)
	public void sendToServer(IPacket packet)
	{
		channel.sendToServer(wrap(new PacketHolder(packet), null));
	}
	
	public static void writePacket(IPacket packet, PacketBuffer buf)
	{
		if(packet == null)
			return;
		buf.writeString(packet.getClass().getCanonicalName());
		packet.write(buf);
	}
	
	public static IPacket readPacket(PacketBuffer buf)
	{
		try
		{
			Class<? extends IPacket> pktc = Class.forName(buf.readString(1024 * 2)).asSubclass(IPacket.class);
			IPacket pkt = INSTANCE.newPacket(pktc);
			pkt.read(buf);
			return pkt;
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		return null;
	}
	
	public static TargetPoint point(Entity entity, double range)
	{
		return point(entity.world, new Vec3d(
				entity.posX + entity.width / 2F,
				entity.posY + entity.height / 2F, entity.posZ + entity.width / 2F
		), range);
	}
	
	public static TargetPoint point(World world, Vec3d pos, double range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.x, pos.y, pos.z, range);
	}
	
	private FMLProxyPacket wrap(PacketHolder pkt, Side target)
	{
		return wrap(pkt, target, null);
	}
	
	private FMLProxyPacket wrap(PacketHolder pkt, Side target, @Nullable FMLProxyPacket origin)
	{
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		if(pkt.packet != null)
			writePacket(pkt.packet, buf);
		FMLProxyPacket fmlpp = new FMLProxyPacket(buf, ch_name);
		if(origin != null) fmlpp.setDispatcher(origin.getDispatcher());
		if(target != null) fmlpp.setTarget(target);
		return fmlpp;
	}
	
	private PacketHolder unwrap(FMLProxyPacket pkt)
	{
		PacketBuffer pb = new PacketBuffer(pkt.payload());
		PacketHolder ph = new PacketHolder(readPacket(pb));
		pb.release();
		return ph;
	}
	
	@SubscribeEvent
	public void packetToClient(ClientCustomPacketEvent e)
	{
		FMLProxyPacket fmlpp = e.getPacket();
		PacketHolder pkt = unwrap(fmlpp);
		if(pkt == null) return;
		NetworkManager networker = e.getManager();
		BooleanConsumer task = (useReply) ->
		{
			PacketHolder reply = pkt.execute(e);
			if(reply != null && reply.packet != null)
			{
				FMLProxyPacket packet = wrap(reply, oppositeSide(e.side()), fmlpp);
				if(useReply)
					e.setReply(packet);
				else
					networker.sendPacket(packet);
			}
		};
		if(pkt.executeOnMainThread())
			HammerCore.pipelineProxy.runFromMainThread(e.side(), task.fill(false));
		else
			task.accept(true);
	}
	
	@SubscribeEvent
	public void packetToServer(ServerCustomPacketEvent e)
	{
		FMLProxyPacket fmlpp = e.getPacket();
		PacketHolder pkt = unwrap(fmlpp);
		if(pkt == null) return;
		NetworkManager networker = e.getManager();
		BooleanConsumer task = (useReply) ->
		{
			PacketHolder reply = pkt.execute(e);
			if(reply != null && reply.packet != null)
			{
				FMLProxyPacket packet = wrap(reply, oppositeSide(e.side()), fmlpp);
				if(useReply)
					e.setReply(packet);
				else
					networker.sendPacket(packet);
			}
		};
		if(pkt.executeOnMainThread())
			HammerCore.pipelineProxy.runFromMainThread(e.side(), task.fill(false));
		else
			task.accept(true);
	}
	
	private Side oppositeSide(Side s)
	{
		return s == Side.CLIENT ? Side.SERVER : Side.CLIENT;
	}
}