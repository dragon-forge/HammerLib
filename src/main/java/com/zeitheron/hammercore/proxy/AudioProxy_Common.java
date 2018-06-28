package com.zeitheron.hammercore.proxy;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AudioProxy_Common
{
	public void construct()
	{
		
	}
	
	public EntityPlayer getClientPlayer()
	{
		return null;
	}
	
	public void playSoundAt(World world, String sound, BlockPos pos, float volume, float pitch, SoundCategory category)
	{
		playSoundAt(world, sound, pos.getX() + .5D, pos.getY(), pos.getZ() + .5D, volume, pitch, category);
	}
	
	public void playSoundAt(World world, String sound, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		try
		{
			List<EntityPlayerMP> ps = world.getMinecraftServer().getPlayerList().getPlayers();
			for(EntityPlayerMP p : ps)
			{
				if(p.world.provider.getDimension() != world.provider.getDimension())
					continue;
				p.connection.sendPacket(new SPacketCustomSound(sound, SoundCategory.BLOCKS, x, y, z, volume, pitch));
			}
		} catch(Throwable err)
		{
		}
	}
	
	public void playBlockStateBreak(World world, IBlockState type, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		playSoundAt(world, type.getBlock().getSoundType().getBreakSound().soundName.toString(), x, y, z, volume, pitch, SoundCategory.BLOCKS);
	}
}