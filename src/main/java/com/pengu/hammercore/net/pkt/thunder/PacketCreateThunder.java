package com.pengu.hammercore.net.pkt.thunder;

import com.pengu.hammercore.client.particle.def.thunder.ThunderHelper;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCreateThunder implements iPacket, iPacketListener<PacketCreateThunder, iPacket>
{
	public Vec3d start, end;
	public Thunder base;
	public Thunder.Layer core, aura;
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("b", base.serializeNBT());
		nbt.setTag("c", core.serializeNBT());
		nbt.setTag("a", aura.serializeNBT());
		Helper.setVec3d(nbt, "s", start);
		Helper.setVec3d(nbt, "e", end);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		aura = Thunder.Layer.deserializeNBT(nbt.getCompoundTag("a"));
		base = Thunder.deserializeNBT(nbt.getCompoundTag("b"));
		core = Thunder.Layer.deserializeNBT(nbt.getCompoundTag("c"));
		start = Helper.getVec3d(nbt, "s");
		end = Helper.getVec3d(nbt, "e");
	}
	
	@Override
	public iPacket onArrived(PacketCreateThunder packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void client()
	{
		ThunderHelper.thunder(Minecraft.getMinecraft().world, start, end, base, core, aura, Thunder.Fractal.DEFAULT_FRACTAL);
	}
}