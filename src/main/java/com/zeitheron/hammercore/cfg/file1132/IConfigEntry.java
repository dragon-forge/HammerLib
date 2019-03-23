package com.zeitheron.hammercore.cfg.file1132;

public interface IConfigEntry
{
	String getDescription();
	
	String getName();
	
	ConfigEntrySerializer<?> getSerializer();
	
	default String getSerializedName()
	{
		return getName().replaceAll("=", "\u2248");
	}
}