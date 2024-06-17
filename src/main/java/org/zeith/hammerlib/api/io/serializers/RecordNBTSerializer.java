package org.zeith.hammerlib.api.io.serializers;

import lombok.SneakyThrows;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializationHelper;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.function.Function;

public class RecordNBTSerializer<T extends Record>
		implements INBTSerializer<T>
{
	protected final Class<T> record;
	protected final RecordComponent[] components;
	protected final String[] componentNames;
	protected final Function<T, Object>[] suppliers;
	protected final Function<Object[], T> factory;
	
	@SneakyThrows
	public RecordNBTSerializer(Class<T> record)
	{
		if(!record.isRecord()) throw new IllegalArgumentException(record + " must be a record.");
		this.record = record;
		this.components = record.getRecordComponents();
		this.suppliers = new Function[components.length];
		this.componentNames = new String[components.length];
		for(int i = 0; i < components.length; i++)
		{
			var m = record.getDeclaredMethod(components[i].getName());
			suppliers[i] = inst -> getField(inst, m);
			var n = components[i].getDeclaredAnnotation(NBTSerializable.class);
			this.componentNames[i] = n != null ? n.value() : components[i].getName();
		}
		var ctor = record.getDeclaredConstructor(Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new));
		factory = args -> newInstance(args, ctor);
	}
	
	@SneakyThrows
	private T newInstance(Object[] args, Constructor<T> ctor)
	{
		return ctor.newInstance(args);
	}
	
	@SneakyThrows
	private Object getField(T inst, Method m)
	{
		return m.invoke(inst);
	}
	
	@Override
	public void serialize(CompoundTag nbt, String key, @NotNull T value)
	{
		var inner = new CompoundTag();
		for(int i = 0; i < components.length; i++)
		{
			var component = components[i];
			NBTSerializationHelper.serializeField(component.getType(), suppliers[i].apply(value), inner, componentNames[i]);
		}
		nbt.put(key, inner);
	}
	
	@Override
	public @Nullable T deserialize(CompoundTag nbt, String key)
	{
		var inner = nbt.getCompound(key);
		Object[] coms = new Object[components.length];
		for(int i = 0; i < coms.length; i++)
		{
			var com = components[i];
			coms[i] = NBTSerializationHelper.deserializeField(com.getType(), inner, componentNames[i]);
		}
		return factory.apply(coms);
	}
}