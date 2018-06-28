package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSpawnZap implements IPacket
{
	public int world;
	public Vec3d start, end;
	public int color;
	
	static
	{
		IPacket.handle(PacketSpawnZap.class, PacketSpawnZap::new);
	}
	
	@Override
	public IPacket execute(Side side, PacketContext ctx)
	{
		HammerCore.particleProxy.spawnZap(world, start, end, color);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		Helper.setVec3d(nbt, "s", start);
		Helper.setVec3d(nbt, "e", end);
		nbt.setInteger("dm", world);
		nbt.setInteger("cl", color);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		start = Helper.getVec3d(nbt, "s");
		end = Helper.getVec3d(nbt, "e");
		world = nbt.getInteger("dm");
		color = nbt.getInteger("cl");
	}
}