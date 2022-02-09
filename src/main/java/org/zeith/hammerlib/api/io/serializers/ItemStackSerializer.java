package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(ItemStack.class)
public class ItemStackSerializer
		implements INBTSerializer<ItemStack>
{
	@Override
	public void serialize(CompoundTag nbt, String key, ItemStack value)
	{
		if(!value.isEmpty())
			nbt.put(key, value.serializeNBT());
	}

	@Override
	public ItemStack deserialize(CompoundTag nbt, String key)
	{
		return nbt.contains(key, Tag.TAG_COMPOUND) ? ItemStack.of(nbt.getCompound(key)) : ItemStack.EMPTY;
	}
}
