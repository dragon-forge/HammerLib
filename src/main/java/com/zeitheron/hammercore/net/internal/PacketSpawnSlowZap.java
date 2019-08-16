package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSpawnSlowZap implements IPacket
{
	public int world, color, maxTicks;
	public float ampl;
	public Vec3d start, end;
	
	static
	{
		IPacket.handle(PacketSpawnSlowZap.class, PacketSpawnSlowZap::new);
	}
	
	@Override
	public void executeOnClient2(PacketContext net)
	{
		HammerCore.particleProxy.spawnSlowZap(world, start, end, color, maxTicks, ampl);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		Helper.setVec3d(nbt, "s", start);
		Helper.setVec3d(nbt, "e", end);
		nbt.setInteger("dm", world);
		nbt.setInteger("cl", color);
		nbt.setInteger("mt", maxTicks);
		nbt.setFloat("am", ampl);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		start = Helper.getVec3d(nbt, "s");
		end = Helper.getVec3d(nbt, "e");
		world = nbt.getInteger("dm");
		color = nbt.getInteger("cl");
		maxTicks = nbt.getInteger("mt");
		ampl = nbt.getFloat("am");
	}
}