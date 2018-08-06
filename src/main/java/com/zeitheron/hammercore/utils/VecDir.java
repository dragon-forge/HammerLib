package com.zeitheron.hammercore.utils;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VecDir
{
	public final Vec3d start;
	public final Vec3d direction;
	public final double distance;
	
	public VecDir(Vec3d start, Vec3d direction, double distance)
	{
		this.start = start;
		this.distance = distance;
		this.direction = direction;
	}
	
	public VecDir(Vec3d start, float pitch, float yaw, double distance)
	{
		this.start = start;
		this.distance = distance;
		this.direction = Vec3d.fromPitchYaw(pitch, yaw);
	}
	
	public Vec3d calcEndVec()
	{
		return start.add(direction.scale(distance));
	}
	
	public <T extends Entity> List<? extends T> getEntitiesWithinDir(World world, Class<? extends T> ent)
	{
		Vec3d end = calcEndVec();
		AxisAlignedBB aabb = new AxisAlignedBB(start.x, start.y, start.z, end.x, end.y, end.z).expand(.5, .5, .5);
		return world.getEntitiesWithinAABB(ent, aabb, e -> e.getEntityBoundingBox().calculateIntercept(start, end) != null);
	}
	
	public <T extends Entity> List<? extends T> getEntitiesWithinDir(World world, Class<? extends T> ent, Predicate<T> pred)
	{
		Vec3d end = calcEndVec();
		AxisAlignedBB aabb = new AxisAlignedBB(start.x, start.y, start.z, end.x, end.y, end.z).expand(.5, .5, .5);
		return world.getEntitiesWithinAABB(ent, aabb, e -> e.getEntityBoundingBox().calculateIntercept(start, end) != null && pred.test(e));
	}
	
	public <T extends Entity> T getClosestEntityWithinDir(World world, Class<? extends T> ent)
	{
		List<? extends T> ts = getEntitiesWithinDir(world, ent);
		
		double min = Double.POSITIVE_INFINITY;
		double curr = 0;
		T last = null;
		
		for(T t : ts)
			if((curr = t.getDistanceSq(start.x, start.y, start.z)) < min && (last = t) != null)
				min = curr;
			
		return last;
	}
	
	public <T extends Entity> T getClosestEntityWithinDir(World world, Class<? extends T> ent, Predicate<T> pred)
	{
		List<? extends T> ts = getEntitiesWithinDir(world, ent, pred);
		
		double min = Double.POSITIVE_INFINITY;
		double curr = 0;
		T last = null;
		
		for(T t : ts)
			if((curr = t.getDistanceSq(start.x, start.y, start.z)) < min && (last = t) != null)
				min = curr;
			
		return last;
	}
	
	public VecDir calcFromDistanceTillEnd(double distance)
	{
		Vec3d start = this.start.add(direction.scale(distance));
		double nd = this.distance - distance;
		return new VecDir(start, direction, nd);
	}
	
	public VecDir calcFromProgressTillEnd(double progress)
	{
		return calcFromDistanceTillEnd(distance * progress);
	}
	
	public VecDir redirect(double from, float pitch, float yaw, double distance)
	{
		return new VecDir(this.start.add(direction.scale(from)), pitch, yaw, distance);
	}
	
	/**
	 * Cuts till the start of entity's hitbox
	 * 
	 * @param ent
	 *            The entity till which this vector direction should be cut
	 * @return the cut directions
	 */
	public VecDir cutTill(Entity ent)
	{
		RayTraceResult res = ent.getEntityBoundingBox().calculateIntercept(start, calcEndVec());
		if(res != null && res.hitVec != null)
			return new VecDir(start, direction, res.hitVec.distanceTo(start));
		return this;
	}
}