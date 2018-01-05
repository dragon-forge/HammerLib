package com.pengu.hammercore.intr.jei;

import java.util.function.Consumer;

import com.pengu.hammercore.core.gui.smooth.GuiBrewingStandSmooth;
import com.pengu.hammercore.core.gui.smooth.GuiFurnaceSmooth;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.client.Minecraft;

@JEIPlugin
public class JeiHC implements IModPlugin, iJeiRecipeModifier
{
	IRecipeRegistry registry;
	
	{
		Instance.JEIModifier = this;
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime)
	{
		registry = runtime.getRecipeRegistry();
	}
	
	@Override
	public void register(IModRegistry reg)
	{
		// Add click areas to our smooth guis
		reg.addRecipeClickArea(GuiBrewingStandSmooth.class, 97, 16, 14, 30, "minecraft.brewing");
		reg.addRecipeClickArea(GuiFurnaceSmooth.class, 78, 32, 28, 23, "minecraft.smelting");
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration arg0)
	{
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry arg0)
	{
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
}