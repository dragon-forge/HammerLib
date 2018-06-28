package com.zeitheron.hammercore.net;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

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
	
	public PacketContext(IContextSender sender, PacketHolder holder, MinecraftServer server)
	{
		this.sender = sender;
		this.holder = holder;
		this.server = server;
		this.side = FMLCommonHandler.instance().getEffectiveSide();
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
	
	public static interface IContextSender
	{
		Side from();
		
		EntityPlayerMP asPlayer();
	}
	
	public static class ContextSenderServer implements IContextSender
	{
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
	}
	
	public static class ContextSenderPlayerMP implements IContextSender
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