package org.zeith.hammerlib.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.util.Optional;

@JeiPlugin
public class JeiHammerLib
		implements IModPlugin, IJeiPluginHL
{
	public static final ResourceLocation HL_PLUGIN = new ResourceLocation(HLConstants.MOD_ID, "jei");
	
	{
		Container.active = this;
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
}