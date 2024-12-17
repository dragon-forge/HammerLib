package org.zeith.hammerlib.core.scans;

import net.neoforged.neoforge.registries.RegisterEvent;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.core.adapter.RegistryAdapter;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.Objects;
import java.util.function.Consumer;

public class ScanRegisters
{
	public static void setup()
	{
		// Register all content providers
		ScanDataHelper.lookupAnnotatedObjects(SimplyRegister.class).forEach(data ->
		{
			if(data.getTargetType() == ElementType.TYPE)
				data.getOwnerMod()
						.ifPresent(mc ->
						{
							HammerLib.LOG.info("Hooked {} from {} to register it's stuff.", data.clazz(), mc.getModId());
							mc.getEventBus()
									.addListener((Consumer<RegisterEvent>) event ->
											RegistryAdapter.register(event, data.getOwnerClass(), mc, data.getProperty("prefix").map(Objects::toString).orElse(""))
									);
						});
		});
	}
}