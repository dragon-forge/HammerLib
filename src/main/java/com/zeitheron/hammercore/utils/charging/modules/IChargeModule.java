package com.zeitheron.hammercore.utils.charging.modules;

import java.util.List;

import com.zeitheron.hammercore.mod.ILoadModule;
import com.zeitheron.hammercore.utils.charging.IPlayerInventoryLister;

public interface IChargeModule extends ILoadModule
{
	void registerListers(List<IPlayerInventoryLister> listers);
}