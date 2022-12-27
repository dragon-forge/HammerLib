package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.client.IEmissivePlayerInfo;
import org.zeith.hammerlib.compat.base._hl.HLAbilities;
import org.zeith.hammerlib.compat.base.sided.SidedAbilityBase;
import org.zeith.hammerlib.util.java.Cast;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
		extends EntityRenderer<T>
		implements RenderLayerParent<T, M>
{
	@Shadow
	public static boolean isEntityUpsideDown(LivingEntity p_194454_)
	{
		return false;
	}
	
	@Shadow
	protected abstract float getBob(T p_115305_, float p_115306_);
	
	@Shadow
	protected abstract void setupRotations(T p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_);
	
	@Shadow
	protected abstract void scale(T p_115314_, PoseStack p_115315_, float p_115316_);
	
	@Shadow
	protected M model;
	
	@Shadow
	protected abstract boolean isBodyVisible(T p_115341_);
	
	@Shadow
	public static int getOverlayCoords(LivingEntity p_115339_, float p_115340_)
	{
		return 0;
	}
	
	@Shadow
	protected abstract float getWhiteOverlayProgress(T p_115334_, float p_115335_);
	
	protected LivingEntityRendererMixin(EntityRendererProvider.Context p_174008_)
	{
		super(p_174008_);
	}
	
	@Inject(
			method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
			)
	)
	private void render_HL(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource src, int p_115313_, CallbackInfo ci)
	{
		var emissive = IEmissivePlayerInfo.get(p_115308_ instanceof AbstractClientPlayer acp ? acp.getPlayerInfo() : null);
		if(emissive == null) return;
		
		var emt = emissive.getEmissiveSkinLocation();
		if(emt == null) return;
		
		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = this.isBodyVisible(p_115308_);
		boolean flag1 = !flag && !p_115308_.isInvisibleTo(minecraft.player);
		
		var emissiveRT = HammerLib.HL_COMPAT_LIST.firstAbility(HLAbilities.BLOOM)
				.map(SidedAbilityBase::client)
				.map(Cast::get2)
				.map(abil -> abil.emissiveTranslucentArmor(emt))
				.orElseGet(() -> RenderType.entityTranslucentEmissive(emt));
		
		VertexConsumer vertexconsumer = src.getBuffer(emissiveRT);
		int i = getOverlayCoords(p_115308_, this.getWhiteOverlayProgress(p_115308_, p_115310_));
		this.model.renderToBuffer(p_115311_, vertexconsumer, p_115313_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
	}
}