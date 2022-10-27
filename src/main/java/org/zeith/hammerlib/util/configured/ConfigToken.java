package org.zeith.hammerlib.util.configured;

import org.zeith.hammerlib.util.configured.io.IElementFactory;
import org.zeith.hammerlib.util.configured.types.ConfigArray;
import org.zeith.hammerlib.util.configured.types.ConfigElement;

import java.util.Locale;
import java.util.Objects;

public class ConfigToken<E extends ConfigElement<E>>
{
	private final String prefix;
	private final Class<E> type;
	private final IElementFactory<E> factory;
	
	public ConfigToken(String prefix, Class<E> type, IElementFactory<E> factory)
	{
		this.prefix = prefix;
		this.type = type;
		this.factory = factory;
		ConfiguredLib.TOKEN_REGISTRY_BY_PREFIX.put(prefix.toLowerCase(Locale.ROOT), this);
	}
	
	public boolean is(ConfigElement<?> elem)
	{
		return type.isInstance(elem);
	}
	
	public String getPrefix()
	{
		return prefix;
	}
	
	public Class<E> getType()
	{
		return type;
	}
	
	public E create(Runnable onChange, String name)
	{
		return factory.create(onChange, this, name);
	}
	
	@SuppressWarnings("unchecked")
	public ConfigToken<ConfigArray<E>> arrayOf()
	{
		Class<ConfigArray<E>> arrayType = (Class<ConfigArray<E>>) (Object) ConfigArray.class;
		return new ConfigToken<>("[" + getPrefix(), arrayType, (onChanged, token, name) -> new ConfigArray<E>(onChanged, token, this, name));
	}
	
	@Override
	public String toString()
	{
		return "ConfigToken{" +
				"prefix='" + prefix + '\'' +
				", type=" + type +
				'}';
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ConfigToken<?> that = (ConfigToken<?>) o;
		return prefix.equals(that.prefix) && type.equals(that.type);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(prefix, type);
	}
}