package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncMouseStack implements IPacket
{
	ItemStack mouse;
	
	static
	{
		IPacket.handle(PacketSyncMouseStack.class, PacketSyncMouseStack::new);
	}
	
	public PacketSyncMouseStack(ItemStack stack)
	{
		this.mouse = stack;
	}
	
	public PacketSyncMouseStack()
	{
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IPacket executeOnClient(PacketContext net)
	{
		Minecraft mc = Minecraft.getMinecraft();
		mc.addScheduledTask(() -> mc.player.inventory.setItemStack(mouse));
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		this.mouse.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.mouse = new ItemStack(nbt);
	}
}