package com.zeitheron.hammercore.cfg.file1132.io;

import java.util.Objects;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.IConfigEntry;

public class ConfigEntryFloat implements IConfigEntry
{
	final Configuration cfg;
	public float min = Float.MIN_VALUE, max = Float.MAX_VALUE;
	String description, name;
	Float value, initialValue;
	
	public ConfigEntryFloat(Configuration cfg, Float initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}
	
	public ConfigEntryFloat setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public ConfigEntryFloat setMinValue(float min)
	{
		this.min = min;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}
	
	public ConfigEntryFloat setMaxValue(float max)
	{
		this.max = max;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}
	
	public ConfigEntryFloat setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}
		
		return this;
	}
	
	public ConfigEntryFloat setValue(Float value)
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
	
	public Float getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}
	
	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return Configuration.SERIALIZER_FLOAT;
	}
}