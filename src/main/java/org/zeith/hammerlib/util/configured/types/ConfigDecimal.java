package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.data.DecimalValueRange;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;

import java.io.*;
import java.math.BigDecimal;
import java.util.Objects;

public class ConfigDecimal
		extends ConfigElement<ConfigDecimal>
{
	private DecimalValueRange range;
	private BigDecimal defaultValue;
	
	private BigDecimal value;
	private String commentWithNoRange;
	
	public ConfigDecimal(Runnable onChanged, ConfigToken<ConfigDecimal> token, String name)
	{
		super(onChanged, token, name);
		this.nameTerminator = c -> c == '=';
	}
	
	public ConfigDecimal withRange(DecimalValueRange range)
	{
		if(!Objects.equals(range, this.range) && onChanged != null)
		{
			if(commentWithNoRange != null)
			{
				this.range = range;
				withComment(commentWithNoRange);
			}
			onChanged.run();
		}
		this.range = range;
		setValue(value);
		return this;
	}
	
	public ConfigDecimal withDefault(BigDecimal value)
	{
		if(!range.test(value)) throw new IllegalArgumentException("Default value does not match the range " + range);
		this.defaultValue = value;
		return this;
	}
	
	public ConfigDecimal withDefault(double value)
	{
		return withDefault(BigDecimal.valueOf(value));
	}
	
	public void setValue(BigDecimal value)
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
	public BigDecimal getValue()
	{
		return value != null ? range != null ? range.enclose(value) : value : defaultValue;
	}
	
	public float getValueF()
	{
		return getValue().floatValue();
	}
	
	public double getValueD()
	{
		return getValue().doubleValue();
	}
	
	@Override
	public ConfigDecimal withComment(String comment)
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
				setValue(new BigDecimal(digits.toString()));
				return true;
			}
			
			if((r >= '0' && r <= '9') || (digits.length() == 0 && r == '-'))
			{
				digits.append((char) r);
			} else if(r == '.') digits.append('.');
			else
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
		buf.writeString(getValue().toString());
	}
	
	@Override
	public void fromBuffer(IByteBuf buf)
	{
		setValue(new BigDecimal(buf.readString()));
	}
	
	@Override
	public String toString()
	{
		return "ConfigDecimal{" +
				"defaultValue=" + defaultValue +
				", value=" + value +
				", name='" + name + '\'' +
				", comment='" + getEscapedComment() + '\'' +
				'}';
	}
}