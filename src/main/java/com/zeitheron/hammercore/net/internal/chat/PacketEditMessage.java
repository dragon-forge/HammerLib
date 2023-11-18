package com.zeitheron.hammercore.net.internal.chat;

import com.zeitheron.hammercore.internal.Chat.*;
import com.zeitheron.hammercore.net.*;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;
import java.util.stream.Collectors;

@MainThreaded
public class PacketEditMessage
		implements IPacket
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
		nbt.setString("Text", ITextComponent.Serializer.componentToJson(text));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		print = new ChatFingerprint(nbt);
		text = ITextComponent.Serializer.jsonToComponent(nbt.getString("Text"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeOnClient2(PacketContext net)
	{
		List<FingerprintedChatLine> edited =
				FingerprintedChatLine
						.getChatLines()
						.stream()
						.filter(FingerprintedChatLine.class::isInstance)
						.map(FingerprintedChatLine.class::cast)
						.filter(l -> l.isThisLine(print))
						.peek(l -> l.setLineComponent(text))
						.collect(Collectors.toList());
		
		// If no messages were edited
		if(edited.isEmpty())
			new FingerprintedChatLine(Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), text, print)
					.add();
	}
}