package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.state.*;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.client.IEmissivePlayerInfo;
import org.zeith.hammerlib.api.client.IEmissivePlayerState;
import org.zeith.hammerlib.compat.base._hl.HLAbilities;
import org.zeith.hammerlib.compat.base.sided.SidedAbilityBase;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.java.Cast;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends EntityRenderState, M extends EntityModel<S>>
		extends EntityRenderer<T, S>
		implements RenderLayerParent<S, M>
{
	@Shadow
	protected M model;
	
	@Shadow
	protected abstract boolean isBodyVisible(LivingEntityRenderState p_361327_);
	
	@Shadow
	protected abstract float getWhiteOverlayProgress(LivingEntityRenderState p_362441_);
	
	protected LivingEntityRendererMixin(EntityRendererProvider.Context p_174008_)
	{
		super(p_174008_);
	}
	
	@Inject(
			method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;shouldRenderLayers(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z"
			)
	)
	private void render_HL(LivingEntityRenderState state, PoseStack p_115311_, MultiBufferSource src, int p_115313_, CallbackInfo ci)
	{
		if(!(state instanceof PlayerRenderState acp) || !(acp instanceof IEmissivePlayerState eps)) return;
		
		var emissive = eps.getEmissivePlayerInfo();
		if(emissive == null) return;
		
		var emt = emissive.getEmissiveSkinLocation();
		if(emt == null) return;
		
		boolean flag = this.isBodyVisible(state);
		boolean flag1 = !flag && !acp.isInvisibleToPlayer;
		
		var emissiveRT = HammerLib.getHLCompats().firstAbility(HLAbilities.BLOOM)
				.map(SidedAbilityBase::client)
				.map(Cast::get2)
				.map(abil -> abil.emissiveTranslucentArmor(emt))
				.orElseGet(() -> RenderType.entityTranslucentEmissive(emt));
		
		VertexConsumer vc = src.getBuffer(emissiveRT);
		int i = LivingEntityRenderer.getOverlayCoords(acp, this.getWhiteOverlayProgress(state));
		this.model.renderToBuffer(p_115311_, vc, p_115313_, i, ColorHelper.packARGB(1.0F, 1.0F, 1.0F, flag1 ? 38.25f : 1.0F));
	}
}