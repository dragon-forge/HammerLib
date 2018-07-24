package com.zeitheron.hammercore.specials.Zeitheron;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;

public class PacketZeitheronAttack implements IPacket
{
	public static int lastAttackTime;
	
	static
	{
		IPacket.handle(PacketZeitheronAttack.class, PacketZeitheronAttack::new);
	}
	
	public int time;
	
	public PacketZeitheronAttack setTime(int time)
	{
		this.time = time;
		return this;
	}
	
	@Override
	public IPacket executeOnClient(PacketContext net)
	{
		lastAttackTime = time;
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