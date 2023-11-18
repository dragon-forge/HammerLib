package com.zeitheron.hammercore.proxy;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.IProcess;
import com.zeitheron.hammercore.api.lighting.ColoredLightManager;
import com.zeitheron.hammercore.api.lighting.impl.IGlowingEntity;
import com.zeitheron.hammercore.client.particle.api.ParticleList;
import com.zeitheron.hammercore.client.particle.def.*;
import com.zeitheron.hammercore.client.particle.def.thunder.ThunderHelper;
import com.zeitheron.hammercore.client.utils.OpnodeLoader;
import com.zeitheron.hammercore.net.internal.thunder.Thunder;
import com.zeitheron.hammercore.net.internal.thunder.Thunder.Layer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;
import java.util.function.Supplier;

public class ParticleProxy_Client
		extends ParticleProxy_Common
{
	{
		MinecraftForge.EVENT_BUS.register(this);
		
		HammerCore.LOG.info("Added particle lighting supplier");
		ColoredLightManager.addGenerator(partialTime ->
				ParticleList.getParticlesList().stream().filter(Predicates.instanceOf(IGlowingEntity.class)).map(p -> (IGlowingEntity) p).map(e ->
						e.produceColoredLight(partialTime)));
	}
	
	private static final List<Particle> particleQueue = new ArrayList<>();
	
	public static void queueParticleSpawn(Particle particle)
	{
		particleQueue.add(particle);
	}
	
	@Override
	public Object sidedExecute(Supplier<Supplier<Object>> server, Supplier<Supplier<Object>> client)
	{
		return client.get().get();
	}
	
	@Override
	public void spawnZap(World w, Vec3d start, Vec3d end, int rgb)
	{
		if(!w.isRemote)
		{
			super.spawnZap(w, start, end, rgb);
			return;
		}
		ParticleZap zap = new ParticleZap(w, start.x, start.y, start.z, end.x, end.y, end.z,
				((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF
		);
		zap.spawn();
	}
	
	@Override
	public void spawnZap(int w, Vec3d start, Vec3d end, int rgb)
	{
		ParticleZap zap = null;
		if(Minecraft.getMinecraft().world.provider.getDimension() == w)
		{
			zap = new ParticleZap(Minecraft.getMinecraft().world, start.x, start.y, start.z, end.x, end.y, end.z,
					((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF
			);
			zap.spawn();
		}
	}
	
	@Override
	public void spawnSlowZap(World w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		if(!w.isRemote)
		{
			super.spawnSlowZap(w, start, end, rgb, maxTicks, ampl);
			return;
		}
		ParticleSlowZap zap = new ParticleSlowZap(w, maxTicks, ampl, start.x, start.y, start.z, end.x, end.y, end.z,
				((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF
		);
		zap.spawn();
	}
	
	@Override
	public void spawnSlowZap(int w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		ParticleSlowZap zap = null;
		if(Minecraft.getMinecraft().world.provider.getDimension() == w)
		{
			zap = new ParticleSlowZap(Minecraft.getMinecraft().world, maxTicks, ampl, start.x, start.y, start.z, end.x, end.y, end.z,
					((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF
			);
			zap.spawn();
		}
	}
	
	@Override
	public void spawnSimpleThunder(World world, Vec3d start, Vec3d end, long seed, int age, float fractMod, Layer core, Layer aura)
	{
		if(world.isRemote)
			ThunderHelper.thunder(world, start, end, new Thunder(seed, age, fractMod), core, aura, Thunder.Fractal.DEFAULT_FRACTAL);
		else
			super.spawnSimpleThunder(world, start, end, seed, age, fractMod, core, aura);
	}
	
	public static final List<IProcess> updatables = new ArrayList<>(16);
	
	@Override
	public void startProcess(IProcess proc)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			if(proc != null && !updatables.contains(proc))
				updatables.add(proc);
		} else
			super.startProcess(proc);
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent evt)
	{
		if(evt.phase != Phase.END)
			return;
		
		while(!particleQueue.isEmpty())
			Minecraft.getMinecraft().effectRenderer.addEffect(particleQueue.remove(0));
		ParticleList.refreshParticles();
		
		for(int i = 0; i < updatables.size(); ++i)
		{
			try
			{
				IProcess upd = updatables.get(i);
				upd.update();
				if(!upd.isAlive())
				{
					upd.onKill();
					updatables.remove(i);
				}
			} catch(Throwable err)
			{
			}
		}
	}
	
	public static double getParticleMotionX(Particle part)
	{
		return part.motionX;
	}
	
	public static double getParticleMotionY(Particle part)
	{
		return part.motionY;
	}
	
	public static double getParticleMotionZ(Particle part)
	{
		return part.motionZ;
	}
	
	public static double getParticlePosX(Particle part)
	{
		return part.posX;
	}
	
	public static double getParticlePosY(Particle part)
	{
		return part.posY;
	}
	
	public static double getParticlePosZ(Particle part)
	{
		return part.posZ;
	}
	
	public static void setParticleMotionX(Particle part, double d)
	{
		part.motionX = d;
	}
	
	public static void setParticleMotionY(Particle part, double d)
	{
		part.motionY = d;
	}
	
	public static void setParticleMotionZ(Particle part, double d)
	{
		part.motionZ = d;
	}
	
	public static void setParticlePosX(Particle part, double d)
	{
		part.posX = d;
	}
	
	public static void setParticlePosY(Particle part, double d)
	{
		part.posY = d;
	}
	
	public static void setParticlePosZ(Particle part, double d)
	{
		part.posZ = d;
	}
	
	@SubscribeEvent
	public void reloadTextures(TextureStitchEvent evt)
	{
		OpnodeLoader.reloadModels();
	}
}