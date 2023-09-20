package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.zeith.hammerlib.client.render.TintingVertexConsumer.hl$contextStack;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
		extends RenderLayer<T, M>
{
	public HumanoidArmorLayerMixin(RenderLayerParent<T, M> pRenderer)
	{
		super(pRenderer);
	}
	
	@Inject(
			method = "renderArmorPiece",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;hasFoil()Z"
			)
	)
	private void renderArmorPieceHook(PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, EquipmentSlot pSlot, int pPackedLight, A pModel, CallbackInfo ci)
	{
		hl$contextStack = pLivingEntity.getItemBySlot(pSlot);
	}
	
	@Inject(
			method = "renderArmorPiece",
			at = @At(value = "TAIL")
	)
	private void renderArmorPieceHookPost(PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, EquipmentSlot pSlot, int pPackedLight, A pModel, CallbackInfo ci)
	{
		hl$contextStack = ItemStack.EMPTY;
	}
}