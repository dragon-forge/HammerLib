package org.zeith.hammerlib.mixins.client.render.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.client.IColoredGlintItemLayerState;
import org.zeith.hammerlib.client.render.RenderCustomGlint;

@Implements({
		@Interface(iface = IColoredGlintItemLayerState.class, prefix = "cgirs$")
})
@Mixin(ItemStackRenderState.LayerRenderState.class)
public class ItemLayerRenderStateMixin
{
	@Unique
	private Integer cgirs$HL_glintColor;
	
	public void cgirs$HL_setGlintColor(Integer color)
	{
		cgirs$HL_glintColor = color;
	}
	
	public Integer cgirs$HL_getGlintColor()
	{
		return cgirs$HL_glintColor;
	}
	
	@Inject(
			method = "clear",
			at = @At("HEAD")
	)
	private void HammerLib_clear(CallbackInfo ci)
	{
		cgirs$HL_glintColor = null;
	}
	
	@WrapMethod(method = "render")
	private void HammerLib_render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Operation<Void> original)
	{
		if(cgirs$HL_glintColor != null)
			// Replace glints with colored.
			bufferSource = RenderCustomGlint.glintTinting(bufferSource, cgirs$HL_glintColor);
		
		original.call(poseStack, bufferSource, packedLight, packedOverlay);
	}
}