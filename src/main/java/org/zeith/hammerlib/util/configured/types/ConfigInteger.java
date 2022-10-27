package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.data.IntValueRange;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;

import java.io.*;
import java.math.BigInteger;
import java.util.Objects;

public class ConfigInteger
		extends ConfigElement<ConfigInteger>
{
	private IntValueRange range;
	private BigInteger defaultValue;
	private BigInteger value;
	private String commentWithNoRange;
	
	public ConfigInteger(Runnable onChanged, ConfigToken<ConfigInteger> token, String name)
	{
		super(onChanged, token, name);
		this.nameTerminator = c -> c == '=';
	}
	
	public ConfigInteger withRange(IntValueRange range)
	{
		if(!Objects.equals(range, this.range) && onChanged != null)
		{
			if(commentWithNoRange != null)
			{
				this.range = range;
				setValue(value);
				withComment(commentWithNoRange);
			}
			onChanged.run();
		}
		
		this.range = range;
		setValue(value);
		
		return this;
	}
	
	public ConfigInteger withDefault(BigInteger value)
	{
		if(!range.test(value)) throw new IllegalArgumentException("Default value (" + value + ") does not match the range " + range);
		this.defaultValue = value;
		return this;
	}
	
	public ConfigInteger withDefault(int value)
	{
		return withDefault(BigInteger.valueOf(value));
	}
	
	public ConfigInteger withDefault(long value)
	{
		return withDefault(BigInteger.valueOf(value));
	}
	
	@Override
	public BigInteger getValue()
	{
		return value != null ? range != null ? range.enclose(value) : value : defaultValue;
	}
	
	public void setValue(BigInteger value)
	{
		if(range != null) value = range.enclose(value);
		if(!Objects.equals(this.value, value))
		{
			this.value = value;
			if(onChanged != null)
				onChanged.run();
		}
	}
	
	@Override
	public ConfigInteger withComment(String comment)
	{
		commentWithNoRange = comment;
		return super.withComment(comment + " (Range: " + range + ")");
	}
	
	@Override
	public boolean read(BufferedReader reader, int depth, String readerStack) throws IOException
	{
		reader.mark(1);
		StringBuilder digits = new StringBuilder();
		int r;
		while((r = reader.read()) >= 0)
		{
			if(r == '\r') continue;
			
			if(r == '\n' || r == ',' || r == ']' || r == '}' || Character.isWhitespace(r))
			{
				reader.reset();
				setValue(new BigInteger(digits.toString()));
				return true;
			}
			
			if((r >= '0' && r <= '9') || (digits.length() == 0 && r == '-'))
			{
				digits.append((char) r);
			} else
			{
				reader.reset();
				throw new IOException(new NumberFormatException("Unexpected character '" + ((char) r) + "' while reading " + readerStack + "; Accumulated: " + digits));
			}
			
			reader.mark(1);
		}
		
		return false;
	}
	
	@Override
	public void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException
	{
		writer.write("" + getValue());
	}
	
	@Override
	public void toBuffer(IByteBuf buf)
	{
		buf.writeString(getValue().toString(Character.MAX_RADIX));
	}
	
	@Override
	public void fromBuffer(IByteBuf buf)
	{
		setValue(new BigInteger(buf.readString(), Character.MAX_RADIX));
	}
	
	@Override
	public String toString()
	{
		return "ConfigInteger{" +
				"defaultValue=" + defaultValue +
				", value=" + value +
				", name='" + name + '\'' +
				", comment='" + getEscapedComment() + '\'' +
				'}';
	}
}