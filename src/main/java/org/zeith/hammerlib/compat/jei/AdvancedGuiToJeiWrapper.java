package org.zeith.hammerlib.compat.jei;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.util.java.Cast;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public class AdvancedGuiToJeiWrapper<T extends AbstractContainerScreen<CTR> & IAdvancedGui<T>, CTR extends AbstractContainerMenu>
		implements IGuiContainerHandler<T>
{
	private static final AdvancedGuiToJeiWrapper RAW_INSTANCE = new AdvancedGuiToJeiWrapper();
	
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
		
		var type = JeiHammerLib.findType(ingr.getClass()).orElse(null);
		if(type == null) return Optional.empty();
		
		Rect2i r = new Rect2i((int) mouseX - 1, (int) mouseY - 1, 3, 3);
		
		var tIngr = new ITypedIngredient<>()
		{
			@Override
			public IIngredientType<Object> getType()
			{
				return Cast.cast(type);
			}
			
			@Override
			public Object getIngredient()
			{
				return ingr;
			}
		};
		
		return Optional.ofNullable(new IClickableIngredient<>()
		{
			@Override
			public ITypedIngredient<Object> getTypedIngredient()
			{
				return tIngr;
			}
			
			@Override
			public Rect2i getArea()
			{
				return r;
			}
		});
	}
}