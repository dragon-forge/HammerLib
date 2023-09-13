package org.zeith.hammerlib.net.properties;

import lombok.var;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.particles.IParticleData;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyParticleType
		extends PropertyBase<IParticleData>
{
	public PropertyParticleType(DirectStorage<IParticleData> value)
	{
		super(IParticleData.class, value);
	}
	
	public PropertyParticleType()
	{
		super(IParticleData.class);
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		var val = this.value.get();
		buf.writeBoolean(val != null);
		if(val != null) DataSerializers.PARTICLE.write(buf, this.value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.value.set(
				buf.readBoolean()
				? DataSerializers.PARTICLE.read(buf)
				: null
		);
	}
}