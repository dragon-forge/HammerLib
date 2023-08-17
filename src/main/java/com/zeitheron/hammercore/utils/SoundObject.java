package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.internal.SimpleRegistration;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

/**
 * Constructed to register sound via
 * {@link SimpleRegistration#registerSound(SoundObject)}. <br>
 * Example: SimpleRegistration.registerSound(new SoundObject("lostthaumaturgy",
 * "bubbling")). <br>
 * Hint: You should keep {@link SoundObject} as a static-final variable for ease
 * of use.
 **/
public class SoundObject
{
	public ResourceLocation name;
	public SoundEvent sound;
	
	public SoundObject(ResourceLocation name)
	{
		this.name = name;
	}
	
	public SoundObject(String modid, String path)
	{
		this(new ResourceLocation(modid, path));
	}
	
	public void playAt(WorldLocation location, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(location.getWorld(), name.toString(), location.getPos(), volume, pitch, category);
	}
	
	public Targeted targeted(EntityPlayerMP towards)
	{
		return new Targeted(towards);
	}
	
	public void playAsPlayer(Entity player, float volume, float pitch)
	{
		HammerCore.audioProxy.playSoundAt(player.world, name.toString(), player.posX, player.posY, player.posZ, volume, pitch, SoundCategory.PLAYERS);
	}
	
	public void playAsMob(Entity ent, float volume, float pitch)
	{
		HammerCore.audioProxy.playSoundAt(ent.world, name.toString(), ent.posX, ent.posY, ent.posZ, volume, pitch,
				ent instanceof IMob ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL
		);
	}
	
	public void playAsBlock(World world, BlockPos pos, float volume, float pitch)
	{
		play(world, pos, volume, pitch, SoundCategory.BLOCKS);
	}
	
	public void play(World world, BlockPos pos, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, name.toString(), pos, volume, pitch, category);
	}
	
	public void play(World world, Vec3d pos, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, name.toString(), pos.x, pos.y, pos.z, volume, pitch, category);
	}
	
	public class Targeted
	{
		protected final EntityPlayerMP target;
		
		public Targeted(EntityPlayerMP target)
		{
			this.target = target;
		}
		
		public void playAsPlayer(Entity player, float volume, float pitch)
		{
			target.connection.sendPacket(new SPacketCustomSound(sound.getSoundName()
					.toString(), SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, volume, pitch));
		}
		
		public void playAsMob(Entity ent, float volume, float pitch)
		{
			target.connection.sendPacket(new SPacketCustomSound(sound.getSoundName().toString(),
					ent instanceof IMob ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL,
					ent.posX, ent.posY, ent.posZ,
					volume, pitch
			));
		}
		
		public void playAsBlock(BlockPos pos, float volume, float pitch)
		{
			play(pos, volume, pitch, SoundCategory.BLOCKS);
		}
		
		public void play(BlockPos pos, float volume, float pitch, SoundCategory category)
		{
			target.connection.sendPacket(new SPacketCustomSound(sound.getSoundName().toString(), category,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, volume, pitch
			));
		}
		
		public void play(Vec3d pos, float volume, float pitch, SoundCategory category)
		{
			target.connection.sendPacket(new SPacketCustomSound(sound.getSoundName().toString(), category,
					pos.x, pos.y, pos.z, volume, pitch
			));
		}
	}
}