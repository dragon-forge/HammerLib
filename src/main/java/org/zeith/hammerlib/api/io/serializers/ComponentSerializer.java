package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(Component.class)
public class ComponentSerializer
		implements INBTSerializer<Component>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull Component value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putString(key, Component.Serializer.toJson(value, provider));
	}
	
	@Override
	public @Nullable Component deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_STRING) ? Component.Serializer.fromJson(nbt.getString(key), provider) : null;
	}
}