package org.zeith.hammerlib.util.configured.types;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.StringReader;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.io.*;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ConfigObject<T extends ConfigObject<T>>
		extends ConfigElement<T>
{
	protected final Map<String, ConfigElement<?>> elements = new HashMap<>();
	
	protected BiPredicate<Integer, Integer> terminalCharacter = (character, depth) -> false;
	
	public ConfigObject(Runnable onChanged, ConfigToken<T> token, String name)
	{
		super(onChanged, token, name);
	}
	
	public ConfigObject()
	{
		super(null, null, "root");
		this.onChanged = this::onChanged;
	}
	
	@Override
	public Map<String, ?> getValue()
	{
		return elements.entrySet()
				.stream()
				.map(e -> Tuples.immutable(e.getKey(), e.getValue().getValue()))
				.filter(t -> t.b() != null)
				.collect(Collectors.toMap(Tuple2::a, Tuple2::b));
	}
	
	protected void onChanged()
	{
	}
	
	@SuppressWarnings("unchecked")
	public <E extends ConfigElement<E>> E getElement(ConfigToken<E> token, String name)
	{
		if(elements.containsKey(name) && !token.is(elements.get(name)))
			elements.remove(name);
		return (E) elements.computeIfAbsent(name, n -> token.create(onChanged, n));
	}
	
	@Override
	public boolean read(BufferedReader reader, int depth, String readerStack) throws IOException
	{
		elements.clear();
		
		while(true)
		{
			reader.mark(1);
			int c;
			if((c = StringReader.skipWhitespaces(reader)) >= 0 && terminalCharacter.test(c, depth))
				return true;
			if(c < 0)
				return depth == 1;
			reader.reset();
			
			var comm = readComment(reader, depth);
			var tokenId = StringReader.readUntilWhitespace(reader);
			var token = ConfiguredLib.getByPrefix(tokenId);
			
			if(token != null)
			{
				var data = token.create(onChanged, name);
				var name = StringReader.readName(reader, data.nameTerminator.or(r -> Character.isWhitespace((char) r) || r == '"'));
				data.name = name;
				
				comm.ifPresent(c0 -> data.comment = c0);
				
				reader.reset();
				
				char ch = (char) reader.read();
				if(ch == '=' && data.read(reader, depth + 1, readerStack + "->" + name))
				{
					elements.put(name, data);
				} else
					return false;
			} else
			{
				reader.reset();
				reader.skip(tokenId.length());
			}
		}
	}
	
	@Override
	public void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException
	{
		int added = 0;
		for(var name : elements.keySet().stream().sorted().toList())
		{
			var val = elements.get(name);
			
			var com = Optional.ofNullable(val.getComment()).filter(s -> !s.isBlank()).orElse(null);
			
			if(com != null)
			{
				var multiline = com.contains("\n");
				
				writer.write("/*");
				if(multiline)
				{
					newLiner.newLine();
					writer.write('\t');
				} else writer.write(' ');
				var ca = com.toCharArray();
				for(int i = 0; i < ca.length; i++)
				{
					char c = ca[i];
					if(c == '\\') writer.write('\\');
					if(c == '*' && i + 1 < ca.length && ca[i + 1] == '/') writer.write('\\');
					if(c == '\n')
					{
						newLiner.newLine();
						writer.write('\t');
						continue;
					}
					writer.write(c);
				}
				if(multiline) newLiner.newLine();
				else writer.write(' ');
				writer.write("*/");
				newLiner.newLine();
			}
			
			writer.write(val.getToken().getPrefix() + " ");
			
			StringReader.writeName(writer, name, c -> Character.isWhitespace((char) c));
			writer.write('=');
			val.write(writer, newLiner.push());
			
			if(++added < elements.size())
			{
				newLiner.newLine();
				newLiner.newLine();
			}
		}
	}
	
	@Override
	public void toBuffer(IByteBuf buf)
	{
		buf.writeShort(elements.size());
		for(var e : elements.entrySet())
		{
			buf.writeString(e.getKey());
			buf.writeString(e.getValue().getToken().getPrefix());
			e.getValue().toBuffer(buf);
		}
	}
	
	@Override
	public void fromBuffer(IByteBuf buf)
	{
		elements.clear();
		
		int size = buf.readShort();
		for(int i = 0; i < size; i++)
		{
			var name = buf.readString();
			var token = buf.readString();
			var e = ConfiguredLib.getByPrefix(token).create(onChanged, name);
			e.fromBuffer(buf);
			elements.put(name, e);
		}
	}
}