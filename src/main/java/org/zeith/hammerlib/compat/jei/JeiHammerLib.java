package org.zeith.hammerlib.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.screen.IAdvancedGui;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

@JeiPlugin
public class JeiHammerLib
		implements IModPlugin
{
	public static final ResourceLocation HL_PLUGIN = new ResourceLocation(HLConstants.MOD_ID, "jei");
	
	@Override
	public ResourceLocation getPluginUid()
	{
		return HL_PLUGIN;
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
}