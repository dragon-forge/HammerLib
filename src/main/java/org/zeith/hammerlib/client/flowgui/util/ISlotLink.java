package org.zeith.hammerlib.client.flowgui.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.inventory.Slot;

public interface ISlotLink
{
	boolean isValidFor(Slot slot);
	
	void patchSlotTransforms(Slot slot, PoseStack pose);
	
	boolean isMouseOver(Slot slot, double mouseX, double mouseY);
}