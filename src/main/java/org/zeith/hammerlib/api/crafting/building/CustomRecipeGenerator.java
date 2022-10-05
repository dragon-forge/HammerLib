package org.zeith.hammerlib.api.crafting.building;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
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
	
	public Optional<T> readRecipe(ResourceLocation path, Resource resource, MinecraftServer server, ICondition.IContext context) throws IOException
	{
		if(decoder != null)
		{
			try(var in = resource.openAsReader())
			{
				var rid = decoder.transformPathToId(path);
				return decoder.tryDecode(path, in)
						.flatMap(io -> decodeRecipe(rid, io, server, context));
			}
		}
		
		return Optional.empty();
	}
	
	public boolean createExampleRecipe(Resource.IoSupplier<BufferedWriter> writer) throws IOException
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
		Optional net = Cast.optionally(this, INetworkable.class);
		return net;
	}
}