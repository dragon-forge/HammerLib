package com.pengu.hammercore.client.particle.old;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ParticleParam
{
	private final HashMap<String, Object> data = new HashMap<String, Object>();
	private boolean isSer = false;
	
	protected void add(String key, Object val)
	{
		if(!isSer)
			data.putIfAbsent(key, val);
	}
	
	protected void clear()
	{
		if(!isSer)
			data.clear();
	}
	
	protected void ser()
	{
		isSer = true;
	}
	
	public Object get(String key)
	{
		return data.get(key);
	}
	
	public ParticleParam getSubParam(String key)
	{
		Object o = get(key);
		return o instanceof ParticleParam ? (ParticleParam) o : null;
	}
	
	public boolean has(String key)
	{
		return get(key) != null;
	}
	
	public Set<String> keys()
	{
		return new HashSet<String>(data.keySet());
	}
}