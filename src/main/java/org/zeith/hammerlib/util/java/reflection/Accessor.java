package org.zeith.hammerlib.util.java.reflection;

import com.google.common.base.Suppliers;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class Accessor
{
	private static final Set<String> MISSING_CLASSES = Collections.synchronizedSet(new HashSet<>());
	private static final Set<String> MISSING_CLASS_MEMBERS = Collections.synchronizedSet(new HashSet<>());
	
	public final String theClass;
	public final String member;
	
	protected final Supplier<Class<?>> lazyClass;
	
	public Accessor(String theClass, String member)
	{
		this.theClass = theClass;
		this.member = member;
		
		this.lazyClass = Suppliers.memoize(() ->
		{
			try
			{
				return Class.forName(theClass);
			} catch(Throwable e)
			{
				if(MISSING_CLASSES.add(theClass))
					e.printStackTrace();
				return null;
			}
		});
	}
	
	@Nullable
	public <T> T staticGet()
	{
		return get(null);
	}
	
	@Nullable
	public <T> T get(Object instance)
	{
		try
		{
			var f = lazyClass.get().getDeclaredField(member);
			f.setAccessible(true);
			return Cast.cast(f.get(instance));
		} catch(ReflectiveOperationException e)
		{
			if(MISSING_CLASS_MEMBERS.add(theClass + "/get/" + member))
				e.printStackTrace();
		}
		
		return null;
	}
	
	public <T> Optional<T> opt(Object instance, Class<T> desiredType)
	{
		try
		{
			var f = lazyClass.get().getDeclaredField(member);
			f.setAccessible(true);
			return Cast.optionally(f.get(instance), desiredType);
		} catch(ReflectiveOperationException e)
		{
			if(MISSING_CLASS_MEMBERS.add(theClass + "/get/" + member))
				e.printStackTrace();
		}
		
		return Optional.empty();
	}
	
	public <T> void set(Object instance, T value)
	{
		try
		{
			var f = lazyClass.get().getDeclaredField(member);
			f.setAccessible(true);
			f.set(instance, value);
		} catch(ReflectiveOperationException e)
		{
			if(MISSING_CLASS_MEMBERS.add(theClass + "/set/" + member))
				e.printStackTrace();
		}
	}
	
	public Object invoke(Object instance, Class<?>[] paramTypes, Object... paramValues)
	{
		try
		{
			var f = lazyClass.get().getDeclaredMethod(member, paramTypes);
			f.setAccessible(true);
			return f.invoke(instance, paramValues);
		} catch(ReflectiveOperationException e)
		{
			if(MISSING_CLASS_MEMBERS.add(theClass + "/invoke/" + member))
				e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return "Accessor{" +
				"class='" + theClass + '\'' +
				", member='" + member + '\'' +
				'}';
	}
}