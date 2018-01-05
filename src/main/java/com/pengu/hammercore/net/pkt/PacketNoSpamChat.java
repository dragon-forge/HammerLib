package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNoSpamChat implements iPacket, iPacketListener<PacketNoSpamChat, iPacket>
{
	private ITextComponent[] chatLines;
	
	public PacketNoSpamChat()
	{
		chatLines = new ITextComponent[0];
	}
	
	public PacketNoSpamChat(ITextComponent... lines)
	{
		this.chatLines = lines;
	}
	
	@Override
	public iPacket onArrived(PacketNoSpamChat packet, MessageContext context)
	{
		HammerCore.renderProxy.sendNoSpamMessages(packet.chatLines);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("ChatLines", chatLines.length);
		int p = 0;
		for(ITextComponent c : chatLines)
		{
			nbt.setString("ChatLine" + p, ITextComponent.Serializer.componentToJson(c));
			p++;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		chatLines = new ITextComponent[nbt.getInteger("ChatLines")];
		for(int i = 0; i < chatLines.length; i++)
			chatLines[i] = ITextComponent.Serializer.jsonToComponent(nbt.getString("ChatLine" + i));
	}
}