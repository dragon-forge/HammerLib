package com.zeitheron.hammercore.cfg.file1132.io;

import java.io.BufferedWriter;
import java.io.IOException;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.ReaderHelper;

public class ConfigSerializerDouble extends ConfigEntrySerializer<ConfigEntryDouble>
{
	public ConfigSerializerDouble()
	{
		super("D");
	}
	
	@Override
	public void write(Configuration config, BufferedWriter writer, ConfigEntryDouble entry, int indents) throws IOException
	{
		if(entry.getDescription() != null)
			writeComment(writer, entry.getDescription() + " (Default: " + entry.initialValue + ", Range: [" + entry.min + ";" + entry.max + "])", indents);
		writeIndents(writer, indents);
		writer.write("+" + type + ":" + entry.getSerializedName() + "=" + entry.getValue() + "\n\n");
	}
	
	@Override
	public ConfigEntryDouble read(Configuration config, ReaderHelper reader, int indents) throws IOException
	{
		ConfigEntryDouble lnt = new ConfigEntryDouble(config, null);
		
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
				lnt.value = Math.max(lnt.min, Math.min(lnt.max, Double.parseDouble(value)));
			} catch(NumberFormatException nfe)
			{
				lnt.value = null;
			}
		}
		
		return lnt;
	}
}