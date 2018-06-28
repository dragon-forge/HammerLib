package com.zeitheron.hammercore.cfg;

import java.lang.reflect.Field;

import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyBool;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyFloat;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyInt;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyString;
import com.zeitheron.hammercore.cfg.fields.ModConfigPropertyStringList;

import net.minecraftforge.common.config.Configuration;

public class ConfigHolder
{
	public final IConfigReloadListener listener;
	public final Configuration configs;
	
	public ConfigHolder(IConfigReloadListener listener, Configuration configs)
	{
		this.listener = listener;
		this.configs = configs;
	}
	
	public void reload()
	{
		Field[] fields = listener.getClass().getDeclaredFields();
		
		for(Field f : fields)
			try
			{
				f.setAccessible(true);
				
				Class<?> type = f.getType();
				
				if(type == String.class)
				{
					ModConfigPropertyString prop = f.getAnnotation(ModConfigPropertyString.class);
					boolean flag = prop.allowedValues() == null || prop.allowedValues().length == 0;
					if(prop != null && flag)
						f.set(listener, configs.getString(prop.name(), prop.category(), prop.defaultValue(), prop.comment()));
					else if(prop != null)
						f.set(listener, configs.getString(prop.name(), prop.category(), prop.defaultValue(), prop.comment(), prop.allowedValues()));
				}
				
				if(type == String[].class)
				{
					ModConfigPropertyStringList prop = f.getAnnotation(ModConfigPropertyStringList.class);
					boolean flag = prop.allowedValues() == null || prop.allowedValues().length == 0;
					if(prop != null && flag)
						f.set(listener, configs.getStringList(prop.name(), prop.category(), prop.defaultValue(), prop.comment()));
					else if(prop != null)
						f.set(listener, configs.getStringList(prop.name(), prop.category(), prop.defaultValue(), prop.comment(), prop.allowedValues()));
				}
				
				if(type == int.class || type == Integer.class)
				{
					ModConfigPropertyInt prop = f.getAnnotation(ModConfigPropertyInt.class);
					if(prop != null)
						f.set(listener, configs.getInt(prop.name(), prop.category(), prop.defaultValue(), prop.min(), prop.max(), prop.comment()));
				}
				
				if(type == float.class || type == Float.class)
				{
					ModConfigPropertyFloat prop = f.getAnnotation(ModConfigPropertyFloat.class);
					if(prop != null)
						f.set(listener, configs.getFloat(prop.name(), prop.category(), prop.defaultValue(), prop.min(), prop.max(), prop.comment()));
				}
				
				if(type == boolean.class || type == Boolean.class)
				{
					ModConfigPropertyBool prop = f.getAnnotation(ModConfigPropertyBool.class);
					if(prop != null)
						f.set(listener, configs.getBoolean(prop.name(), prop.category(), prop.defaultValue(), prop.comment()));
				}
			} catch(Throwable err)
			{
			}
		
		listener.reloadCustom(configs);
		if(configs.hasChanged())
			configs.save();
	}
}