package com.zeitheron.hammercore.cfg.file1132.io;

import java.util.Objects;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.IConfigEntry;

public class ConfigEntryBoolean implements IConfigEntry
{
	final Configuration cfg;
	String description, name;
	Boolean value, initialValue;
	
	public ConfigEntryBoolean(Configuration cfg, Boolean initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}
	
	public ConfigEntryBoolean setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public ConfigEntryBoolean setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}
		
		return this;
	}
	
	public ConfigEntryBoolean setValue(Boolean value)
	{
		if(!Objects.equals(value, this.value))
		{
			this.value = value;
			cfg.markChanged();
		}
		
		return this;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public Boolean getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}
	
	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return Configuration.SERIALIZER_BIT;
	}
}