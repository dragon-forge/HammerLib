package org.zeith.hammerlib.mixins.client.render;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.client.render.RenderCustomGlint;
import org.zeith.hammerlib.client.render.TintingVertexConsumer;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
	//<editor-fold desc="renderStatic">
	@Inject(
			method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
			at = @At("HEAD")
	)
	private void preRenderHook(LivingEntity pEntity, ItemStack pItemStack, ItemDisplayContext pDiplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBufferSource, Level pLevel, int pCombinedLight, int pCombinedOverlay, int pSeed, CallbackInfo ci)
	{
		IColoredFoilItem.Binds.pushContextStack(pItemStack);
	}
	
	@Inject(
			method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
			at = @At("TAIL")
	)
	private void postRenderHook(LivingEntity pEntity, ItemStack pItemStack, ItemDisplayContext pDiplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBufferSource, Level pLevel, int pCombinedLight, int pCombinedOverlay, int pSeed, CallbackInfo ci)
	{
		IColoredFoilItem.Binds.popContextStack();
	}
	//</editor-fold>
	
	
	@WrapMethod(method = "getArmorFoilBuffer")
	private static VertexConsumer getArmorFoilBufferHook(MultiBufferSource bufferSource, RenderType renderType, boolean hasFoil, Operation<VertexConsumer> original)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(hasFoil && TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			bufferSource = RenderCustomGlint.glintTinting(bufferSource, color);
		}
		
		return original.call(bufferSource, renderType, hasFoil);
	}
	
	@WrapMethod(method = "getArmorFoilBuffer")
	private static VertexConsumer getCompassFoilBufferHook(MultiBufferSource bufferSource, RenderType renderType, boolean hasFoil, Operation<VertexConsumer> original)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			bufferSource = RenderCustomGlint.glintTinting(bufferSource, color);
		}
		
		return original.call(bufferSource, renderType, hasFoil);
	}
	
	@WrapMethod(method = "getCompassFoilBuffer")
	private static VertexConsumer getCompassFoilBufferDirectHook(MultiBufferSource bufferSource, RenderType renderType, PoseStack.Pose pose, Operation<VertexConsumer> original)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			bufferSource = RenderCustomGlint.glintTinting(bufferSource, color);
		}
		
		return original.call(bufferSource, renderType, pose);
	}
	
	@WrapMethod(method = "getFoilBuffer")
	private static VertexConsumer getFoilBufferHook(MultiBufferSource bufferSource, RenderType renderType, boolean isItem, boolean glint, Operation<VertexConsumer> original)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(glint && TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			bufferSource = RenderCustomGlint.glintTinting(bufferSource, color);
		}
		
		return original.call(bufferSource, renderType, isItem, glint);
	}
}