package com.zeitheron.hammercore.utils;

import java.lang.ref.WeakReference;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer.StateImplementation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;

public class PositionedStateImplementation extends StateImplementation
{
	public WeakReference<IBlockAccess> world = new WeakReference<>(null);
	public BlockPos pos;
	
	public static PositionedStateImplementation convert(StateImplementation si)
	{
		return new PositionedStateImplementation(si.getBlock(), si.getProperties(), si.getPropertyValueTable());
	}
	
	public PositionedStateImplementation withPos(BlockPos pos, IBlockAccess world)
	{
		this.pos = pos.toImmutable();
		this.world = new WeakReference<>(world);
		return this;
	}
	
	public IBlockAccess getWorld()
	{
		return world != null ? world.get() : null;
	}
	
	public BlockPos getPos()
	{
		return pos;
	}
	
	public PositionedStateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
	{
		super(blockIn, propertiesIn);
	}
	
	public PositionedStateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable)
	{
		super(blockIn, propertiesIn, propertyValueTable);
	}
}