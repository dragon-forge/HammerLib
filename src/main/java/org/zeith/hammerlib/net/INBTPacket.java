package org.zeith.hammerlib.net;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface INBTPacket
		extends IPacket
{
	default void write(CompoundTag nbt)
	{
	}

	default void read(CompoundTag nbt)
	{
	}

	@Override
	default void write(RegistryFriendlyByteBuf buf)
	{
		CompoundTag comp = new CompoundTag();
		write(comp);
		buf.writeNbt(comp);
	}

	@Override
	default void read(RegistryFriendlyByteBuf buf)
	{
		read(buf.readNbt());
	}
}