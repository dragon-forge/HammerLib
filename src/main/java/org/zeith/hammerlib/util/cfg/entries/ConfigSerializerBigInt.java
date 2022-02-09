package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.ReaderHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;

public class ConfigSerializerBigInt
		extends ConfigEntrySerializer<ConfigEntryBigInt>
{
	public ConfigSerializerBigInt()
	{
		super("BI");
	}

	@Override
	public void write(ConfigFile config, BufferedWriter writer, ConfigEntryBigInt entry, int indents) throws IOException
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
	public ConfigEntryBigInt read(ConfigFile config, ReaderHelper reader, int indents) throws IOException
	{
		ConfigEntryBigInt lnt = new ConfigEntryBigInt(config, null);

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
				lnt.value = new BigInteger(value);
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