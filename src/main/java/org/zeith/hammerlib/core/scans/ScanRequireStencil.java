package org.zeith.hammerlib.core.scans;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.RequireStencil;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.core.scans.base.IAnnotationScanListener;
import org.zeith.hammerlib.core.scans.base.IScanListener;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.*;

public class ScanRequireStencil
{
	private static final Map<String, FMLModContainer> REQUIRING_STENCIL = new HashMap<>();
	
	public static IScanListener create()
	{
		return IAnnotationScanListener.forAnnotation(RequireStencil.class, ElementType.TYPE, ScanRequireStencil::handle);
	}
	
	private static void handle(ScanDataHelper.ModAwareAnnotationData data)
	{
		data.getOwnerMod().ifPresent(mc ->
				REQUIRING_STENCIL.put(data.clazz().getClassName(), mc)
		);
	}
	
	public static Runnable requestStencil()
	{
		var cc = ConfigHL.INSTANCE.get(LogicalSide.CLIENT).clientSide;
		if(cc.glStencil < 0) return () ->
		{
		};
		
		boolean stencilReq = cc.glStencil > 0;
		for(Map.Entry<String, FMLModContainer> en : REQUIRING_STENCIL.entrySet())
		{
			stencilReq = true;
			var mi = en.getValue().getModInfo();
			HammerLib.LOG.info("Mod's {}({}) class {} is requesting stencils to be enabled.", mi.getDisplayName(), mi.getModId(), en.getKey());
		}
		
		if(!stencilReq) return () ->
		{
		};
		
		return () -> Minecraft.getInstance().getMainRenderTarget().enableStencil();
	}
}