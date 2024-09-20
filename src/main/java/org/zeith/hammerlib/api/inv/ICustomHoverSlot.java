package org.zeith.hammerlib.api.inv;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Implement your Slot with this interface to override when this slot is hovered.
 */
public interface ICustomHoverSlot
{
	/**
	 * Is this slot being hovered over?
	 */
	@OnlyIn(Dist.CLIENT)
	boolean isSlotBeingHovered(AbstractContainerScreen<?> screen, double mouseX, double mouseY);
}