package org.zeith.hammerlib.api.crafting.building;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.zeith.hammerlib.api.crafting.IGeneralRecipe;
import org.zeith.hammerlib.api.crafting.itf.IFileDecoder;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.itf.INetworkable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public abstract class CustomRecipeGenerator<T extends IGeneralRecipe, DEC extends IFileDecoder<IO>, IO>
{
	protected final String defaultDir;
	
	protected final DEC decoder;
	
	public CustomRecipeGenerator(ResourceLocation registryPath, Function<String, DEC> decoder)
	{
		this.defaultDir = "recipes_hl/" + registryPath.getNamespace() + "/" + registryPath.getPath();
		this.decoder = decoder.apply(this.defaultDir);
	}
	
	public final String getFileDir()
	{
		return defaultDir;
	}
	
	public boolean pathMatches(ResourceLocation path)
	{
		return decoder.doesPathMatch(path);
	}
	
	public abstract Optional<T> decodeRecipe(ResourceLocation recipeId, IO io, MinecraftServer server, ICondition.IContext context);
	
	public Optional<IO> createTemplate()
	{
		return Optional.empty();
	}
	
	protected JsonElement itemStackTemplate()
	{
		var $ = new JsonObject();
		$.addProperty("item", "example:item_stack");
		return $;
	}
	
	protected JsonElement ingredientTemplate()
	{
		var $ = new JsonObject();
		$.addProperty("tag", "example:ingredient");
		return $;
	}
	
	public Optional<T> readRecipe(ResourceLocation path, Resource resource, MinecraftServer server, ICondition.IContext context) throws IOException
	{
		if(decoder != null)
		{
			try(var in = resource.openAsReader())
			{
				var rid = decoder.transformPathToId(path);
				return rid == null // If the recipe ID was unable to be transformed, don't try to decode.
						? Optional.empty()
						: decoder.tryDecode(path, in)
						.flatMap(io -> decodeRecipe(rid, io, server, context));
			}
		}
		
		return Optional.empty();
	}
	
	public boolean createExampleRecipe(IoSupplier<BufferedWriter> writer) throws IOException
	{
		var template = createTemplate().orElse(null);
		if(template != null)
			try(var wr = writer.get())
			{
				decoder.write(template, wr);
				return true;
			}
		return false;
	}
	
	/**
	 * Override to allow network sync for custom recipes.
	 */
	public Optional<INetworkable<T>> getSerializer()
	{
		Optional opt = Cast.optionally(this, INetworkable.class);
		return opt;
	}
}