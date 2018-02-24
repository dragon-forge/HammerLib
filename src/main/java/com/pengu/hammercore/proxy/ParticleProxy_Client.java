package com.pengu.hammercore.proxy;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.api.dynlight.DynamicLightGetter;
import com.pengu.hammercore.client.OpnodeLoader;
import com.pengu.hammercore.client.particle.api.ParticleList;
import com.pengu.hammercore.client.particle.def.ParticleSlowZap;
import com.pengu.hammercore.client.particle.def.ParticleZap;
import com.pengu.hammercore.client.particle.def.thunder.ThunderHelper;
import com.pengu.hammercore.client.particle.old.iOldParticle;
import com.pengu.hammercore.client.render.Render3D;
import com.pengu.hammercore.net.pkt.thunder.Thunder;
import com.pengu.hammercore.net.pkt.thunder.Thunder.Layer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ParticleProxy_Client extends ParticleProxy_Common
{
	{
		MinecraftForge.EVENT_BUS.register(new DynamicLightGetter());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new Render3D());
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
	
	@Override
	public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		return DynamicLightGetter.getLightValue(blockState, world, pos);
	}
	
	public void clientTick(ClientTickEvent evt)
	{
		while(!particleQueue.isEmpty())
			Minecraft.getMinecraft().effectRenderer.addEffect(particleQueue.remove(0));
		ParticleList.refreshParticles();
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