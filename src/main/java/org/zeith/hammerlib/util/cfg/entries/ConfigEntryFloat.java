package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.IConfigEntry;

import java.util.Objects;

public class ConfigEntryFloat
		implements IConfigEntry
{
	final ConfigFile cfg;
	public float min = Float.MIN_VALUE, max = Float.MAX_VALUE;
	String description, name;
	Float value, initialValue;

	public ConfigEntryFloat(ConfigFile cfg, Float initialValue)
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
		if(Float.isNaN(min)) return this;
		this.min = min;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}

	public ConfigEntryFloat setMaxValue(float max)
	{
		if(Float.isNaN(max)) return this;
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
		return ConfigFile.SERIALIZER_FLOAT;
	}
}