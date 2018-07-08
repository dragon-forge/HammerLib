package com.zeitheron.hammercore.internal;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.chat.PacketDeleteMessage;
import com.zeitheron.hammercore.net.internal.chat.PacketEditMessage;
import com.zeitheron.hammercore.net.internal.chat.PacketSendMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Chat
{
	public static ChatFingerprint sendMessageAll(ITextComponent text)
	{
		return sendMessageTo(p -> p != null, text);
	}
	
	public static void editMessageForAll(ITextComponent text, ChatFingerprint print)
	{
		editMessageFor(p -> p != null, text, print);
	}
	
	public static void deleteMessageForAll(ChatFingerprint print)
	{
		deleteMessageFor(p -> p != null, print);
	}
	
	//
	
	public static ChatFingerprint sendMessageTo(Predicate<EntityPlayerMP> player, ITextComponent text)
	{
		ChatFingerprint print = new ChatFingerprint();
		MinecraftServer mcs = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(mcs != null)
			mcs.getPlayerList().getPlayers().stream().filter(player).forEach(mp -> HCNet.INSTANCE.sendTo(new PacketSendMessage().withPrint(print).withText(text), mp));
		return print;
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
			return obj instanceof ChatFingerprint ? ((ChatFingerprint) obj).l == l : false;
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
		
		public static List<ChatLine> getChatLines()
		{
			Field field = GuiNewChat.class.getDeclaredFields()[3];
			field.setAccessible(true);
			
			try
			{
				return (List<ChatLine>) field.get(Minecraft.getMinecraft().ingameGUI.getChatGUI());
			} catch(Throwable err)
			{
				return Minecraft.getMinecraft().ingameGUI.getChatGUI().drawnChatLines;
			}
		}
		
		public static void deleteChatLine(ChatLine line)
		{
			Minecraft.getMinecraft().ingameGUI.getChatGUI().drawnChatLines.remove(line);
			getChatLines().remove(line);
		}
		
		public void add()
		{
			getChatLines().add(0, this);
			Minecraft.getMinecraft().ingameGUI.getChatGUI().drawnChatLines.add(0, this);
		}
	}
}