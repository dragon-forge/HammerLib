/* Decompiled with CFR 0_123. */
package com.zeitheron.hammercore.lib.zlib.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.zeitheron.hammercore.lib.zlib.database.Database;
import com.zeitheron.hammercore.lib.zlib.database.DatabaseEntry;
import com.zeitheron.hammercore.lib.zlib.database.EnumDBType;
import com.zeitheron.hammercore.lib.zlib.error.DatabaseException;
import com.zeitheron.hammercore.lib.zlib.tuple.TwoTuple;

public class DatabaseWrapper
{
	public final Database db;
	
	public DatabaseWrapper(Database db)
	{
		this.db = db;
	}
	
	public int size()
	{
		return this.db.size();
	}
	
	public TwoTuple<DatabaseEntry, Integer> addEntry(String content, String code) throws DatabaseException
	{
		if(this.db.dbType == EnumDBType.KEY_VALUE)
		{
			DatabaseEntry dbe = new DatabaseEntry(content.getBytes(), code.getBytes(), this.db.dbType);
			return new TwoTuple<DatabaseEntry, Integer>(dbe, this.db.addEntry(dbe));
		}
		return null;
	}
	
	public TwoTuple<DatabaseEntry, Integer> addEntry(String code) throws DatabaseException
	{
		if(this.db.dbType == EnumDBType.MATCHER)
		{
			DatabaseEntry dbe = new DatabaseEntry(code.getBytes(), this.db.dbType);
			return new TwoTuple<DatabaseEntry, Integer>(dbe, this.db.addEntry(dbe));
		}
		return null;
	}
	
	public DatabaseEntry getEntry(int id)
	{
		return this.db.getEntry(id);
	}
	
	public void save(File out) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(out);
		this.save(fos);
		fos.close();
	}
	
	public void save(OutputStream out) throws IOException
	{
		this.db.save(out);
	}
}
