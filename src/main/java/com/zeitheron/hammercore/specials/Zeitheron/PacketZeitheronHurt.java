package com.zeitheron.hammercore.specials.Zeitheron;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;

public class PacketZeitheronHurt implements IPacket
{
	public static int lastHurtTime;
	
	static
	{
		IPacket.handle(PacketZeitheronHurt.class, PacketZeitheronHurt::new);
	}
	
	public int time;
	
	public PacketZeitheronHurt setTime(int time)
	{
		this.time = time;
		return this;
	}
	
	@Override
	public IPacket executeOnClient(PacketContext net)
	{
		lastHurtTime = time;
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("i", time);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		time = nbt.getInteger("i");
	}
}