package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.StringReader;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigArray<E extends ConfigElement<E>>
		extends ConfigElement<ConfigArray<E>>
{
	protected final ConfigToken<E> elementToken;
	protected final List<E> elements = new ArrayList<>();
	
	protected boolean hasRead;
	
	public ConfigArray(Runnable onChanged, ConfigToken<ConfigArray<E>> token, ConfigToken<E> elementToken, String name)
	{
		super(onChanged, token, name);
		this.elementToken = elementToken;
		this.nameTerminator = c -> c == '=';
	}
	
	@Override
	public String getComment()
	{
		if(!elements.isEmpty())
			return elements.get(0).getComment();
		return super.getComment();
	}
	
	@Override
	public ConfigArray<E> withComment(String comment)
	{
		for(E element : elements)
			element.withComment(comment);
		return super.withComment(comment);
	}
	
	public ConfigToken<E> getElementToken()
	{
		return elementToken;
	}
	
	public E createElement()
	{
		var e = elementToken.create(onChanged, "[" + elements.size() + "]");
		if(comment != null) e.withComment(comment);
		elements.add(e);
		return e;
	}
	
	@Override
	public List<?> getValue()
	{
		return elements.stream().map(ConfigElement::getValue).toList();
	}
	
	public List<E> getElements()
	{
		return elements;
	}
	
	public void clear()
	{
		elements.clear();
	}
	
	public boolean hasRead()
	{
		return hasRead;
	}
	
	@Override
	public boolean read(BufferedReader reader, int depth, String readerStack) throws IOException
	{
		reader.mark(1);
		if(reader.read() != '[')
			return false;
		
		hasRead = true;
		clear();
		
		while(true)
		{
			reader.mark(32);
			int c;
			if((c = StringReader.skipWhitespaces(reader)) >= 0)
			{
				char ch = (char) c;
				
				if(ch == ']')
					return true;
				
				if(c == ',' && !elements.isEmpty())
					continue;
				
				reader.reset();
			} else
				return false;
			
			var data = elementToken.create(onChanged, "[" + elements.size() + "]");
			
			if(data.read(reader, depth + 1, readerStack + "->" + name))
			{
				if(comment != null)
					data.withComment(comment);
				elements.add(data);
			}
		}
	}
	
	@Override
	public void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException
	{
		writer.write('[');
		
		if(elements.isEmpty())
		{
			writer.write(']');
			return;
		}
		
		if(elements.size() > 1) newLiner.newLine();
		var pushed = newLiner.push();
		for(int i = 0; i < elements.size(); i++)
		{
			E com = elements.get(i);
			com.write(writer, pushed);
			if(i + 1 < elements.size())
			{
				writer.write(',');
				newLiner.newLine();
			}
		}
		
		if(elements.size() > 1) newLiner.newLine(-1);
		writer.write(']');
	}
	
	@Override
	public void toBuffer(IByteBuf buf)
	{
		buf.writeShort(elements.size());
		buf.writeString(elementToken.getPrefix());
		for(E e : elements) e.toBuffer(buf);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void fromBuffer(IByteBuf buf)
	{
		clear();
		
		int size = buf.readShort();
		var type = ConfiguredLib.getByPrefix(buf.readString());
		for(int i = 0; i < size; i++)
		{
			var inst = type.create(onChanged, "[" + i + "]");
			inst.fromBuffer(buf);
			if(elementToken.is(inst))
				elements.add((E) inst);
		}
	}
	
	@Override
	public String toString()
	{
		return "ConfigArray{" +
				"components=" + elements +
				", name='" + name + '\'' +
				", comment='" + getEscapedComment() + '\'' +
				'}';
	}
}