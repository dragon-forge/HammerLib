package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(ItemStack.class)
public class ItemStackSerializer
		implements INBTSerializer<ItemStack>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, ItemStack value)
	{
		if(!value.isEmpty())
			nbt.put(key, value.serializeNBT());
	}

	@Override
	public ItemStack deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_COMPOUND) ? ItemStack.of(nbt.getCompound(key)) : ItemStack.EMPTY;
	}
}
