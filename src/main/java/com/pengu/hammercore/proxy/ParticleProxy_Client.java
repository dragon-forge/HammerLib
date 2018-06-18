package com.pengu.hammercore.proxy;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.client.OpnodeLoader;
import com.pengu.hammercore.client.particle.api.ParticleList;
import com.pengu.hammercore.client.particle.def.ParticleSlowZap;
import com.pengu.hammercore.client.particle.def.ParticleZap;
import com.pengu.hammercore.client.particle.def.thunder.ThunderHelper;
import com.pengu.hammercore.client.particle.old.iOldParticle;
import com.pengu.hammercore.net.pkt.thunder.Thunder;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class ParticleProxy_Client extends ParticleProxy_Common
{
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private static final List<Particle> particleQueue = new ArrayList<>();
	
	public static void queueParticleSpawn(Particle particle)
	{
		particleQueue.add(particle);
	}
	
	@Override
	public iOldParticle spawnZap(World w, Vec3d start, Vec3d end, int rgb)
	{
		if(!w.isRemote)
			return super.spawnZap(w, start, end, rgb);
		ParticleZap zap = new ParticleZap(w, start.x, start.y, start.z, end.x, end.y, end.z, ((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF);
		zap.spawn();
		return zap;
	}
	
	@Override
	public iOldParticle spawnZap(int w, Vec3d start, Vec3d end, int rgb)
	{
		ParticleZap zap = null;
		if(Minecraft.getMinecraft().world.provider.getDimension() == w)
		{
			zap = new ParticleZap(Minecraft.getMinecraft().world, start.x, start.y, start.z, end.x, end.y, end.z, ((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF);
			zap.spawn();
		}
		return zap;
	}
	
	@Override
	public iOldParticle spawnSlowZap(World w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		if(!w.isRemote)
			return super.spawnSlowZap(w, start, end, rgb, maxTicks, ampl);
		ParticleSlowZap zap = new ParticleSlowZap(w, maxTicks, ampl, start.x, start.y, start.z, end.x, end.y, end.z, ((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF);
		zap.spawn();
		return zap;
	}
	
	@Override
	public iOldParticle spawnSlowZap(int w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		ParticleSlowZap zap = null;
		if(Minecraft.getMinecraft().world.provider.getDimension() == w)
		{
			zap = new ParticleSlowZap(Minecraft.getMinecraft().world, maxTicks, ampl, start.x, start.y, start.z, end.x, end.y, end.z, ((rgb >> 16) & 0xFF) / (float) 0xFF, ((rgb >> 8) & 0xFF) / (float) 0xFF, ((rgb >> 0) & 0xFF) / (float) 0xFF);
			zap.spawn();
		}
		return zap;
	}
	
	@Override
	public void spawnSimpleThunder(World world, Vec3d start, Vec3d end, long seed, int age, float fractMod, Layer core, Layer aura)
	{
		if(world.isRemote)
			ThunderHelper.thunder(world, start, end, new Thunder(seed, age, fractMod), core, aura, Thunder.Fractal.DEFAULT_FRACTAL);
		else
			super.spawnSimpleThunder(world, start, end, seed, age, fractMod, core, aura);
	}
	
	public static final List<iProcess> updatables = new ArrayList<>(16);
	
	@Override
	public void startProcess(iProcess proc)
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
				iProcess upd = updatables.get(i);
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