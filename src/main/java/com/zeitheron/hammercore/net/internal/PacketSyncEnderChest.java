package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.event.client.EnderInventoryAcceptEvent;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncEnderChest implements IPacket
{
	static
	{
		IPacket.handle(PacketSyncEnderChest.class, PacketSyncEnderChest::new);
	}
	
	public InventoryEnderChest inventory;
	
	public PacketSyncEnderChest withInventory(InventoryEnderChest inventory)
	{
		this.inventory = inventory;
		return this;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Items", inventory.saveInventoryToNBT());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		inventory = new InventoryEnderChest();
		inventory.loadInventoryFromNBT(nbt.getTagList("Items", NBT.TAG_COMPOUND));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player != null)
			player.getInventoryEnderChest().loadInventoryFromNBT(inventory.saveInventoryToNBT());
		MinecraftForge.EVENT_BUS.post(new EnderInventoryAcceptEvent(player));
	}
}