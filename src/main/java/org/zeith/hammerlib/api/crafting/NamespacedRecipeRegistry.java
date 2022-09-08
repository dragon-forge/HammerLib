package org.zeith.hammerlib.api.crafting;

import com.google.common.collect.BiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLPaths;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.crafting.building.CustomRecipeGenerator;
import org.zeith.hammerlib.api.crafting.itf.IRecipeReceiver;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.PacketAddCustomRecipe;
import org.zeith.hammerlib.util.java.collections.UnmodifiableConcatCollection;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * A more advanced registry name based registry for any subtype of {@link INameableRecipe}.
 */
public class NamespacedRecipeRegistry<T extends INameableRecipe>
		extends AbstractRecipeRegistry<T, NamespacedRecipeRegistry.NamespacedRecipeContainer<T>, ResourceLocation>
{
	protected final RecipeRegistryFactory.RegistryFingerprint<T> fingerprint;
	protected final Set<ResourceLocation> customRecipes = Collections.synchronizedSet(new HashSet<>());
	private final Map<ResourceLocation, Boolean> recipeStates = new HashMap<>();
	
	/**
	 * Please use {@link RecipeRegistryFactory#namespacedBuilder(Class)} instead! This will ensure that multiple registries with same type and ID won't be created, and ensures same instance over few method calls.
	 */
	@Deprecated(forRemoval = true)
	public NamespacedRecipeRegistry(Class<T> type, ResourceLocation id)
	{
		this(new RecipeRegistryFactory.RegistryFingerprint<>(type, id), null);
	}
	
	NamespacedRecipeRegistry(RecipeRegistryFactory.RegistryFingerprint<T> fingerprint, CustomRecipeGenerator<T, ?, ?> custom)
	{
		super(fingerprint.type(), new NamespacedRecipeContainer<>(fingerprint), fingerprint.regId(), custom);
		this.fingerprint = fingerprint;
	}
	
	@Override
	public ResourceLocation addRecipe(T recipe)
	{
		ResourceLocation loc = recipe.getRecipeName();
		
		if(loc == null) throw new IllegalArgumentException("Attempted to register a recipe with null recipe name!");
		if(container.elements.containsKey(loc))
			throw new IllegalArgumentException("Attempted to register a recipe with registry name that is already taken!");
		
		container.elements.put(loc, recipe);
		return loc;
	}
	
	@Override
	public void removeRecipe(T recipe)
	{
		ResourceLocation loc = container.elementsInv.get(recipe);
		if(loc != null) container.remove(loc);
	}
	
	@Override
	protected void removeAllRecipes()
	{
		container.removeAllRecipes();
	}
	
	@Override
	public T getRecipe(ResourceLocation identifier)
	{
		if(identifier == null) return null;
		return container.elements.get(identifier);
	}
	
	protected Path getBuiltinRecipes()
	{
		var codedRecipes = FMLPaths.CONFIGDIR.get()
				.resolve(getRegistryId().getNamespace())
				.resolve("custom_recipes")
				.resolve(getRegistryId().getPath() + ".json");
		codedRecipes.getParent().toFile().mkdirs();
		return codedRecipes;
	}
	
	@Override
	public void reload(MinecraftServer server, ICondition.IContext context)
	{
		super.reload(server, context);
		
		// Lists all BUILTIN recipes that are not listed in the json file. We use it
		var unregisteredKeys = getRecipes()
				.stream()
				.map(INameableRecipe::getRecipeName)
				.filter(f -> !customRecipes.contains(f))
				.map(ResourceLocation::toString)
				.toList();
		
		var cfgPath = getBuiltinRecipes();
		try
		{
			var obj = new JSONObject();
			
			for(var key : unregisteredKeys)
				obj.put(key, true);
			
			for(var key : recipeStates.keySet())
				if(getRecipe(key) != null)
					obj.put(key.toString(), recipeStates.get(key));
			
			Files.writeString(cfgPath, obj.toString(2));
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void reloadDatapackRecipes(MinecraftServer server, ICondition.IContext context)
	{
		recipeStates.clear();
		var cfgPath = getBuiltinRecipes();
		var cfgFile = cfgPath.toFile();
		if(cfgFile.isFile())
		{
			try
			{
				new JSONTokener(Files.readString(cfgPath))
						.nextValueOBJ()
						.ifPresent(obj ->
						{
							for(var str : obj.keySet())
								recipeStates.put(new ResourceLocation(str), obj.getBoolean(str));
						});
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(server != null && custom != null)
		{
			customRecipes.clear();
			
			var resources = server.getServerResources();
			var mgr = resources.resourceManager();
			
			for(Map.Entry<ResourceLocation, Resource> entry : mgr.listResources(custom.getFileDir(), custom::pathMatches).entrySet())
			{
				try
				{
					custom.readRecipe(entry.getKey(), entry.getValue(), server, context)
							.ifPresent(recipe ->
							{
								addRecipe(recipe);
								customRecipes.add(recipe.getRecipeName());
							});
				} catch(IOException e)
				{
					HammerLib.LOG.error("Failed to read recipe for registry " + getClass().getSimpleName() + "<" + type.getSimpleName() + "> (" + getRegistryId() + "): " + entry.getKey(), e);
				}
			}
			
			for(ServerPlayer player : server.getPlayerList().getPlayers())
			{
				syncToPlayer(player);
			}
		}
	}
	
	@Override
	public void syncToPlayer(ServerPlayer player)
	{
		super.syncToPlayer(player);
		
		if(custom != null)
			custom.getSerializer().ifPresent(io ->
			{
				UUID transportSession = UUID.randomUUID();
				
				for(ResourceLocation recipe : customRecipes)
				{
					var r = getRecipe(recipe);
					if(r != null)
						Network.sendTo(player, new PacketAddCustomRecipe(this, r, transportSession));
				}
			});
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addClientSideRecipe(T recipe, UUID transportSession)
	{
		super.addClientSideRecipe(recipe, transportSession);
		
		if(container.elementsClient.containsKey(recipe.getRecipeName()))
			throw new IllegalArgumentException("Attempted to sync a client recipe with registry name that is already in use! (" + recipe.getRecipeName() + ")");
		container.elementsClient.put(recipe.getRecipeName(), recipe);
		for(IRecipeReceiver<T> rcv : fingerprint.clientReceivers())
			rcv.onReceive(recipe);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void resetClientRecipes()
	{
		container.elementsClient.clear();
	}
	
	protected static class NamespacedRecipeContainer<T extends INameableRecipe>
			implements IRecipeContainer<T>
	{
		private final Class<T> type;
		
		private final BiMap<ResourceLocation, T> elementsClient;
		
		private final BiMap<ResourceLocation, T> elements;
		private final BiMap<T, ResourceLocation> elementsInv;
		private final Collection<T> elementsViewClient, elementsViewServer;
		
		public NamespacedRecipeContainer(RecipeRegistryFactory.RegistryFingerprint<T> fingerprint)
		{
			this.type = fingerprint.type();
			
			this.elements = fingerprint.storage();
			this.elementsInv = elements.inverse();
			
			this.elementsClient = fingerprint.clientExtraStorage();
			
			this.elementsViewClient = new UnmodifiableConcatCollection<>(List.of(
					elements.values(),
					elementsClient.values()
			));
			
			this.elementsViewServer = new UnmodifiableConcatCollection<>(List.of(
					elements.values()
			));
		}
		
		@Override
		public Collection<T> getRecipes()
		{
			return LogicalSidePredictor.getCurrentLogicalSide() == LogicalSide.SERVER ? this.elementsViewServer : this.elementsViewClient;
		}
		
		void removeAllRecipes()
		{
			elementsClient.clear();
			elements.clear();
		}
		
		@Override
		public Class<T> getType()
		{
			return type;
		}
		
		public void remove(ResourceLocation loc)
		{
			elements.remove(loc);
		}
	}
}