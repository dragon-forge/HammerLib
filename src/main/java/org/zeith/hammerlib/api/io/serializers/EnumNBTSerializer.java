package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public class EnumNBTSerializer<ET extends Enum<ET>>
		implements INBTSerializer<ET>
{
	final Class<ET> type;
	final ET[] constants;

	public EnumNBTSerializer(Class<ET> type)
	{
		this.type = type;
		this.constants = type.getEnumConstants();
	}

	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull ET value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putInt(key, value.ordinal());
	}

	@Override
	public ET deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		if(nbt.contains(key, Tag.TAG_INT))
			return constants[Math.abs(nbt.getInt(key)) % constants.length];
		return null;
	}
}