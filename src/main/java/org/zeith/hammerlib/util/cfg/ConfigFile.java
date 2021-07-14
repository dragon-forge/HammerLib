package org.zeith.hammerlib.util.cfg;

import net.minecraftforge.fml.ModList;
import org.zeith.hammerlib.util.cfg.entries.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfigFile
{
	static final Map<String, ConfigEntrySerializer<?>> serializers = new HashMap<>();

	public static final ConfigSerializerCategory SERIALIZER_CATEGORY = new ConfigSerializerCategory();
	public static final ConfigSerializerString SERIALIZER_STRING = new ConfigSerializerString();
	public static final ConfigSerializerBoolean SERIALIZER_BIT = new ConfigSerializerBoolean();
	public static final ConfigSerializerStringArray SERIALIZER_STRING_ARRAY = new ConfigSerializerStringArray();
	public static final ConfigSerializerInt SERIALIZER_INT = new ConfigSerializerInt();
	public static final ConfigSerializerLong SERIALIZER_LONG = new ConfigSerializerLong();
	public static final ConfigSerializerBigInt SERIALIZER_BIG_INT = new ConfigSerializerBigInt();
	public static final ConfigSerializerFloat SERIALIZER_FLOAT = new ConfigSerializerFloat();
	public static final ConfigSerializerDouble SERIALIZER_DOUBLE = new ConfigSerializerDouble();
	public static final ConfigSerializerBigDecimal SERIALIZER_BIG_DECIMAL = new ConfigSerializerBigDecimal();

	public static Map<String, ConfigEntrySerializer<?>> getSerializers()
	{
		return serializers;
	}

	public final File config;
	public final Map<String, ConfigEntryCategory> categories = new HashMap<>();

	String comment;
	boolean changed;
	String hlversion;

	public ConfigFile(File config)
	{
		this.config = config;
		load();
	}

	public boolean hasChanged()
	{
		return changed;
	}

	public String getComment()
	{
		return comment;
	}

	public void markChanged()
	{
		this.changed = true;
	}

	public ConfigEntryCategory getCategory(String s)
	{
		if(!categories.containsKey(s))
		{
			ConfigEntryCategory c;
			categories.put(s, c = new ConfigEntryCategory(this));
			c.setName(s);
		}
		return categories.get(s);
	}

	public void setComment(String comment)
	{
		if(!Objects.equals(comment, this.comment))
			this.comment = comment;
	}

	public String getHLVersion()
	{
		return hlversion;
	}

	public void load()
	{
		if(config.isFile())
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(config), StandardCharsets.UTF_8)))
			{
				String r = reader.readLine();
				while(r.startsWith("# "))
					r = reader.readLine();
				if(!r.startsWith("~ Saved With Hammer Lib "))
					throw new IOException("Config doesn't have version that it was saved with! (" + r + ")");
				String ver = r.substring(24);

				StringBuilder lines = new StringBuilder();
				String ln;
				while((ln = reader.readLine()) != null)
					lines.append(ln).append('\n');

				ReaderHelper rh = new ReaderHelper(lines.toString());

				Map<String, ConfigEntryCategory> categories = new HashMap<>();
				while(true)
				{
					ConfigEntryCategory cat = SERIALIZER_CATEGORY.read(this, rh, 0);
					if(cat != null && cat.getName() != null)
						categories.put(cat.getName(), cat);
					else
						break;
				}

				this.hlversion = ver;
				this.categories.clear();
				this.categories.putAll(categories);
			} catch(Throwable ioe)
			{
				ioe.printStackTrace();
				config.renameTo(new File(config.getAbsolutePath() + ".errored"));
			}
	}

	public void save()
	{
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(config), StandardCharsets.UTF_8)))
		{
			if(comment != null)
				writeComment(writer, comment);
			StringBuilder version = new StringBuilder();
			ModList.get().getModContainerById("hammerlib").ifPresent(mc -> version.append(mc.getModInfo().getVersion().toString()));
			writer.write(String.format("~ Saved With Hammer Lib %s\n\n", version.length() > 0 ? version.toString() : "???"));
			for(ConfigEntryCategory cat : categories.values().stream().sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList()))
			{
				SERIALIZER_CATEGORY.write(this, writer, cat, 0);
				writer.write('\n');
			}
		} catch(IOException ioe)
		{
		}

		changed = false;
	}

	protected void writeComment(BufferedWriter writer, String comment) throws IOException
	{
		String commentLines[] = comment.split("\n");
		for(String ln : commentLines)
			writer.write("# " + ln + "\n");
	}
}