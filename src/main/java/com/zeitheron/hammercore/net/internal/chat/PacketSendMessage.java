package com.zeitheron.hammercore.net.internal.chat;

import com.zeitheron.hammercore.internal.Chat.ChatFingerprint;
import com.zeitheron.hammercore.internal.Chat.FingerprintedChatLine;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.utils.UTF8Strings;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MainThreaded
public class PacketSendMessage implements IPacket
{
	static
	{
		IPacket.handle(PacketSendMessage.class, PacketSendMessage::new);
	}
	
	public ChatFingerprint print;
	public ITextComponent text;
	
	public PacketSendMessage withText(ITextComponent text)
	{
		this.text = text;
		return this;
	}
	
	public PacketSendMessage withPrint(ChatFingerprint print)
	{
		this.print = print;
		return this;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.merge(print.serializeNBT());
		nbt.setByteArray("Text", UTF8Strings.utf8Bytes(ITextComponent.Serializer.componentToJson(text)));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		print = new ChatFingerprint(nbt);
		text = ITextComponent.Serializer.jsonToComponent(UTF8Strings.utf8Str(nbt.getByteArray("Text")));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		new FingerprintedChatLine(Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), text, print).add();
	}
}