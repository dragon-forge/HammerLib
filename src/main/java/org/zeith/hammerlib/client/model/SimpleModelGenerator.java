package org.zeith.hammerlib.client.model;

import com.google.gson.*;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Supplier;

public class SimpleModelGenerator<T extends IUnbakedGeometry<T>>
		implements IGeometryLoader<T>
{
	private final Supplier<T> factory;
	
	public SimpleModelGenerator(Supplier<T> factory)
	{
		this.factory = factory;
	}
	
	@Override
	public T read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException
	{
		return factory.get();
	}
}