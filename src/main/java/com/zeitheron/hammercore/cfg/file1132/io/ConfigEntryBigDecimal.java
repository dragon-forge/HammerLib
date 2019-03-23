package com.zeitheron.hammercore.cfg.file1132.io;

import java.math.BigDecimal;
import java.util.Objects;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.IConfigEntry;

public class ConfigEntryBigDecimal implements IConfigEntry
{
	final Configuration cfg;
	public BigDecimal min, max;
	String description, name;
	BigDecimal value, initialValue;
	
	public ConfigEntryBigDecimal(Configuration cfg, BigDecimal initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}
	
	public ConfigEntryBigDecimal setDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	public ConfigEntryBigDecimal setMinValue(BigDecimal min)
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
	
	public ConfigEntryBigDecimal setMaxValue(BigDecimal max)
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
	
	public ConfigEntryBigDecimal setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}
		
		return this;
	}
	
	public ConfigEntryBigDecimal setValue(BigDecimal value)
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
	
	public BigDecimal getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}
	
	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return Configuration.SERIALIZER_BIG_DECIMAL;
	}
}