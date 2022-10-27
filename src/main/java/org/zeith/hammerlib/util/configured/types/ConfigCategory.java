package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.StringReader;

import java.io.*;

public class ConfigCategory
		extends ConfigObject<ConfigCategory>
{
	public ConfigCategory(Runnable onChanged, ConfigToken<ConfigCategory> token, String name)
	{
		super(onChanged, token, name);
		this.terminalCharacter = (character, depth) -> character == '}';
	}
	
	public ConfigCategory setupSubCategory(String name)
	{
		return getElement(ConfiguredLib.CATEGORY, name);
	}
	
	@Override
	public boolean read(BufferedReader reader, int depth, String readerStack) throws IOException
	{
		if(StringReader.skipWhitespaces(reader) != '{')
			return false;
		return super.read(reader, depth, readerStack);
	}
	
	@Override
	public void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException
	{
		writer.write("{");
		newLiner.newLine();
		super.write(writer, newLiner);
		newLiner.newLine(-1);
		writer.write("}");
	}
	
	@Override
	public String toString()
	{
		return "ConfigCategory{" +
				"elements=" + elements +
				", name='" + name + '\'' +
				", comment='" + getEscapedComment() + '\'' +
				'}';
	}
}