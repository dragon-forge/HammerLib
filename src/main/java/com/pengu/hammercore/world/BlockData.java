package com.pengu.hammercore.world;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockData implements Serializable
{
	private final int dimId;
	private final long pos;
	private final String registryName;
	private final int meta;
	private final String nbt;
	private transient BlockPos posBP;
	private final int marker;
	
	public BlockData(World world, BlockPos pos, IBlockState state)
	{
		this(world, pos, state, null);
	}
	
	public BlockData(World world, BlockPos pos, IBlockState state, @Nullable TileEntity tile)
	{
		this.marker = 3;
		this.pos = pos.toLong();
		this.dimId = world.provider.getDimension();
		this.registryName = state.getBlock().getRegistryName().toString();
		this.meta = state.getBlock().getMetaFromState(state);
		this.posBP = pos;
		if(tile != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			tile.writeToNBT(nbt);
			this.nbt = nbt.toString();
		} else
			nbt = null;
	}
	
	public BlockData(World world, BlockPos pos, IBlockState state, int marker)
	{
		this(world, pos, state, marker, null);
	}
	
	public BlockData(World world, BlockPos pos, IBlockState state, int marker, @Nullable TileEntity tile)
	{
		this.marker = marker;
		this.pos = pos.toLong();
		this.dimId = world.provider.getDimension();
		this.registryName = state.getBlock().getRegistryName().toString();
		this.meta = state.getBlock().getMetaFromState(state);
		this.posBP = pos;
		if(tile != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			tile.writeToNBT(nbt);
			this.nbt = nbt.toString();
		} else
			nbt = null;
	}
	
	public void place(World world)
	{
		BlockPos pos = posBP != null ? posBP : (posBP = BlockPos.fromLong(this.pos));
		IBlockState state = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(registryName)).getStateFromMeta(meta);
		TileEntity tile = null;
		try
		{
			tile = TileEntity.create(world, JsonToNBT.getTagFromJson(nbt));
		} catch(Exception e)
		{
		}
		world.setBlockState(pos, state, 3);
		if(tile != null)
			world.setTileEntity(pos, tile);
	}
	
	public boolean canBePlaced(World world)
	{
		BlockPos pos = posBP != null ? posBP : (posBP = BlockPos.fromLong(this.pos));
		return world.getChunkProvider().isChunkGeneratedAt(pos.getX() >> 4, pos.getZ() >> 4);
	}
}