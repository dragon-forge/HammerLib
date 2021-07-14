package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.ReaderHelper;

import java.io.BufferedWriter;
import java.io.IOException;

public class ConfigSerializerDouble
		extends ConfigEntrySerializer<ConfigEntryDouble>
{
	public ConfigSerializerDouble()
	{
		super("D");
	}

	@Override
	public void write(ConfigFile config, BufferedWriter writer, ConfigEntryDouble entry, int indents) throws IOException
	{
		if(entry.getDescription() != null)
			writeComment(writer, entry.getDescription() + " (Default: " + entry.initialValue + ", Range: [" + entry.min + ";" + entry.max + "])", indents);
		writeIndents(writer, indents);
		writer.write("+" + type + ":" + entry.getSerializedName() + "=" + entry.getValue() + "\n\n");
	}

	@Override
	public ConfigEntryDouble read(ConfigFile config, ReaderHelper reader, int indents) throws IOException
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