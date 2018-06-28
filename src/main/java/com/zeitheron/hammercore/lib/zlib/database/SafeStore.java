package com.zeitheron.hammercore.lib.zlib.database;

import java.security.SecureRandom;
import java.util.Arrays;

public class SafeStore
{
	private final byte[] store;
	
	public SafeStore(byte[] store)
	{
		this.store = store;
	}
	
	public boolean matches(byte[] contents)
	{
		return equals(of(contents, store.length));
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
		return of(contents, 32);
	}
	
	public static SafeStore of(CharSequence contents, int storeLen)
	{
		return of((contents + "").getBytes(), storeLen);
	}
	
	public static SafeStore of(byte[] contents, int storeLen)
	{
		SecureRandom rand = new SecureRandom(contents);
		byte[] data = new byte[storeLen];
		rand.nextBytes(data);
		return new SafeStore(data);
	}
}