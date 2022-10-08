package org.zeith.hammerlib.compat.jei;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.screen.IAdvancedGui;

import java.util.List;

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
	public @Nullable Object getIngredientUnderMouse(T containerScreen, double mouseX, double mouseY)
	{
		return containerScreen.getIngredientUnderMouse(mouseX, mouseY);
	}
}