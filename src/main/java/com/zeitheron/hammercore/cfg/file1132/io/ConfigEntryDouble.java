package com.zeitheron.hammercore.cfg.file1132.io;

import java.util.Objects;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.IConfigEntry;

public class ConfigEntryDouble implements IConfigEntry
{
	final Configuration cfg;
	public double min = Double.MIN_VALUE, max = Double.MAX_VALUE;
	String description, name;
	Double value, initialValue;
	
	public ConfigEntryDouble(Configuration cfg, Double initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}
	
	public ConfigEntryDouble setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public ConfigEntryDouble setMinValue(double min)
	{
		this.min = min;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}
	
	public ConfigEntryDouble setMaxValue(double max)
	{
		this.max = max;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}
	
	public ConfigEntryDouble setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}
		
		return this;
	}
	
	public ConfigEntryDouble setValue(Double value)
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
	
	public Double getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}
	
	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return Configuration.SERIALIZER_DOUBLE;
	}
}