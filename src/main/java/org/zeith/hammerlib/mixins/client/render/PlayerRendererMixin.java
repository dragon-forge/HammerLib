package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.api.client.IEmissivePlayerInfo;
import org.zeith.hammerlib.compat.base._hl.HLAbilities;
import org.zeith.hammerlib.compat.base.sided.SidedAbilityBase;
import org.zeith.hammerlib.util.java.Cast;

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
		if(emissive == null) return;
		
		var emt = emissive.getEmissiveSkinLocation();
		if(emt == null) return;
		
		var emissiveRT = HammerLib.HL_COMPAT_LIST.firstAbility(HLAbilities.BLOOM)
				.map(SidedAbilityBase::client)
				.map(Cast::get2)
				.map(abil -> abil.emissiveTranslucentArmor(emt))
				.orElseGet(() -> RenderType.entityTranslucentEmissive(emt));
		
		var skin = buf.getBuffer(emissiveRT);
		arm.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
		sleeve.render(pose, skin, lighting, OverlayTexture.NO_OVERLAY);
	}
}