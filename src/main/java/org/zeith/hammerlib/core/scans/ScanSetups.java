package org.zeith.hammerlib.core.scans;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.modscan.ModAnnotation;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.Setup;
import org.zeith.hammerlib.annotations.client.ClientSetup;
import org.zeith.hammerlib.core.adapter.RegistryAdapter;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScanSetups
{
	public static void setup()
	{
		List<ModAnnotation.EnumHolder> bothSides = Stream.of(Dist.values())
				.map(dst -> new ModAnnotation.EnumHolder("Lnet/neoforged/api/distmarker/Dist;", dst.name()))
				.collect(Collectors.toList());
		
		// Register all setups
		ScanDataHelper.lookupAnnotatedObjects(Setup.class).forEach(data ->
		{
			Object side = data.getProperty("side")
					.orElse(bothSides);
			
			if(side instanceof List<?> lst && !lst.isEmpty())
			{
				for(Object o : lst)
				{
					if(o instanceof ModAnnotation.EnumHolder h && FMLEnvironment.dist.name().equals(h.value()))
					{
						if(data.getTargetType() == ElementType.METHOD)
						{
							HammerLib.LOG.info("Injecting setup into " + data.clazz().getClassName());
							data.getOwnerMod()
									.map(FMLModContainer::getEventBus)
									.ifPresent(b -> b.addListener((Consumer<FMLCommonSetupEvent>) event -> RegistryAdapter.setup(event, data.getOwnerClass(), data.getMemberName())));
						}
						
						break;
					}
				}
			} else
				HammerLib.LOG.warn("What the hell is this? " + data.parent.clazz() + "->" + data.getMemberName());
		});
	}
	
	public static void clientSetup()
	{
		ScanDataHelper.lookupAnnotatedObjects(ClientSetup.class).forEach(data ->
		{
			if(data.getTargetType() == ElementType.METHOD)
			{
				HammerLib.LOG.info("Injecting client-setup into " + data.clazz().getClassName());
				data.getOwnerMod()
						.map(FMLModContainer::getEventBus)
						.ifPresent(b -> b.addListener((Consumer<FMLClientSetupEvent>) event ->
								RegistryAdapter.clientSetup(event, data.getOwnerClass(), data.getMemberName())
						));
			}
		});
	}
}