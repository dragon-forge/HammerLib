package com.zeitheron.hammercore.utils.structure.io;

import com.zeitheron.hammercore.utils.WorldLocation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class StructureElement
{
	public BlockPos relativePos = BlockPos.ORIGIN;
	public NBTTagCompound nbt;
	public String block;
	public int meta;
	
	public StructureElement(String block, int meta, String nbt, BlockPos rel)
	{
		this.block = block;
		this.meta = meta;
		this.relativePos = rel;
		if(nbt != null && !nbt.isEmpty())
			try
			{
				this.nbt = JsonToNBT.getTagFromJson(nbt);
			} catch(NBTException e)
			{
				throw new IllegalStateException(e);
			}
	}
	
	public IBlockState getState()
	{
		return GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(block)).getStateFromMeta(meta);
	}
	
	public NBTTagCompound getTileTag()
	{
		return nbt != null ? nbt.copy() : new NBTTagCompound();
	}
	
	public void place(WorldLocation loc)
	{
		loc.setState(getState());
		TileEntity tile = loc.getTile();
		tile.readFromNBT(tile.writeToNBT(getTileTag()));
	}
	
	public static StructureElement capture(WorldLocation loc, WorldLocation center)
	{
		String nbt = "";
		TileEntity tile = loc.getTile();
		if(tile != null)
			nbt = tile.writeToNBT(new NBTTagCompound()).toString();
		return new StructureElement(loc.getBlock().getRegistryName().toString(), loc.getMeta(), nbt, loc.getPos().subtract(center.getPos()));
	}
}