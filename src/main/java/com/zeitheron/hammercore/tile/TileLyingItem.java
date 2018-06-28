package com.zeitheron.hammercore.tile;

import com.zeitheron.hammercore.net.props.NetPropertyAbstract;
import com.zeitheron.hammercore.net.props.NetPropertyBool;
import com.zeitheron.hammercore.net.props.NetPropertyItemStack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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