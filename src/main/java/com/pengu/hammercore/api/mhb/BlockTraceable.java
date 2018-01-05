package com.pengu.hammercore.api.mhb;

import java.util.LinkedList;
import java.util.List;

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
	
	protected float minX, minY, minZ, maxX, maxY, maxZ;
	protected final RayTracer RayTracer = new RayTracer();
	protected iRayCubeGetter registry;
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
		registry = iRayCubeGetter.Instance.getter;
		RayTraceResult hit = RayTracer.retraceBlock(worldIn, playerIn, pos);
		return onBoxActivated(hit != null ? Math.max(0, hit.subHit) : 0, registry.getBoundCubes6(this) != null && registry.getBoundCubes6(this).length > 0 ? registry.getBoundCubes6(this)[hit != null ? Math.max(0, hit.subHit) : 0] : null, worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public final AxisAlignedBB getSelectedBoundingBox(IBlockState s, World w, BlockPos p)
	{
		registry = iRayCubeGetter.Instance.getter;
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
			return selectAxis(s, w, p, registry.func_0x834823_a());
		return FULL_BLOCK_AABB;
	}
	
	protected final AxisAlignedBB selectAxis(IBlockState s, World w, BlockPos p, EntityPlayer player)
	{
		registry = iRayCubeGetter.Instance.getter;
		if(player == null)
			return getFullBoundingBox(w, p, s);
		RayTraceResult hit = RayTracer.retraceBlock(w, player, p);
		
		AxisAlignedBB aabb = getFullBoundingBox(w, p, s);
		
		if(hit != null && registry.getBoundCubes6(this) != null && hit.subHit >= 0 && hit.subHit < registry.getBoundCubes6(this).length)
		{
			Cuboid6[] cubes = registry.getBoundCubes6(this);
			current_face = hit.sideHit;
			
			setBlockBounds((float) cubes[hit.subHit].min.x, (float) cubes[hit.subHit].min.y, (float) cubes[hit.subHit].min.z, (float) cubes[hit.subHit].max.x, (float) cubes[hit.subHit].max.y, (float) cubes[hit.subHit].max.z);
			aabb = new AxisAlignedBB(minX + p.getX(), minY + p.getY(), minZ + p.getZ(), maxX + p.getX(), maxY + p.getY(), maxZ + p.getZ());
		} else
		
		if(hit != null && registry.getBoundCubes6(this) == null && registry.getBoundCubeManager(this) != null && registry.getBoundCubeManager(this).getCuboids(w, p, w.getBlockState(p)) != null && hit.subHit >= 0 && hit.subHit < registry.getBoundCubeManager(this).getCuboids(w, p, s).length)
		{
			Cuboid6[] cubes = registry.getBoundCubeManager(this).getCuboids(w, p, s);
			current_face = hit.sideHit;
			
			setBlockBounds((float) cubes[hit.subHit].min.x, (float) cubes[hit.subHit].min.y, (float) cubes[hit.subHit].min.z, (float) cubes[hit.subHit].max.x, (float) cubes[hit.subHit].max.y, (float) cubes[hit.subHit].max.z);
			aabb = new AxisAlignedBB(minX + p.getX(), minY + p.getY(), minZ + p.getZ(), maxX + p.getX(), maxY + p.getY(), maxZ + p.getZ());
		}
		
		return aabb;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState s, World w, BlockPos p, AxisAlignedBB aabb, List<AxisAlignedBB> l, Entity ent, boolean wut)
	{
		registry = iRayCubeGetter.Instance.getter;
		if(!includeAllHitboxes(w, p, s))
		{
			super.addCollisionBoxToList(s, w, p, aabb, l, ent, wut);
			return;
		}
		Cuboid6[] cbs = null;
		if(registry == null)
			return;
		if(registry.getBoundCubeManager(this) != null)
			cbs = registry.getBoundCubeManager(this).getCuboids(w, p, s);
		else if(registry.getBoundCubes6(this) != null)
			cbs = registry.getBoundCubes6(this);
		if(cbs != null)
			for(Cuboid6 c : cbs)
				addCollisionBoxToList(p, aabb, l, c.aabb());
	}
	
	private void setBlockBounds(float x, float y, float z, float x2, float y2, float z2)
	{
		minX = x;
		minY = y;
		minZ = z;
		maxX = x2;
		maxY = y2;
		maxZ = z2;
	}
	
	public final RayTraceResult collisionRayTrace(IBlockState s, World world, BlockPos p, Vec3d start, Vec3d end)
	{
		registry = iRayCubeGetter.Instance.getter;
		
		if(registry == null)
			return super.collisionRayTrace(s, world, p, start, end);
		
		exited = false;
		List<IndexedCuboid6> cuboids = new LinkedList();
		
		Cuboid6[] cbs = null;
		if(registry.getBoundCubeManager(this) != null)
			cbs = registry.getBoundCubeManager(this).getCuboids(world, p, world.getBlockState(p));
		else if(registry.getBoundCubes6(this) != null)
			cbs = registry.getBoundCubes6(this);
		
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
		
		// @since 1.5.5
		return RayTracer.rayTraceCuboidsClosest(new Vector3(start), new Vector3(end), p, cuboids);
	}
	
	public Cuboid6 getCuboidFromPlayer(EntityPlayer player, BlockPos pos)
	{
		RayTraceResult hit = RayTracer.retraceBlock(player.world, player, pos);
		
		if(registry == null)
			return null;
		
		if(hit != null && registry.getBoundCubes6(this) != null && hit.subHit >= 0 && hit.subHit < registry.getBoundCubes6(this).length)
		{
			Cuboid6[] cubes = registry.getBoundCubes6(this);
			return cubes[hit.subHit];
		} else
		
		if(hit != null && registry.getBoundCubes6(this) == null && registry.getBoundCubeManager(this) != null && registry.getBoundCubeManager(this).getCuboids(player.world, pos, player.world.getBlockState(pos)) != null && hit.subHit >= 0 && hit.subHit < registry.getBoundCubeManager(this).getCuboids(player.world, pos, player.world.getBlockState(pos)).length)
		{
			Cuboid6[] cubes = registry.getBoundCubeManager(this).getCuboids(player.world, pos, player.world.getBlockState(pos));
			return cubes[hit.subHit];
		}
		
		return null;
	}
	
	public Cuboid6 getCuboidFromRTR(World world, RayTraceResult hit)
	{
		BlockPos pos = hit == null ? null : hit.getBlockPos();
		if(pos == null || registry == null)
			return null;
		
		if(hit != null && registry.getBoundCubes6(this) != null && hit.subHit >= 0 && hit.subHit < registry.getBoundCubes6(this).length)
		{
			Cuboid6[] cubes = registry.getBoundCubes6(this);
			return cubes[hit.subHit];
		} else
		
		if(hit != null && registry.getBoundCubes6(this) == null && registry.getBoundCubeManager(this) != null && registry.getBoundCubeManager(this).getCuboids(world, pos, world.getBlockState(pos)) != null && hit.subHit >= 0 && hit.subHit < registry.getBoundCubeManager(this).getCuboids(world, pos, world.getBlockState(pos)).length)
		{
			Cuboid6[] cubes = registry.getBoundCubeManager(this).getCuboids(world, pos, world.getBlockState(pos));
			return cubes[Math.abs(hit.subHit) % cubes.length]; // @since 1.5.2
		}
		
		return null;
	}
}