package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.StringReader;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public abstract class ConfigElement<T extends ConfigElement<T>>
{
	protected final ConfigToken<T> token;
	protected Runnable onChanged;
	protected String name;
	protected String comment;
	protected IntPredicate nameTerminator = c -> false;
	
	public ConfigElement(Runnable onChanged, ConfigToken<T> token, String name)
	{
		this.onChanged = onChanged;
		this.token = token;
		this.name = name;
		if(onChanged != null) onChanged.run();
	}
	
	public abstract Object getValue();
	
	public String getName()
	{
		return name;
	}
	
	public final ConfigToken<T> getToken()
	{
		return token;
	}
	
	public String getComment()
	{
		return this.comment;
	}
	
	public String getEscapedComment()
	{
		var comment = getComment();
		return (comment != null ? comment.replace("\n", "\\n") : "");
	}
	
	public T withComment(String comment)
	{
		if(!Objects.equals(this.comment, comment) && onChanged != null)
			onChanged.run();
		this.comment = comment;
		return (T) this;
	}
	
	protected Optional<String> readComment(BufferedReader reader, int depth) throws IOException
	{
		if(StringReader.skipWhitespaces(reader) != '/'
				|| reader.read() != '*')
		{
			reader.reset();
			return Optional.empty();
		}
		
		StringBuilder sb = new StringBuilder();
		boolean closed = false;
		
		boolean backspace = false;
		
		int r;
		while((r = reader.read()) >= 0)
		{
			if(r == '\\')
			{
				if(!backspace)
				{
					backspace = true;
				} else
				{
					sb.append((char) r);
					backspace = false;
				}
				continue;
			}
			
			if(r == '*' && backspace)
			{
				sb.append((char) r);
				continue;
			}
			
			if(r == '/' && sb.charAt(sb.length() - 1) == '*' && !backspace)
			{
				sb.deleteCharAt(sb.length() - 1);
				closed = true;
				break;
			}
			
			sb.append((char) r);
			backspace = false;
		}
		
		var str = sb.toString()
				.replace(System.lineSeparator(), "\n")
				.lines()
				.map(String::trim)
				.filter(s -> !s.isBlank())
				.collect(Collectors.joining("\n"));
		
		while(str.startsWith("\n")) str = str.substring(1);
		while(str.endsWith("\n")) str = str.substring(0, str.length() - 1);
		
		return !closed ? Optional.empty() : Optional.of(str.stripIndent());
	}
	
	public abstract boolean read(BufferedReader reader, int depth, String readerStack) throws IOException;
	
	public abstract void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException;
	
	public abstract void toBuffer(IByteBuf buf);
	
	public abstract void fromBuffer(IByteBuf buf);
}