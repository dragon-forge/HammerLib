package com.zeitheron.hammercore.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AudioProxy_Client extends AudioProxy_Common
{
	@Override
	public void construct()
	{
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public void playSoundAt(World world, String sound, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		if(!world.isRemote)
			super.playSoundAt(world, sound, x, y, z, volume, pitch, category);
		else
			try
			{
				EntityPlayerSP p = Minecraft.getMinecraft().player;
				if(p.world.provider.getDimension() != world.provider.getDimension())
					return;
				p.connection.handleCustomSound(new SPacketCustomSound(sound, category, x, y, z, volume, pitch));
			} catch(Throwable err)
			{
			}
	}
	
	@Override
	public void playBlockStateBreak(World world, IBlockState type, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		playSoundAt(Minecraft.getMinecraft().world, type.getBlock().getSoundType().getBreakSound().getRegistryName().toString(), x, y, z, volume, pitch, SoundCategory.BLOCKS);
	}
}