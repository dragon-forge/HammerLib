package org.zeith.hammerlib.compat.top;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.proxy.HLConstants;

@BaseCompat.LoadCompat(
		modid = "theoneprobe",
		compatType = BaseHLCompat.class
)
public class TOPCompat
		extends BaseHLCompat
{
	public TOPCompat()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueCompat);
		HLConstants.enableHammerLibTooltipEngine = false;
	}
	
	private void enqueueCompat(InterModEnqueueEvent e)
	{
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTOP::new);
	}
}