package com.zeitheron.hammercore.intr.jei;

import java.util.Arrays;
import java.util.function.Consumer;

import com.zeitheron.hammercore.client.gui.impl.smooth.GuiBrewingStandSmooth;
import com.zeitheron.hammercore.client.gui.impl.smooth.GuiFurnaceSmooth;
import com.zeitheron.hammercore.utils.classes.ClassWrapper;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IFocus.Mode;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JeiHC implements IModPlugin, IJeiHelper
{
	private static Object KeyBinding_showRecipe, KeyBinding_showUses;
	
	IRecipeRegistry registry;
	IJeiRuntime runtime;
	
	{
		Instance.JEIModifier = this;
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime)
	{
		this.runtime = runtime;
		registry = runtime.getRecipeRegistry();
	}
	
	@Override
	public void register(IModRegistry reg)
	{
		// Add click areas to our smooth guis
		reg.addRecipeClickArea(GuiBrewingStandSmooth.class, 97, 16, 14, 30, "minecraft.brewing");
		reg.addRecipeClickArea(GuiFurnaceSmooth.class, 78, 32, 28, 23, "minecraft.smelting");
		
		ClassWrapper keyBindings = ClassWrapper.create("mezz.jei.config.KeyBindings");
		KeyBinding_showRecipe = keyBindings.findField("showRecipe").get();
		KeyBinding_showUses = keyBindings.findField("showUses").get();
	}
	
	@Override
	public void addJEI(Object recipe)
	{
		Minecraft.getMinecraft().addScheduledTask(() ->
		{
			if(recipe instanceof Consumer)
				((Consumer) recipe).accept(registry);
			else
			{
				String uid = null;
				
				IRecipeWrapper w = registry.getRecipeWrapper(recipe, uid);
				registry.unhideRecipe(w, uid);
			}
		});
	}
	
	@Override
	public void removeJEI(Object recipe)
	{
		Minecraft.getMinecraft().addScheduledTask(() ->
		{
			if(recipe instanceof Consumer)
				((Consumer) recipe).accept(registry);
			else
			{
				String uid = null;
				
				IRecipeWrapper w = registry.getRecipeWrapper(recipe, uid);
				registry.hideRecipe(w, uid);
			}
		});
	}
	
	@Override
	public Object getKeybind_showRecipes()
	{
		return KeyBinding_showRecipe;
	}
	
	@Override
	public Object getKeybind_showUses()
	{
		return KeyBinding_showUses;
	}
	
	@Override
	public <T> void showRecipes(T ingredient)
	{
		runtime.getRecipesGui().show(registry.createFocus(Mode.OUTPUT, ingredient));
	}
	
	@Override
	public <T> void showUses(T ingredient)
	{
		runtime.getRecipesGui().show(registry.createFocus(Mode.INPUT, ingredient));
	}
	
	@Override
	public void showCategories(String... uid)
	{
		runtime.getRecipesGui().showCategories(Arrays.asList(uid));
	}
	
	@Override
	public ItemStack getSlotUnderMouseInJEI()
	{
		Object mouse = runtime.getIngredientListOverlay().getIngredientUnderMouse();
		if(mouse == null || !(mouse instanceof ItemStack))
			return ItemStack.EMPTY;
		return (ItemStack) mouse;
	}
}