package org.zeith.hammerlib.api.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.configured.ConfigFile;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.lang.reflect.Modifier;

public interface IConfigRoot
{
	/**
	 * Return true to avoid syncing the configs from server to client.
	 */
	default boolean avoidSync()
	{
		return false;
	}

	default void toNetwork(CompoundTag tag)
	{
		for(var field : ReflectionUtil.getFieldsUpTo(getClass(), Object.class))
		{
			if(Modifier.isStatic(field.getModifiers()))
				continue;
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
			if(Modifier.isStatic(field.getModifiers()))
				continue;
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
						struct.fromNetwork(tag.getCompound(name));
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

	default void load(ConfigFile file) throws ConfigException
	{
		for(var field : ReflectionUtil.getFieldsUpTo(getClass(), Object.class))
		{
			if(Modifier.isStatic(field.getModifiers()))
				continue;
			if(field.isAnnotationPresent(Config.LoadOnlyIn.class) && FMLEnvironment.dist != field.getAnnotation(Config.LoadOnlyIn.class).value())
				continue;
			try
			{
				var ea = field.getAnnotation(Config.ConfigEntry.class);

				String tmp0;
				var name = ea != null && (tmp0 = ea.entry()) != null && !tmp0.isEmpty() ? ea.entry() : field.getName();
				var description = ea != null ? ea.comment() : null;

				var type = field.getType();

				if(IConfigStructure.class.isAssignableFrom(type))
				{
					var sub = file.setupCategory(name)
							.withComment(description);

					var struct = (IConfigStructure) field.get(this);

					if(struct == null)
					{
						struct = UnsafeHacks.newInstance(type.asSubclass(IConfigStructure.class));
						field.set(this, struct);
					}

					struct.load(sub);
				} else
					throw new ConfigException("Unknown field type for config root in field: " + field);
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

	default void updateInstance(LogicalSide side) throws ConfigException
	{
		for(var field : getClass().getDeclaredFields())
		{
			if(Modifier.isStatic(field.getModifiers()) && ConfigHolder.class.isAssignableFrom(field.getType()))
			{
				try
				{
					if(ReflectionUtil.doesParameterTypeArgsMatch(field, getClass()))
						((ConfigHolder) field.get(null)).update(this, side);
					else
						throw new ConfigException("Found non-self config holder " + field);
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
}