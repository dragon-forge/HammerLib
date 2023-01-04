package org.zeith.hammerlib.util.configured.struct.mappers;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.data.IntValueRange;
import org.zeith.hammerlib.util.configured.struct.RangeInt;
import org.zeith.hammerlib.util.configured.struct.RangeLong;
import org.zeith.hammerlib.util.configured.struct.reflection.IField;
import org.zeith.hammerlib.util.configured.types.ConfigInteger;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

public class IntegerMapper<N extends Number>
		implements ITokenMapper<ConfigInteger, N>
{
	protected final Class<N> type;
	protected final Function<BigInteger, N> converter;
	
	public IntegerMapper(Class<N> type, Function<BigInteger, N> converter)
	{
		this.type = type;
		this.converter = converter;
	}
	
	@Override
	public Class<N> getType()
	{
		return type;
	}
	
	@Override
	public ConfigToken<ConfigInteger> getToken()
	{
		return ConfiguredLib.INT;
	}
	
	@Override
	public N apply(ConfigInteger element)
	{
		return Optional.ofNullable(element.getValue()).map(converter).orElse(null);
	}
	
	@Override
	public void defaultValue(ConfigInteger element, IField<?> ownerField, N defaultValue)
	{
		Optional.ofNullable(ownerField)
				.map(f -> f.annotation(RangeInt.class).map(IntValueRange::fromIntRange)
						.orElseGet(() -> f.annotation(RangeLong.class).map(IntValueRange::fromLongRange).orElse(null))
				).ifPresent(element::withRange);
		
		if(defaultValue != null)
		{
			if(defaultValue instanceof BigInteger) element.withDefault((BigInteger) defaultValue);
			else element.withDefault(BigInteger.valueOf(defaultValue.longValue()));
		}
	}
}