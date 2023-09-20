package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.client.render.*;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M>
{
	@Unique
	private static ItemStack hl$contextStack = ItemStack.EMPTY;
	
	public HumanoidArmorLayerMixin(RenderLayerParent<T, M> pRenderer)
	{
		super(pRenderer);
	}
	
	@Inject(
			method = "renderArmorPiece",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderGlint(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;)V"
			),
			cancellable = true
	)
	private void renderArmorPieceHook(PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, EquipmentSlot pSlot, int pPackedLight, A pModel, CallbackInfo ci)
	{
		hl$contextStack = pLivingEntity.getItemBySlot(pSlot);
		
		IColoredFoilItem icgi;
		if(TintingVertexConsumer.tintingEnabled && !hl$contextStack.isEmpty() &&
				(icgi = IColoredFoilItem.get(hl$contextStack)) != null)
		{
			int color = icgi.getFoilColor(hl$contextStack);
			pModel.renderToBuffer(pPoseStack,
					TintingVertexConsumer.wrap(
							pBuffer.getBuffer(RenderCustomGlint.armorEntityGlint()),
							color
					),
					pPackedLight,
					OverlayTexture.NO_OVERLAY,
					1.0F, 1.0F, 1.0F, 1.0F
			);
			
			ci.cancel();
		}
	}
}