package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.IConfigEntry;

import java.util.Objects;

public class ConfigEntryBoolean
		implements IConfigEntry
{
	final ConfigFile cfg;
	String description, name;
	Boolean value, initialValue;

	public ConfigEntryBoolean(ConfigFile cfg, Boolean initialValue)
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
		return ConfigFile.SERIALIZER_BIT;
	}
}