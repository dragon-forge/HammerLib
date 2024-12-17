package org.zeith.hammerlib.compat.top;

import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import org.zeith.hammerlib.compat.base.*;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.proxy.HLConstants;

@ModCompat(
		modid = "theoneprobe",
		type = BaseHLCompat.class
)
public class TOPCompat
		extends BaseHLCompat
{
	public TOPCompat(CompatContext ctx)
	{
		super(ctx);
		ctx.getModBus().addListener(this::enqueueCompat);
		HLConstants.enableHammerLibTooltipEngine = false;
	}
	
	private void enqueueCompat(InterModEnqueueEvent e)
	{
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTOP::new);
	}
}