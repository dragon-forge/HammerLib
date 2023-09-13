package org.zeith.hammerlib.api.io.serializers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.zeith.hammerlib.api.io.NBTSerializer;

import java.util.function.Supplier;

@NBTSerializer(BlockState.class)
public class BlockStateSerializer
		extends BaseCodecSerializer<BlockState>
{
	public BlockStateSerializer()
	{
		super(BlockState.CODEC, Suppliers.memoize(Blocks.AIR::defaultBlockState));
	}
}