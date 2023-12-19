package org.zeith.hammerlib.api.config;

import net.minecraft.nbt.*;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.neoforged.fml.loading.FMLEnvironment;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.data.*;
import org.zeith.hammerlib.util.configured.types.ConfigCategory;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface IConfigStructure
{
	default void toNetwork(CompoundTag tag)
	{
		for(var field : ReflectionUtil.getFieldsUpTo(getClass(), Object.class))
		{
			if(field.isAnnotationPresent(Config.AvoidSync.class) || field.isAnnotationPresent(Config.LoadOnlyIn.class))
				continue;
			try
			{
				var ea = field.getAnnotation(Config.ConfigEntry.class);
				
				String tmp0;
				var name = ea != null && (tmp0 = ea.entry()) != null && !tmp0.isEmpty() ? ea.entry() : field.getName();
				
				var type = field.getType();
				
				if(IConfigStructure.class.isAssignableFrom(type))
				{
					var struct = (IConfigStructure) field.get(this);
					
					if(struct != null)
					{
						CompoundTag sub = new CompoundTag();
						struct.toNetwork(sub);
						tag.put(name, sub);
					}
				} else if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type))
				{
					tag.putInt(name, field.getInt(this));
				} else if(long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type))
				{
					tag.putLong(name, field.getLong(this));
				} else if(float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type))
				{
					tag.putFloat(name, field.getFloat(this));
				} else if(double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type))
				{
					tag.putDouble(name, field.getDouble(this));
				} else if(boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type))
				{
					tag.putBoolean(name, field.getBoolean(this));
				} else if(String.class.equals(type))
				{
					tag.putString(name, (String) field.get(this));
				} else
					throw new ConfigException("Unknown field type for config root in field: " + field);
			} catch(Throwable err)
			{
				if(err instanceof ConfigException ce)
					throw ce;
				if(err instanceof ReflectiveOperationException)
					HammerLib.LOG.error("Reflective operation occurred while writing configs to network from " + getClass().getName(), err);
				throw new ConfigException(err);
			}
		}
	}
	
	default void fromNetwork(CompoundTag tag)
	{
		for(var field : ReflectionUtil.getFieldsUpTo(getClass(), Object.class))
		{
			if(field.isAnnotationPresent(Config.AvoidSync.class) || field.isAnnotationPresent(Config.LoadOnlyIn.class))
				continue;
			try
			{
				var ea = field.getAnnotation(Config.ConfigEntry.class);
				
				String tmp0;
				var name = ea != null && (tmp0 = ea.entry()) != null && !tmp0.isEmpty() ? ea.entry() : field.getName();
				
				var type = field.getType();
				
				if(IConfigStructure.class.isAssignableFrom(type))
				{
					var struct = (IConfigStructure) field.get(this);
					
					if(struct == null)
					{
						struct = UnsafeHacks.newInstance(type.asSubclass(IConfigStructure.class));
						field.set(this, struct);
					}
					
					if(tag.contains(name, Tag.TAG_COMPOUND))
						struct.toNetwork(tag.getCompound(name));
				} else if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type))
				{
					field.setInt(this, tag.getInt(name));
				} else if(long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type))
				{
					field.setLong(this, tag.getLong(name));
				} else if(float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type))
				{
					field.setFloat(this, tag.getFloat(name));
				} else if(double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type))
				{
					field.setDouble(this, tag.getDouble(name));
				} else if(boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type))
				{
					field.setBoolean(this, tag.getBoolean(name));
				} else if(String.class.equals(type))
				{
					field.set(this, tag.getString(name));
				} else
					throw new ConfigException("Unknown field type for config root in field: " + field);
			} catch(Throwable err)
			{
				if(err instanceof ConfigException ce)
					throw ce;
				if(err instanceof ReflectiveOperationException)
					HammerLib.LOG.error("Reflective operation occurred while reading configs from network into " + getClass().getName(), err);
				throw new ConfigException(err);
			}
		}
	}
	
	default void load(ConfigCategory category) throws ConfigException
	{
		for(var field : ReflectionUtil.getFieldsUpTo(getClass(), Object.class))
		{
			if(field.isAnnotationPresent(Config.LoadOnlyIn.class) && FMLEnvironment.dist != field.getAnnotation(Config.LoadOnlyIn.class).value())
				continue;
			try
			{
				var ea = field.getAnnotation(Config.ConfigEntry.class);
				
				String tmp0;
				var name = ea != null && (tmp0 = ea.entry()) != null && !tmp0.isEmpty() ? ea.entry() : field.getName();
				var comment = ea != null ? ea.comment() : null;
				
				var type = field.getType();
				
				if(IConfigStructure.class.isAssignableFrom(type))
				{
					var sub = category.getElement(ConfiguredLib.CATEGORY, name)
							.withComment(comment);
					
					var struct = (IConfigStructure) field.get(this);
					
					if(struct == null)
					{
						struct = UnsafeHacks.newInstance(type.asSubclass(IConfigStructure.class));
						field.set(this, struct);
					}
					
					struct.load(sub);
				} else if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type))
				{
					var entry = field.getAnnotation(Config.IntEntry.class);
					
					field.setInt(this, category.getElement(ConfiguredLib.INT, name)
							.withRange(IntValueRange.rangeClosed(entry.min(), entry.max()))
							.withComment(comment)
							.withDefault(entry.value())
							.getValue()
							.intValue());
				} else if(long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type))
				{
					var entry = field.getAnnotation(Config.LongEntry.class);
					
					field.setLong(this, category.getElement(ConfiguredLib.INT, name)
							.withRange(IntValueRange.rangeClosed(entry.min(), entry.max()))
							.withComment(comment)
							.withDefault(entry.value())
							.getValue()
							.longValue());
				} else if(float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type))
				{
					var entry = field.getAnnotation(Config.FloatEntry.class);
					
					field.setFloat(this, category.getElement(ConfiguredLib.DECIMAL, name)
							.withRange(DecimalValueRange.rangeClosed(entry.min(), entry.max()))
							.withComment(comment)
							.withDefault(entry.value())
							.getValue()
							.floatValue());
				} else if(double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type))
				{
					var entry = field.getAnnotation(Config.DoubleEntry.class);
					
					field.setDouble(this, category.getElement(ConfiguredLib.DECIMAL, name)
							.withRange(DecimalValueRange.rangeClosed(entry.min(), entry.max()))
							.withComment(comment)
							.withDefault(entry.value())
							.getValue()
							.doubleValue());
				} else if(boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type))
				{
					var entry = field.getAnnotation(Config.BooleanEntry.class);
					
					field.setBoolean(this, category.getElement(ConfiguredLib.BOOLEAN, name)
							.withComment(comment)
							.withDefault(entry.value())
							.getValue());
				} else if(String.class.equals(type))
				{
					var entry = field.getAnnotation(Config.StringEntry.class);
					
					var all = entry.allowed();
					var nln = all.length > 0 ? "\n" + Arrays.stream(all).map(JSONObject::quote).collect(Collectors.joining(", ")) : "";
					
					field.set(this, category.getElement(ConfiguredLib.STRING, name)
							.withComment(comment)
							.withDefault(entry.value())
							.getValue());
				} else
					throw new ConfigException("Unknown field type for config structure in field: " + field);
			} catch(Throwable err)
			{
				if(err instanceof ConfigException ce)
					throw ce;
				if(err instanceof ReflectiveOperationException)
					HammerLib.LOG.error("Reflective operation occurred while applying configs into " + getClass().getName(), err);
				throw new ConfigException(err);
			}
		}
	}
}