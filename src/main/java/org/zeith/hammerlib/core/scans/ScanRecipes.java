package org.zeith.hammerlib.core.scans;

import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.util.configured.io.UnsafeHax;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

public class ScanRecipes
{
	public static void setup()
	{
		// Register all recipe providers
		ScanDataHelper.lookupAnnotatedObjects(ProvideRecipes.class).forEach(data ->
		{
			var ow = data.getOwnerMod().orElse(null);
			if(ow == null)
			{
				HammerLib.LOG.info("Skipping mod-less @ProvideRecipes annotation in {}", data.clazz());
				return;
			}
			
			Class<?> c = data.getOwnerClass();
			if(!IRecipeProvider.class.isAssignableFrom(c))
			{
				HammerLib.LOG.error("@ProvideRecipes {} does not implement {}", c, IRecipeProvider.class);
				return;
			}
			
			IRecipeProvider provider = (IRecipeProvider) UnsafeHax.unitializedInstance(c);
			if(provider == null) return;
			
			var bus = ow.getEventBus();
			if(bus == null)
			{
				HammerLib.LOG.warn("Skipping registration of {} since we couldn't find relevant mod bus.", data.clazz());
				return;
			}
			bus.addListener(provider::provideRecipes);
			bus.addListener(provider::spoofRecipes);
		});
	}
}