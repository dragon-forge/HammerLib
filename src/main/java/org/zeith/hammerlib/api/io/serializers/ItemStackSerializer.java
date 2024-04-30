package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(ItemStack.class)
public class ItemStackSerializer
		implements INBTSerializer<ItemStack>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull ItemStack value, CompoundTag nbt)
	{
		if(!value.isEmpty())
			nbt.put(key, value.saveOptional(provider));
	}
	
	@Override
	public ItemStack deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_COMPOUND) ? ItemStack.parseOptional(provider, nbt.getCompound(key)) : ItemStack.EMPTY;
	}
}
