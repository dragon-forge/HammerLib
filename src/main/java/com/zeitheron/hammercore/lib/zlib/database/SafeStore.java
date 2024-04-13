package com.zeitheron.hammercore.lib.zlib.database;

import com.zeitheron.hammercore.utils.java.Hashers;

import java.util.Arrays;

public class SafeStore
{
	public final byte[] store;
	
	public SafeStore(byte[] store)
	{
		this.store = store;
	}
	
	public boolean matches(byte[] contents)
	{
		return equals(of(contents));
	}
	
	public boolean matches(CharSequence contents)
	{
		return matches((contents + "").getBytes());
	}
	
	@Override
	public boolean equals(Object a)
	{
		return a instanceof SafeStore && Arrays.equals(((SafeStore) a).store, store);
	}
	
	public static SafeStore of(CharSequence contents)
	{
		return of((contents + "").getBytes());
	}
	
	public static SafeStore of(byte[] contents)
	{
		return new SafeStore(Hashers.SHA256.hashifyRaw(contents));
	}
}