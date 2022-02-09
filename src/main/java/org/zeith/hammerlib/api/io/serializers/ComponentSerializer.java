package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(Component.class)
public class ComponentSerializer
		implements INBTSerializer<Component>
{
	@Override
	public void serialize(CompoundTag nbt, String key, Component value)
	{
		if(value != null)
			nbt.putString(key, Component.Serializer.toJson(value));
	}

	@Override
	public Component deserialize(CompoundTag nbt, String key)
	{
		return nbt.contains(key, Tag.TAG_STRING) ? Component.Serializer.fromJson(nbt.getString(key)) : null;
	}
}