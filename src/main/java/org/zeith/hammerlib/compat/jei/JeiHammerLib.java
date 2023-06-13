package org.zeith.hammerlib.compat.jei;

import com.google.common.base.Preconditions;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.abstractions.recipes.*;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.util.*;
import java.util.stream.Stream;

@JeiPlugin
public class JeiHammerLib
		implements IModPlugin, IJeiPluginHL
{
	public static final ResourceLocation HL_PLUGIN = new ResourceLocation(HLConstants.MOD_ID, "jei");
	public static final Map<Class<?>, IIngredientType<?>> INGREDIENT_TYPES = new HashMap<>();
	
	{
		Container.active = this;
		
		
		// Default JEI values. Mods may register this at any point they see fit.
		registerType(ItemStack.class, VanillaTypes.ITEM_STACK);
		registerType(FluidStack.class, ForgeTypes.FLUID_STACK);
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
	
	static final Map<RecipeType<?>, mezz.jei.api.recipe.RecipeType<?>> MC2JEI = new HashMap<>();
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		RecipeVisualizationRegistry.getVisualizedEntries().forEach(tup ->
		{
			var type = tup.a();
			var typeID = ForgeRegistries.RECIPE_TYPES.getKey(type);
			if(typeID == null) return;
			var vis = tup.b();
			mezz.jei.api.recipe.RecipeType jeiRT = MC2JEI.computeIfAbsent(type, vanilla -> new mezz.jei.api.recipe.RecipeType<>(typeID, vis.getVisualizedType()));
			var cat = new VisualizedRecipeCategory<>(jeiRT, vis.getGroup());
			registration.addRecipeCategories(cat);
		});
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		RecipeVisualizationRegistry.getVisualizedEntries().forEach(tup ->
		{
			var type = tup.a();
			var typeID = ForgeRegistries.RECIPE_TYPES.getKey(type);
			if(typeID == null) return;
			var vis = tup.b();
			mezz.jei.api.recipe.RecipeType jeiRT = MC2JEI.computeIfAbsent(type, vanilla -> new mezz.jei.api.recipe.RecipeType<>(typeID, vis.getVisualizedType()));
			registerRecipesFor(registration, (RecipeType) type, jeiRT, (IRecipeVisualizer) vis);
		});
	}
	
	private <R extends Recipe<C>, C extends net.minecraft.world.Container, T extends IVisualizedRecipe<R>> void registerRecipesFor(
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
		ScanDataHelper.lookupAnnotatedObjects(IAdvancedGui.ApplyToJEI.class)
				.stream()
				.map(ScanDataHelper.ModAwareAnnotationData::getOwnerClass)
				.filter(raw -> AbstractContainerScreen.class.isAssignableFrom(raw) && IAdvancedGui.class.isAssignableFrom(raw))
				.forEach(f -> registration.addGuiContainerHandler(f.asSubclass(AbstractContainerScreen.class), Cast.cast(AdvancedGuiToJeiWrapper.get())));
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
	
	public static <T extends Recipe<C>, C extends net.minecraft.world.Container> Stream<T> getRecipes(RecipeType<T> type)
	{
		var lvl = Minecraft.getInstance().level;
		return lvl != null ? RecipeHelper.getRecipes(lvl, type) : Stream.of();
	}
}