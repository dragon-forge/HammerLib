package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.client.render.*;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
	@Unique
	private static ItemStack hl$contextStack = ItemStack.EMPTY;
	
	@Inject(
			method = "render",
			at = @At("HEAD")
	)
	private void preRenderHook(ItemStack pItemStack, ItemTransforms.TransformType pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel, CallbackInfo ci)
	{
		hl$contextStack = pItemStack;
	}
	
	@Inject(
			method = "render",
			at = @At("TAIL")
	)
	private void postRenderHook(ItemStack pItemStack, ItemTransforms.TransformType pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel, CallbackInfo ci)
	{
		hl$contextStack = ItemStack.EMPTY;
	}
	
	@Inject(
			method = "getArmorFoilBuffer",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getArmorFoilBufferHook(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint, CallbackInfoReturnable<VertexConsumer> cir)
	{
		IColoredFoilItem icgi;
		if(pWithGlint && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(VertexMultiConsumer.create(
							new TintingVertexConsumer(
									pBuffer.getBuffer(pNoEntity
													  ? RenderCustomGlint.armorGlint()
													  : RenderCustomGlint.armorEntityGlint()
									),
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
		IColoredFoilItem icgi;
		if(!hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(VertexMultiConsumer.create(
							new SheetedDecalTextureGenerator(
									new TintingVertexConsumer(
											pBuffer.getBuffer(RenderCustomGlint.glint()),
											color
									),
									pMatrixEntry.pose(),
									pMatrixEntry.normal()
							),
							pBuffer.getBuffer(pRenderType)
					)
			);
		}
	}
	
	@Inject(
			method = "getCompassFoilBufferDirect",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getCompassFoilBufferDirectHook(MultiBufferSource pBuffer, RenderType pRenderType, PoseStack.Pose pMatrixEntry, CallbackInfoReturnable<VertexConsumer> cir)
	{
		IColoredFoilItem icgi;
		if(!hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(VertexMultiConsumer.create(
							new SheetedDecalTextureGenerator(
									new TintingVertexConsumer(
											pBuffer.getBuffer(RenderCustomGlint.glintDirect()),
											color
									),
									pMatrixEntry.pose(),
									pMatrixEntry.normal()
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
		IColoredFoilItem icgi;
		if(pGlint && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(Minecraft.useShaderTransparency() && pRenderType == Sheets.translucentItemSheet()
							   ?
							   VertexMultiConsumer.create(
									   new TintingVertexConsumer(
											   pBuffer.getBuffer(RenderCustomGlint.glintTranslucent()),
											   color
									   ),
									   pBuffer.getBuffer(pRenderType)
							   )
							   :
							   VertexMultiConsumer.create(
									   new TintingVertexConsumer(
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
	
	@Inject(
			method = "getFoilBufferDirect",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getFoilBufferDirectHook(MultiBufferSource pBuffer, RenderType pRenderType, boolean pNoEntity, boolean pWithGlint, CallbackInfoReturnable<VertexConsumer> cir)
	{
		IColoredFoilItem icgi;
		if(pWithGlint && !hl$contextStack.isEmpty() && (icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			cir.setReturnValue(VertexMultiConsumer.create(
							new TintingVertexConsumer(
									pBuffer.getBuffer(pNoEntity
													  ? RenderCustomGlint.glintDirect()
													  : RenderCustomGlint.entityGlintDirect()),
									color
							),
							pBuffer.getBuffer(pRenderType)
					)
			);
		}
	}
}