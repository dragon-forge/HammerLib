package org.zeith.hammerlib.net.packets;

import com.mojang.authlib.exceptions.MinecraftClientException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.api.crafting.AbstractRecipeRegistry;
import org.zeith.hammerlib.api.crafting.IGeneralRecipe;
import org.zeith.hammerlib.net.*;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.itf.INetworkable;

import java.util.*;

@MainThreaded
public class PacketAddCustomRecipe
		implements IPacket
{
	private AbstractRecipeRegistry registryCache;
	private ResourceLocation registry;
	private IGeneralRecipe recipe;
	private UUID transportSession;
	
	public PacketAddCustomRecipe(AbstractRecipeRegistry registryCache, IGeneralRecipe recipe, UUID transportSession)
	{
		this.registryCache = registryCache;
		this.registry = registryCache.getRegistryId();
		this.recipe = recipe;
		this.transportSession = transportSession;
	}
	
	public PacketAddCustomRecipe()
	{
	}
	
	private AbstractRecipeRegistry getRegistry()
	{
		if(registryCache != null)
			return registryCache;
		return registryCache = AbstractRecipeRegistry.getAllRegistries().stream().filter(r -> r.getRegistryId().equals(registry)).findFirst().orElse(null);
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeResourceLocation(registry);
		buf.writeUUID(transportSession);
		Optional<INetworkable> n = getRegistry().getNetworkSerializer();
		n.orElseThrow().toNetwork(buf, recipe);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		registry = buf.readResourceLocation();
		transportSession = buf.readUUID();
		
		Optional<INetworkable> n = Optional.ofNullable(getRegistry())
				.flatMap(AbstractRecipeRegistry::getNetworkSerializer);
		
		recipe = Cast.cast(n.orElseThrow(() -> new MinecraftClientException(MinecraftClientException.ErrorType.JSON_ERROR, "Unable to find recipe registry " + registry + " client-side!", new NoSuchElementException(registry + " registry."))).fromNetwork(buf));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientExecute(PacketContext ctx)
	{
		try
		{
			getRegistry().addClientSideRecipe(recipe, transportSession);
		} catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
}