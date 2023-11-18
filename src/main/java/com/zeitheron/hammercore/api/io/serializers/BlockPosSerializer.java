package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(BlockPos.class)
public class BlockPosSerializer
		implements INBTSerializer<BlockPos>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull BlockPos value)
	{
		if(value != null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("x", value.getX());
			tag.setInteger("y", value.getY());
			tag.setInteger("z", value.getZ());
			nbt.setTag(key, tag);
		}
	}
	
	@Override
	public BlockPos deserialize(NBTTagCompound nbt, String key)
	{
		if(nbt.hasKey(key, Constants.NBT.TAG_COMPOUND))
		{
			NBTTagCompound tag = nbt.getCompoundTag(key);
			return new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
		}
		return null;
	}
}