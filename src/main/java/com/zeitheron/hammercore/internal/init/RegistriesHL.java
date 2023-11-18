package com.zeitheron.hammercore.internal.init;


import com.zeitheron.hammercore.HammerCore;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistriesHL
{
//	private static IForgeRegistry<ParticleType> PARTICLE_TYPES;
	
	@SubscribeEvent
	public static void registryEvent(RegistryEvent.NewRegistry e)
	{
		HammerCore.LOG.info("Creating new Forge Registries.");

//		PARTICLE_TYPES = new RegistryBuilder<ParticleType>()
//				.setType(ParticleType.class)
//				.setName(new ResourceLocation("particle_configs"))
//				.disableSaving()
//				.create();
	}

//	public static IForgeRegistry<ParticleType> PARTICLE_TYPES()
//	{
//		return PARTICLE_TYPES;
//	}
}