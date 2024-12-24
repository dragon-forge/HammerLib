package org.zeith.hammerlib.core.scans.base;

import net.minecraftforge.forgespi.language.ModFileScanData;

public interface IScanListener
{
	void accept(ModFileScanData data);
	
	default IScanListener then(IScanListener next)
	{
		return data ->
		{
			accept(data);
			next.accept(data);
		};
	}
}