package org.zeith.hammerlib.net.properties;

import com.google.common.base.Suppliers;
import net.minecraft.block.*;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyBlockState
		extends PropertyBaseCodec<BlockState>
{
	public PropertyBlockState(DirectStorage<BlockState> value)
	{
		super(BlockState.CODEC, Suppliers.memoize(Blocks.AIR::defaultBlockState), BlockState.class, value);
	}
	
	public PropertyBlockState()
	{
		super(BlockState.CODEC, Suppliers.memoize(Blocks.AIR::defaultBlockState), BlockState.class);
	}
}