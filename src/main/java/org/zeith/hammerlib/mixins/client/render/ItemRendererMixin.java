package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
	
	
	@Inject(
			method = "getArmorFoilBuffer",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getArmorFoilBufferHook(MultiBufferSource pBuffer, RenderType pRenderType, boolean pWithGlint, CallbackInfoReturnable<VertexConsumer> cir)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(pWithGlint && TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(VertexMultiConsumer.create(
							TintingVertexConsumer.wrap(
									pBuffer.getBuffer(RenderCustomGlint.armorEntityGlint()),
									color
							),
							pBuffer.getBuffer(pRenderType)
					)
			);
		}
	}
	
	@Inject(
			method = "getCompassFoilBuffer",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getCompassFoilBufferHook(MultiBufferSource pBuffer, RenderType pRenderType, PoseStack.Pose pMatrixEntry, CallbackInfoReturnable<VertexConsumer> cir)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(VertexMultiConsumer.create(
							new SheetedDecalTextureGenerator(
									TintingVertexConsumer.wrap(
											pBuffer.getBuffer(RenderCustomGlint.glint()),
											color
									),
									pMatrixEntry,
									0.0078125F
							),
							pBuffer.getBuffer(pRenderType)
					)
			);
		}
	}
	
	@Inject(
			method = "getCompassFoilBuffer",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getCompassFoilBufferDirectHook(MultiBufferSource pBuffer, RenderType pRenderType, PoseStack.Pose pMatrixEntry, CallbackInfoReturnable<VertexConsumer> cir)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(VertexMultiConsumer.create(
							new SheetedDecalTextureGenerator(
									TintingVertexConsumer.wrap(
											pBuffer.getBuffer(RenderCustomGlint.glintDirect()),
											color
									),
									pMatrixEntry,
									0.0078125F
							),
							pBuffer.getBuffer(pRenderType)
					)
			);
		}
	}
	
	@Inject(
			method = "getFoilBuffer",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getFoilBufferHook(MultiBufferSource pBuffer, RenderType pRenderType, boolean pIsItem, boolean pGlint, CallbackInfoReturnable<VertexConsumer> cir)
	{
		var hl$contextStack = IColoredFoilItem.Binds.getContextStack();
		
		IColoredFoilItem icgi;
		if(pGlint && TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(Minecraft.useShaderTransparency() && pRenderType == Sheets.translucentItemSheet()
							   ?
							   VertexMultiConsumer.create(
									   TintingVertexConsumer.wrap(
											   pBuffer.getBuffer(RenderCustomGlint.glintTranslucent()),
											   color
									   ),
									   pBuffer.getBuffer(pRenderType)
							   )
							   :
							   VertexMultiConsumer.create(
									   TintingVertexConsumer.wrap(
											   pBuffer.getBuffer(pIsItem
																 ? RenderCustomGlint.glint()
																 : RenderCustomGlint.entityGlint()),
											   color
									   ),
									   pBuffer.getBuffer(pRenderType)
							   )
			);
		}
	}
}