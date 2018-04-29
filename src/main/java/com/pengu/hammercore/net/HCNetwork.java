package com.pengu.hammercore.net;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.packetAPI.PacketManager;
import com.pengu.hammercore.net.packetAPI.p2p.P2PManager;
import com.pengu.hammercore.net.pkt.PacketParticle;
import com.pengu.hammercore.net.pkt.PacketSwingArm;
import com.pengu.hammercore.net.pkt.PacketSyncMouseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HCNetwork
{
	public static iPacketManager manager = new PacketManager("hammercore");
	public static P2PManager p2p = new P2PManager(manager);
	
	public static void preInit()
	{
	}
	
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
		{
			WorldServer sw = mcs.getWorld(dim);
			if(sw != null)
				sw.getPlayers(EntityPlayerMP.class, p -> !(p instanceof FakePlayer)).forEach(player -> s_sendTo(packet, player));
		}
	}
	
	public static iPacketManager getManager(String name)
	{
		return manager;
	}
	
	public static P2PManager getP2P()
	{
		return p2p;
	}
	
	public static void setMouseStack(EntityPlayer player, ItemStack stack)
	{
		if(player != null)
		{
			if(!player.world.isRemote)
				manager.sendTo(new PacketSyncMouseStack(stack), (EntityPlayerMP) player);
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
		{
			manager.sendToAllAround(pp, new TargetPoint(world.provider.getDimension(), x, y, z, 64));
		} else
			try
			{
				pp.client();
			} catch(Throwable err)
			{
				// Is this possible to throw an error?
			}
	}
	
	/** Swings the player's arms on server AND client if called from server. */
	public static void swingArm(EntityPlayer player, EnumHand hand)
	{
		player.swingArm(hand);
		if(player instanceof EntityPlayerMP && !player.world.isRemote)
			manager.sendTo(new PacketSwingArm(hand), (EntityPlayerMP) player);
	}
	
	public static void clinit()
	{
	}
}