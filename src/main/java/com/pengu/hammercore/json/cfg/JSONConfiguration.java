package com.pengu.hammercore.json.cfg;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.pengu.hammercore.json.JSONArray;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONTokener;
import com.pengu.hammercore.json.io.Jsonable;

public class JSONConfiguration implements Jsonable
{
	public ArrayList<iJSONConfigEntry> entries = new ArrayList<>();
	private boolean hasChanged;
	
	public JSONConfiguration(Path json) throws IOException, JSONException
	{
		this(json.toFile().isFile() ? Files.lines(json) : new ArrayList<String>().stream());
	}
	
	public JSONConfiguration(Stream<String> lines) throws JSONException
	{
		this(() ->
		{
			StringBuilder lnr = new StringBuilder();
			lines.forEach(ln -> lnr.append(ln + "\n"));
			return lnr.toString();
		});
	}
	
	public JSONConfiguration(Supplier<String> json) throws JSONException
	{
		this(json.get());
	}
	
	public JSONConfiguration(String json) throws JSONException
	{
		entries.clear();
		if(json != null && !json.isEmpty())
			entries.addAll((Collection) Jsonable.deserialize(json));
	}
	
	public ArrayList<iJSONConfigEntry> listEntries()
	{
		return entries;
	}
	
	public iJSONConfigEntry getEntryByName(String name)
	{
		for(int i = 0; i < entries.size(); ++i)
		{
			iJSONConfigEntry entry = entries.get(i);
			if(name.equals(entry.getName()))
				return entry;
		}
		return null;
	}
	
	public <M extends iJSONConfigEntry> M getEntryOfTypeByName(String name, Class<M> type)
	{
		for(int i = 0; i < entries.size(); ++i)
		{
			iJSONConfigEntry entry = entries.get(i);
			if(type.isAssignableFrom(entry.getClass()) && name.equals(entry.getName()))
				return (M) entry;
		}
		return null;
	}
	
	public void setEntry(String name, iJSONConfigEntry entry)
	{
		iJSONConfigEntry exentry = getEntryByName(name);
		if(exentry != null)
			entries.remove(exentry);
		entries.add(entry);
		hasChanged = true;
	}
	
	public iJSONConfigEntry getEntryOfTypeOrDefault(String name, Class<?> type, String desc, Object value)
	{
		Class<? extends iJSONConfigEntry> typeReg = iJSONConfigEntry.entryClassByClass(type);
		iJSONConfigEntry entry = getEntryOfTypeByName(name, typeReg);
		if(entry != null)
			return entry;
		entry = iJSONConfigEntry.entryByClassAndData(type, name, desc, value);
		setEntry(name, entry);
		hasChanged = true;
		return entry;
	}
	
	public boolean hasChanged()
	{
		return hasChanged;
	}
	
	public void save(OutputStream out) throws IOException
	{
		out.write(serialize().getBytes());
	}
	
	@Override
	public String serialize()
	{
		String ret = "[]";
		try
		{
			ret = Jsonable.serializeIterable(entries);
		} catch(NotSerializableException e)
		{
			e.printStackTrace();
		}
		try
		{
			ret = ((JSONArray) new JSONTokener(ret).nextValue()).toString();
		} catch(JSONException e)
		{
			e.printStackTrace();
		}
		return ret;
	}
}