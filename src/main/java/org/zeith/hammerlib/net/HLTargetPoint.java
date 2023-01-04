package org.zeith.hammerlib.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

import java.util.function.Supplier;

public class HLTargetPoint
		extends Vector3d
{
	public final ServerPlayerEntity excluded;
	public final double range;
	public final RegistryKey<World> dim;
	
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
	public HLTargetPoint(final ServerPlayerEntity excluded, final double x, final double y, final double z, final double range, final RegistryKey<World> dim)
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
	public HLTargetPoint(final double x, final double y, final double z, final double range, final RegistryKey<World> dim)
	{
		super(x, y, z);
		this.excluded = null;
		this.range = range;
		this.dim = dim;
	}
	
	public HLTargetPoint(final Vector3i pos, final double range, final RegistryKey<World> dim)
	{
		this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, dim);
	}
	
	public HLTargetPoint(final Vector3d pos, final double range, final RegistryKey<World> dim)
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
	public HLTargetPoint(final ServerPlayerEntity excluded, final double x, final double y, final double z, final double range, final World world)
	{
		super(x, y, z);
		this.excluded = excluded;
		this.range = range;
		this.dim = world.dimension();
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
	public HLTargetPoint(final double x, final double y, final double z, final double range, final World world)
	{
		super(x, y, z);
		this.excluded = null;
		this.range = range;
		this.dim = world.dimension();
	}
	
	public HLTargetPoint(final Vector3i pos, final double range, final World world)
	{
		this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, range, world.dimension());
	}
	
	public HLTargetPoint(final Vector3d pos, final double range, final World world)
	{
		this(pos.x, pos.y, pos.z, range, world.dimension());
	}
	
	public Supplier<TargetPoint> toForge()
	{
		TargetPoint tp = new TargetPoint(excluded, x, y, z, range, dim);
		return () -> tp;
	}
}