package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketNoSpamChat;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Lets you send messages to player without spamming
 */
public class ChatUtil
{
	/**
	 * Returns a standard {@link ChatComponentText} for the given {@link String}
	 * .
	 * 
	 * @param s
	 *            The string to wrap.
	 * 
	 * @return An {@link ITextComponent} containing the string.
	 */
	public static ITextComponent wrap(String s)
	{
		return new TextComponentString(s);
	}
	
	/**
	 * @see #wrap(String)
	 */
	public static ITextComponent[] wrap(String... s)
	{
		ITextComponent[] ret = new ITextComponent[s.length];
		for(int i = 0; i < ret.length; i++)
			ret[i] = wrap(s[i]);
		return ret;
	}
	
	/**
	 * Returns a translatable chat component for the given string and format
	 * args.
	 * 
	 * @param s
	 *            The string to format
	 * @param args
	 *            The args to apply to the format
	 */
	public static ITextComponent wrapFormatted(String s, Object... args)
	{
		return new TextComponentTranslation(s, args);
	}
	
	/**
	 * Simply sends the passed lines to the player in a chat message.
	 * 
	 * @param player
	 *            The player to send the chat to
	 * @param lines
	 *            The lines to send
	 */
	public static void sendChat(EntityPlayer player, String... lines)
	{
		sendChat(player, wrap(lines));
	}
	
	/**
	 * Localizes the lines before sending them.
	 * 
	 * @see #sendChat(EntityPlayer, String...)
	 */
	public static void sendChatUnloc(EntityPlayer player, String... unlocLines)
	{
		sendChat(player, localizeAll(unlocLines));
	}
	
	/**
	 * Sends all passed chat components to the player.
	 * 
	 * @param player
	 *            The player to send the chat lines to.
	 * @param lines
	 *            The {@link ITextComponent chat components} to send.yes
	 */
	public static void sendChat(EntityPlayer player, ITextComponent... lines)
	{
		for(ITextComponent c : lines)
			player.sendMessage(c);
	}
	
	/**
	 * Localizes the strings before sending them.
	 * 
	 * @see #sendNoSpamClient(String...)
	 */
	public static void sendNoSpamClientUnloc(String... unlocLines)
	{
		sendNoSpamClient(localizeAll(unlocLines));
	}
	
	/**
	 * Same as {@link #sendNoSpamClient(ITextComponent...)}, but wraps the
	 * Strings automatically.
	 * 
	 * @param lines
	 *            The chat lines to send
	 * 
	 * @see #wrap(String)
	 */
	public static void sendNoSpamClient(String... lines)
	{
		sendNoSpamClient(wrap(lines));
	}
	
	/**
	 * Skips the packet sending, unsafe to call on servers.
	 * 
	 * @see #sendNoSpam(EntityPlayerMP, ITextComponent...)
	 */
	public static void sendNoSpamClient(ITextComponent... lines)
	{
		HammerCore.renderProxy.sendNoSpamMessages(lines);
	}
	
	/**
	 * Localizes the strings before sending them.
	 * 
	 * @see #sendNoSpam(EntityPlayer, String...)
	 */
	public static void sendNoSpamUnloc(EntityPlayer player, String... unlocLines)
	{
		sendNoSpam(player, localizeAll(unlocLines));
	}
	
	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(EntityPlayer, ITextComponent...)
	 */
	public static void sendNoSpam(EntityPlayer player, String... lines)
	{
		sendNoSpam(player, wrap(lines));
	}
	
	/**
	 * First checks if the player is instanceof {@link EntityPlayerMP} before
	 * casting.
	 * 
	 * @see #sendNoSpam(EntityPlayerMP, ITextComponent...)
	 */
	public static void sendNoSpam(EntityPlayer player, ITextComponent... lines)
	{
		if(player instanceof EntityPlayerMP)
			sendNoSpam((EntityPlayerMP) player, lines);
	}
	
	/**
	 * Localizes the strings before sending them.
	 * 
	 * @see #sendNoSpam(EntityPlayerMP, String...)
	 */
	public static void sendNoSpamUnloc(EntityPlayerMP player, String... unlocLines)
	{
		sendNoSpam(player, localizeAll(unlocLines));
	}
	
	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(EntityPlayerMP, ITextComponent...)
	 */
	public static void sendNoSpam(EntityPlayerMP player, String... lines)
	{
		sendNoSpam(player, wrap(lines));
	}
	
	/**
	 * Sends a chat message to the client, deleting past messages also sent via
	 * this method.
	 * 
	 * Credit to RWTema for the idea
	 * 
	 * @param player
	 *            The player to send the chat message to
	 * @param lines
	 *            The chat lines to send.
	 */
	public static void sendNoSpam(EntityPlayerMP player, ITextComponent... lines)
	{
		if(lines.length > 0)
			HCNet.INSTANCE.sendTo(new PacketNoSpamChat(lines), player);
	}
	
	public static ITextComponent[] localizeAll(String... strings)
	{
		return ArrayHelper.collect(ArrayHelper.stream(strings).map(TextComponentTranslation::new));
	}
}