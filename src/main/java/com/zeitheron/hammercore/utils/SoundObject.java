package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.internal.SimpleRegistration;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

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
}