package com.pengu.hammercore.api.dynlight;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public class DynLightContainer
{
	private final iDynlightSrc src;
	private int x, y, z, prevX, prevY, prevZ;
	
	public DynLightContainer(iDynlightSrc src)
	{
		this.src = src;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public iDynlightSrc getLightSource()
	{
		return src;
	}
	
	public boolean update()
	{
		iMovable mov = src.getSrcInfo();
		
		if(mov == null || !mov.isAlive())
			return false;
		
		if(hasSrcMoved(mov))
		{
			mov.getWorld().checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y, z));
			mov.getWorld().checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(prevX, prevY, prevZ));
		}
		
		return true;
	}
	
	private boolean hasSrcMoved(iMovable mov)
	{
		int nx = mov.getX();
		int ny = mov.getY();
		int nz = mov.getZ();
		
		if(x != nx || y != ny || z != nz)
		{
			prevX = x;
			prevY = y;
			prevZ = z;
			x = nx;
			y = ny;
			z = nz;
			
			return true;
		}
		
		return false;
	}
}