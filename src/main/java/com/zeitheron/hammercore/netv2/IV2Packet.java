package com.zeitheron.hammercore.netv2;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * New experimental network system that will be defaulted in MC 1.13.
 */
public interface IV2Packet
{
	void readFromNBT(NBTTagCompound nbt);
	
	void writeToNBT(NBTTagCompound nbt);
	
	default IV2Packet execute(Side side)
	{
		if(side == Side.CLIENT)
			return executeOnClient();
		return executeOnServer();
	}
	
	@SideOnly(Side.CLIENT)
	IV2Packet executeOnClient();
	
	IV2Packet executeOnServer();
}