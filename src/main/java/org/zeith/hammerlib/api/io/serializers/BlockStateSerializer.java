package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(BlockState.class)
public class BlockStateSerializer
		implements INBTSerializer<BlockState>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, BlockState value)
	{
		if(value != null)
			nbt.put(key, NBTUtil.writeBlockState(value));
	}
	
	@Override
	public BlockState deserialize(CompoundNBT nbt, String key)
	{
		if(nbt.contains(key, Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT tag = nbt.getCompound(key);
			return NBTUtil.readBlockState(tag);
		}
		return null;
	}
}