package com.zeitheron.hammercore.cfg.file1132.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.ReaderHelper;

public final class ConfigSerializerStringArray extends ConfigEntrySerializer<ConfigEntryStringArray>
{
	public ConfigSerializerStringArray()
	{
		super("SA");
	}
	
	@Override
	public void write(Configuration config, BufferedWriter writer, ConfigEntryStringArray entry, int indents) throws IOException
	{
		if(entry.getDescription() != null)
		{
			List<String> strings = new ArrayList<>();
			for(String k : entry.initialValue)
				strings.add("\"" + k + "\"");
			
			writeComment(writer, entry.getDescription() + " (Default: " + strings + ")", indents);
		}
		writeIndents(writer, indents);
		writer.write("+" + type + ":" + entry.getSerializedName() + "=[\n");
		
		for(String e : entry.value)
		{
			int i = 0;
			for(String s : e.split("\n"))
			{
				writeIndents(writer, indents + (i < 1 ? 2 : 0));
				writer.write(s + "\n");
				++i;
			}
		}
		
		writeIndents(writer, indents);
		writer.write("]\n\n");
	}
	
	@Override
	public ConfigEntryStringArray read(Configuration config, ReaderHelper reader, int indents) throws IOException
	{
		ConfigEntryStringArray cat = new ConfigEntryStringArray(config, null);
		
		// Skips comments
		reader.until('+', true);
		
		if(reader.eat(type, true) && reader.eat(':', true))
		{
			String name = reader.until('=', true);
			
			if(reader.eat('[', true))
			{
				cat.name = name;
				
				char[] ics = new char[indents + 2];
				Arrays.fill(ics, ' ');
				ics[0] = '\n';
				ics[indents + 1] = ']';
				String sub = reader.getRest();
				sub = sub.substring(0, sub.indexOf("\n]"));
				while(sub.startsWith("\n") || sub.startsWith(" "))
					sub = sub.substring(1);
				int il = sub.length();
				cat.value = sub.split("\n  ");
				reader.pop(reader.push() + il);
			}
		}
		
		return cat;
	}
}