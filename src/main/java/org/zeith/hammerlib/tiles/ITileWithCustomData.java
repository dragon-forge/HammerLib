package org.zeith.hammerlib.tiles;

import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public interface ITileWithCustomData
{
	@SideOnly(Side.CLIENT)
	String getF3Registry();
	
	@SideOnly(Side.CLIENT)
	void addProperties(Map<String, Object> properties, RayTraceResult trace);
}