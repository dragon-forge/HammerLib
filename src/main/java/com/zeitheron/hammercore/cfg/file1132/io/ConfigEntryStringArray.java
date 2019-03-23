package com.zeitheron.hammercore.cfg.file1132.io;

import java.util.Arrays;
import java.util.Objects;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.IConfigEntry;

public class ConfigEntryStringArray implements IConfigEntry
{
	final Configuration cfg;
	String description, name;
	String[] value, initialValue;
	
	public ConfigEntryStringArray(Configuration cfg, String[] initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}
	
	public ConfigEntryStringArray setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public ConfigEntryStringArray setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}
		
		return this;
	}
	
	public ConfigEntryStringArray setValue(String[] value)
	{
		if(!Arrays.equals(value, this.value))
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
	
	public String[] getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}
	
	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return Configuration.SERIALIZER_STRING_ARRAY;
	}
}