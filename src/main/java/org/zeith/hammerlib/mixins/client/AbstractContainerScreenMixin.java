package org.zeith.hammerlib.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.api.client.gui.IClientSlotPatch;
import org.zeith.hammerlib.api.inv.ICustomHoverSlot;
import org.zeith.hammerlib.client.flowgui.util.ISlotLink;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.Nullable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin
{
	@Shadow
	protected abstract void init();
	
	@Shadow
	@Nullable
	protected Slot hoveredSlot;
	
	@Shadow
	protected int leftPos;
	
	@Shadow
	protected int topPos;
	
	@Inject(
			method = "isHovering(Lnet/minecraft/world/inventory/Slot;DD)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_isHovering(Slot pSlot, double pMouseX, double pMouseY, CallbackInfoReturnable<Boolean> cir)
	{
		if(pSlot instanceof ICustomHoverSlot hs)
			cir.setReturnValue(hs.isSlotBeingHovered(Cast.cast(this), pMouseX, pMouseY));
		var link = ((IClientSlotPatch) pSlot).getLinkedHover();
		if(link != null) cir.setReturnValue(link.isMouseOver(pSlot, pMouseX, pMouseY));
	}
	
	@Inject(
			method = "renderSlot",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
					shift = At.Shift.AFTER
			)
	)
	private void HammerLib_renderSlot(GuiGraphics pGuiGraphics, Slot pSlot, CallbackInfo ci)
	{
		IClientSlotPatch patch = (IClientSlotPatch) pSlot;
		var link = patch.getLinkedHover();
		if(link == null) return;
		
		var pose = pGuiGraphics.pose();
		pose.translate(-pSlot.x - leftPos, -pSlot.y - topPos, 0);
		link.patchSlotTransforms(pSlot, pose);
	}
	
	@WrapOperation(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlightBack(Lnet/minecraft/client/gui/GuiGraphics;)V"
			)
	)
	private void HammerLib_renderSlotHighlightBack(AbstractContainerScreen instance, GuiGraphics guiGraphics, Operation<Void> original)
	{
		ISlotLink link;
		if(!(hoveredSlot instanceof IClientSlotPatch patch) || (link = patch.getLinkedHover()) == null)
		{
			original.call(instance, guiGraphics);
			return;
		}
		
		var pose = guiGraphics.pose();
		pose.pushPose();
		pose.translate(-hoveredSlot.x - leftPos, -hoveredSlot.y - topPos, 0);
		link.patchSlotTransforms(hoveredSlot, pose);
		
		original.call(instance, guiGraphics);
		
		pose.popPose();
	}
	
	@WrapOperation(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlightFront(Lnet/minecraft/client/gui/GuiGraphics;)V"
			)
	)
	private void HammerLib_renderSlotHighlightFront(AbstractContainerScreen instance, GuiGraphics guiGraphics, Operation<Void> original)
	{
		ISlotLink link;
		if(!(hoveredSlot instanceof IClientSlotPatch patch) || (link = patch.getLinkedHover()) == null)
		{
			original.call(instance, guiGraphics);
			return;
		}
		
		var pose = guiGraphics.pose();
		pose.pushPose();
		pose.translate(-hoveredSlot.x - leftPos, -hoveredSlot.y - topPos, 0);
		link.patchSlotTransforms(hoveredSlot, pose);
		
		original.call(instance, guiGraphics);
		
		pose.popPose();
	}
}