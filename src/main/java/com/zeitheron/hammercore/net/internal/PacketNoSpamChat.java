package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;

public class PacketNoSpamChat implements IPacket
{
	private ITextComponent[] chatLines;
	
	static
	{
		IPacket.handle(PacketNoSpamChat.class, PacketNoSpamChat::new);
	}
	
	public PacketNoSpamChat()
	{
		chatLines = new ITextComponent[0];
	}
	
	public PacketNoSpamChat(ITextComponent... lines)
	{
		this.chatLines = lines;
	}
	
	@Override
	public IPacket execute(Side side, PacketContext ctx)
	{
		HammerCore.renderProxy.sendNoSpamMessages(chatLines);
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