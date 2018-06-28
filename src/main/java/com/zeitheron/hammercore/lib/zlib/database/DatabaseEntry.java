package com.zeitheron.hammercore.lib.zlib.database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Arrays;

import com.zeitheron.hammercore.lib.zlib.database.EnumDBType;

public class DatabaseEntry implements Serializable
{
	private static final long serialVersionUID = 4272837444934151609L;
	byte[] data;
	EnumDBType type;
	
	public DatabaseEntry(byte[] data, EnumDBType type)
	{
		this.type = type;
		if(type == EnumDBType.KEY_VALUE)
		{
			this.data = data;
		} else if(type == EnumDBType.MATCHER)
		{
			this.data = DatabaseEntry.recode(data, data.length);
		}
	}
	
	public DatabaseEntry(byte[] data, byte[] code, EnumDBType type)
	{
		if(type != EnumDBType.KEY_VALUE)
		{
			throw new UnsupportedOperationException("Cannot create db with key and value of type " + type.ordinal());
		}
		this.type = type;
		this.set(data, code);
	}
	
	public byte[] get(byte[] code)
	{
		if(this.type != EnumDBType.KEY_VALUE)
		{
			throw new UnsupportedOperationException("Cannot obtain data in db of type " + this.type.ordinal());
		}
		code = DatabaseEntry.recode(code, this.data.length);
		byte[] decr = new byte[this.data.length];
		int i = 0;
		while(i < this.data.length)
		{
			decr[i] = (byte) (this.data[i] - code[i]);
			++i;
		}
		return decr;
	}
	
	public String get(String code)
	{
		return new String(this.get(code.getBytes()));
	}
	
	public void set(byte[] data, byte[] code)
	{
		if(this.type != EnumDBType.KEY_VALUE)
		{
			throw new UnsupportedOperationException("Cannot set data in db of type " + this.type.ordinal());
		}
		code = DatabaseEntry.recode(code, data.length);
		this.data = new byte[data.length];
		int i = 0;
		while(i < data.length)
		{
			this.data[i] = (byte) (data[i] + code[i]);
			++i;
		}
	}
	
	public void set(String data, String code)
	{
		this.set(data.getBytes(), code.getBytes());
	}
	
	public boolean matches(byte[] data)
	{
		if(this.type != EnumDBType.MATCHER)
		{
			throw new UnsupportedOperationException("Cannot compare data in db of type " + this.type.ordinal());
		}
		if(data.length != this.data.length)
		{
			return false;
		}
		data = DatabaseEntry.recode(data, this.data.length);
		return Arrays.equals(data, this.data);
	}
	
	public boolean matches(String data)
	{
		return this.matches(data.getBytes());
	}
	
	private static byte[] recode(byte[] key, int size)
	{
		SecureRandom coder = new SecureRandom(key);
		byte[] buf = new byte[size];
		coder.nextBytes(buf);
		return buf;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.writeInt(this.type.ordinal());
		out.writeObject(this.data);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		this.type = EnumDBType.values()[in.readInt()];
		this.data = (byte[]) in.readObject();
	}
	
	private void readObjectNoData() throws ObjectStreamException
	{
	}
}