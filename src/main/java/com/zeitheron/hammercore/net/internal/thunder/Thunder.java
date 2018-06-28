package com.zeitheron.hammercore.net.internal.thunder;

import com.zeitheron.hammercore.client.particle.def.thunder.ThunderBoltParticle;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Thunder
{
	public final long seed;
	public final int age;
	public final float angleMult;
	
	public Thunder(long seed, int age, float multiplier)
	{
		this.seed = seed;
		this.age = age;
		this.angleMult = multiplier;
	}
	
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound c = new NBTTagCompound();
		c.setLong("Seed", seed);
		c.setInteger("Age", age);
		c.setFloat("Mult", angleMult);
		return c;
	}
	
	public static Thunder deserializeNBT(NBTTagCompound nbt)
	{
		return new Thunder(nbt.getLong("Seed"), nbt.getInteger("Age"), nbt.getFloat("Mult"));
	}
	
	public static final class Layer
	{
		private static long counter = 0L;
		
		public final int blendFunc, color;
		public final boolean active;
		
		{
			++counter;
		}
		
		public Layer(int func, int color, boolean active)
		{
			this.blendFunc = func;
			this.color = color;
			this.active = active;
		}
		
		public Layer(boolean active)
		{
			this(771, counter % 2 == 1 ? 0xFF00FF : 0x0, active);
		}
		
		public Layer()
		{
			this(true);
		}
		
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound c = new NBTTagCompound();
			c.setBoolean("Active", active);
			/** Don't store useless data. Thanks. */
			if(active)
			{
				c.setInteger("BFunc", blendFunc);
				c.setInteger("RGB", color);
			}
			return c;
		}
		
		public static Layer deserializeNBT(NBTTagCompound nbt)
		{
			return new Layer(nbt.getInteger("BFunc"), nbt.getInteger("RGB"), nbt.getBoolean("Active"));
		}
	}
	
	public static class Fractal
	{
		public static final Fractal DEFAULT_FRACTAL = new Fractal();
		
		public final int splits;
		public final float baseAngle;
		
		public Fractal(int splits, float angle)
		{
			this.splits = splits;
			this.baseAngle = angle;
		}
		
		public Fractal()
		{
			this(2, 45F);
		}
		
		@SideOnly(Side.CLIENT)
		public void apply(ThunderBoltParticle thunder)
		{
			thunder.defaultFractal(splits, baseAngle);
		}
	}
}