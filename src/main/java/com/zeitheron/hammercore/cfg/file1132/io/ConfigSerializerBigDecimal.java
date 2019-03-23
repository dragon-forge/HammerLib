package com.zeitheron.hammercore.cfg.file1132.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;

import com.zeitheron.hammercore.cfg.file1132.ConfigEntrySerializer;
import com.zeitheron.hammercore.cfg.file1132.Configuration;
import com.zeitheron.hammercore.cfg.file1132.ReaderHelper;

public class ConfigSerializerBigDecimal extends ConfigEntrySerializer<ConfigEntryBigDecimal>
{
	public ConfigSerializerBigDecimal()
	{
		super("BD");
	}
	
	@Override
	public void write(Configuration config, BufferedWriter writer, ConfigEntryBigDecimal entry, int indents) throws IOException
	{
		if(entry.getDescription() != null)
		{
			String range;
			
			if(entry.min != null)
				range = "[" + entry.min + ";";
			else
				range = "(-Inf;";
			
			if(entry.max != null)
				range += entry.max + "]";
			else
				range += "+Inf)";
			
			writeComment(writer, entry.getDescription() + " (Default: " + entry.initialValue + ", Range: " + range + ")", indents);
		}
		writeIndents(writer, indents);
		writer.write("+" + type + ":" + entry.getSerializedName() + "=" + entry.getValue() + "\n\n");
	}
	
	@Override
	public ConfigEntryBigDecimal read(Configuration config, ReaderHelper reader, int indents) throws IOException
	{
		ConfigEntryBigDecimal lnt = new ConfigEntryBigDecimal(config, null);
		
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
				lnt.value = new BigDecimal(value);
				if(lnt.min != null)
					lnt.value = lnt.value.max(lnt.min);
				if(lnt.max != null)
					lnt.value = lnt.value.min(lnt.max);
			} catch(NumberFormatException nfe)
			{
				lnt.value = null;
			}
		}
		
		return lnt;
	}
}