package com.zeitheron.hammercore.client.render.world;

import com.zeitheron.hammercore.utils.XYZMap;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.*;

import javax.vecmath.Vector3f;

public class VirtualWorld implements IBlockAccess
{
	public XYZMap<TileEntity> tiles = new XYZMap();
	public XYZMap<IBlockState> states = new XYZMap();
	public XYZMap<Biome> biomes = new XYZMap();
	public XYZMap<Integer> colors = new XYZMap();
	
	private final Vector3f minPos = new Vector3f(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	private final Vector3f maxPos = new Vector3f(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	
	public BlockPos[] getAllPlacedStatePositions()
	{
		return this.states.toKeyArray();
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos)
	{
		return this.tiles.getOnPos(pos);
	}
	
	public void setTileEntity(BlockPos pos, TileEntity tile)
	{
		this.tiles.setOnPos(pos, tile);
		indexPos(pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getCombinedLight(BlockPos pos, int light)
	{
		Integer actual = this.colors.getOnPos(pos);
		if(actual == null)
			this.colors.setOnPos(pos, actual = Integer.valueOf(15728880));
		return actual.intValue();
	}
	
	public void setCombinedLight(BlockPos pos, int light)
	{
		this.colors.setOnPos(pos, Integer.valueOf(light));
	}
	
	@Override
	public IBlockState getBlockState(BlockPos pos)
	{
		IBlockState actual = this.states.getOnPos(pos);
		if(actual == null)
			this.states.setOnPos(pos, actual = Blocks.AIR.getDefaultState());
		return actual;
	}
	
	public void setBlockState(BlockPos pos, IBlockState state)
	{
		this.states.setOnPos(pos, state);
		if(state.getBlock() != Blocks.AIR)
			indexPos(pos);
		if(state.getBlock().hasTileEntity(state))
			setTileEntity(pos, state.getBlock().createTileEntity(null, state));
	}
	
	@Override
	public boolean isAirBlock(BlockPos pos)
	{
		return getBlockState(pos).getBlock() == Blocks.AIR;
	}
	
	@Override
	public Biome getBiome(BlockPos pos)
	{
		pos = new BlockPos(pos.getX(), 0, pos.getZ());
		Biome actual = this.biomes.getOnPos(pos);
		if(actual == null)
			this.biomes.setOnPos(pos, actual = Biomes.PLAINS);
		return actual;
	}
	
	public void setBiome(BlockPos pos, Biome biome)
	{
		this.biomes.setOnPos(new BlockPos(pos.getX(), 0, pos.getZ()), biome);
	}
	
	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction)
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public WorldType getWorldType()
	{
		return WorldType.DEFAULT;
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _unknown)
	{
		return getBlockState(pos).isSideSolid(this, pos, side);
	}
	
	private void indexPos(BlockPos pos)
	{
		minPos.setX(Math.min(minPos.getX(), pos.getX()));
		minPos.setY(Math.min(minPos.getY(), pos.getY()));
		minPos.setZ(Math.min(minPos.getZ(), pos.getZ()));
		maxPos.setX(Math.max(maxPos.getX(), pos.getX()));
		maxPos.setY(Math.max(maxPos.getY(), pos.getY()));
		maxPos.setZ(Math.max(maxPos.getZ(), pos.getZ()));
	}
	
	public void clear()
	{
		biomes.clear();
		colors.clear();
		states.clear();
		tiles.clear();
		
		minPos.set(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		maxPos.set(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	}
	
	public Vector3f getSize()
	{
		Vector3f result = new Vector3f();
		result.setX(maxPos.getX() - minPos.getX() + 1);
		result.setY(maxPos.getY() - minPos.getY() + 1);
		result.setZ(maxPos.getZ() - minPos.getZ() + 1);
		return result;
	}
	
	public Vector3f getMinPos()
	{
		return minPos;
	}
	
	public Vector3f getMaxPos()
	{
		return maxPos;
	}
	
	Object render;
	
	@SideOnly(Side.CLIENT)
	public VirtualWorldRender getRender()
	{
		VirtualWorldRender vwr = Cast.cast(render, VirtualWorldRender.class);
		if(vwr == null)
			render = vwr = new VirtualWorldRender(this);
		return vwr;
	}
}