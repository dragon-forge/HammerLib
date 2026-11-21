package org.zeith.hammerlib.core.scans;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.modscan.ModAnnotation;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.api.proxy.IProxy;
import org.zeith.hammerlib.core.adapter.RegistryAdapter;
import org.zeith.hammerlib.core.scans.base.*;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.*;

public class ScanSetups
{
	private static final String DIST_DESC = Type.getType(Dist.class).getDescriptor();
	private static final List<ModAnnotation.EnumHolder> BOTH_SIDES = Stream
			.of(Dist.values())
			.map(dst -> new ModAnnotation.EnumHolder(DIST_DESC, dst.name()))
			.collect(Collectors.toList());
	
	public static IScanListener create()
	{
		IScanListener l = IAnnotationScanListener.forAnnotation(Setup.class, ElementType.METHOD, ScanSetups::handleSetup);
		return IProxy.createSided(
				() -> () -> l.then(IAnnotationScanListener.forAnnotation(ClientSetup.class, ElementType.METHOD, ScanSetups::clientSetup)),
				() -> () -> l
		);
	}
	
	private static void handleSetup(ScanDataHelper.ModAwareAnnotationData data)
	{
		Object side = data.getProperty("side").orElse(BOTH_SIDES);
		
		if(side instanceof List<?> lst && !lst.isEmpty())
		{
			for(Object o : lst)
			{
				if(o instanceof ModAnnotation.EnumHolder h && FMLEnvironment.dist.name().equals(h.value()))
				{
					HammerLib.LOG.info("Injecting setup into {}", data.clazz().getClassName());
					
					data.getOwnerMod()
					    .map(FMLModContainer::getEventBus)
					    .ifPresent(b ->
								b.addListener((Consumer<FMLCommonSetupEvent>) event -> RegistryAdapter.setup(event, data.getOwnerClass(), data.getMemberName()))
						);
					
					return;
				}
			}
		} else
			HammerLib.LOG.warn("What the hell is this? {}->{}", data.parent.clazz(), data.getMemberName());
	}
	
	private static void clientSetup(ScanDataHelper.ModAwareAnnotationData data)
	{
		HammerLib.LOG.info("Injecting client-setup into {}", data.clazz().getClassName());
		
		data.getOwnerMod()
		    .map(FMLModContainer::getEventBus)
		    .ifPresent(b -> b.addListener((Consumer<FMLClientSetupEvent>) event ->
					RegistryAdapter.clientSetup(event, data.getOwnerClass(), data.getMemberName())
			));
	}
}