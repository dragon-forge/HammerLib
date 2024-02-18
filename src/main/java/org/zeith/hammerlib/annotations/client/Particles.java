package org.zeith.hammerlib.annotations.client;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.ap.*;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * Annotate a {@link net.minecraft.core.particles.ParticleType} constant, and provide a value to {@link net.minecraft.client.particle.ParticleProvider}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Particles
{
	Class<?> value();
	
	class AP
			implements IAnnotationProcessor<Particles>
	{
		@Override
		public void onPostRegistered(IAPContext context, Particles annotation, Field field, Object value)
		{
			var mc = context.getOwnerMod().orElse(null);
			if(mc != null && value instanceof ParticleType<?> pt)
			{
				mc.getEventBus().addListener(
						HammerLib.PROXY.addParticleTypeProvider(
								Type.getType(field.getDeclaringClass()), field.getName(),
								Type.getType(annotation.value())
						)
				);
				var id = context.getRegistryName().map(ResourceLocation::toString).orElse("??:??");
				HammerLib.LOG.debug("Applied TESR for {}[{}] {}.{}", field.getType().getSimpleName(), id, field.getDeclaringClass().getSimpleName(), field.getName());
			}
		}
	}
}