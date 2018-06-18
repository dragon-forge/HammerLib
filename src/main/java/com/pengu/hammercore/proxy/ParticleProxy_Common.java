package com.pengu.hammercore.proxy;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.client.particle.old.iOldParticle;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.PacketSpawnSlowZap;
import com.pengu.hammercore.net.pkt.PacketSpawnZap;
import com.pengu.hammercore.net.pkt.thunder.PacketCreateThunder;
import com.pengu.hammercore.net.pkt.thunder.Thunder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ParticleProxy_Common
{
	public void startProcess(iProcess proc)
	{
		if(proc != null && !HammerCore.updatables.contains(proc))
			HammerCore.updatables.add(proc);
	}
	
	public iOldParticle spawnZap(World w, Vec3d start, Vec3d end, int rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w.provider.getDimension();
		
		/** Get center between 2 points */
		double x = start.x + (end.x - start.x) / 2;
		double y = start.y + (end.y - start.y) / 2;
		double z = start.z + (end.z - start.z) / 2;
		
		HCNetwork.manager.sendToAllAround(zap, new TargetPoint(w.provider.getDimension(), x, y, z, 128));
		return null;
	}
	
	public iOldParticle spawnZap(int w, Vec3d start, Vec3d end, int rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w;
		
		/** Get center between 2 points */
		double x = start.x + (end.x - start.x) / 2;
		double y = start.y + (end.y - start.y) / 2;
		double z = start.z + (end.z - start.z) / 2;
		
		HCNetwork.manager.sendToAllAround(zap, new TargetPoint(w, x, y, z, 128));
		return null;
	}
	
	public iOldParticle spawnSlowZap(World w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		PacketSpawnSlowZap zap = new PacketSpawnSlowZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w.provider.getDimension();
		zap.maxTicks = maxTicks;
		zap.ampl = ampl;
		
		/** Get center between 2 points */
		double x = start.x + (end.x - start.x) / 2;
		double y = start.y + (end.y - start.y) / 2;
		double z = start.z + (end.z - start.z) / 2;
		
		HCNetwork.manager.sendToAllAround(zap, new TargetPoint(w.provider.getDimension(), x, y, z, 128));
		return null;
	}
	
	public iOldParticle spawnSlowZap(int w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		PacketSpawnSlowZap zap = new PacketSpawnSlowZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w;
		zap.maxTicks = maxTicks;
		zap.ampl = ampl;
		
		/** Get center between 2 points */
		double x = start.x + (end.x - start.x) / 2;
		double y = start.y + (end.y - start.y) / 2;
		double z = start.z + (end.z - start.z) / 2;
		
		HCNetwork.manager.sendToAllAround(zap, new TargetPoint(w, x, y, z, 128));
		return null;
	}
	
	/**
	 * Creates a new thunder FX using default fractal.
	 */
	public void spawnSimpleThunder(World world, Vec3d start, Vec3d end, long seed, int age, float fractMod, Thunder.Layer core, Thunder.Layer aura)
	{
		PacketCreateThunder p = new PacketCreateThunder();
		p.base = new Thunder(seed, age, fractMod);
		p.core = core;
		p.aura = aura;
		p.start = start;
		p.end = end;
		
		/** Get center between 2 points */
		double x = start.x + (end.x - start.x) / 2;
		double y = start.y + (end.y - start.y) / 2;
		double z = start.z + (end.z - start.z) / 2;
		
		HCNetwork.manager.sendToAllAround(p, new TargetPoint(world.provider.getDimension(), x, y, z, 128));
	}
}