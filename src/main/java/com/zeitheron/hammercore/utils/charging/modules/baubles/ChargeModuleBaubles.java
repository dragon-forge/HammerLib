package com.zeitheron.hammercore.utils.charging.modules.baubles;

import java.util.List;

import com.zeitheron.hammercore.mod.ModuleLoader;
import com.zeitheron.hammercore.utils.charging.IPlayerInventoryLister;
import com.zeitheron.hammercore.utils.charging.modules.IChargeModule;

@ModuleLoader(requiredModid = "baubles")
public class ChargeModuleBaubles implements IChargeModule
{
	@Override
	public void registerListers(List<IPlayerInventoryLister> listers)
	{
	}
}