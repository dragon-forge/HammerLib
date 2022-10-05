package org.zeith.hammerlib.api.crafting.itf;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.api.crafting.building.GsonFileDecoder;

import java.io.*;
import java.util.Optional;

public interface IFileDecoder<IO>
{
	boolean doesPathMatch(ResourceLocation location);
	
	ResourceLocation transformPathToId(ResourceLocation location);
	
	Optional<IO> tryDecode(ResourceLocation path, BufferedReader reader) throws IOException;
	
	void write(IO data, BufferedWriter writer) throws IOException;
	
	static GsonFileDecoder gson(String path)
	{
		return new GsonFileDecoder(path);
	}
	
	static GsonFileDecoder gson(String path, Gson gson)
	{
		return new GsonFileDecoder(path, gson);
	}
}