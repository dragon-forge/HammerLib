package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants.NBT;

@MainThreaded
public class PacketTeleportPlayer implements IPacket
{
	static
	{
		IPacket.handle(PacketTeleportPlayer.class, PacketTeleportPlayer::new);
	}
	
	Vec3d target;
	Integer dimension;
	
	public PacketTeleportPlayer withTarget(Vec3d target)
	{
		this.target = target;
		return this;
	}
	
	public PacketTeleportPlayer withDimension(int dimension)
	{
		this.dimension = dimension;
		return this;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		Helper.setVec3d(nbt, "Target", target);
		if(dimension != null)
			nbt.setInteger("Dim", dimension);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		target = Helper.getVec3d(nbt, "Target");
		if(nbt.hasKey("Dim", NBT.TAG_INT))
			dimension = nbt.getInteger("Dim");
	}
	
	@Override
	public IPacket executeOnServer(PacketContext net)
	{
		EntityPlayerMP mp = net.getSender();
		if(mp == null)
			return null;
		WorldUtil.teleportPlayer(mp, dimension != null ? dimension : mp.world.provider.getDimension(), target.x, target.y, target.z);
		return null;
	}
}