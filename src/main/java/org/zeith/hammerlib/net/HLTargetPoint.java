package org.zeith.hammerlib.net;

import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.util.java.Cast;

public class HLTargetPoint
		extends Vec3
{
	public final ServerPlayer excluded;
	public final double range;
	public final ServerLevel dim;
	
	/**
	 * A target point with excluded entity
	 *
	 * @param excluded
	 * 		Entity to exclude
	 * @param x
	 * 		X
	 * @param y
	 * 		Y
	 * @param z
	 * 		Z
	 * @param range
	 * 		Radius
	 * @param dim
	 * 		DimensionType
	 */
	public HLTargetPoint(final ServerPlayer excluded, final double x, final double y, final double z, final double range, final ServerLevel dim)
	{
		super(x, y, z);
		this.excluded = excluded;
		this.range = range;
		this.dim = dim;
	}
	
	/**
	 * A target point without excluded entity
	 *
	 * @param x
	 * 		X
	 * @param y
	 * 		Y
	 * @param z
	 * 		Z
	 * @param range
	 * 		Radius
	 * @param dim
	 * 		DimensionType
	 */
	public HLTargetPoint(final double x, final double y, final double z, final double range, final ServerLevel dim)
	{
		super(x, y, z);
		this.excluded = null;
		this.range = range;
		this.dim = dim;
	}
	
	public HLTargetPoint(final Vec3i pos, final double range, final ServerLevel dim)
	{
		this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, dim);
	}
	
	public HLTargetPoint(final Vec3 pos, final double range, final ServerLevel dim)
	{
		this(pos.x, pos.y, pos.z, range, dim);
	}
	
	// WORLD-based versions
	
	/**
	 * A target point with excluded entity
	 *
	 * @param excluded
	 * 		Entity to exclude
	 * @param x
	 * 		X
	 * @param y
	 * 		Y
	 * @param z
	 * 		Z
	 * @param range
	 * 		Radius
	 * @param world
	 * 		World
	 */
	public HLTargetPoint(final ServerPlayer excluded, final double x, final double y, final double z, final double range, final Level world)
	{
		super(x, y, z);
		this.excluded = excluded;
		this.range = range;
		this.dim = Cast.cast(world, ServerLevel.class);
	}
	
	/**
	 * A target point without excluded entity
	 *
	 * @param x
	 * 		X
	 * @param y
	 * 		Y
	 * @param z
	 * 		Z
	 * @param range
	 * 		Radius
	 * @param world
	 * 		World
	 */
	public HLTargetPoint(final double x, final double y, final double z, final double range, final Level world)
	{
		super(x, y, z);
		this.excluded = null;
		this.range = range;
		this.dim = Cast.cast(world, ServerLevel.class);
	}
	
	public HLTargetPoint(final Vec3i pos, final double range, final Level world)
	{
		this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, world);
	}
	
	public HLTargetPoint(final Vec3 pos, final double range, final Level world)
	{
		this(pos.x, pos.y, pos.z, range, world);
	}
}