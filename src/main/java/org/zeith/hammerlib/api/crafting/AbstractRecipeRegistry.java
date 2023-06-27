package org.zeith.hammerlib.api.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.crafting.building.*;
import org.zeith.hammerlib.event.listeners.TagsUpdateListener;
import org.zeith.hammerlib.event.recipe.ReloadRecipeRegistryEvent;
import org.zeith.hammerlib.util.mcf.itf.INetworkable;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.*;

/**
 * An abstract registry for any subtype of {@link IGeneralRecipe}.
 */
@Mod.EventBusSubscriber
public abstract class AbstractRecipeRegistry<T extends IGeneralRecipe, CTR extends IRecipeContainer<T>, IDX>
{
	private static final List<AbstractRecipeRegistry<?, ?, ?>> ALL_REGISTRIES = new ArrayList<>();
	private static final List<AbstractRecipeRegistry<?, ?, ?>> ALL_REGISTRIES_VIEW = Collections.unmodifiableList(ALL_REGISTRIES);
	protected final Class<T> type;
	protected final CTR container;
	
	protected final CustomRecipeGenerator<T, ?, ?> custom;
	
	private final ResourceLocation id;
	
	public AbstractRecipeRegistry(Class<T> type, CTR container, ResourceLocation id, CustomRecipeGenerator<T, ?, ?> custom)
	{
		if(HammerLib.PROXY.hasFinishedLoading())
			throw new IllegalStateException("Attempted to create recipe registry for type " + type.getName() + " too late! Please do it in construct or preInit!");
		
		this.id = id;
		this.type = type;
		this.container = container;
		this.custom = custom;
		
		synchronized(ALL_REGISTRIES)
		{
			ALL_REGISTRIES.add(this);
		}
		
		HammerLib.LOG.info("Allocated new instance of " + getClass().getName() + " with id " + JSONObject.quote(id.toString()));
	}
	
	public void syncToPlayer(ServerPlayer player)
	{
	}
	
	private UUID activeSyncSession;
	
	@OnlyIn(Dist.CLIENT)
	public void addClientSideRecipe(T recipe, UUID transportSession)
	{
		if(!Objects.equals(activeSyncSession, transportSession))
		{
			activeSyncSession = transportSession;
			resetClientRecipes();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void resetClientRecipes()
	{
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addClientsideContents()
	{
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.Pre(this));
		
		// Remove all STATIC recipes, but keep client-side received.
		removeStaticRecipes();
		
		int prev = getRecipeCount();
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.AddRecipes<>(this, TagsUpdateListener.REMOTE_TAG_ACCESS, null));
		HammerLib.LOG.info("Added " + (getRecipeCount() - prev) + " static recipes into recipe registry " + id);
		
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.Post(this));
	}
	
	public Optional<INetworkable<T>> getNetworkSerializer()
	{
		return custom != null ? custom.getSerializer() : Optional.empty();
	}
	
	IRecipeBuilderFactory<T, ?> recipeBuilder;
	
	public GeneralRecipeBuilder<T, ?> builder(IRecipeRegistrationEvent<T> event)
	{
		if(recipeBuilder != null)
			return recipeBuilder.newBuilder(event);
		throw new UnsupportedOperationException("Building is not supported for " + getClass().getSimpleName() + "<" + type.getSimpleName() + ">{" + id + "}");
	}
	
	public abstract IDX addRecipe(T recipe);
	
	public abstract void removeRecipe(T recipe);
	
	protected abstract void removeAllRecipes();
	
	protected abstract void removeStaticRecipes();
	
	public abstract T getRecipe(IDX identifier);
	
	public Collection<T> getRecipes()
	{
		return container.getRecipes();
	}
	
	public int getRecipeCount()
	{
		return container.getRecipeCount();
	}
	
	public Class<T> getRecipeType()
	{
		return type;
	}
	
	public static List<AbstractRecipeRegistry<?, ?, ?>> getAllRegistries()
	{
		return ALL_REGISTRIES_VIEW;
	}
	
	public ResourceLocation getRegistryId()
	{
		return id;
	}
	
	public void reload(MinecraftServer server, ICondition.IContext context)
	{
		if(server == null)
			server = ServerLifecycleHooks.getCurrentServer();
		
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.Pre(this));
		removeAllRecipes();
		reloadDatapackRecipes(server, context);
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.AddRecipes<>(this, context, server));
		HammerLib.postEvent(new ReloadRecipeRegistryEvent.Post(this));
	}
	
	protected void reloadDatapackRecipes(MinecraftServer server, ICondition.IContext context)
	{
	}
	
	@SubscribeEvent
	public static void serverStart(ServerStartingEvent e)
	{
		HammerLib.LOG.info("Reloading recipes in " + ALL_REGISTRIES.size() + " HL recipe registries.");
		for(AbstractRecipeRegistry<?, ?, ?> reg : ALL_REGISTRIES)
			reg.reload(e.getServer(), e.getServer().getServerResources().managers().getConditionContext());
	}
	
	@SubscribeEvent
	public static void serverStop(ServerStoppingEvent e)
	{
		HammerLib.LOG.info("Purging cache from " + ALL_REGISTRIES.size() + " HL recipe registries.");
		for(AbstractRecipeRegistry<?, ?, ?> reg : ALL_REGISTRIES)
			reg.removeAllRecipes();
	}
}