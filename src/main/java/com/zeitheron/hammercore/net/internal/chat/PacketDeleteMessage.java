package com.zeitheron.hammercore.net.internal.chat;

import java.util.ArrayList;

import com.zeitheron.hammercore.internal.Chat.ChatFingerprint;
import com.zeitheron.hammercore.internal.Chat.FingerprintedChatLine;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MainThreaded
public class PacketDeleteMessage implements IPacket
{
	static
	{
		IPacket.handle(PacketDeleteMessage.class, PacketDeleteMessage::new);
	}
	
	public ChatFingerprint print;
	
	public PacketDeleteMessage withPrint(ChatFingerprint print)
	{
		this.print = print;
		return this;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.merge(print.serializeNBT());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		print = new ChatFingerprint(nbt);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		new ArrayList<>(FingerprintedChatLine.getChatLines()).stream().filter(l -> l instanceof FingerprintedChatLine).map(FingerprintedChatLine.class::cast).filter(l -> l.isThisLine(print)).forEach(FingerprintedChatLine::deleteChatLine);
	}
}