package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.client.IEmissivePlayerInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin
		extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
{
	public PlayerRendererMixin(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_)
	{
		super(p_174289_, p_174290_, p_174291_);
	}
	
	@Inject(
			method = "renderHand",
			at = @At("TAIL")
	)
	private void renderHand_HL(PoseStack pose, MultiBufferSource buf, int lighting, AbstractClientPlayer player, ModelPart arm, ModelPart sleeve, CallbackInfo ci)
	{
		var emissive = IEmissivePlayerInfo.get(player.getPlayerInfo());
		
		if(emissive != null)
		{
			var skin = buf.getBuffer(RenderType.entityTranslucentEmissive(emissive.getEmissiveSkinLocation()));
			arm.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
			sleeve.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
		}
	}
}