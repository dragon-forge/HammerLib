package com.pengu.hammercore.client.particle.def.lightbolt;

import java.awt.Color;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightningBoltHelper
{
	public static void lightning(World world, double x, double y, double z, double tx, double ty, double tz, long seed, int duration, float multiplier)
	{
		LightningBolt bolt = new LightningBolt(world, x, y, z, tx, ty, tz, seed, duration, multiplier);
		bolt.defaultFractal();
		bolt.finalizeBolt();
	}
	
	public static void lightning(World world, double x, double y, double z, double tx, double ty, double tz, long seed, int duration, float multiplier, Color color)
	{
		LightningBolt bolt = new LightningBolt(world, x, y, z, tx, ty, tz, seed, duration, multiplier);
		bolt.bcol = color.getRGB();
		bolt.dcol = color.darker().darker().getRGB();
		bolt.defaultFractal();
		bolt.finalizeBolt();
	}
	
	public static void lightning(World world, double x, double y, double z, double tx, double ty, double tz, long seed, int duration, float multiplier, Color color, int blendBright, int blendDark)
	{
		LightningBolt bolt = new LightningBolt(world, x, y, z, tx, ty, tz, seed, duration, multiplier);
		bolt.setBlendFunc(blendBright, blendDark);
		bolt.bcol = color.getRGB();
		bolt.dcol = color.darker().darker().getRGB();
		bolt.defaultFractal();
		bolt.finalizeBolt();
	}
}