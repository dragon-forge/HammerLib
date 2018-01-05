package com.pengu.hammercore.net.packetAPI.p2p;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface iTask
{
	void writeToNBT(NBTTagCompound nbt);
	
	void readFromNBT(NBTTagCompound nbt);
	
	void execute(MessageContext ctx);
}