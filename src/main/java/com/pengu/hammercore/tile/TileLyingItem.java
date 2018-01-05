package com.pengu.hammercore.tile;

import com.pengu.hammercore.net.utils.NetPropertyAbstract;
import com.pengu.hammercore.net.utils.NetPropertyBool;
import com.pengu.hammercore.net.utils.NetPropertyItemStack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * This class was generated 2017-09-17:11:32:48
 * 
 * @author APengu
 */
public class TileLyingItem extends TileSyncable
{
	public final NetPropertyItemStack lying;
	public final NetPropertyBool placedByPlayer;
	
	{
		lying = new NetPropertyItemStack(this, ItemStack.EMPTY);
		placedByPlayer = new NetPropertyBool(this, false);
	}
	
	@Override
	public void notifyOfChange(NetPropertyAbstract prop)
	{
		if(lying.get().isEmpty() && !world.isRemote)
			world.setBlockToAir(pos);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
	}
}