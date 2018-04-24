package com.pengu.hammercore.api.mhb;

import java.util.LinkedList;
import java.util.List;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.MultiHitboxGetter;
import com.pengu.hammercore.raytracer.IndexedCuboid6;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.vec.Cuboid6;
import com.pengu.hammercore.vec.Vector3;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Represents a base for blocks that have multiple hitboxes. Used for
 * MultipartAPI
 */
public abstract class BlockTraceable extends Block
{
	public static EnumFacing current_face = EnumFacing.DOWN;
	
	protected final RayTracer RayTracer = new RayTracer();
	private boolean exited = true;
	
	public BlockTraceable(Material material)
	{
		super(material);
	}
	
	public BlockTraceable(Material material, MapColor mapColor)
	{
		super(material, mapColor);
	}
	
	public AxisAlignedBB getFullBoundingBox(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return FULL_BLOCK_AABB;
	}
	
	public boolean includeAllHitboxes(World world, BlockPos pos, IBlockState state)
	{
		return true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return getFullBoundingBox(world, pos, state);
	}
	
	public boolean onBoxActivated(int boxID, Cuboid6 box, World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	public boolean canSeeCouboid(World w, BlockPos p, int boxID, Cuboid6 box, Vector3 start, Vector3 end)
	{
		return RayTracer.rayTraceCuboid(start, end, box);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		RayTraceResult hit = com.pengu.hammercore.raytracer.RayTracer.retraceBlock(worldIn, playerIn, pos);
		
		int boxID = hit != null ? Math.max(0, hit.subHit) : 0;
		Cuboid6[] boxes = getCurrentCuboids(worldIn, pos);
		Cuboid6 box = boxes != null && boxes.length > 0 ? boxes[hit != null ? Math.max(0, Math.min(hit.subHit, boxes.length - 1)) : 0] : null;
		return onBoxActivated(boxID, box, worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	public final Cuboid6[] getCurrentCuboids(World world, BlockPos pos)
	{
		Cuboid6[] boxes = MultiHitboxGetter.getCuboidsAt(world, pos);
		return boxes == null || boxes.length == 0 ? null : boxes;
	}
	
	@Override
	public final AxisAlignedBB getSelectedBoundingBox(IBlockState s, World w, BlockPos p)
	{
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
			return selectAxis(s, w, p, HammerCore.renderProxy.getClientPlayer());
		return FULL_BLOCK_AABB;
	}
	
	protected final AxisAlignedBB selectAxis(IBlockState s, World w, BlockPos p, EntityPlayer player)
	{
		if(player == null)
			return getFullBoundingBox(w, p, s);
		RayTraceResult hit = com.pengu.hammercore.raytracer.RayTracer.retraceBlock(w, player, p);
		
		AxisAlignedBB aabb = getFullBoundingBox(w, p, s);
		
		Cuboid6[] boxes = getCurrentCuboids(w, p);
		
		if(hit != null && boxes != null && hit.subHit >= 0 && hit.subHit < boxes.length)
		{
			current_face = hit.sideHit;
			aabb = new AxisAlignedBB(boxes[hit.subHit].min.x + p.getX(), boxes[hit.subHit].min.y + p.getY(), boxes[hit.subHit].min.z + p.getZ(), boxes[hit.subHit].max.x + p.getX(), boxes[hit.subHit].max.y + p.getY(), boxes[hit.subHit].max.z + p.getZ());
		}
		
		return aabb;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState s, World w, BlockPos p, AxisAlignedBB aabb, List<AxisAlignedBB> l, Entity ent, boolean wut)
	{
		if(!includeAllHitboxes(w, p, s))
		{
			super.addCollisionBoxToList(s, w, p, aabb, l, ent, wut);
			return;
		}
		
		Cuboid6[] boxes = getCurrentCuboids(w, p);
		if(boxes != null)
			for(Cuboid6 c : boxes)
				addCollisionBoxToList(p, aabb, l, c.aabb());
	}
	
	@Override
	public final RayTraceResult collisionRayTrace(IBlockState s, World world, BlockPos p, Vec3d start, Vec3d end)
	{
		exited = false;
		List<IndexedCuboid6> cuboids = new LinkedList();
		
		Cuboid6[] cbs = getCurrentCuboids(world, p);
		
		if(cbs == null)
			return super.collisionRayTrace(s, world, p, start, end);
		
		if(cbs != null)
			for(int i = 0; i < cbs.length; ++i)
			{
				Cuboid6 c = cbs[i];
				Cuboid6 cc = new Cuboid6(c.aabb().minX + p.getX(), c.aabb().minY + p.getY(), c.aabb().minZ + p.getZ(), c.aabb().maxX + p.getX(), c.aabb().maxY + p.getY(), c.aabb().maxZ + p.getZ());
				if(!canSeeCouboid(world, p, i, cc, new Vector3(start), new Vector3(end)))
					continue;
				cuboids.add(new IndexedCuboid6(i, c));
			}
		
		exited = true;
		
		return com.pengu.hammercore.raytracer.RayTracer.rayTraceCuboidsClosest(new Vector3(start), new Vector3(end), p, cuboids);
	}
	
	public Cuboid6 getCuboidFromPlayer(EntityPlayer player, BlockPos pos)
	{
		RayTraceResult hit = com.pengu.hammercore.raytracer.RayTracer.retraceBlock(player.world, player, pos);
		Cuboid6[] cubes = getCurrentCuboids(player.getEntityWorld(), pos);
		return hit != null && cubes != null && hit.subHit >= 0 && hit.subHit < cubes.length ? cubes[hit.subHit] : null;
	}
	
	public Cuboid6 getCuboidFromRTR(World world, RayTraceResult hit)
	{
		BlockPos pos = hit == null ? null : hit.getBlockPos();
		if(pos == null)
			return null;
		Cuboid6[] cubes = getCurrentCuboids(world, pos);
		return hit != null && cubes != null && hit.subHit >= 0 && hit.subHit < cubes.length ? cubes[hit.subHit] : null;
	}
}