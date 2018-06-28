package com.zeitheron.hammercore.client.particle.def.thunder;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ThVector3
{
	public float x;
	public float y;
	public float z;
	
	public ThVector3(final double x, final double y, final double z)
	{
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}
	
	public ThVector3(BlockPos pos)
	{
		this.x = pos.getX() + 0.5f;
		this.y = pos.getY() + 0.5f;
		this.z = pos.getZ() + 0.5f;
	}
	
	public ThVector3(Entity entity)
	{
		this(entity.posX, entity.posY, entity.posZ);
	}
	
	public ThVector3 add(final ThVector3 vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
		return this;
	}
	
	public ThVector3 sub(final ThVector3 vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
		return this;
	}
	
	public ThVector3 scale(final float scale)
	{
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		return this;
	}
	
	public ThVector3 scale(final float scalex, final float scaley, final float scalez)
	{
		this.x *= scalex;
		this.y *= scaley;
		this.z *= scalez;
		return this;
	}
	
	public ThVector3 normalize()
	{
		final float length = this.length();
		this.x /= length;
		this.y /= length;
		this.z /= length;
		return this;
	}
	
	public float length()
	{
		return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	public float lengthPow2()
	{
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}
	
	public ThVector3 copy()
	{
		return new ThVector3(this.x, this.y, this.z);
	}
	
	public static ThVector3 crossProduct(final ThVector3 vec1, final ThVector3 vec2)
	{
		return new ThVector3(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z, vec1.x * vec2.y - vec1.y * vec2.x);
	}
	
	public static ThVector3 xCrossProduct(final ThVector3 vec)
	{
		return new ThVector3(0.0, vec.z, -vec.y);
	}
	
	public static ThVector3 zCrossProduct(final ThVector3 vec)
	{
		return new ThVector3(-vec.y, vec.x, 0.0);
	}
	
	public static float dotProduct(final ThVector3 vec1, final ThVector3 vec2)
	{
		return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
	}
	
	public static float angle(final ThVector3 vec1, final ThVector3 vec2)
	{
		return anglePreNorm(vec1.copy().normalize(), vec2.copy().normalize());
	}
	
	public static float anglePreNorm(final ThVector3 vec1, final ThVector3 vec2)
	{
		return (float) Math.acos(dotProduct(vec1, vec2));
	}
	
	public ThVector3 rotate(final float angle, final ThVector3 axis)
	{
		return ThMat4.rotationMat(angle, axis).translate(this);
	}
	
	@Override
	public String toString()
	{
		return "[" + x + "," + y + "," + z + "]";
	}
	
	public Vec3d toVec3D()
	{
		return new Vec3d(x, y, z);
	}
	
	public static ThVector3 getPerpendicular(final ThVector3 vec)
	{
		if(vec.z == 0.0f)
			return zCrossProduct(vec);
		return xCrossProduct(vec);
	}
	
	public boolean isZero()
	{
		return this.x == 0.0f && this.y == 0.0f && this.z == 0.0f;
	}
}