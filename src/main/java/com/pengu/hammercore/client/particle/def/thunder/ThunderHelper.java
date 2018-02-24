package com.pengu.hammercore.client.particle.def.thunder;

import com.pengu.hammercore.net.pkt.thunder.Thunder;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ThunderHelper
{
	/**
	 * A helper method that summons thunder effect. Shouldn't be used on servers!
	 */
	public static void thunder(World world, Vec3d start, Vec3d end, Thunder props, Thunder.Layer core, Thunder.Layer aura, Thunder.Fractal fractal)
	{
		if(fractal == null)
			fractal = Thunder.Fractal.DEFAULT_FRACTAL;
		
		ThunderBoltParticle p = new ThunderBoltParticle(world, start.x, start.y, start.z, end.x, end.y, end.z, props.seed, props.age, props.angleMult);
		fractal.apply(p);
		p.aura = aura;
		p.core = core;
		p.finalizeBolt();
	}
}