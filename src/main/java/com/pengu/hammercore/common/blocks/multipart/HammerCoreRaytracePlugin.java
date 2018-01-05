package com.pengu.hammercore.common.blocks.multipart;

import com.pengu.hammercore.api.mhb.RaytracePlugin;
import com.pengu.hammercore.api.mhb.iRayCubeRegistry;
import com.pengu.hammercore.api.mhb.iRayRegistry;
import com.pengu.hammercore.core.init.BlocksHC;

@RaytracePlugin
public class HammerCoreRaytracePlugin implements iRayRegistry
{
	@Override
	public void registerCubes(iRayCubeRegistry cube)
	{
		BlockMultipart multipart = (BlockMultipart) BlocksHC.MULTIPART;
		cube.bindBlockCubeManager(multipart, multipart);
	}
}