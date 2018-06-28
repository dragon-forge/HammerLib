package com.zeitheron.hammercore.internal.blocks.multipart;

import com.zeitheron.hammercore.api.mhb.RaytracePlugin;
import com.zeitheron.hammercore.api.mhb.IRayCubeRegistry;
import com.zeitheron.hammercore.api.mhb.IRayRegistry;
import com.zeitheron.hammercore.internal.init.BlocksHC;

@RaytracePlugin
public class HammerCoreRaytracePlugin implements IRayRegistry
{
	@Override
	public void registerCubes(IRayCubeRegistry cube)
	{
		BlockMultipart multipart = (BlockMultipart) BlocksHC.MULTIPART;
		cube.bindBlockCubeManager(multipart, multipart);
	}
}