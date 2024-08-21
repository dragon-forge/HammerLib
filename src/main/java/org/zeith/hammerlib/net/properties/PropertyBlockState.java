package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;

public class PropertyBlockState
		extends PropertyBase<IBlockState>
{
	public PropertyBlockState(DirectStorage<IBlockState> value)
	{
		super(IBlockState.class, value);
	}
	
	public PropertyBlockState()
	{
		super(IBlockState.class, DirectStorage.allocate(Blocks.AIR.getDefaultState()));
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
	
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
	
	}
}