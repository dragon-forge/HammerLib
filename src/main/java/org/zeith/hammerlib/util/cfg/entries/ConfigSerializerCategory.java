package org.zeith.hammerlib.util.cfg.entries;

import org.zeith.hammerlib.util.cfg.ConfigEntrySerializer;
import org.zeith.hammerlib.util.cfg.ConfigFile;
import org.zeith.hammerlib.util.cfg.IConfigEntry;
import org.zeith.hammerlib.util.cfg.ReaderHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class ConfigSerializerCategory
		extends ConfigEntrySerializer<ConfigEntryCategory>
{
	public ConfigSerializerCategory()
	{
		super("C");
	}

	@Override
	@SuppressWarnings({
			"rawtypes",
			"unchecked"
	})
	public void write(ConfigFile config, BufferedWriter writer, ConfigEntryCategory entry, int indents) throws IOException
	{
		if(entry.getDescription() != null)
			writeComment(writer, entry.getDescription() + "\n", indents);
		writeIndents(writer, indents);
		writer.write("+" + type + ":" + entry.getName() + "={\n");

		for(IConfigEntry e : entry.entries.values().stream().sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList()))
		{
			ConfigEntrySerializer s = e.getSerializer();
			s.write(config, writer, e, indents + 2);
		}

		writeIndents(writer, indents);
		writer.write("}\n\n");
	}

	@Override
	public ConfigEntryCategory read(ConfigFile config, ReaderHelper reader, int indents) throws IOException
	{
		ConfigEntryCategory cat = new ConfigEntryCategory(config);

		// Skips comments
		reader.until('+', true);

		if(reader.eat(type, true) && reader.eat(':', true))
		{
			String name = reader.until('=', true);

			if(reader.eat('{', true))
			{
				cat.name = name;

				char[] ics = new char[indents + 2];
				Arrays.fill(ics, ' ');
				ics[0] = '\n';
				ics[indents + 1] = '}';

				String sub = reader.getRest();
				sub = sub.substring(0, sub.indexOf("\n}"));
				int il = sub.length();
				sub = sub.replaceAll("\n  ", "\n");

				ReaderHelper child = new ReaderHelper(sub);

				while(true)
				{
					int pos = child.push();
					child.until('+', true);
					if(child.currentLine().contains("#"))
					{
						child.until('\n', true);
						continue;
					}
					String c = child.until(':', true);
					ConfigEntrySerializer<?> s = ConfigFile.getSerializers().get(c);
					child.pop(pos);
					if(s != null)
					{
						IConfigEntry e = s.read(config, child, indents + 2);
						if(e != null && e.getName() != null)
							cat.entries.put(e.getName(), e);
						else
							break;
					} else
						break;
				}

				reader.pop(reader.push() + il);
			}
		}

		return cat;
	}
}