package com.zeitheron.hammercore.cfg.file1132.io;

import java.math.BigInteger;
import java.util.Objects;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.IConfigEntry;

public class ConfigEntryBigInt implements IConfigEntry
{
	final Configuration cfg;
	public BigInteger min, max;
	String description, name;
	BigInteger value, initialValue;
	
	public ConfigEntryBigInt(Configuration cfg, BigInteger initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}
	
	public ConfigEntryBigInt setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public ConfigEntryBigInt setMinValue(BigInteger min)
	{
		this.min = min;
		if(value != null)
		{
			if(min != null)
				value = value.max(min);
			if(max != null)
				value = value.min(max);
		}
		return this;
	}
	
	public ConfigEntryBigInt setMaxValue(BigInteger max)
	{
		this.max = max;
		if(value != null)
		{
			if(min != null)
				value = value.max(min);
			if(max != null)
				value = value.min(max);
		}
		return this;
	}
	
	public ConfigEntryBigInt setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}
		
		return this;
	}
	
	public ConfigEntryBigInt setValue(BigInteger value)
	{
		if(value != null)
		{
			if(min != null)
				value = value.max(min);
			if(max != null)
				value = value.min(max);
		}
		
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
	
	public BigInteger getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}
	
	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return Configuration.SERIALIZER_BIG_INT;
	}
}