package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

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
	public void serialize(CompoundNBT nbt, String key, ET value)
	{
		if(value != null)
			nbt.putInt(key, value.ordinal());
	}

	@Override
	public ET deserialize(CompoundNBT nbt, String key)
	{
		if(nbt.contains(key, Constants.NBT.TAG_INT))
			return constants[Math.abs(nbt.getInt(key)) % constants.length];
		return null;
	}
}