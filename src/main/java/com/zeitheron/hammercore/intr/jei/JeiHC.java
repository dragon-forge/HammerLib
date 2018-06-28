package com.zeitheron.hammercore.intr.jei;

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
import net.minecraft.client.Minecraft;

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
				registry.addRecipe(recipe);
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
				registry.removeRecipe(recipe);
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
}