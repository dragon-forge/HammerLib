package org.zeith.hammerlib.api.crafting.building;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.crafting.itf.IFileDecoder;

import java.io.*;
import java.util.Locale;
import java.util.Optional;

public class GsonFileDecoder
		implements IFileDecoder<JsonElement>
{
	public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	
	private static final String PATH_SUFFIX = ".json";
	private static final int PATH_SUFFIX_LENGTH = PATH_SUFFIX.length();
	
	final String directory;
	private final Gson gson;
	
	public GsonFileDecoder(String directory)
	{
		this(directory, GSON);
	}
	
	public GsonFileDecoder(String directory, Gson gson)
	{
		this.directory = directory;
		this.gson = gson;
	}
	
	@Override
	public boolean doesPathMatch(ResourceLocation location)
	{
		return location.getPath().toLowerCase(Locale.ROOT).endsWith(PATH_SUFFIX);
	}
	
	@Override
	public ResourceLocation transformPathToId(ResourceLocation location)
	{
		int i = this.directory.length() + 1;
		String s = location.getPath();
		return new ResourceLocation(location.getNamespace(), s.substring(i, s.length() - PATH_SUFFIX_LENGTH));
	}
	
	@Override
	public Optional<JsonElement> tryDecode(ResourceLocation path, BufferedReader reader) throws IOException
	{
		try
		{
			return Optional.ofNullable(GsonHelper.fromJson(this.gson, reader, JsonElement.class));
		} catch(IllegalArgumentException | JsonParseException err)
		{
			HammerLib.LOG.error("Couldn't parse data file {} from {}", transformPathToId(path), path, err);
		}
		
		return Optional.empty();
	}
	
	@Override
	public void write(JsonElement data, BufferedWriter writer) throws IOException
	{
		writer.write(GSON.toJson(data));
	}
}