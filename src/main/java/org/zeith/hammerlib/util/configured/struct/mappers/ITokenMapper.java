package org.zeith.hammerlib.util.configured.struct.mappers;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.struct.reflection.IField;
import org.zeith.hammerlib.util.configured.types.ConfigElement;

public interface ITokenMapper<T extends ConfigElement<T>, D>
{
	Class<D> getType();
	
	ConfigToken<T> getToken();
	
	D apply(T element);
	
	void defaultValue(T element, IField<?> ownerField, D defaultValue);
	
	@SuppressWarnings("unchecked")
	default ArrayMapper<T, D> arrayOf()
	{
		return new ArrayMapper<>(getType(), (Class<D[]>) getType().arrayType(), getToken().arrayOf(), this::apply, this::defaultValue);
	}
}