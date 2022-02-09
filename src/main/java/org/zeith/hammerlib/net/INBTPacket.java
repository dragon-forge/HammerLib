package org.zeith.hammerlib.net;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

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
	default void write(FriendlyByteBuf buf)
	{
		CompoundTag comp = new CompoundTag();
		write(comp);
		buf.writeNbt(comp);
	}

	@Override
	default void read(FriendlyByteBuf buf)
	{
		read(buf.readNbt());
	}
}