package org.zeith.hammerlib.api.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.io.serializers.*;
import org.zeith.hammerlib.util.java.Cast;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class NBTSerializationHelper
{
	private static final BiMap<Class<?>, INBTSerializer<?>> SERIALIZER_MAP = HashBiMap.create();
	private static final BiMap<INBTSerializer<?>, Class<?>> SERIALIZER_MAP_INVERSE = SERIALIZER_MAP.inverse();

	public static <T> void registerSerializer(Class<T> type, INBTSerializer<T> serializer)
	{
		SERIALIZER_MAP.put(type, serializer);
	}

	static
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
		registerSerializer(String.class, new StringSerializer());
		registerSerializer(byte[].class, new ByteArraySerializer());
		registerSerializer(int[].class, new IntArraySerializer());
		registerSerializer(long[].class, new LongArraySerializer());
		registerSerializer(BlockPos.class, new BlockPosSerializer());
		registerSerializer(Vector3d.class, new Vec3dSerializer());
		registerSerializer(ResourceLocation.class, new ResourceLocationSerializer());
	}

	public static CompoundNBT serialize(Object instance)
	{
		Class<?> type = instance.getClass();
		CompoundNBT nbt = new CompoundNBT();

		for(Field field : type.getDeclaredFields())
		{
			field.setAccessible(true);
			NBTSerializable nbts = field.getAnnotation(NBTSerializable.class);

			if(nbts != null)
			{
				try
				{
					if(Modifier.isFinal(field.getModifiers()))
					{
						if(INBTSerializable.class.isAssignableFrom(field.getType()))
						{
							INBTSerializable s = (INBTSerializable) field.get(instance);
							if(s != null)
								nbt.put(field.getName(), s.serializeNBT());
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
							serializer.serialize(nbt, field.getName(), Cast.cast(field.get(instance)));
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
		for(Field field : type.getDeclaredFields())
		{
			field.setAccessible(true);
			NBTSerializable nbts = field.getAnnotation(NBTSerializable.class);

			if(nbts != null)
			{
				try
				{
					if(Modifier.isFinal(field.getModifiers()))
					{
						if(INBTSerializable.class.isAssignableFrom(field.getType()))
						{
							INBTSerializable s = (INBTSerializable) field.get(instance);
							if(s != null)
								s.deserializeNBT(nbt.get(field.getName()));
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
							Object val = serializer.deserialize(nbt, field.getName());
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