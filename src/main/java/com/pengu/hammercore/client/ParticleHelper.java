package com.pengu.hammercore.client;

import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.util.EnumHelper;

public class ParticleHelper
{
	public static EnumParticleTypes newParticleType(String name, String particleNameIn, boolean shouldIgnoreRangeIn, int argumentCountIn)
	{
		Class<?>[] classes = new Class<?>[] { String.class, int.class, boolean.class, int.class };
		int id = nextParticleID();
		EnumParticleTypes p = EnumHelper.addEnum(EnumParticleTypes.class, name, classes, particleNameIn, id, shouldIgnoreRangeIn, argumentCountIn);
		// Assign particle to name and id
		EnumParticleTypes.BY_NAME.put(particleNameIn, p);
		EnumParticleTypes.PARTICLES.put(id, p);
		return p;
	}
	
	private static int nextParticleID()
	{
		int i = 0;
		while(EnumParticleTypes.getParticleFromId(i++) != null)
			;
		return i;
	}
}