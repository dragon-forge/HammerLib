package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.IConfigEntry;

import java.util.Objects;

public class ConfigEntryLong
		implements IConfigEntry
{
	final ConfigFile cfg;
	public long min = Long.MIN_VALUE, max = Long.MAX_VALUE;
	String description, name;
	Long value, initialValue;

	public ConfigEntryLong(ConfigFile cfg, Long initialValue)
	{
		this.value = initialValue;
		this.initialValue = initialValue;
		this.cfg = cfg;
	}

	public ConfigEntryLong setDescription(String description)
	{
		this.description = description;
		return this;
	}

	public ConfigEntryLong setMinValue(long min)
	{
		this.min = min;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}

	public ConfigEntryLong setMaxValue(long max)
	{
		this.max = max;
		if(value != null)
			value = Math.max(min, Math.min(max, value));
		return this;
	}

	public ConfigEntryLong setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
			cfg.markChanged();
		}

		return this;
	}

	public ConfigEntryLong setValue(Long value)
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

	public Long getValue()
	{
		if(value == null)
			setValue(initialValue);
		return value;
	}

	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return ConfigFile.SERIALIZER_LONG;
	}
}