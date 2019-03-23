package com.zeitheron.hammercore.cfg.file1132.io;

import java.io.BufferedWriter;
import java.io.IOException;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.ReaderHelper;

public class ConfigSerializerString extends ConfigEntrySerializer<ConfigEntryString>
{
	public ConfigSerializerString()
	{
		super("S");
	}
	
	@Override
	public void write(Configuration config, BufferedWriter writer, ConfigEntryString entry, int indents) throws IOException
	{
		if(entry.getDescription() != null)
			writeComment(writer, entry.getDescription() + " (Default: \"" + entry.initialValue + "\")", indents);
		writeIndents(writer, indents);
		writer.write("+" + type + ":" + entry.getSerializedName() + "=" + entry.getValue() + "\n\n");
	}
	
	@Override
	public ConfigEntryString read(Configuration config, ReaderHelper reader, int indents) throws IOException
	{
		ConfigEntryString str = new ConfigEntryString(config, null);
		
		// Skips comments
		reader.until('+', true);
		
		if(reader.eat(type, true) && reader.eat(':', true))
		{
			String name = reader.until('=', true);
			String value = reader.getRest();
			if(value.contains("\n"))
				value = value.substring(0, value.indexOf('\n'));
			
			str.name = name;
			str.value = value;
		}
		
		return str;
	}
}