package org.zeith.hammerlib.util.cfg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

public abstract class ConfigEntrySerializer<T extends IConfigEntry>
{
	public final String type;

	public ConfigEntrySerializer(String type)
	{
		if(ConfigFile.serializers.get(type) != null)
			throw new IllegalArgumentException("Duplicate config serializer for key '" + type + "'!");
		ConfigFile.serializers.put(type, this);
		this.type = type;
	}

	public abstract T read(ConfigFile config, ReaderHelper reader, int indents) throws IOException;

	public abstract void write(ConfigFile config, BufferedWriter writer, T entry, int indents) throws IOException;

	protected void writeComment(BufferedWriter writer, String comment, int indents) throws IOException
	{
		String commentLines[] = comment.split("\n");
		for(String ln : commentLines)
		{
			writeIndents(writer, indents);
			writer.write("# " + ln + "\n");
		}
	}

	protected void writeIndents(BufferedWriter writer, int indents) throws IOException
	{
		char[] write = new char[indents];
		Arrays.fill(write, ' ');
		writer.write(write);
	}
}