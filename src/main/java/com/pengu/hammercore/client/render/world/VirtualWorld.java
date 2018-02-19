package com.pengu.hammercore.client.render.world;

import com.pengu.hammercore.common.utils.XYZMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VirtualWorld implements IBlockAccess
{
	public XYZMap<TileEntity> tiles = new XYZMap();
	public XYZMap<IBlockState> states = new XYZMap();
	public XYZMap<Biome> biomes = new XYZMap();
	public XYZMap<Integer> colors = new XYZMap();
	
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
}