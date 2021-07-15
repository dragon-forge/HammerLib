package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(BlockPos.class)
public class BlockPosSerializer
		implements INBTSerializer<BlockPos>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, BlockPos value)
	{
		if(value != null)
		{
			CompoundNBT tag = new CompoundNBT();
			tag.putInt("x", value.getX());
			tag.putInt("y", value.getY());
			tag.putInt("z", value.getZ());
			nbt.put(key, tag);
		}
	}

	@Override
	public BlockPos deserialize(CompoundNBT nbt, String key)
	{
		if(nbt.contains(key, Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT tag = nbt.getCompound(key);
			return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		}
		return null;
	}
}