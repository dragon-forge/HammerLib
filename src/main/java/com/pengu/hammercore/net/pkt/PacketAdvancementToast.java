package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketAdvancementToast implements iPacket, iPacketListener<PacketAdvancementToast, iPacket>
{
	private ResourceLocation advancement;
	
	public PacketAdvancementToast(ResourceLocation advancement)
	{
		this.advancement = advancement;
	}
	
	public PacketAdvancementToast()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Advancement", advancement.toString());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		advancement = new ResourceLocation(nbt.getString("Advancement"));
	}
	
	@Override
	public iPacket onArrived(PacketAdvancementToast packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		Advancement adv = Minecraft.getMinecraft().getConnection().getAdvancementManager().getAdvancementList().getAdvancement(advancement);
		if(adv != null)
			Minecraft.getMinecraft().getToastGui().add(new AdvancementToast(adv));
	}
}