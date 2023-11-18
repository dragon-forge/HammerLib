package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(ItemStack.class)
public class ItemStackSerializer
		implements INBTSerializer<ItemStack>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull ItemStack value)
	{
		if(!value.isEmpty())
			nbt.setTag(key, value.serializeNBT());
	}
	
	@Override
	public ItemStack deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_COMPOUND) ? new ItemStack(nbt.getCompoundTag(key)) : ItemStack.EMPTY;
	}
}
