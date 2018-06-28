package com.zeitheron.hammercore.lib.zlib.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zeitheron.hammercore.lib.zlib.database.DatabaseEntry;
import com.zeitheron.hammercore.lib.zlib.database.EnumDBType;
import com.zeitheron.hammercore.lib.zlib.error.DatabaseException;

public final class Database
{
	public final EnumDBType dbType;
	private final List<DatabaseEntry> entries = new ArrayList<DatabaseEntry>();
	
	public Database(EnumDBType type)
	{
		this.dbType = type;
	}
	
	public Database(File file, EnumDBType type) throws IOException, DatabaseException
	{
		this(new FileInputStream(file), type);
	}
	
	public Database(File in) throws IOException, DatabaseException
	{
		this(new FileInputStream(in));
	}
	
	public Database(InputStream in) throws IOException, DatabaseException
	{
		int type = in.read();
		this.dbType = type < 0 || type >= EnumDBType.values().length ? null : EnumDBType.values()[type];
		if(this.dbType == null)
			this.report(in, "Unknown db type: " + type);
		this.load(in, false);
	}
	
	public Database(InputStream in, EnumDBType type) throws IOException, DatabaseException
	{
		this.dbType = type;
		this.load(in, true);
	}
	
	private void load(InputStream input, boolean readType) throws IOException, DatabaseException
	{
		if(readType)
		{
			int _tbyte = input.read();
			if(this.dbType.ordinal() != _tbyte)
			{
				throw this.report(input, "Invalid db type: " + _tbyte);
			}
		}
		ObjectInputStream ois = new ObjectInputStream(input);
		int left = ois.readInt();
		int entry = 0;
		while(left > 0)
		{
			++entry;
			try
			{
				DatabaseEntry dbe = (DatabaseEntry) ois.readObject();
				if(dbe.type != this.dbType)
				{
					throw this.report(ois, "Invalid db type (" + dbe.type.ordinal() + ") in entry #" + entry + ". Required: " + this.dbType.ordinal());
				}
				this.entries.add(dbe);
			} catch(ClassNotFoundException dbe)
			{
				// empty catch block
			}
			--left;
		}
		int end = input.read();
		if(end != 85)
		{
			throw this.report(input, "Corrupted end of db (" + end + ") !");
		}
	}
	
	public void save(OutputStream output) throws IOException
	{
		output.write(this.dbType.ordinal());
		ObjectOutputStream out = output instanceof ObjectOutputStream ? (ObjectOutputStream) output : new ObjectOutputStream(output);
		out.writeInt(this.entries.size());
		for(DatabaseEntry entry : this.entries)
		{
			out.writeObject(entry);
		}
		output.write(85);
	}
	
	public int size()
	{
		return this.entries.size();
	}
	
	public DatabaseEntry[] entries()
	{
		return this.entries.toArray(new DatabaseEntry[0]);
	}
	
	public int addEntry(DatabaseEntry entry) throws DatabaseException
	{
		if(entry.type != this.dbType)
		{
			throw new DatabaseException("Invalid db type (" + entry.type.ordinal() + ") in entry " + entry + ". Required: " + this.dbType.ordinal());
		}
		for(DatabaseEntry ent : this.entries)
		{
			if(!Arrays.equals(ent.data, entry.data) || ent.type.ordinal() != entry.type.ordinal())
				continue;
			return this.entries.indexOf(ent);
		}
		int index = this.entries.indexOf(entry);
		if(index == -1)
		{
			this.entries.add(entry);
			index = this.entries.indexOf(entry);
		}
		return index;
	}
	
	public DatabaseEntry getEntry(int id)
	{
		return this.entries.get(id);
	}
	
	public DatabaseException report(InputStream input, String message) throws IOException, DatabaseException
	{
		input.close();
		return new DatabaseException(message);
	}
}
