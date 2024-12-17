package org.zeith.hammerlib.mixins.client.render.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.item.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.zeith.hammerlib.api.client.IColoredGlintItemLayerState;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.client.render.TintingVertexConsumer;

@Mixin(SpecialModelWrapper.class)
public class SpecialModelWrapperMixin
{
	@WrapOperation(
			method = "update",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState$LayerRenderState;setFoilType(Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V")
	)
	private void HammerLib_update_customFoil(ItemStackRenderState.LayerRenderState instance, ItemStackRenderState.FoilType foilType, Operation<Void> original,
											 ItemStackRenderState state, ItemStack stack
	)
	{
		IColoredFoilItem icgi;
		if(stack.hasFoil() && TintingVertexConsumer.tintingEnabled && !stack.isEmpty() && (icgi = IColoredFoilItem.get(stack)) != null)
		{
			int color = icgi.getFoilColor(stack);
			((IColoredGlintItemLayerState) instance).HL_setGlintColor(color);
		}
	}
}