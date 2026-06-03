package com.zeitheron.hammercore.api.lighting;

import com.zeitheron.hammercore.client.utils.gl.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.*;

import java.util.Objects;

public class ColoredLight
		implements IGLWritable
{
	public static final ColoredLight[] EMPTY_ARRAY = new ColoredLight[0];
	public static final int FLOAT_SIZE = 3 + 4 + 1;
	
	public float x, y, z;
	public float r, g, b, a;
	public float radius;
	
	public ColoredLight(float x, float y, float z, float r, float g, float b, float a, float radius)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.radius = radius;
	}
	
	public ColoredLight(ColoredLight toCopy)
	{
		this.x = toCopy.x;
		this.y = toCopy.y;
		this.z = toCopy.z;
		this.r = toCopy.r;
		this.g = toCopy.g;
		this.b = toCopy.b;
		this.a = toCopy.a;
		this.radius = toCopy.radius;
	}
	
	public ColoredLight reposition(Entity entity, float partialTicks)
	{
		x = (float) (entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks);
		y = (float) (entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks);
		z = (float) (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks);
		return this;
	}
	
	public ColoredLight reposition(Vec3d pos)
	{
		x = (float) pos.x;
		y = (float) pos.y;
		z = (float) pos.z;
		return this;
	}
	
	public ColoredLight recolor(float r, float g, float b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		return this;
	}
	
	public ColoredLight recolor(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}
	
	public ColoredLight resize(float radius)
	{
		this.radius = radius;
		return this;
	}
	
	public ColoredLight copy()
	{
		return new ColoredLight(this);
	}
	
	@Override
	public int getFloatSize()
	{
		return FLOAT_SIZE;
	}
	
	@Override
	public void writeFloats(IGLFloatBufferStream stream)
	{
		stream.putAll(
				r, g, b, a,
				x, y, z,
				radius
		);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof ColoredLight)) return false;
		ColoredLight that = (ColoredLight) o;
		return Float.compare(x, that.x) == 0 && Float.compare(y, that.y) == 0 && Float.compare(z, that.z) == 0 && Float.compare(r, that.r) == 0 &&
				Float.compare(g, that.g) == 0 && Float.compare(b, that.b) == 0 && Float.compare(a, that.a) == 0 && Float.compare(radius, that.radius) == 0;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(x, y, z, r, g, b, a, radius);
	}
	
	@Override
	public String toString()
	{
		return "ColoredLight{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				", r=" + r +
				", g=" + g +
				", b=" + b +
				", a=" + a +
				", radius=" + radius +
				'}';
	}
	
	public Builder toBuilder()
	{
		return builder().pos(x, y, z).color(r, g, this.b, a).radius(radius);
	}
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	public static final class Builder
	{
		private float x = Float.NaN;
		private float y = Float.NaN;
		private float z = Float.NaN;
		
		private float r = Float.NaN;
		private float g = Float.NaN;
		private float b = Float.NaN;
		private float a = Float.NaN;
		
		private float radius = Float.NaN;
		
		public Builder pos(BlockPos pos)
		{
			return pos(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
		}
		
		public Builder pos(Vec3d pos)
		{
			return pos(pos.x, pos.y, pos.z);
		}
		
		public Builder pos(Entity e)
		{
			return pos(e.posX + e.width / 2, e.posY + e.height / 2, e.posZ + e.width / 2);
		}
		
		public Builder pos(Entity entity, float partialTicks)
		{
			float
					x = (float) (entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks),
					y = (float) (entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks),
					z = (float) (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks);
			
			return pos(x + entity.width / 2, y + entity.height / 2, z + entity.width / 2);
		}
		
		public Builder pos(double x, double y, double z)
		{
			return pos((float) x, (float) y, (float) z);
		}
		
		public Builder pos(float x, float y, float z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}
		
		public Builder color(Vec3d c, float alpha)
		{
			return color((float) c.x, (float) c.y, (float) c.z, alpha);
		}
		
		public Builder color(Vec3d c)
		{
			return color((float) c.x, (float) c.y, (float) c.z, 1F);
		}
		
		public Builder color(int c, boolean hasAlpha)
		{
			return color(extract(c, 2), extract(c, 1), extract(c, 0), hasAlpha ? extract(c, 3) : 1);
		}
		
		private float extract(int i, int idx)
		{
			return ((i >> (idx * 8)) & 0xFF) / 255f;
		}
		
		public Builder color(float r, float g, float b)
		{
			return color(r, g, b, 1f);
		}
		
		public Builder color(float r, float g, float b, float a)
		{
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			return this;
		}
		
		public Builder alpha(int alpha)
		{
			return alpha(alpha / 255F);
		}
		
		public Builder alpha(float alpha)
		{
			this.a = alpha;
			return this;
		}
		
		public Builder radius(float radius)
		{
			this.radius = radius;
			return this;
		}
		
		public ColoredLight build()
		{
			if(Float.isFinite(x) && Float.isFinite(y) && Float.isFinite(z) && Float.isFinite(r) && Float.isFinite(g) && Float.isFinite(b) && Float.isFinite(a) && Float.isFinite(radius))
			{
				return new ColoredLight(x, y, z, r, g, b, a, radius);
			} else
			{
				throw new IllegalArgumentException("Position, color, and radius must be set, and cannot be infinite");
			}
		}
	}
}