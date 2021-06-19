package com.zeitheron.hammercore.internal;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.chat.PacketDeleteMessage;
import com.zeitheron.hammercore.net.internal.chat.PacketEditMessage;
import com.zeitheron.hammercore.net.internal.chat.PacketSendMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Chat
{
	public static ChatFingerprint sendMessageAll(ITextComponent text)
	{
		return sendMessageTo(Objects::nonNull, text);
	}
	
	public static void sendMessageAll(ITextComponent text, ChatFingerprint print)
	{
		sendMessageTo(Objects::nonNull, text, print);
	}
	
	public static void editMessageForAll(ITextComponent text, ChatFingerprint print)
	{
		editMessageFor(Objects::nonNull, text, print);
	}
	
	public static void deleteMessageForAll(ChatFingerprint print)
	{
		deleteMessageFor(Objects::nonNull, print);
	}
	
	//
	
	public static ChatFingerprint sendMessageTo(Predicate<EntityPlayerMP> player, ITextComponent text)
	{
		ChatFingerprint print = new ChatFingerprint();
		sendMessageTo(player, text, print);
		return print;
	}
	
	public static void sendMessageTo(Predicate<EntityPlayerMP> player, ITextComponent text, ChatFingerprint print)
	{
		MinecraftServer mcs = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(mcs != null)
			mcs.getPlayerList().getPlayers().stream().filter(player).forEach(mp -> HCNet.INSTANCE.sendTo(new PacketSendMessage().withPrint(print).withText(text), mp));
	}
	
	public static void editMessageFor(Predicate<EntityPlayerMP> player, ITextComponent text, ChatFingerprint print)
	{
		MinecraftServer mcs = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(mcs != null)
			mcs.getPlayerList().getPlayers().stream().filter(player).forEach(mp -> HCNet.INSTANCE.sendTo(new PacketEditMessage().withPrint(print).withText(text), mp));
	}
	
	public static void deleteMessageFor(Predicate<EntityPlayerMP> player, ChatFingerprint print)
	{
		MinecraftServer mcs = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(mcs != null)
			mcs.getPlayerList().getPlayers().stream().filter(player).forEach(mp -> HCNet.INSTANCE.sendTo(new PacketDeleteMessage().withPrint(print), mp));
	}
	
	//
	
	public static ChatFingerprint sendMessageTo(EntityPlayerMP player, ITextComponent text)
	{
		ChatFingerprint print = new ChatFingerprint();
		HCNet.INSTANCE.sendTo(new PacketSendMessage().withPrint(print).withText(text), player);
		return print;
	}
	
	public static void editMessageFor(EntityPlayerMP player, ITextComponent text, ChatFingerprint print)
	{
		HCNet.INSTANCE.sendTo(new PacketEditMessage().withPrint(print).withText(text), player);
	}
	
	public static void deleteMessageFor(EntityPlayerMP player, ChatFingerprint print)
	{
		HCNet.INSTANCE.sendTo(new PacketDeleteMessage().withPrint(print), player);
	}
	
	//
	
	@SideOnly(Side.CLIENT)
	public static void printNoSpam_client(long id, ITextComponent line)
	{
		ChatFingerprint fp = new ChatFingerprint(id);
		
		List<FingerprintedChatLine> rem = FingerprintedChatLine.getChatLines().stream().filter(l -> l instanceof FingerprintedChatLine).map(l -> (FingerprintedChatLine) l).filter(l -> l.print.equals(fp)).collect(Collectors.toList());
		FingerprintedChatLine.getChatLines().removeAll(rem);
		Minecraft.getMinecraft().ingameGUI.getChatGUI().drawnChatLines.removeAll(rem);
		
		new PacketEditMessage().withPrint(fp).withText(line).executeOnClient(null);
	}
	
	public static class ChatFingerprint implements INBTSerializable<NBTTagCompound>
	{
		private static final Random RNG = new Random();
		public long l;
		
		public ChatFingerprint(long l)
		{
			this.l = l;
		}
		
		public ChatFingerprint(NBTTagCompound nbt)
		{
			deserializeNBT(nbt);
		}
		
		public ChatFingerprint()
		{
			this.l = RNG.nextLong();
		}
		
		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setLong("N", l);
			return nbt;
		}
		
		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			l = nbt.getLong("N");
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof ChatFingerprint && ((ChatFingerprint) obj).l == l;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class FingerprintedChatLine extends ChatLine
	{
		public final ChatFingerprint print;
		
		public ITextComponent lineComponent;
		
		public FingerprintedChatLine(int updateCounterCreatedIn, ITextComponent lineStringIn, ChatFingerprint print)
		{
			super(updateCounterCreatedIn, lineStringIn, 0);
			lineComponent = lineStringIn;
			this.print = print;
		}
		
		public boolean isThisLine(ChatFingerprint print)
		{
			return Objects.equals(print, this.print);
		}
		
		@Override
		public ITextComponent getChatComponent()
		{
			return lineComponent;
		}
		
		public void setLineComponent(ITextComponent lineComponent)
		{
			this.lineComponent = lineComponent;
		}
		
		@SideOnly(Side.CLIENT)
		public static List<ChatLine> getChatLines()
		{
			return Minecraft.getMinecraft().ingameGUI.getChatGUI().chatLines;
		}
		
		@SideOnly(Side.CLIENT)
		public static void deleteChatLine(ChatLine line)
		{
			Minecraft.getMinecraft().ingameGUI.getChatGUI().drawnChatLines.remove(line);
			getChatLines().remove(line);
		}
		
		@SideOnly(Side.CLIENT)
		public void add()
		{
			getChatLines().add(0, this);
			Minecraft.getMinecraft().ingameGUI.getChatGUI().drawnChatLines.add(0, this);
		}
	}
}