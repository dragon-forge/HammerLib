package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(ITextComponent.class)
public class ITextComponentSerializer
		implements INBTSerializer<ITextComponent>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull ITextComponent value)
	{
		if(value != null)
			nbt.setString(key, ITextComponent.Serializer.componentToJson(value));
	}
	
	@Override
	public ITextComponent deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_STRING) ? ITextComponent.Serializer.fromJsonLenient(nbt.getString(key)) : null;
	}
}