package org.zeith.hammerlib.net.properties;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializers;
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
	public void write(FriendlyByteBuf buf)
	{
		var val = this.value.get();
		buf.writeBoolean(val != null);
		if(val != null) EntityDataSerializers.PARTICLE.write(buf, this.value.get());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		this.value.set(
				buf.readBoolean()
				? EntityDataSerializers.PARTICLE.read(buf)
				: null
		);
	}
}