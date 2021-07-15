package org.zeith.hammerlib.api.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.io.serializers.BooleanSerializer;
import org.zeith.hammerlib.api.io.serializers.EnumNBTSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;
import org.zeith.hammerlib.api.io.serializers.NumberSerializer;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NBTSerializationHelper
{
	private static final BiMap<Class<?>, INBTSerializer<?>> SERIALIZER_MAP = HashBiMap.create();

	public static <T> void registerSerializer(Class<T> type, INBTSerializer<T> serializer)
	{
		SERIALIZER_MAP.putIfAbsent(type, serializer);
	}

	public static void construct()
	{
		registerSerializer(Boolean.class, new BooleanSerializer<>());
		registerSerializer(Byte.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE, ByteNBT::valueOf, ByteNBT::getAsByte));
		registerSerializer(Short.class, new NumberSerializer<>(Constants.NBT.TAG_SHORT, ShortNBT::valueOf, ShortNBT::getAsShort));
		registerSerializer(Float.class, new NumberSerializer<>(Constants.NBT.TAG_FLOAT, FloatNBT::valueOf, FloatNBT::getAsFloat));
		registerSerializer(Double.class, new NumberSerializer<>(Constants.NBT.TAG_DOUBLE, DoubleNBT::valueOf, DoubleNBT::getAsDouble));
		registerSerializer(Integer.class, new NumberSerializer<>(Constants.NBT.TAG_INT, IntNBT::valueOf, IntNBT::getAsInt));
		registerSerializer(Long.class, new NumberSerializer<>(Constants.NBT.TAG_LONG, LongNBT::valueOf, LongNBT::getAsLong));

		registerSerializer(boolean.class, new BooleanSerializer<>());
		registerSerializer(byte.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE, ByteNBT::valueOf, ByteNBT::getAsByte));
		registerSerializer(short.class, new NumberSerializer<>(Constants.NBT.TAG_SHORT, ShortNBT::valueOf, ShortNBT::getAsShort));
		registerSerializer(float.class, new NumberSerializer<>(Constants.NBT.TAG_FLOAT, FloatNBT::valueOf, FloatNBT::getAsFloat));
		registerSerializer(double.class, new NumberSerializer<>(Constants.NBT.TAG_DOUBLE, DoubleNBT::valueOf, DoubleNBT::getAsDouble));
		registerSerializer(int.class, new NumberSerializer<>(Constants.NBT.TAG_INT, IntNBT::valueOf, IntNBT::getAsInt));
		registerSerializer(long.class, new NumberSerializer<>(Constants.NBT.TAG_LONG, LongNBT::valueOf, LongNBT::getAsLong));

		registerSerializer(BigInteger.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE_ARRAY, b -> new ByteArrayNBT(b.toByteArray()), (ByteArrayNBT nbt) -> new BigInteger(nbt.getAsByteArray())));
		registerSerializer(BigDecimal.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE_ARRAY, b -> new ByteArrayNBT(b.toString().getBytes(StandardCharsets.UTF_8)), (ByteArrayNBT nbt) -> new BigDecimal(new String(nbt.getAsByteArray(), StandardCharsets.UTF_8))));

		ScanDataHelper.lookupAnnotatedObjects(NBTSerializer.class).forEach(data ->
		{
			data.getProperty("value").map(List.class::cast).ifPresent(ts ->
			{
				List<Type> types = ts;
				INBTSerializer ser = Cast.cast(UnsafeHacks.newInstance(data.getOwnerClass()));
				for(Type type : types)
				{
					Class<?> c = ReflectionUtil.fetchClassAny(type);
					if(c != null)
					{
						SERIALIZER_MAP.putIfAbsent(c, ser);
						HammerLib.LOG.debug("Registered NBT serializer for type " + c + ": " + ser);
					} else
						HammerLib.LOG.error("Unable to find class " + type.getInternalName() + "!");
				}
			});
		});
	}

	public static CompoundNBT serialize(Object instance)
	{
		Class<?> type = instance.getClass();
		CompoundNBT nbt = new CompoundNBT();

		for(Field field : ReflectionUtil.getFieldsUpTo(type, null))
		{
			field.setAccessible(true);
			NBTSerializable nbts = field.getAnnotation(NBTSerializable.class);

			if(nbts != null)
			{
				String name = nbts.value();
				if(name.trim().isEmpty()) name = field.getName();

				try
				{
					if(Modifier.isFinal(field.getModifiers()))
					{
						if(INBTSerializable.class.isAssignableFrom(field.getType()))
						{
							INBTSerializable s = (INBTSerializable) field.get(instance);
							if(s != null) nbt.put(name, s.serializeNBT());
						} else
						{
							HammerLib.LOG.warn("Don't know how to serialize " + field + " in " + type);
						}
					} else
					{
						INBTSerializer<?> serializer;
						if(field.getType().isEnum())
							serializer = new EnumNBTSerializer<>(Cast.cast(field.getType()));
						else
							serializer = SERIALIZER_MAP.get(field.getType());

						if(serializer != null)
						{
							serializer.serialize(nbt, name, Cast.cast(field.get(instance)));
						} else
						{
							HammerLib.LOG.warn("Don't know how to serialize " + field + " in " + type);
						}
					}
				} catch(ReflectiveOperationException e)
				{
					HammerLib.LOG.error("Failed to serialize field " + field + " in " + type);
				}
			}
		}

		return nbt;
	}

	public static void deserialize(Object instance, CompoundNBT nbt)
	{
		Class<?> type = instance.getClass();
		for(Field field : ReflectionUtil.getFieldsUpTo(type, null))
		{
			field.setAccessible(true);
			NBTSerializable nbts = field.getAnnotation(NBTSerializable.class);

			if(nbts != null)
			{
				String name = nbts.value();
				if(name.trim().isEmpty()) name = field.getName();

				try
				{
					if(Modifier.isFinal(field.getModifiers()))
					{
						if(INBTSerializable.class.isAssignableFrom(field.getType()))
						{
							INBTSerializable s = (INBTSerializable) field.get(instance);
							if(s != null)
								s.deserializeNBT(nbt.get(name));
						} else
						{
							HammerLib.LOG.warn("Don't know how to deserialize " + field + " in " + type);
						}
					} else
					{
						INBTSerializer<?> serializer;
						if(field.getType().isEnum())
							serializer = new EnumNBTSerializer<>(Cast.cast(field.getType()));
						else serializer = SERIALIZER_MAP.get(field.getType());

						if(serializer != null)
						{
							Object val = serializer.deserialize(nbt, name);
							if(val != null || !field.getType().isPrimitive()) field.set(instance, val);
						} else
						{
							HammerLib.LOG.warn("Don't know how to deserialize " + field + " in " + type);
						}
					}
				} catch(Throwable e)
				{
					HammerLib.LOG.error("Failed to deserialize field " + field + " in " + type);
				}
			}
		}
	}
}