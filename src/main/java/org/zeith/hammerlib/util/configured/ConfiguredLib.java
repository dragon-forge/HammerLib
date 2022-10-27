package org.zeith.hammerlib.util.configured;

import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;
import org.zeith.hammerlib.util.configured.types.*;

import java.io.File;
import java.util.*;

public class ConfiguredLib
{
	public static final String VERSION = "1.0.0";
	
	static final Map<String, ConfigToken<?>> TOKEN_REGISTRY_BY_PREFIX = new HashMap<>();
	
	
	public static final ConfigToken<ConfigCategory> CATEGORY = new ConfigToken<>("C", ConfigCategory.class, ConfigCategory::new);
	public static final ConfigToken<ConfigBoolean> BOOLEAN = new ConfigToken<>("B", ConfigBoolean.class, ConfigBoolean::new);
	public static final ConfigToken<ConfigString> STRING = new ConfigToken<>("S", ConfigString.class, ConfigString::new);
	public static final ConfigToken<ConfigInteger> INT = new ConfigToken<>("I", ConfigInteger.class, ConfigInteger::new);
	public static final ConfigToken<ConfigDecimal> DECIMAL = new ConfigToken<>("D", ConfigDecimal.class, ConfigDecimal::new);
	
	
	@SuppressWarnings({
			"rawtypes",
			"unchecked"
	})
	public static ConfigToken<?> getByPrefix(String prefix)
	{
		prefix = prefix.toLowerCase(Locale.ROOT);
		if(prefix.startsWith("["))
		{
			ConfigToken cToken = getByPrefix(prefix.substring(1));
			TOKEN_REGISTRY_BY_PREFIX.put("[" + cToken.getPrefix(), new ConfigToken(prefix, ConfigArray.class, (onChanged, token, name) -> new ConfigArray(onChanged, token, cToken, name)));
		}
		return TOKEN_REGISTRY_BY_PREFIX.get(prefix);
	}
	
	public static ConfigFile create(File file, boolean load)
	{
		var cfg = new ConfigFile(file);
		if(load) cfg.load();
		return cfg;
	}
	
	public static ConfigFile createInMemory(IByteBuf buf)
	{
		return new ConfigFile(buf);
	}
}