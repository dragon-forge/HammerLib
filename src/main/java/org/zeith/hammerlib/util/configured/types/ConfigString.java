package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.StringReader;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;

import java.io.*;

public class ConfigString
		extends ConfigElement<ConfigString>
{
	protected String defaultValue;
	protected String value;
	
	public ConfigString(Runnable onChanged, ConfigToken<ConfigString> token, String name)
	{
		super(onChanged, token, name);
	}
	
	public ConfigString withDefault(String string)
	{
		this.defaultValue = string;
		return this;
	}
	
	@Override
	public String getValue()
	{
		return value != null ? value : defaultValue;
	}
	
	@Override
	public boolean read(BufferedReader reader, int depth, String readerStack) throws IOException
	{
		this.value = StringReader.readName(reader, c -> c == '\r' || c == '\n');
		return true;
	}
	
	@Override
	public void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException
	{
		StringReader.writeName(writer, getValue(), c -> true);
	}
	
	@Override
	public void toBuffer(IByteBuf buf)
	{
		buf.writeString(getValue());
	}
	
	@Override
	public void fromBuffer(IByteBuf buf)
	{
		value = buf.readString();
	}
	
	@Override
	public String toString()
	{
		return "ConfigString{" +
				"defaultValue='" + defaultValue + '\'' +
				", value='" + value + '\'' +
				", name='" + name + '\'' +
				", comment='" + getEscapedComment() + '\'' +
				'}';
	}
}
