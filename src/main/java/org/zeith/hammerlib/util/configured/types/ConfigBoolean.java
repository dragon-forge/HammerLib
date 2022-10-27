package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;

import java.io.*;

public class ConfigBoolean
		extends ConfigElement<ConfigBoolean>
{
	protected boolean defaultValue;
	protected Boolean value;
	
	public ConfigBoolean(Runnable onChanged, ConfigToken<ConfigBoolean> token, String name)
	{
		super(onChanged, token, name);
		this.nameTerminator = c -> c == '=';
	}
	
	public ConfigBoolean withDefault(boolean value)
	{
		defaultValue = value;
		return this;
	}
	
	@Override
	public Boolean getValue()
	{
		return value != null ? value : defaultValue;
	}
	
	@Override
	public boolean read(BufferedReader reader, int depth, String readerStack) throws IOException
	{
		reader.mark(5);
		char[] buf = new char[5];
		int r = reader.read(buf);
		if(r == 5 && new String(buf).equalsIgnoreCase("false"))
		{
			value = false;
			return true;
		}
		reader.reset();
		
		reader.mark(4);
		buf = new char[4];
		r = reader.read(buf);
		if(r == 4 && new String(buf).equalsIgnoreCase("true"))
		{
			value = true;
			return true;
		}
		
		reader.reset();
		throw new IOException(new IllegalArgumentException("Unexpected input: \"" + new String(buf) + "\" at " + readerStack));
	}
	
	@Override
	public void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException
	{
		writer.write("" + getValue());
	}
	
	@Override
	public void toBuffer(IByteBuf buf)
	{
		buf.writeByte(getValue() ? 1 : 0);
	}
	
	@Override
	public void fromBuffer(IByteBuf buf)
	{
		value = buf.readByte() > 0;
	}
	
	@Override
	public String toString()
	{
		return "ConfigBoolean{" +
				"defaultValue=" + defaultValue +
				", value=" + value +
				", name='" + name + '\'' +
				", comment='" + getEscapedComment() + '\'' +
				'}';
	}
}