package com.zeitheron.hammercore.net.internal.chat;

import com.zeitheron.hammercore.internal.Chat.ChatFingerprint;
import com.zeitheron.hammercore.internal.Chat.FingerprintedChatLine;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketEditMessage implements IPacket
{
	static
	{
		IPacket.handle(PacketEditMessage.class, PacketEditMessage::new);
	}
	
	public ChatFingerprint print;
	public ITextComponent text;
	
	public PacketEditMessage withText(ITextComponent text)
	{
		this.text = text;
		return this;
	}
	
	public PacketEditMessage withPrint(ChatFingerprint print)
	{
		this.print = print;
		return this;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.merge(print.serializeNBT());
		nbt.setByteArray("Text", ITextComponent.Serializer.componentToJson(text).getBytes());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		print = new ChatFingerprint(nbt);
		text = ITextComponent.Serializer.jsonToComponent(new String(nbt.getByteArray("Text")));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		Minecraft.getMinecraft().addScheduledTask(() -> FingerprintedChatLine.getChatLines().stream().filter(l -> l instanceof FingerprintedChatLine).map(FingerprintedChatLine.class::cast).filter(l -> l.isThisLine(print)).forEach(l -> l.setLineComponent(text)));
		return null;
	}
}