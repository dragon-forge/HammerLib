package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(ITextComponent.class)
public class TextComponentSerializer
		implements INBTSerializer<ITextComponent>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, ITextComponent value)
	{
		if(value != null)
			nbt.putString(key, ITextComponent.Serializer.toJson(value));
	}

	@Override
	public ITextComponent deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_STRING) ? ITextComponent.Serializer.fromJson(nbt.getString(key)) : null;
	}
}