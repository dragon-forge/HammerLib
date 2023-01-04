package org.zeith.hammerlib.util.configured;

import org.zeith.hammerlib.util.configured.io.IoNewLiner;
import org.zeith.hammerlib.util.configured.io.buf.IByteBuf;
import org.zeith.hammerlib.util.configured.types.ConfigCategory;
import org.zeith.hammerlib.util.configured.types.ConfigObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

public class ConfigFile
		extends ConfigObject<ConfigFile>
{
	protected File file;
	private String version = ConfiguredLib.VERSION;
	
	protected boolean hasChanged;
	
	{
		this.terminalCharacter = (character, depth) -> character <= 0 && depth == 0;
	}
	
	public ConfigFile(File file)
	{
		this.file = file;
	}
	
	public ConfigFile(IByteBuf buf)
	{
		fromBuffer(buf);
	}
	
	public ConfigCategory setupCategory(String name)
	{
		return getElement(ConfiguredLib.CATEGORY, name);
	}
	
	@Override
	protected void onChanged()
	{
		hasChanged = true;
	}
	
	public boolean hasChanged()
	{
		return hasChanged;
	}
	
	public ConfigFile resetChangedMarker()
	{
		hasChanged = false;
		return this;
	}
	
	public String getVersion()
	{
		return version;
	}
	
	@Override
	public boolean read(BufferedReader reader, int depth, String readerStack) throws IOException
	{
		if(depth != 0) throw new IOException("Invalid depth for config file (" + depth + ")");
		if(reader.read() != '['
				|| reader.read() != 'C'
				|| reader.read() != 'F'
				|| reader.read() != 'G'
				|| reader.read() != '='
		) return false;
		StringBuilder sb = new StringBuilder();
		int c;
		while((c = reader.read()) >= 0 && c != ']') sb.append((char) c);
		if(c != ']') return false;
		this.version = sb.toString();
		boolean r = super.read(reader, 0, file.getName());
		hasChanged = false;
		return r;
	}
	
	@Override
	public void write(BufferedWriter writer, IoNewLiner newLiner) throws IOException
	{
		writer.write("[CFG=" + ConfiguredLib.VERSION + "]");
		newLiner.newLine();
		newLiner.newLine();
		super.write(writer, newLiner);
		hasChanged = false;
	}
	
	public Optional<Exception> load()
	{
		if(file.isFile())
		{
			try(BufferedReader in = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)))
			{
				read(in, 0, "");
				return Optional.empty();
			} catch(Exception e)
			{
				return Optional.of(e);
			}
		}
		
		return Optional.of(new FileNotFoundException(file.getName()));
	}
	
	public void save()
	{
		if(file == null) return;
		try(BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)))
		{
			write(out, depth -> out.write(System.lineSeparator() + (repeat("\t", depth))));
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String repeat(String sequence, int times)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < times; ++i) sb.append(sequence);
		return sb.toString();
	}
	
	@Override
	public void toBuffer(IByteBuf buf)
	{
		buf.writeString(version);
		super.toBuffer(buf);
	}
	
	@Override
	public void fromBuffer(IByteBuf buf)
	{
		version = buf.readString();
		super.fromBuffer(buf);
	}
	
	@Override
	public String toString()
	{
		return "ConfigFile{" +
				"file=" + file +
				", version='" + version + '\'' +
				", elements=" + elements +
				'}';
	}
}