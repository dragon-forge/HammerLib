package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketParticle implements IPacket
{
	public int world;
	public Vec3d pos, motion;
	public EnumParticleTypes particle;
	public int[] params;
	
	static
	{
		IPacket.handle(PacketParticle.class, PacketParticle::new);
	}
	
	public PacketParticle(World world, EnumParticleTypes particle, Vec3d pos, Vec3d motion, int... params)
	{
		this.world = world.provider.getDimension();
		this.pos = pos;
		this.motion = motion;
		this.particle = particle;
		this.params = params;
	}
	
	public PacketParticle()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("p0", particle.ordinal());
		nbt.setInteger("p1", world);
		nbt.setDouble("p2", pos.x);
		nbt.setDouble("p3", pos.y);
		nbt.setDouble("p4", pos.z);
		nbt.setDouble("p5", motion.x);
		nbt.setDouble("p6", motion.y);
		nbt.setDouble("p7", motion.z);
		nbt.setIntArray("p8", params);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		particle = EnumParticleTypes.values()[nbt.getInteger("p0")];
		world = nbt.getInteger("p1");
		pos = new Vec3d(nbt.getDouble("p2"), nbt.getDouble("p3"), nbt.getDouble("p4"));
		motion = new Vec3d(nbt.getDouble("p5"), nbt.getDouble("p6"), nbt.getDouble("p7"));
		params = nbt.getIntArray("p8");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		WorldClient wc = Minecraft.getMinecraft().world;
		if(wc == null || world != wc.provider.getDimension())
			return null;
		Minecraft.getMinecraft().addScheduledTask(() -> wc.spawnParticle(particle, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z, params));
		return null;
	}
}