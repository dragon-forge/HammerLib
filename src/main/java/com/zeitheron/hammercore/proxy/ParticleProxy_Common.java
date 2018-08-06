package com.zeitheron.hammercore.proxy;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.IProcess;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketSpawnSlowZap;
import com.zeitheron.hammercore.net.internal.PacketSpawnZap;
import com.zeitheron.hammercore.net.internal.thunder.PacketCreateThunder;
import com.zeitheron.hammercore.net.internal.thunder.Thunder;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ParticleProxy_Common
{
	public void startProcess(IProcess proc)
	{
		if(proc != null && !HammerCore.updatables.contains(proc))
			HammerCore.updatables.add(proc);
	}
	
	public void spawnZap(World w, Vec3d start, Vec3d end, int rgb)
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
		
		HCNet.INSTANCE.sendToAllAround(zap, new TargetPoint(w.provider.getDimension(), x, y, z, 128));
	}
	
	public void spawnZap(int w, Vec3d start, Vec3d end, int rgb)
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
		
		HCNet.INSTANCE.sendToAllAround(zap, new TargetPoint(w, x, y, z, 128));
	}
	
	public void spawnSlowZap(World w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
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
		
		HCNet.INSTANCE.sendToAllAround(zap, new TargetPoint(w.provider.getDimension(), x, y, z, 128));
	}
	
	public void spawnSlowZap(int w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
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
		
		HCNet.INSTANCE.sendToAllAround(zap, new TargetPoint(w, x, y, z, 128));
	}
	
	/**
	 * Creates a new thunder FX using default fractal.
	 * 
	 * @param world
	 *            The world
	 * @param start
	 *            The start
	 * @param end
	 *            The end
	 * @param seed
	 *            The seed
	 * @param age
	 *            The age
	 * @param fractMod
	 *            The fractal modifier
	 * @param core
	 *            The core layer
	 * @param aura
	 *            The aura layer
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
		
		HCNet.INSTANCE.sendToAllAround(p, new TargetPoint(world.provider.getDimension(), x, y, z, 128));
	}
}