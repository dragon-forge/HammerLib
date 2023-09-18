package org.zeith.hammerlib.annotations.client;

import net.minecraft.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.*;
import java.util.*;

/**
 * Annotate a {@link net.minecraft.core.particles.ParticleType} constant, and provide a value to {@link net.minecraft.client.particle.ParticleProvider}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Particles
{
	Class<?> value();
	
	@ApiStatus.Internal
	record Target(Type owner, String field)
	{
		public static final Map<Target, Info> VALUES = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Util.make(new HashMap<>(), map ->
		{
			ScanDataHelper.lookupAnnotatedObjects(Particles.class).forEach(data ->
			{
				if(data.getTargetType() == ElementType.FIELD)
					data.getOwnerMod()
							.ifPresent(mc ->
									map.put(new Target(data.clazz(), data.getMemberName()), new Info(
											() -> mc.getEventBus().addListener(
													HammerLib.PROXY.addParticleTypeProvider(
															data.clazz(),
															data.getMemberName(),
															data.getProperty("value")
																	.map(Type.class::cast)
																	.orElse(null)
													)
											)
									))
							);
			});
		}));
		
		public static Info get(Class<?> cls, String field)
		{
			return VALUES.get(new Particles.Target(Type.getType(cls), field));
		}
	}
	
	@ApiStatus.Internal
	record Info(Runnable applicant)
	{
		public void apply()
		{
			applicant.run();
		}
	}
}