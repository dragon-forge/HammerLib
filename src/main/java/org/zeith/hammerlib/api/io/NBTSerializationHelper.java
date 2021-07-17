package org.zeith.hammerlib.api.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.api.io.serializers.BooleanSerializer;
import org.zeith.hammerlib.api.io.serializers.EnumNBTSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;
import org.zeith.hammerlib.api.io.serializers.NumberSerializer;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NBTSerializationHelper
{
	public static final Logger LOG = LogManager.getLogger("HammerLib");

	private static final BiMap<Class<?>, INBTSerializer<?>> SERIALIZER_MAP = HashBiMap.create();
	private static final BiMap<Class<?>, INBTSerializer<?>> ENUM_SERIALIZER_MAP = HashBiMap.create();

	public static <T extends Enum<T>> INBTSerializer<T> forEnum(Class<T> type)
	{
		return Cast.cast(ENUM_SERIALIZER_MAP.computeIfAbsent(type, t -> new EnumNBTSerializer<>(type)));
	}

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
						LOG.debug("Registered NBT serializer for type " + c + ": " + ser);
					} else
						LOG.error("Unable to find class " + type.getInternalName() + "!");
				}
			});
		});
	}

	public static void serializeField(Class<?> type, Object instance, CompoundNBT nbt, String key)
	{
		INBTSerializer<?> serializer;
		if(type.isEnum()) serializer = forEnum(Cast.cast(type));
		else serializer = SERIALIZER_MAP.get(type);

		if(serializer != null)
		{
			serializer.serialize(nbt, key, Cast.cast(instance));
		} else
		{
			if(type.isArray())
			{
				if(instance != null)
				{
					CompoundNBT lst = new CompoundNBT();
					Class<?> compType = type.getComponentType();
					int length = Array.getLength(instance);
					for(int i = 0; i < length; ++i)
					{
						Object component = Array.get(instance, i);
						serializeField(compType, component, lst, Integer.toString(i));
					}
					nbt.put(key, lst);
				}
			} else
				LOG.warn("Don't know how to serialize " + type + " " + key + " in " + type);
		}
	}

	public static Object deserializeField(Class<?> type, CompoundNBT nbt, String key)
	{
		INBTSerializer<?> serializer;
		if(type.isEnum()) serializer = forEnum(Cast.cast(type));
		else serializer = SERIALIZER_MAP.get(type);

		if(serializer != null)
		{
			return serializer.deserialize(nbt, key);
		} else
		{
			if(type.isArray())
			{
				if(nbt.contains(key, Constants.NBT.TAG_COMPOUND))
				{
					CompoundNBT lst = nbt.getCompound(key);

					Class<?> compType = type.getComponentType();

					int length = lst.size();
					Object instance = Array.newInstance(compType, length);

					for(int i = 0; i < length; ++i)
						Array.set(instance, i, deserializeField(compType, lst, Integer.toString(i)));

					return instance;
				}

				return null;
			} else
				LOG.warn("Don't know how to deserialize " + type + " " + key + " in " + type);
		}

		return null;
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
							LOG.warn("Don't know how to serialize " + field + " in " + type);
						}
					} else
					{
						serializeField(field.getType(), field.get(instance), nbt, field.getName());
					}
				} catch(ReflectiveOperationException e)
				{
					LOG.error("Failed to serialize field " + field + " in " + type);
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
							LOG.warn("Don't know how to deserialize " + field + " in " + type);
						}
					} else
					{
						Object val = deserializeField(field.getType(), nbt, field.getName());
						if(val != null || !type.isPrimitive()) field.set(instance, val);
					}
				} catch(Throwable e)
				{
					LOG.error("Failed to deserialize field " + field + " in " + type);
				}
			}
		}
	}
}