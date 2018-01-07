package com.endie.lib.database.check;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import com.endie.lib.tuple.TwoTuple;

/**
 * Allows to store passwords linked to usernames. Note: This only allows to
 * verify said password by username. Database itself doesn't store any
 * passwords!
 */
public class CheckingDatabase
{
	public Map<String, byte[]> data = new HashMap<>();
	private Supplier<DataOutputStream> dos = null;
	
	public CheckingDatabase setAutoSave(Supplier<DataOutputStream> dos)
	{
		this.dos = dos;
		return this;
	}
	
	public CheckingDatabase()
	{
	}
	
	public CheckingDatabase(InputStream in) throws IOException
	{
		this(in instanceof DataInputStream ? (DataInputStream) in : new DataInputStream(new GZIPInputStream(in)));
	}
	
	public CheckingDatabase(DataInputStream d) throws IOException
	{
		int entries = d.readInt();
		
		for(int i = 0; i < entries; ++i)
		{
			TwoTuple<String, byte[]> t = readEntry(d);
			data.put(t.get1(), t.get2());
		}
	}
	
	public boolean matches(String key, byte[] password)
	{
		if(!data.containsKey(key))
			return false;
		return new String(translate(data.get(key), password)).trim().equals(key);
	}
	
	public boolean isRegistered(String key)
	{
		return data.containsKey(key);
	}
	
	public boolean register(String key, byte[] password)
	{
		if(data.containsKey(key))
			return false;
		data.put(key, translate((key + "  ").getBytes(), password));
		autosave();
		return true;
	}
	
	public void autosave()
	{
		new Thread(() ->
		{
			if(dos != null)
			{
				DataOutputStream d = null;
				try
				{
					d = this.dos.get();
					
					if(d != null)
						save(d);
				} catch(IOException e)
				{
					e.printStackTrace();
				} finally
				{
					if(d != null)
						try
						{
							d.close();
						} catch(IOException e)
						{
							e.printStackTrace();
						}
				}
			}
		}).start();
	}
	
	public boolean remove(String key, byte[] password)
	{
		if(matches(key, password))
		{
			boolean removed = data.remove(key) != null;
			if(removed)
				autosave();
			return removed;
		}
		return false;
	}
	
	public void save(DataOutputStream d) throws IOException
	{
		d.writeInt(data.size());
		for(String key : data.keySet())
			writeEntry(d, key, data.get(key));
	}
	
	public static byte[] translate(byte[] data, byte[] seed)
	{
		SecureRandom r = new SecureRandom();
		r.setSeed(seed);
		
		BitSet bits = BitSet.valueOf(data);
		
		for(int i = 0; i < bits.length(); ++i)
		{
			boolean bit = bits.get(i);
			if(r.nextBoolean())
				bits.set(i, !bit);
		}
		
		return bits.toByteArray();
	}
	
	public static TwoTuple<String, byte[]> readEntry(DataInputStream d) throws IOException
	{
		byte[] b1 = new byte[d.readShort()];
		d.readFully(b1);
		
		byte[] b2 = new byte[d.readShort()];
		d.readFully(b2);
		
		return new TwoTuple<String, byte[]>(new String(b1), b2);
	}
	
	public static void writeEntry(DataOutputStream d, String key, byte[] encrypted) throws IOException
	{
		byte[] k = key.getBytes();
		
		d.writeShort(k.length);
		d.write(k);
		
		d.writeShort(encrypted.length);
		d.write(encrypted);
	}
}