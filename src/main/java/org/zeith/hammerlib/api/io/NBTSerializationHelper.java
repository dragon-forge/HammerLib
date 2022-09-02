package org.zeith.hammerlib.api.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.api.io.serializers.*;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.reflect.*;
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
		registerSerializer(Byte.class, new NumberSerializer<>(Tag.TAG_BYTE, ByteTag::valueOf, ByteTag::getAsByte));
		registerSerializer(Short.class, new NumberSerializer<>(Tag.TAG_SHORT, ShortTag::valueOf, ShortTag::getAsShort));
		registerSerializer(Float.class, new NumberSerializer<>(Tag.TAG_FLOAT, FloatTag::valueOf, FloatTag::getAsFloat));
		registerSerializer(Double.class, new NumberSerializer<>(Tag.TAG_DOUBLE, DoubleTag::valueOf, DoubleTag::getAsDouble));
		registerSerializer(Integer.class, new NumberSerializer<>(Tag.TAG_INT, IntTag::valueOf, IntTag::getAsInt));
		registerSerializer(Long.class, new NumberSerializer<>(Tag.TAG_LONG, LongTag::valueOf, LongTag::getAsLong));
		
		registerSerializer(boolean.class, new BooleanSerializer<>());
		registerSerializer(byte.class, new NumberSerializer<>(Tag.TAG_BYTE, ByteTag::valueOf, ByteTag::getAsByte));
		registerSerializer(short.class, new NumberSerializer<>(Tag.TAG_SHORT, ShortTag::valueOf, ShortTag::getAsShort));
		registerSerializer(float.class, new NumberSerializer<>(Tag.TAG_FLOAT, FloatTag::valueOf, FloatTag::getAsFloat));
		registerSerializer(double.class, new NumberSerializer<>(Tag.TAG_DOUBLE, DoubleTag::valueOf, DoubleTag::getAsDouble));
		registerSerializer(int.class, new NumberSerializer<>(Tag.TAG_INT, IntTag::valueOf, IntTag::getAsInt));
		registerSerializer(long.class, new NumberSerializer<>(Tag.TAG_LONG, LongTag::valueOf, LongTag::getAsLong));
		
		registerSerializer(BigInteger.class, new NumberSerializer<>(Tag.TAG_BYTE_ARRAY, b -> new ByteArrayTag(b.toByteArray()), (ByteArrayTag nbt) -> new BigInteger(nbt.getAsByteArray())));
		registerSerializer(BigDecimal.class, new NumberSerializer<>(Tag.TAG_BYTE_ARRAY, b -> new ByteArrayTag(b.toString().getBytes(StandardCharsets.UTF_8)), (ByteArrayTag nbt) -> new BigDecimal(new String(nbt.getAsByteArray(), StandardCharsets.UTF_8))));
		
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
	
	public static void serializeField(Class<?> type, Object instance, CompoundTag nbt, String key)
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
					CompoundTag lst = new CompoundTag();
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
	
	public static Object deserializeField(Class<?> type, CompoundTag nbt, String key)
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
				if(nbt.contains(key, Tag.TAG_COMPOUND))
				{
					CompoundTag lst = nbt.getCompound(key);
					
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
	
	public static CompoundTag serialize(Object instance)
	{
		Class<?> type = instance.getClass();
		CompoundTag nbt = new CompoundTag();
		
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
						if(field.get(instance) instanceof INBTSerializable<?> s)
						{
							nbt.put(name, s.serializeNBT());
						} else
						{
							if(!INBTSerializer.class.isAssignableFrom(field.getType()))
								LOG.warn("Don't know how to serialize " + field + " in " + type);
						}
					} else
					{
						serializeField(field.getType(), field.get(instance), nbt, name);
					}
				} catch(ReflectiveOperationException e)
				{
					LOG.error("Failed to serialize field " + field + " in " + type, e);
				}
			}
		}
		
		return nbt;
	}
	
	public static void deserialize(Object instance, CompoundTag nbt)
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
						if(field.get(instance) instanceof INBTSerializable s)
						{
							Tag tag = nbt.get(name);
							if(tag != null) s.deserializeNBT(tag);
						} else
						{
							if(INBTSerializer.class.isAssignableFrom(field.getType()))
							{
								if(nbt.contains(name))
								{
									LOG.warn("Can't deserialize " + field + " in " + type + " since the final value is null. Trying to deserialize tag: " + nbt.get(name));
								}
							} else
								LOG.warn("Don't know how to deserialize " + field + " in " + type);
						}
					} else
					{
						Object val = deserializeField(field.getType(), nbt, name);
						if(val != null || !field.getType().isPrimitive()) field.set(instance, val);
					}
				} catch(Throwable e)
				{
					LOG.error("Failed to deserialize field " + field + " in " + type, e);
				}
			}
		}
	}
}