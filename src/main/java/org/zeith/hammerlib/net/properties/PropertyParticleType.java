package org.zeith.hammerlib.net.properties;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.zeith.hammerlib.util.java.DirectStorage;


public class PropertyParticleType
		extends PropertyBase<ParticleOptions>
{
	public PropertyParticleType(DirectStorage<ParticleOptions> value)
	{
		super(ParticleOptions.class, value);
	}
	
	public PropertyParticleType()
	{
		super(ParticleOptions.class);
	}
	
	@Override
	public void write(RegistryFriendlyByteBuf buf)
	{
		var val = this.value.get();
		buf.writeBoolean(val != null);
		if(val != null) ParticleTypes.STREAM_CODEC.encode(buf, this.value.get());
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		this.value.set(
				buf.readBoolean()
				? ParticleTypes.STREAM_CODEC.decode(buf)
				: null
		);
	}
}