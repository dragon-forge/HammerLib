package org.zeith.hammerlib.compat.jei;

import com.google.common.base.Preconditions;
import mezz.jei.api.*;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.logging.log4j.*;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.recipes.*;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.util.*;
import java.util.stream.Stream;

@JeiPlugin
public class JeiHammerLib
		implements IModPlugin, IJeiPluginHL
{
	public static final Logger LOG = LogManager.getLogger(JeiHammerLib.class);
	public static final ResourceLocation HL_PLUGIN = new ResourceLocation(HLConstants.MOD_ID, "jei");
	public static final Map<Class<?>, IIngredientType<?>> INGREDIENT_TYPES = new HashMap<>();
	
	{
		Container.active = this;
		
		
		// Default JEI values. Mods may register this at any point they see fit.
		registerType(ItemStack.class, VanillaTypes.ITEM_STACK);
//		FIXME: registerType(FluidStack.class, ForgeTypes.FLUID_STACK);
	}
	
	public static <T> Optional<IIngredientType<T>> findType(Class<T> type)
	{
		Class<?> cl = type;
		do
		{
			cl = cl.getSuperclass();
			if(INGREDIENT_TYPES.containsKey(cl))
				return Cast.cast(Optional.of(INGREDIENT_TYPES.get(cl)));
		} while(cl != null);
		return Optional.empty();
	}
	
	public static <T> void registerType(Class<T> type, IIngredientType<T> iType)
	{
		Preconditions.checkNotNull(type, "type");
		Preconditions.checkNotNull(iType, "iType");
		INGREDIENT_TYPES.put(type, iType);
	}
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return HL_PLUGIN;
	}
	
	IJeiRuntime runtime;
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		this.runtime = jeiRuntime;
	}
	
	static final RecipeVisualizationRegistry visRegistry = new RecipeVisualizationRegistry();
	static final Map<RecipeType<?>, mezz.jei.api.recipe.RecipeType<?>> MC2JEI = new HashMap<>();
	
	mezz.jei.api.recipe.RecipeType jeiFromMc(Tuple2<RecipeType<?>, IRecipeVisualizer<?, ?>> tup)
	{
		var type = tup.a();
		var typeID = BuiltInRegistries.RECIPE_TYPE.getKey(type);
		if(typeID == null) return null;
		var vis = tup.b();
		return Cast.cast(MC2JEI.computeIfAbsent(type, vanilla -> new mezz.jei.api.recipe.RecipeType<>(typeID, vis.getVisualizedType())));
	}
	
	public Stream<Tuple2<RecipeType<?>, IRecipeVisualizer<?, ?>>> getVisualizedEntries()
	{
		return visRegistry.getVisualizedEntries();
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		visRegistry.reload();
		
		getVisualizedEntries().forEach(tup ->
		{
			var jeiRT = jeiFromMc(tup);
			if(jeiRT == null) return;
			var cat = new VisualizedRecipeCategory<>(jeiRT, tup.b().getGroup());
			registration.addRecipeCategories(cat);
		});
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		getVisualizedEntries().forEach(tup ->
		{
			var jeiRT = jeiFromMc(tup);
			if(jeiRT == null) return;
			var type = tup.a();
			var typeID = BuiltInRegistries.RECIPE_TYPE.getKey(type);
			if(typeID == null) return;
			registerRecipesFor(registration, (RecipeType) type, jeiRT, (IRecipeVisualizer) tup.b());
		});
		
		HammerLib.EVENT_BUS.post(new RecipeVisualizationRegistry.RegisterIngredientInfoEvent(
				registration::addItemStackInfo,
				(fluidStacks, components) ->
				{
//					registration.addIngredientInfo(fluidStacks, ForgeTypes.FLUID_STACK, components)
				}
		));
	}
	
	private <R extends Recipe<C>, C extends net.minecraft.world.Container, T extends IVisualizedRecipe<R>>
	void registerRecipesFor(
			IRecipeRegistration reg,
			RecipeType<R> recipeType, mezz.jei.api.recipe.RecipeType<T> type,
			IRecipeVisualizer<R, T> visualizer)
	{
		var lst = getRecipes(recipeType).map(visualizer::visualize).toList();
		reg.addRecipes(type, lst);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		getVisualizedEntries().forEach(tup ->
		{
			var jeiRT = jeiFromMc(tup);
			if(jeiRT == null) return;
			var vis = tup.b();
			var g = vis.getGroup();
			for(IRecipeVisualizer.ClickArea area : g.clickAreas())
				registration.addRecipeClickArea(area.menu(), area.x(), area.y(), area.width(), area.height(), jeiRT);
		});
		
		for(var data : ScanDataHelper.lookupAnnotatedObjects(IRecipeVisualizer.JEIClickArea.class)
				.stream()
				.toList())
		{
			try
			{
				var f = data.getOwnerClass().getDeclaredField(data.getMemberName());
				f.setAccessible(true);
				
				var obj = f.get(null);
				
				IRecipeVisualizer.ClickArea unas = null;
				mezz.jei.api.recipe.RecipeType jeiType = null;
				
				if(obj instanceof IRecipeVisualizer.AssignedClickArea assigned)
				{
					unas = assigned.area();
					jeiType = JeiVisRecipeType.getJEIType(assigned.type());
				} else if(obj instanceof IRecipeVisualizer.JeiBasedClickArea jb)
				{
					unas = jb.area();
					try
					{
						jeiType = jb.type().staticGet();
					} catch(Throwable e)
					{
						// class-cast
						LOG.error("Failed to staticGet JEI RecipeType from " + jb.type());
					}
				}
				
				if(jeiType == null || unas == null)
				{
					LOG.warn("Tried to assign a click area to non-visualized recipe type. This is not supposed to happen!");
					continue;
				}
				
				registration.addRecipeClickArea(unas.menu(), unas.x(), unas.y(), unas.width(), unas.height(), jeiType);
			} catch(ReflectiveOperationException e)
			{
				LOG.error("Failed to read click area " + data.clazz() + "." + data.getMemberName());
			}
		}
		
		ScanDataHelper.lookupAnnotatedObjects(IAdvancedGui.ApplyToJEI.class)
				.stream()
				.map(ScanDataHelper.ModAwareAnnotationData::getOwnerClass)
				.filter(raw -> AbstractContainerScreen.class.isAssignableFrom(raw) &&
							   IAdvancedGui.class.isAssignableFrom(raw))
				.forEach(f -> registration.addGuiContainerHandler(f.asSubclass(AbstractContainerScreen.class), Cast.cast(AdvancedGuiToJeiWrapper.get())));
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		getVisualizedEntries().forEach(tup ->
		{
			mezz.jei.api.recipe.RecipeType jeiRT = jeiFromMc(tup);
			if(jeiRT == null) return;
			var vis = tup.b();
			var g = vis.getGroup();
			for(ItemStack stack : g.catalyst())
				registration.addRecipeCatalyst(stack, jeiRT);
		});
	}
	
	@Override
	public <T> Optional<T> getIngredientUnderMouseJEI(Class<T> type)
	{
		return Optional.ofNullable(runtime)
				.map(IJeiRuntime::getIngredientListOverlay)
				.flatMap(IIngredientListOverlay::getIngredientUnderMouse)
				.filter(ing -> ing.getType().getIngredientClass().equals(ing.getType().getIngredientClass()))
				.filter(ing -> type.isInstance(ing.getIngredient()))
				.map(ing -> Cast.cast(ing.getIngredient()));
	}
	
	public static <T extends Recipe<C>, C extends net.minecraft.world.Container> Stream<RecipeHolder<T>> getRecipes(RecipeType<T> type)
	{
		var lvl = Minecraft.getInstance().level;
		return lvl != null ? RecipeHelper.getRecipeHolders(lvl, type) : Stream.of();
	}
}