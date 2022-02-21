package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.IConfigEntry;

import java.util.Objects;

public class ConfigEntryDouble
		implements IConfigEntry
{
	final ConfigFile cfg;
	public double min = Double.MIN_VALUE, max = Double.MAX_VALUE;
	String description, name;
	Double value, initialValue;

	public ConfigEntryDouble(ConfigFile cfg, Double initialValue)
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
		if(Double.isNaN(min)) return this;
		this.min = min;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}

	public ConfigEntryDouble setMaxValue(double max)
	{
		if(Double.isNaN(max)) return this;
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
		return ConfigFile.SERIALIZER_DOUBLE;
	}
}