package com.zeitheron.hammercore.raytracer;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.utils.math.MathHelper;
import com.zeitheron.hammercore.utils.math.vec.Cuboid6;
import com.zeitheron.hammercore.utils.math.vec.Vector3;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RayTracer
{
	private Vector3 vec = new Vector3();
	private Vector3 vec2 = new Vector3();
	
	private Vector3 s_vec = new Vector3();
	private double s_dist;
	private int s_side;
	private IndexedCuboid6 c_cuboid;
	
	private static ThreadLocal<RayTracer> t_inst = new ThreadLocal<RayTracer>();
	
	public static RayTracer instance()
	{
		RayTracer inst = t_inst.get();
		if(inst == null)
			t_inst.set(inst = new RayTracer());
		return inst;
	}
	
	private void traceSide(int side, Vector3 start, Vector3 end, Cuboid6 cuboid)
	{
		vec.set(start);
		Vector3 hit = null;
		switch(side)
		{
		case 0:
			hit = vec.XZintercept(end, cuboid.min.y);
		break;
		case 1:
			hit = vec.XZintercept(end, cuboid.max.y);
		break;
		case 2:
			hit = vec.XYintercept(end, cuboid.min.z);
		break;
		case 3:
			hit = vec.XYintercept(end, cuboid.max.z);
		break;
		case 4:
			hit = vec.YZintercept(end, cuboid.min.x);
		break;
		case 5:
			hit = vec.YZintercept(end, cuboid.max.x);
		break;
		}
		if(hit == null)
			return;
		
		switch(side)
		{
		case 0:
		case 1:
			if(!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z))
				return;
		break;
		case 2:
		case 3:
			if(!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y))
				return;
		break;
		case 4:
		case 5:
			if(!MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z))
				return;
		break;
		}
		
		double dist = vec2.set(hit).subtract(start).magSquared();
		if(dist < s_dist)
		{
			s_side = side;
			s_dist = dist;
			s_vec.set(vec);
		}
	}
	
	public boolean rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid)
	{
		s_dist = Double.MAX_VALUE;
		s_side = -1;
		for(int i = 0; i < 6; i++)
			traceSide(i, start, end, cuboid);
		return s_side >= 0;
	}
	
	public IndexedCuboid6 rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids)
	{
		double c_dist = Double.MAX_VALUE;
		int c_side = 0;
		Vector3 c_vec = Vector3.zero;
		IndexedCuboid6 c_hit = null;
		
		for(IndexedCuboid6 cuboid : cuboids)
		{
			if(rayTraceCuboid(start, end, cuboid) && s_dist < c_dist)
			{
				c_dist = s_dist;
				c_side = s_side;
				c_vec = s_vec;
				c_hit = cuboid;
			}
		}
		
		if(c_hit != null)
		{
			s_dist = c_dist;
			s_side = c_side;
			s_vec = c_vec;
		}
		
		return c_hit;
	}
	
	public ExtendedRayTraceResult rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, BlockPos pos, Object data)
	{
		return rayTraceCuboid(start, end, cuboid) && s_side != -1 ? new ExtendedRayTraceResult(s_vec, s_side, pos, data, s_dist) : null;
	}
	
	public ExtendedRayTraceResult rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, Entity entity, Object data)
	{
		return rayTraceCuboid(start, end, cuboid) ? new ExtendedRayTraceResult(entity, s_vec, data, s_dist) : null;
	}
	
	public ExtendedRayTraceResult rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockPos pos)
	{
		IndexedCuboid6 hit = rayTraceCuboids(start, end, cuboids);
		return hit != null && s_side != -1 ? new ExtendedRayTraceResult(s_vec, s_side, pos, hit.data, s_dist) : null;
	}
	
	public ExtendedRayTraceResult rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, Entity entity)
	{
		IndexedCuboid6 hit = rayTraceCuboids(start, end, cuboids);
		return hit != null ? new ExtendedRayTraceResult(entity, s_vec, hit.data, s_dist) : null;
	}
	
	public static CuboidRayTraceResult rayTraceCuboidsClosest(Vector3 start, Vector3 end, BlockPos pos, List<IndexedCuboid6> cuboids)
	{
		List<CuboidRayTraceResult> results = new ArrayList<>();
		for(IndexedCuboid6 cuboid6 : cuboids)
		{
			CuboidRayTraceResult hit = rayTrace(pos, start, end, cuboid6);
			if(hit != null)
				hit.setData(cuboid6.data);
			results.add(hit);
		}
		CuboidRayTraceResult closestHit = null;
		double curClosest = Double.MAX_VALUE;
		for(CuboidRayTraceResult hit : results)
			if(hit != null)
				if(curClosest > hit.dist)
				{
					closestHit = hit;
					curClosest = hit.dist;
				}
		return closestHit;
	}
	
	public static CuboidRayTraceResult rayTrace(BlockPos pos, Vector3 start, Vector3 end, IndexedCuboid6 cuboid)
	{
		Vector3 posvec = new Vector3(pos);
		Vector3 startRay = start.copy().subtract(posvec);
		Vector3 endRay = end.copy().subtract(posvec);
		RayTraceResult bbResult = cuboid.aabb().calculateIntercept(startRay.vec3(), endRay.vec3());
		
		if(bbResult != null)
		{
			Vector3 hitVec = new Vector3(bbResult.hitVec).add(posvec);
			EnumFacing sideHit = bbResult.sideHit;
			double dist = hitVec.copy().subtract(start).magSquared();
			return new CuboidRayTraceResult(hitVec, pos, sideHit, cuboid, dist);
		}
		
		return null;
	}
	
	public static RayTraceResult retraceBlock(World world, EntityPlayer player, BlockPos pos)
	{
		IBlockState b = world.getBlockState(pos);
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1.0F);
		double reach = getBlockReachDistance(player);
		Vec3d endVec = headVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		return b.collisionRayTrace(world, pos, headVec, endVec);
	}
	
	private static double getBlockReachDistance_server(EntityPlayerMP player)
	{
		return player.interactionManager.getBlockReachDistance();
	}
	
	@SideOnly(Side.CLIENT)
	private static double getBlockReachDistance_client()
	{
		return HammerCore.renderProxy.getBlockReachDistance_client();
	}
	
	public static RayTraceResult retraceFully(EntityPlayer player)
	{
		return retraceFully(player, getBlockReachDistance(player));
	}
	
	public static RayTraceResult retrace(EntityPlayer player)
	{
		return retrace(player, getBlockReachDistance(player));
	}
	
	public static RayTraceResult retrace_stopOnLiquid(EntityPlayer player)
	{
		return retrace_stopOnLiquid(player, getBlockReachDistance(player));
	}
	
	public static Entity retraceEntity(EntityPlayer player)
	{
		return retraceEntity(player, getBlockReachDistance(player));
	}
	
	public static RayTraceResult retraceFully(EntityPlayer player, double reach)
	{
		Entity entity = retraceEntity(player, reach);
		RayTraceResult block = retrace(player, reach);
		if(entity != null && (block == null || (entity.getDistanceSq(player) <= player.getDistanceSq(block.getBlockPos()))))
			return new RayTraceResult(entity);
		return block;
	}
	
	public static RayTraceResult retrace(EntityPlayer player, double reach)
	{
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1);
		Vec3d endVec = headVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		return player.world.rayTraceBlocks(headVec, endVec, false, true, true);
	}
	
	public static RayTraceResult retrace(EntityPlayer player, double reach, boolean entities)
	{
		if(entities)
		{
			Entity entityResult = retraceEntity(player, reach);
			if(entityResult != null)
				return new RayTraceResult(entityResult);
		}
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1);
		Vec3d endVec = headVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		return player.world.rayTraceBlocks(headVec, endVec, false, true, true);
	}
	
	public static RayTraceResult retrace_stopOnLiquid(EntityPlayer player, double reach)
	{
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1);
		Vec3d endVec = headVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		return player.world.rayTraceBlocks(headVec, endVec, true, true, true);
	}
	
	public static Entity retraceEntity(EntityLivingBase ent, double reach)
	{
		Vec3d begin = getStartVec(ent);
		Vec3d lookVec = ent.getLook(1);
		Vec3d end = begin.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
		List<Entity> tracedList = ent.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(begin.x, begin.y, begin.z, end.x, end.y, end.z), t -> t != ent && ent.canEntityBeSeen(t) && t.canBeCollidedWith() && t.getEntityBoundingBox().calculateIntercept(begin, end) != null);
		Entity traced = null;
		for(int i = 0; i < tracedList.size(); ++i)
			if(traced == null || ent.getDistanceSq(tracedList.get(i)) < ent.getDistanceSq(traced))
				traced = tracedList.get(i);
		return traced;
	}
	
	public static Vec3d getCorrectedHeadVec(EntityLivingBase entity)
	{
		Vector3 v = Vector3.fromEntity(entity);
		v.y += entity.getEyeHeight();
		return v.vec3();
	}
	
	public static Vec3d getStartVec(EntityLivingBase entity)
	{
		return getCorrectedHeadVec(entity);
	}
	
	public static double getBlockReachDistance(EntityPlayer player)
	{
		return player.world.isRemote ? getBlockReachDistance_client() : player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP) player) : 5D;
	}
	
	public static Vec3d getEndVec(EntityPlayer player)
	{
		Vec3d headVec = getCorrectedHeadVec(player);
		Vec3d lookVec = player.getLook(1.0F);
		double reach = getBlockReachDistance(player);
		return headVec.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);
	}
}