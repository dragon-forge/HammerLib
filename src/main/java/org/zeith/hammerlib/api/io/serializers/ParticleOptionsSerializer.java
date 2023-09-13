package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.util.mcf.ByteBufTransposer;

@NBTSerializer(ParticleOptions.class)
public class ParticleOptionsSerializer
		implements INBTSerializer<ParticleOptions>
{
	@Override
	public void serialize(CompoundTag nbt, String key, @NotNull ParticleOptions value)
	{
		nbt.putByteArray(key, ByteBufTransposer.transpose(value, EntityDataSerializers.PARTICLE::write));
	}
	
	@Override
	public ParticleOptions deserialize(CompoundTag nbt, String key)
	{
		return ByteBufTransposer.read(nbt.getByteArray(key), EntityDataSerializers.PARTICLE::read);
	}
}