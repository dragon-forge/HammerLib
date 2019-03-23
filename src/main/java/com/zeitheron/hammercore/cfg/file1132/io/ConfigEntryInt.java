package com.zeitheron.hammercore.cfg.file1132.io;

import java.util.Objects;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.IConfigEntry;

public class ConfigEntryInt implements IConfigEntry
{
	final Configuration cfg;
	public int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;
	String description, name;
	Integer value, initialValue;
	
	public ConfigEntryInt(Configuration cfg, Integer initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}
	
	public ConfigEntryInt setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public ConfigEntryInt setMinValue(int min)
	{
		this.min = min;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}
	
	public ConfigEntryInt setMaxValue(int max)
	{
		this.max = max;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}
	
	public ConfigEntryInt setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}
		
		return this;
	}
	
	public ConfigEntryInt setValue(Integer value)
	{
		if(value != null)
			value = Math.max(min, Math.min(max, value));
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
	
	public Integer getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}
	
	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return Configuration.SERIALIZER_INT;
	}
}