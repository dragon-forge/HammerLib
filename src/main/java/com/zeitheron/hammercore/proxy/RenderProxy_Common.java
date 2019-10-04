package com.zeitheron.hammercore.proxy;

import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.utils.IRenderHelper;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.utils.ILoadable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RenderProxy_Common implements ILoadable
{
	public void construct()
	{
	}
	
	public void loadComplete()
	{
	}
	
	public void cl_loadOpts(HCClientOptions opts, NBTTagCompound nbt)
	{
	}
	
	public void cl_saveOpts(HCClientOptions opts, NBTTagCompound nbt)
	{
	}
	
	public IRenderHelper getRenderHelper()
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
	
	public World getWorld(PacketContext context, int dim)
	{
		if(context == null)
			return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim);
		if(context.server != null)
			return context.server.getWorld(dim);
		return null;
	}
	
	public World getWorld(PacketContext context)
	{
		return getWorld(context, 0);
	}
	
	public double getBlockReachDistance_client()
	{
		return 0;
	}
	
	public void bindTexture(ResourceLocation texture)
	{
	}
	
	public void noModel(Block blk)
	{
	}
}