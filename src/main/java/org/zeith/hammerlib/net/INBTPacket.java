package org.zeith.hammerlib.net;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public interface INBTPacket
		extends IPacket
{
	default void write(CompoundNBT nbt)
	{
	}

	default void read(CompoundNBT nbt)
	{
	}

	@Override
	default void write(PacketBuffer buf)
	{
		CompoundNBT comp = new CompoundNBT();
		write(comp);
		buf.writeNbt(comp);
	}

	@Override
	default void read(PacketBuffer buf)
	{
		read(buf.readNbt());
	}
}