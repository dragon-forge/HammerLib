package org.zeith.hammerlib.core.scans;

import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.core.adapter.RegistryAdapter;
import org.zeith.hammerlib.core.scans.base.IAnnotationScanListener;
import org.zeith.hammerlib.core.scans.base.IScanListener;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.Objects;
import java.util.function.Consumer;

public class ScanRegisters
{
	public static IScanListener create()
	{
		return IAnnotationScanListener.forAnnotation(SimplyRegister.class, ElementType.TYPE, ScanRegisters::handle);
	}
	
	private static void handle(ScanDataHelper.ModAwareAnnotationData data)
	{
		// Register all content providers
		data.getOwnerMod()
				.ifPresent(mc ->
				{
					HammerLib.LOG.info("Hooked {} from {} to register it's stuff.", data.clazz(), mc.getModId());
					mc.getEventBus()
							.addListener((Consumer<RegisterEvent>) event ->
									RegistryAdapter.register(event, data.getOwnerClass(), mc, data.getProperty("prefix").map(Objects::toString).orElse(""))
							);
				});
	}
}