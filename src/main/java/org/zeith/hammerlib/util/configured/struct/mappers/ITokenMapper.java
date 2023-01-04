package org.zeith.hammerlib.util.configured.struct.mappers;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.struct.reflection.IField;
import org.zeith.hammerlib.util.configured.types.ConfigElement;
import org.zeith.hammerlib.util.java.Cast;

public interface ITokenMapper<T extends ConfigElement<T>, D>
{
	Class<D> getType();
	
	ConfigToken<T> getToken();
	
	D apply(T element);
	
	void defaultValue(T element, IField<?> ownerField, D defaultValue);
	
	@SuppressWarnings("unchecked")
	default ArrayMapper<T, D> arrayOf()
	{
		try
		{
			Class<D[]> arrayType = Cast.cast(getType().getClassLoader().loadClass("[L" + getType().getName() + ";"));
			return new ArrayMapper<>(getType(), arrayType, getToken().arrayOf(), this::apply, this::defaultValue);
		} catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
}