package com.zeitheron.hammercore.api.io;

import com.google.common.collect.*;
import com.zeitheron.hammercore.api.io.serializers.*;
import com.zeitheron.hammercore.utils.ReflectionUtil;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.apache.logging.log4j.*;
import org.objectweb.asm.Type;

import java.lang.reflect.*;
import java.math.*;
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
	
	public static void construct(ASMDataTable table)
	{
		registerSerializer(Boolean.class, new BooleanSerializer<>());
		
		registerSerializer(Byte.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE, NBTTagByte::new, NBTTagByte::getByte));
		registerSerializer(Short.class, new NumberSerializer<>(Constants.NBT.TAG_SHORT, NBTTagShort::new, NBTTagShort::getShort));
		registerSerializer(Float.class, new NumberSerializer<>(Constants.NBT.TAG_FLOAT, NBTTagFloat::new, NBTTagFloat::getFloat));
		registerSerializer(Double.class, new NumberSerializer<>(Constants.NBT.TAG_DOUBLE, NBTTagDouble::new, NBTTagDouble::getDouble));
		registerSerializer(Integer.class, new NumberSerializer<>(Constants.NBT.TAG_INT, NBTTagInt::new, NBTTagInt::getInt));
		registerSerializer(Long.class, new NumberSerializer<>(Constants.NBT.TAG_LONG, NBTTagLong::new, NBTTagLong::getLong));
		
		registerSerializer(boolean.class, new BooleanSerializer<>());
		registerSerializer(byte.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE, NBTTagByte::new, NBTTagByte::getByte));
		registerSerializer(short.class, new NumberSerializer<>(Constants.NBT.TAG_SHORT, NBTTagShort::new, NBTTagShort::getShort));
		registerSerializer(float.class, new NumberSerializer<>(Constants.NBT.TAG_FLOAT, NBTTagFloat::new, NBTTagFloat::getFloat));
		registerSerializer(double.class, new NumberSerializer<>(Constants.NBT.TAG_DOUBLE, NBTTagDouble::new, NBTTagDouble::getDouble));
		registerSerializer(int.class, new NumberSerializer<>(Constants.NBT.TAG_INT, NBTTagInt::new, NBTTagInt::getInt));
		registerSerializer(long.class, new NumberSerializer<>(Constants.NBT.TAG_LONG, NBTTagLong::new, NBTTagLong::getLong));
		
		registerSerializer(BigInteger.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE_ARRAY, b -> new NBTTagByteArray(b.toByteArray()), (NBTTagByteArray nbt) -> new BigInteger(nbt.getByteArray())));
		registerSerializer(BigDecimal.class, new NumberSerializer<>(Constants.NBT.TAG_BYTE_ARRAY, b -> new NBTTagByteArray(b.toString()
																															.getBytes(StandardCharsets.UTF_8)), (NBTTagByteArray nbt) -> new BigDecimal(new String(nbt.getByteArray(), StandardCharsets.UTF_8))));
		
		for(ASMDataTable.ASMData dat : table.getAll(NBTSerializer.class.getCanonicalName()))
		{
			try
			{
				List<Type> types = Cast.cast(dat.getAnnotationInfo().get("value"));
				Constructor<?> ctor = Class.forName(dat.getClassName()).getDeclaredConstructor();
				ctor.setAccessible(true);
				INBTSerializer ser = Cast.cast(ctor.newInstance());
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
			} catch(ReflectiveOperationException roe)
			{
				LOG.error("Failed to create an instance of " + dat.getClassName(), roe);
			}
		}
	}
	
	public static void serializeField(Class<?> type, Object instance, NBTTagCompound nbt, String key)
	{
		if(instance == null) return;
		
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
				NBTTagCompound lst = new NBTTagCompound();
				Class<?> compType = type.getComponentType();
				int length = Array.getLength(instance);
				for(int i = 0; i < length; ++i)
				{
					Object component = Array.get(instance, i);
					serializeField(compType, component, lst, Integer.toString(i));
				}
				nbt.setTag(key, lst);
			} else
				LOG.warn("Don't know how to serialize " + type + " " + key + " in " + type);
		}
	}
	
	public static Object deserializeField(Class<?> type, NBTTagCompound nbt, String key)
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
				if(nbt.hasKey(key, Constants.NBT.TAG_COMPOUND))
				{
					NBTTagCompound lst = nbt.getCompoundTag(key);
					
					Class<?> compType = type.getComponentType();
					
					int length = lst.getKeySet().size();
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
	
	public static NBTTagCompound serialize(Object instance)
	{
		Class<?> type = instance.getClass();
		NBTTagCompound nbt = new NBTTagCompound();
		
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
						Object inst = field.get(instance);
						if(inst instanceof INBTSerializable<?>)
						{
							INBTSerializable<?> s = (INBTSerializable<?>) inst;
							nbt.setTag(name, s.serializeNBT());
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
	
	public static void deserialize(Object instance, NBTTagCompound nbt)
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
						Object inst = field.get(instance);
						if(inst instanceof INBTSerializable<?>)
						{
							NBTBase tag = nbt.getTag(name);
							if(tag != null) ((INBTSerializable) inst).deserializeNBT(tag);
						} else
						{
							if(INBTSerializer.class.isAssignableFrom(field.getType()))
							{
								if(nbt.hasKey(name))
								{
									LOG.warn("Can't deserialize " + field + " in " + type +
											" since the final value is null. Trying to deserialize tag: " +
											nbt.getTag(name));
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