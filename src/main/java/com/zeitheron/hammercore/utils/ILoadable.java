package com.zeitheron.hammercore.utils;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

public interface ILoadable
{
	default void preInit()
	{
		
	}
	
	default void preInit(ASMDataTable table)
	{
		preInit();
	}
	
	default void init()
	{
		
	}
	
	default void postInit()
	{
		
	}
}