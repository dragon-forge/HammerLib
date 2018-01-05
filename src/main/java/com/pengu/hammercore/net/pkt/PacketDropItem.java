package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * If sent to server, creates an {@link EntityItem} at sender's position
 */
public class PacketDropItem implements iPacket, iPacketListener<PacketDropItem, iPacket>
{
	ItemStack stack;
	NBTTagCompound ei;
	
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
	public iPacket onArrived(PacketDropItem packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			EntityPlayerMP mp = context.getServerHandler().player;
			EntityItem ei = new EntityItem(mp.world, mp.posX, mp.posY, mp.posZ, packet.stack);
			ei.setNoPickupDelay();
			if(packet.ei != null)
				ei.readFromNBT(packet.ei);
			mp.world.spawnEntity(ei);
		}
		return null;
	}
}