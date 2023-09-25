package org.zeith.hammerlib.annotations.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.*;
import java.util.Map;

/**
 * Annotate a {@link net.minecraft.core.particles.ParticleType} constant, and provide a value to {@link net.minecraft.client.particle.ParticleProvider}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Particles
{
	Class<?> value();
	
	@ApiStatus.Internal
	record Info(Runnable applicant)
	{
		public static final Map<ScanDataHelper.ScanTarget, Info> VALUES = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () ->
				ScanDataHelper.gatherScans(Particles.class, ElementType.FIELD, (target, mc, data) ->
						new Info(
								() -> mc.getEventBus().addListener(
										HammerLib.PROXY.addParticleTypeProvider(
												data.clazz(),
												data.getMemberName(),
												data.getProperty("value")
														.map(Type.class::cast)
														.orElse(null)
										)
								)
						)
				)
		);
		
		public static Info get(Class<?> cls, String field)
		{
			return VALUES.get(ScanDataHelper.ScanTarget.of(cls, field));
		}
		
		public void apply()
		{
			applicant.run();
		}
	}
}