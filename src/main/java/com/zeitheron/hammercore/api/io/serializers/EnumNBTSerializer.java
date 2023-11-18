package com.zeitheron.hammercore.api.io.serializers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

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
	public void serialize(NBTTagCompound nbt, String key, @Nonnull ET value)
	{
		if(value != null)
			nbt.setInteger(key, value.ordinal());
	}
	
	@Override
	public ET deserialize(NBTTagCompound nbt, String key)
	{
		if(nbt.hasKey(key, Constants.NBT.TAG_INT))
			return constants[Math.abs(nbt.getInteger(key)) % constants.length];
		return null;
	}
}