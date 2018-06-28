package com.zeitheron.hammercore.client.color;

import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerInterpolator
{
	public static int getRendered(EntityPlayer player, NBTTagCompound data)
	{
		if(data == null)
			return 0x00FF00;
		long ctime = player.world.getTotalWorldTime();
		int old = data.getInteger("Old");
		int nev = data.getInteger("New");
		long stime = data.getLong("TimeStart");
		int time = data.getInteger("Time");
		long ttime = stime + time;
		return time == 0 ? nev : ctime > ttime ? nev : ctime < stime ? old : ColorHelper.interpolate(old, nev, 1F - (float) ((ttime - ctime) / (double) time));
	}
	
	public static NBTTagCompound targetTo(NBTTagCompound data, EntityPlayer player, int targetColor, int time)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		int currentColor = getRendered(player, data);
		nbt.setInteger("Old", currentColor);
		nbt.setInteger("New", targetColor);
		nbt.setInteger("Time", time);
		nbt.setLong("TimeStart", player.world.getTotalWorldTime());
		return nbt;
	}
}