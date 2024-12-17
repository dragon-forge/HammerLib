package org.zeith.hammerlib.api.io;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeith.hammerlib.api.io.serializers.*;
import org.zeith.hammerlib.api.io.serializers.codec.CodecSerializer;
import org.zeith.hammerlib.api.io.serializers.codec.ICodecSerializer;
import org.zeith.hammerlib.core.scans.base.IAnnotationScanListener;
import org.zeith.hammerlib.core.scans.base.IScanListener;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NBTSerializationHelper
{
	public static final Logger LOG = LoggerFactory.getLogger("HammerLib");
	
	private static final BiMap<Class<?>, INBTSerializer<?>> SERIALIZER_MAP = HashBiMap.create();
	private static final BiMap<Class<? extends Enum<?>>, INBTSerializer<?>> ENUM_SERIALIZER_MAP = HashBiMap.create();
	private static final BiMap<Class<? extends Record>, INBTSerializer<?>> RECORD_SERIALIZER_MAP = HashBiMap.create();
	
	public static <T> void registerCodecSerializer(ICodecSerializer<T> serializer)
	{
		registerSerializer(serializer.type(), serializer.asSerializer());
	}
	
	public static <T> void registerSerializer(Class<T> type, INBTSerializer<T> serializer)
	{
		SERIALIZER_MAP.putIfAbsent(type, serializer);
	}
	
	static
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
	}
	
	public static IScanListener create()
	{
		return IAnnotationScanListener.forAnnotation(NBTSerializer.class, ElementType.TYPE, data ->
				data.getProperty("value").map(List.class::cast).ifPresentOrElse(ts ->
						{
							try
							{
								List<Type> types = Cast.cast(ts);
								var ctor = data.getOwnerClass().getDeclaredConstructor();
								ctor.setAccessible(true);
								INBTSerializer<?> ser = Cast.cast(ctor.newInstance());
								for(Type type : types)
								{
									Class<?> c = ReflectionUtil.fetchClassAny(type);
									if(c != null)
									{
										SERIALIZER_MAP.putIfAbsent(c, ser);
										LOG.debug("Registered NBT serializer for type {}: {}", c, ser);
									} else
										LOG.error("Unable to find class {}!", type.getInternalName());
								}
							} catch(ReflectiveOperationException roe)
							{
								LOG.error("Failed to create an instance of {}", data.getOwnerClass().getName(), roe);
							}
						}, () -> LOG.error("Completely ignored broken @NBTSerializer annotation with data {}", data.parent.annotationData())
				)
		).then(IAnnotationScanListener.forAnnotation(CodecSerializer.class, ElementType.FIELD, data ->
				{
					Field field = ReflectionUtil.lookupField(data.getOwnerClass(), data.getMemberName());
					if(field == null)
					{
						LOG.error("Completely ignored invalid @CodecSerializer at {} {}. (field not found)", data.clazz(), data.getMemberName());
						return;
					}
					
					if(!Modifier.isStatic(field.getModifiers()))
					{
						LOG.error("Completely ignored invalid @CodecSerializer at {} {}. (field is not static)", data.clazz(), data.getMemberName());
						return;
					}
					
					ICodecSerializer<?> codecSer = ReflectionUtil.fetchValue(field, null, ICodecSerializer.class).orElse(null);
					if(codecSer == null)
					{
						LOG.error("@CodecSerializer field at {} {} is not ICodecSerializer. (unable to obtain value that implements ICodecSerializer)", data.clazz(), data.getMemberName());
						return;
					}
					
					var c = codecSer.type();
					var ser = codecSer.asSerializer();
					registerCodecSerializer(codecSer);
					LOG.debug("Registered codec NBT serializer for type {}: {}", c, ser);
				}
		));
	}
	
	public static <T> INBTSerializer<T> getSerializer(Class<T> type)
	{
		if(type.isRecord())
			return Cast.cast(RECORD_SERIALIZER_MAP.computeIfAbsent(Cast.cast(type), RecordNBTSerializer::new));
		if(type.isEnum())
			return Cast.cast(ENUM_SERIALIZER_MAP.computeIfAbsent(Cast.cast(type), EnumNBTSerializer::create));
		return Cast.cast(SERIALIZER_MAP.get(type));
	}
	
	public static void serializeField(HolderLookup.Provider provider, Class<?> type, Object instance, CompoundTag nbt, String key)
	{
		if(instance == null) return;
		
		INBTSerializer<?> serializer = getSerializer(type);
		
		if(serializer != null)
		{
			serializer.serialize(provider, key, Cast.cast(instance), nbt);
		} else
		{
			if(type.isArray())
			{
				CompoundTag lst = new CompoundTag();
				Class<?> compType = type.getComponentType();
				int length = Array.getLength(instance);
				for(int i = 0; i < length; ++i)
				{
					Object component = Array.get(instance, i);
					serializeField(provider, compType, component, lst, Integer.toString(i));
				}
				nbt.put(key, lst);
			} else
				LOG.warn("Don't know how to serialize {} {}", type, key);
		}
	}
	
	public static Object deserializeField(HolderLookup.Provider provider, Class<?> type, CompoundTag nbt, String key)
	{
		INBTSerializer<?> serializer = getSerializer(type);
		
		if(serializer != null)
		{
			return serializer.deserialize(provider, key, nbt);
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
						Array.set(instance, i, deserializeField(provider, compType, lst, Integer.toString(i)));
					
					return instance;
				}
				
				return null;
			} else
				LOG.warn("Don't know how to deserialize {} {}", type, key);
		}
		
		return null;
	}
	
	public static CompoundTag serialize(HolderLookup.Provider provider, Object instance)
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
							nbt.put(name, s.serializeNBT(provider));
						} else
						{
							if(!INBTSerializer.class.isAssignableFrom(field.getType()))
								LOG.warn("Don't know how to serialize {} in {}", field, type);
						}
					} else
					{
						serializeField(provider, field.getType(), field.get(instance), nbt, name);
					}
				} catch(ReflectiveOperationException e)
				{
					LOG.error("Failed to serialize field {} in {}", field, type, e);
				}
			}
		}
		
		return nbt;
	}
	
	public static void deserialize(HolderLookup.Provider provider, Object instance, CompoundTag nbt)
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
							if(tag != null) s.deserializeNBT(provider, tag);
						} else
						{
							if(INBTSerializer.class.isAssignableFrom(field.getType()))
							{
								if(nbt.contains(name))
								{
									LOG.warn("Can't deserialize {} in {} since the final value is null. Trying to deserialize tag: {}", field, type, nbt.get(name));
								}
							} else
								LOG.warn("Don't know how to deserialize {} in {}", field, type);
						}
					} else
					{
						Object val = deserializeField(provider, field.getType(), nbt, name);
						if(val != null || !field.getType().isPrimitive()) field.set(instance, val);
					}
				} catch(Throwable e)
				{
					LOG.error("Failed to deserialize field {} in {}", field, type, e);
				}
			}
		}
	}
}