package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.IConfigEntry;

import java.util.Arrays;
import java.util.Objects;

public class ConfigEntryStringArray
		implements IConfigEntry
{
	final ConfigFile cfg;
	String description, name;
	String[] value, initialValue;

	public ConfigEntryStringArray(ConfigFile cfg, String[] initialValue)
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
		return ConfigFile.SERIALIZER_STRING_ARRAY;
	}
}