package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.IConfigEntry;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigEntryCategory
		implements IConfigEntry
{
	final ConfigFile cfg;
	String description, name;
	Map<String, IConfigEntry> entries = new HashMap<>();

	public ConfigEntryCategory(ConfigFile cfg)
	{
		this.cfg = cfg;
	}

	public ConfigEntryCategory getCategory(String name)
	{
		if(!(entries.get(name) instanceof ConfigEntryCategory))
		{
			ConfigEntryCategory ces = new ConfigEntryCategory(cfg);
			ces.setName(name);
			entries.put(name, ces);
		}
		return (ConfigEntryCategory) entries.get(name);
	}

	public ConfigEntryString getStringEntry(String name, String defaultValue)
	{
		if(!(entries.get(name) instanceof ConfigEntryString))
		{
			ConfigEntryString ces = new ConfigEntryString(cfg, defaultValue);
			ces.setName(name);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryString ces = (ConfigEntryString) entries.get(name);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryStringArray getStringArrayEntry(String name, String... defaultValue)
	{
		if(!(entries.get(name) instanceof ConfigEntryStringArray))
		{
			ConfigEntryStringArray ces = new ConfigEntryStringArray(cfg, defaultValue);
			ces.setName(name);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryStringArray ces = (ConfigEntryStringArray) entries.get(name);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryBoolean getBooleanEntry(String name, boolean defaultValue)
	{
		if(!(entries.get(name) instanceof ConfigEntryBoolean))
		{
			ConfigEntryBoolean ces = new ConfigEntryBoolean(cfg, defaultValue);
			ces.setName(name);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryBoolean ces = (ConfigEntryBoolean) entries.get(name);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryInt getIntEntry(String name, int defaultValue, int min, int max)
	{
		if(!(entries.get(name) instanceof ConfigEntryInt))
		{
			ConfigEntryInt ces = new ConfigEntryInt(cfg, defaultValue);
			ces.setName(name).setMinValue(min).setMaxValue(max);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryInt ces = (ConfigEntryInt) entries.get(name);
			ces.setMinValue(min).setMaxValue(max);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryBigInt getBigIntEntry(String name, BigInteger defaultValue, BigInteger min, BigInteger max)
	{
		if(!(entries.get(name) instanceof ConfigEntryBigInt))
		{
			ConfigEntryBigInt ces = new ConfigEntryBigInt(cfg, defaultValue);
			ces.setName(name).setMinValue(min).setMaxValue(max);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryBigInt ces = (ConfigEntryBigInt) entries.get(name);
			ces.setMinValue(min).setMaxValue(max);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryBigDecimal getBigDecimalEntry(String name, BigDecimal defaultValue, BigDecimal min, BigDecimal max)
	{
		if(!(entries.get(name) instanceof ConfigEntryBigDecimal))
		{
			ConfigEntryBigDecimal ces = new ConfigEntryBigDecimal(cfg, defaultValue);
			ces.setName(name).setMinValue(min).setMaxValue(max);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryBigDecimal ces = (ConfigEntryBigDecimal) entries.get(name);
			ces.setMinValue(min).setMaxValue(max);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryLong getLongEntry(String name, long defaultValue, long min, long max)
	{
		if(!(entries.get(name) instanceof ConfigEntryLong))
		{
			ConfigEntryLong ces = new ConfigEntryLong(cfg, defaultValue);
			ces.setName(name).setMinValue(min).setMaxValue(max);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryLong ces = (ConfigEntryLong) entries.get(name);
			ces.setMinValue(min).setMaxValue(max);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryFloat getFloatEntry(String name, float defaultValue, float min, float max)
	{
		if(!(entries.get(name) instanceof ConfigEntryFloat))
		{
			ConfigEntryFloat ces = new ConfigEntryFloat(cfg, defaultValue);
			ces.setName(name).setMinValue(min).setMaxValue(max);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryFloat ces = (ConfigEntryFloat) entries.get(name);
			ces.setMinValue(min).setMaxValue(max);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryDouble getDoubleEntry(String name, double defaultValue, double min, double max)
	{
		if(!(entries.get(name) instanceof ConfigEntryDouble))
		{
			ConfigEntryDouble ces = new ConfigEntryDouble(cfg, defaultValue);
			ces.setName(name).setMinValue(min).setMaxValue(max);
			entries.put(name, ces);
			return ces;
		} else
		{
			ConfigEntryDouble ces = (ConfigEntryDouble) entries.get(name);
			ces.setMinValue(min).setMaxValue(max);
			ces.initialValue = defaultValue;
			return ces;
		}
	}

	public ConfigEntryCategory setDescription(String description)
	{
		this.description = description;
		return this;
	}

	public ConfigEntryCategory setName(String name)
	{
		if(!Objects.equals(name, this.name))
		{
			this.name = name;
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

	public Map<String, IConfigEntry> getEntries()
	{
		return entries;
	}

	public IConfigEntry getEntry(String key)
	{
		return entries.get(key);
	}

	@Override
	public ConfigEntrySerializer<?> getSerializer()
	{
		return ConfigFile.SERIALIZER_CATEGORY;
	}
}