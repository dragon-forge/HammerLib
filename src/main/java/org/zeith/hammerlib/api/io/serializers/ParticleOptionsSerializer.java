package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(ParticleOptions.class)
public class ParticleOptionsSerializer
		extends BaseCodecSerializer<ParticleOptions>
{
	public ParticleOptionsSerializer()
	{
		super(ParticleTypes.CODEC, () -> null);
	}
}