package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(BlockPos.class)
public class BlockPosSerializer
		implements INBTSerializer<BlockPos>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull BlockPos value, CompoundTag nbt)
	{
		if(value != null)
		{
			CompoundTag tag = new CompoundTag();
			tag.putInt("x", value.getX());
			tag.putInt("y", value.getY());
			tag.putInt("z", value.getZ());
			nbt.put(key, tag);
		}
	}

	@Override
	public BlockPos deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		if(nbt.contains(key, Tag.TAG_COMPOUND))
		{
			CompoundTag tag = nbt.getCompound(key);
			return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		}
		return null;
	}
}