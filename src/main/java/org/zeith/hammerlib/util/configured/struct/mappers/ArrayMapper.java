package org.zeith.hammerlib.util.configured.struct.mappers;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.io.UnsafeHax;
import org.zeith.hammerlib.util.configured.struct.reflection.IField;
import org.zeith.hammerlib.util.configured.struct.reflection.TriConsumer;
import org.zeith.hammerlib.util.configured.types.ConfigArray;
import org.zeith.hammerlib.util.configured.types.ConfigElement;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;

public class ArrayMapper<A extends ConfigElement<A>, O>
		implements ITokenMapper<ConfigArray<A>, O[]>
{
	protected final Class<O> comType;
	protected final Class<O[]> type;
	protected final ConfigToken<ConfigArray<A>> token;
	protected final Function<A, O> converter;
	protected final TriConsumer<A, IField<?>, O> setDefault;
	
	public ArrayMapper(Class<O> comType, Class<O[]> type, ConfigToken<ConfigArray<A>> token, Function<A, O> converter, TriConsumer<A, IField<?>, O> setDefault)
	{
		this.comType = comType;
		this.type = type;
		this.token = token;
		this.converter = converter;
		this.setDefault = setDefault;
	}
	
	@Override
	public Class<O[]> getType()
	{
		return type;
	}
	
	@Override
	public ConfigToken<ConfigArray<A>> getToken()
	{
		return token;
	}
	
	@Override
	public O[] apply(ConfigArray<A> element)
	{
		return element.getElements()
				.stream()
				.map(converter)
				.toArray(length -> UnsafeHax.cast(Array.newInstance(comType, length)));
	}
	
	@Override
	public void defaultValue(ConfigArray<A> element, IField<?> ownerField, O[] def)
	{
		List<A> eSize = element.getElements();
		
		if(!element.hasRead() && def != null)
		{
			for(O o : def)
				setDefault.accept(element.createElement(), ownerField, o);
			return;
		}
		
		int size = Math.max(eSize.size(), def != null ? def.length : 0);
		
		for(int i = 0; i < size; i++)
		{
			A elem = eSize.get(i);
			setDefault.accept(elem, ownerField, def != null && i < def.length ? def[i] : null);
		}
	}
}