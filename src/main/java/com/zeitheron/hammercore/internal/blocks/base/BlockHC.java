package com.zeitheron.hammercore.internal.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockHC extends Block
{
	public BlockHC(Material material, String name)
	{
		super(material);
		setUnlocalizedName(name);
		setResistance(2.0f);
		setHardness(1.5f);
	}
	
	public BlockHC(Material mat, String name, SoundType st)
	{
		this(mat, name);
		setSoundType(st);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
}