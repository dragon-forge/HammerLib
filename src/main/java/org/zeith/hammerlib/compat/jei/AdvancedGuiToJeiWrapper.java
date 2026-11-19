package org.zeith.hammerlib.compat.jei;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IClickableIngredient;
import mezz.jei.common.ingredients.TypedIngredient;
import mezz.jei.common.input.ClickableIngredient;
import mezz.jei.common.util.ImmutableRect2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.screen.IAdvancedGui;

import java.util.*;

@SuppressWarnings("ALL")
public class AdvancedGuiToJeiWrapper<T extends AbstractContainerScreen<CTR> & IAdvancedGui<T>, CTR extends AbstractContainerMenu>
		implements IGuiContainerHandler<T>
{
	private static final AdvancedGuiToJeiWrapper RAW_INSTANCE = new AdvancedGuiToJeiWrapper();
	
	@NotNull
	public static <T extends AbstractContainerScreen<CTR> & IAdvancedGui, CTR extends AbstractContainerMenu> IGuiContainerHandler<T> get()
	{
		return RAW_INSTANCE;
	}
	
	@Override
	public List<Rect2i> getGuiExtraAreas(T containerScreen)
	{
		return containerScreen.getExtraAreas();
	}
	
	@Override
	public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(T containerScreen, double mouseX, double mouseY)
	{
		var ingr = containerScreen.getIngredientUnderMouse(mouseX, mouseY);
		if(ingr == null) return Optional.empty();
		
		IIngredientType type = JeiHammerLib.findType(ingr.getClass()).orElse(null);
		if(type == null) return Optional.empty();
		
		ImmutableRect2i r = new ImmutableRect2i((int) mouseX - 1, (int) mouseY - 1, 3, 3);
		
		var tIngr = TypedIngredient.createUnvalidated(type, ingr);
		
		return Optional.ofNullable(new ClickableIngredient<>(tIngr, r));
	}
}