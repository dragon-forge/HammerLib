package org.zeith.hammerlib.core.scans;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.ConfigureMainRenderTargetEvent;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.RequireStencil;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.core.scans.base.IAnnotationScanListener;
import org.zeith.hammerlib.core.scans.base.IScanListener;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.*;
import java.util.function.BooleanSupplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ScanRequireStencil
{
	private static final Map<String, FMLModContainer> REQUIRING_STENCIL = new HashMap<>();
	
	private static BooleanSupplier requestStencils;
	
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
	
	public static BooleanSupplier requestStencil()
	{
		if(requestStencils != null) return requestStencils;
		
		var cc = ConfigHL.INSTANCE.get(LogicalSide.CLIENT).clientSide;
		if(cc.glStencil < 0)
			return requestStencils = Cast.constantB(false);
		
		boolean stencilReq = cc.glStencil > 0;
		for(Map.Entry<String, FMLModContainer> en : REQUIRING_STENCIL.entrySet())
		{
			stencilReq = true;
			var mi = en.getValue().getModInfo();
			HammerLib.LOG.info("Mod's {}({}) class {} is requesting stencils to be enabled.", mi.getDisplayName(), mi.getModId(), en.getKey());
		}
		
		return requestStencils = Cast.constantB(stencilReq);
	}
	
	@SubscribeEvent
	public static void setupStencils(ConfigureMainRenderTargetEvent e)
	{
		if(requestStencil().getAsBoolean())
			e.enableStencil();
	}
}