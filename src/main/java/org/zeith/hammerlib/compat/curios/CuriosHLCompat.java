package org.zeith.hammerlib.compat.curios;

import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.util.charging.ItemChargeHelper;

@BaseCompat.LoadCompat(
		modid = "curios",
		compatType = BaseHLCompat.class
)
public class CuriosHLCompat
		extends BaseHLCompat
{
	public CuriosHLCompat()
	{
		ItemChargeHelper.registerInventoryFactory(new CuriosInvLister());
	}
}