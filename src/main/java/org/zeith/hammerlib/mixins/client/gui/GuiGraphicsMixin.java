package org.zeith.hammerlib.mixins.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.items.IColoredFoilItem;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin
{
	@Inject(
			method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
			at = @At("HEAD")
	)
	private void HammerLib_preRenderItem(LivingEntity pEntity, Level pLevel, ItemStack pStack, int pX, int pY, int pSeed, int pGuiOffset, CallbackInfo ci)
	{
		IColoredFoilItem.Binds.pushContextStack(pStack);
	}
	
	@Inject(
			method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
			at = @At("TAIL")
	)
	private void HammerLib_postRenderItem(LivingEntity pEntity, Level pLevel, ItemStack pStack, int pX, int pY, int pSeed, int pGuiOffset, CallbackInfo ci)
	{
		IColoredFoilItem.Binds.popContextStack();
	}
}