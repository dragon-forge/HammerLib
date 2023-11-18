package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.*;

@NBTSerializer(IBlockState.class)
public class BlockStateSerializer
		implements INBTSerializer<IBlockState>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull IBlockState value)
	{
		if(value != null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("id", value.getBlock().getRegistryName().toString());
			tag.setInteger("meta", value.getBlock().getMetaFromState(value));
			nbt.setTag(key, tag);
		}
	}
	
	@Nullable
	@Override
	public IBlockState deserialize(NBTTagCompound nbt, String key)
	{
		if(nbt.hasKey(key, Constants.NBT.TAG_COMPOUND))
		{
			NBTTagCompound tag = nbt.getCompoundTag(key);
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("id")));
			return b == null ? null : b.getStateFromMeta(tag.getByte("meta"));
		}
		return null;
	}
}