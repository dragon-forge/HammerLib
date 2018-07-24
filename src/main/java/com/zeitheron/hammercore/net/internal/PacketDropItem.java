package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * If sent to server, creates an {@link EntityItem} at sender's position
 */
@MainThreaded
public class PacketDropItem implements IPacket
{
	ItemStack stack;
	NBTTagCompound ei;
	
	static
	{
		IPacket.handle(PacketDropItem.class, () -> new PacketDropItem(ItemStack.EMPTY));
	}
	
	public PacketDropItem(ItemStack stack)
	{
		this.stack = stack;
	}
	
	public PacketDropItem(EntityItem ei)
	{
		this.stack = ItemStack.EMPTY;
		this.ei = ei.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("i", stack.writeToNBT(new NBTTagCompound()));
		if(ei != null)
			nbt.setTag("e", ei);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		stack = new ItemStack(nbt.getCompoundTag("i"));
		if(nbt.hasKey("e"))
			ei = nbt.getCompoundTag("e");
	}
	
	@Override
	public IPacket executeOnServer(PacketContext net)
	{
		EntityPlayerMP mp = net.getSender();
		EntityItem ei = new EntityItem(mp.world, mp.posX, mp.posY, mp.posZ, stack);
		ei.setNoPickupDelay();
		if(ei != null)
			ei.readFromNBT(this.ei);
		mp.world.spawnEntity(ei);
		return null;
	}
}