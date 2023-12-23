package org.zeith.hammerlib.compat.curios;

import org.zeith.hammerlib.compat.base.*;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.util.charging.ItemChargeHelper;

@BaseCompat.LoadCompat(
		modid = "curios",
		compatType = BaseHLCompat.class
)
public class CuriosHLCompat
		extends BaseHLCompat
{
	public CuriosHLCompat(CompatContext ctx)
	{
		super(ctx);
		ItemChargeHelper.registerInventoryFactory(new CuriosInvLister());
	}
}