package com.pengu.hammercore.proxy;

import com.pengu.hammercore.client.particle.iRenderHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RenderProxy_Common
{
	public void construct()
	{
		
	}
	
	public void preInit(ASMDataTable table)
	{
		
	}
	
	public void init()
	{
		
	}
	
	public iRenderHelper getRenderHelper()
	{
		return null;
	}
	
	public EntityPlayer getClientPlayer()
	{
		return null;
	}
	
	public void sendNoSpamMessages(ITextComponent[] messages)
	{
	}
	
	public World getWorld(MessageContext context, int dim)
	{
		if(context == null)
			return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim);
		if(context.side == Side.SERVER)
			return context.getServerHandler().player.mcServer.getWorld(dim);
		return null;
	}
	
	public World getWorld(MessageContext context)
	{
		if(context == null)
			return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
		if(context.side == Side.SERVER)
			return context.getServerHandler().player.world;
		return null;
	}
	
	public double getBlockReachDistance_client()
	{
		return 0;
	}
	
	public void bindTexture(ResourceLocation texture)
	{
		
	}

	public void postInit()
	{
	}
}