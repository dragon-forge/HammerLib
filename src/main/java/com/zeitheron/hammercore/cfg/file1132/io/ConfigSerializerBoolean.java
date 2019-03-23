package com.zeitheron.hammercore.cfg.file1132.io;

import java.io.BufferedWriter;
import java.io.IOException;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.ReaderHelper;

public class ConfigSerializerBoolean extends ConfigEntrySerializer<ConfigEntryBoolean>
{
	public ConfigSerializerBoolean()
	{
		super("B");
	}
	
	@Override
	public void write(Configuration config, BufferedWriter writer, ConfigEntryBoolean entry, int indents) throws IOException
	{
		if(entry.getDescription() != null)
			writeComment(writer, entry.getDescription() + " (Default: " + entry.initialValue + ")", indents);
		writeIndents(writer, indents);
		writer.write("+" + type + ":" + entry.getSerializedName() + "=" + entry.getValue() + "\n\n");
	}
	
	@Override
	public ConfigEntryBoolean read(Configuration config, ReaderHelper reader, int indents) throws IOException
	{
		ConfigEntryBoolean lnt = new ConfigEntryBoolean(config, null);
		
		// Skips comments
		reader.until('+', true);
		
		if(reader.eat(type, true) && reader.eat(':', true))
		{
			String name = reader.until('=', true);
			String value = reader.getRest();
			if(value.contains("\n"))
				value = value.substring(0, value.indexOf('\n'));
			
			lnt.name = name;
			try
			{
				lnt.value = Boolean.parseBoolean(value);
			} catch(NumberFormatException nfe)
			{
				lnt.value = null;
			}
		}
		
		return lnt;
	}
}