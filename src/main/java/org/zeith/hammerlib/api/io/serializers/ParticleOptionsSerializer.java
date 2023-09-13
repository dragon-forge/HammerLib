package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.particles.IParticleData;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.util.mcf.ByteBufTransposer;

@NBTSerializer(IParticleData.class)
public class ParticleOptionsSerializer
		implements INBTSerializer<IParticleData>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, @NotNull IParticleData value)
	{
		nbt.putByteArray(key, ByteBufTransposer.transpose(value, DataSerializers.PARTICLE::write));
	}
	
	@Override
	public IParticleData deserialize(CompoundNBT nbt, String key)
	{
		return ByteBufTransposer.read(nbt.getByteArray(key), DataSerializers.PARTICLE::read);
	}
}