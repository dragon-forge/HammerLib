package com.pengu.hammercore.net.packetAPI;

import com.pengu.hammercore.utils.NPEUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public interface iPacket
{
	public void writeToNBT(NBTTagCompound nbt);
	
	public void readFromNBT(NBTTagCompound nbt);
	
	public static final class Helper
	{
		private Helper()
		{
			NPEUtils.noInstancesError();
		}
		
		public static Vec3d getVec3d(NBTTagCompound nbt, String key)
		{
			return new Vec3d(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"), nbt.getFloat(key + "Z"));
		}
		
		public static NBTTagCompound setVec3d(NBTTagCompound nbt, String key, Vec3d vec)
		{
			nbt.setFloat(key + "X", (float) vec.x);
			nbt.setFloat(key + "Y", (float) vec.y);
			nbt.setFloat(key + "Z", (float) vec.z);
			return nbt;
		}
	}
}