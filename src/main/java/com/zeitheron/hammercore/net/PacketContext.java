package com.zeitheron.hammercore.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PacketContext
{
	@Nonnull
	public final IContextSender sender;

	@Nonnull
	public final PacketHolder holder;

	@Nullable
	public final MinecraftServer server;

	@Nonnull
	public final Side side;

	private IPacket reply;

	public PacketContext(IContextSender sender, PacketHolder holder, MinecraftServer server)
	{
		this.sender = sender;
		this.holder = holder;
		this.server = server;
		this.side = FMLCommonHandler.instance().getEffectiveSide();
	}

	public PacketContext withReply(IPacket reply)
	{
		this.reply = reply;
		return this;
	}

	public IPacket getReply()
	{
		return reply;
	}

	@Nullable
	public EntityPlayerMP getSender()
	{
		if(sender instanceof ContextSenderPlayerMP)
			return ((ContextSenderPlayerMP) sender).asPlayer();
		if(server != null && holder != null && holder.containsPlayer())
			return holder.getPlayer(server);
		return null;
	}

	public interface IContextSender
	{
		Side from();

		EntityPlayerMP asPlayer();

		/**
		 * This method retrieves either a packet sender (server), or a client player (client)
		 */
		default EntityPlayer asBoundPlayer()
		{
			return asPlayer();
		}
	}

	public static class ContextSenderServer
			implements IContextSender
	{
		final EntityPlayer player;

		public ContextSenderServer(EntityPlayer player)
		{
			this.player = player;
		}

		@Override
		public Side from()
		{
			return Side.SERVER;
		}

		@Override
		public EntityPlayerMP asPlayer()
		{
			return null;
		}

		@Override
		public EntityPlayer asBoundPlayer()
		{
			return player;
		}
	}

	public static class ContextSenderPlayerMP
			implements IContextSender
	{
		public EntityPlayerMP player;

		public ContextSenderPlayerMP(EntityPlayerMP player)
		{
			this.player = player;
		}

		@Override
		public Side from()
		{
			return Side.CLIENT;
		}

		@Override
		public EntityPlayerMP asPlayer()
		{
			return player;
		}
	}
}