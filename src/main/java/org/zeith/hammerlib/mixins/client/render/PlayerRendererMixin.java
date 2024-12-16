package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.client.IEmissivePlayerInfo;
import org.zeith.hammerlib.api.client.IEmissivePlayerState;
import org.zeith.hammerlib.compat.base._hl.HLAbilities;
import org.zeith.hammerlib.compat.base.sided.SidedAbilityBase;
import org.zeith.hammerlib.util.java.Cast;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin
		extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel>
{
	public PlayerRendererMixin(EntityRendererProvider.Context p_174289_, PlayerModel p_174290_, float p_174291_)
	{
		super(p_174289_, p_174290_, p_174291_);
	}
	
	@Inject(
			method = "renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;ZLnet/minecraft/client/player/AbstractClientPlayer;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
					shift = At.Shift.AFTER
			)
	)
	private void renderLeftHand_HL(PoseStack pose, MultiBufferSource buf, int lighting, ResourceLocation texture, boolean secondLayer, AbstractClientPlayer player, CallbackInfo ci)
	{
		var emissive = IEmissivePlayerInfo.get(player.getPlayerInfo());
		if(emissive == null) return;
		
		var emt = emissive.getEmissiveSkinLocation();
		if(emt == null) return;
		
		var emissiveRT = HammerLib.getHLCompats().firstAbility(HLAbilities.BLOOM)
				.map(SidedAbilityBase::client)
				.map(Cast::get2)
				.map(abil -> abil.emissiveTranslucentArmor(emt))
				.orElseGet(() -> RenderType.entityTranslucentEmissive(emt));
		
		PlayerModel playermodel = this.getModel();
		var skin = buf.getBuffer(emissiveRT);
		playermodel.leftArm.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
		playermodel.leftSleeve.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
	}
	
	@Inject(
			method = "renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;ZLnet/minecraft/client/player/AbstractClientPlayer;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
					shift = At.Shift.AFTER
			)
	)
	private void renderRightHand_HL(PoseStack pose, MultiBufferSource buf, int lighting, ResourceLocation texture, boolean secondLayer, AbstractClientPlayer player, CallbackInfo ci)
	{
		var emissive = IEmissivePlayerInfo.get(player.getPlayerInfo());
		if(emissive == null) return;
		
		var emt = emissive.getEmissiveSkinLocation();
		if(emt == null) return;
		
		var emissiveRT = HammerLib.getHLCompats().firstAbility(HLAbilities.BLOOM)
				.map(SidedAbilityBase::client)
				.map(Cast::get2)
				.map(abil -> abil.emissiveTranslucentArmor(emt))
				.orElseGet(() -> RenderType.entityTranslucentEmissive(emt));
		
		PlayerModel playermodel = this.getModel();
		var skin = buf.getBuffer(emissiveRT);
		playermodel.rightArm.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
		playermodel.rightSleeve.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
	}
	
	@Inject(
			method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V",
			at = @At("HEAD")
	)
	private void extractRenderState_HL(AbstractClientPlayer player, PlayerRenderState state, float p_364121_, CallbackInfo ci)
	{
		((IEmissivePlayerState) state).setEmissivePlayerInfo(IEmissivePlayerInfo.get(player.getPlayerInfo()));
	}
}