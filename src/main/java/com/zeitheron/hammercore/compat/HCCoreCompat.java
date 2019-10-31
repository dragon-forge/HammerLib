package com.zeitheron.hammercore.compat;

import com.zeitheron.hammercore.mod.ILoadModule;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HCCoreCompat implements ILoadModule
{
	public void init()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void initClient()
	{
	}
}