package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.client.render.TintingVertexConsumer;

@Mixin(EquipmentLayerRenderer.class)
public abstract class EquipmentLayerRendererMixin
{
	@ModifyVariable(
			method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"
			),
			index = 18
	)
	private VertexConsumer renderArmorPieceHook(VertexConsumer vc,
												EquipmentClientInfo.LayerType pLayerType,
												ResourceKey<EquipmentAsset> pEquipmentAsset,
												Model pArmorModel,
												ItemStack pItem
	)
	{
		IColoredFoilItem icgi;
		if(TintingVertexConsumer.tintingEnabled && !pItem.isEmpty() &&
		   (icgi = IColoredFoilItem.get(pItem)) != null)
		{
			int color = icgi.getFoilColor(pItem);
			return TintingVertexConsumer.wrap(
					vc,
					color
			);
		}
		return vc;
	}
}