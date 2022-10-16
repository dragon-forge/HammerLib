package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.event.listeners.client.InputListener;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
	@Shadow
	@Nullable
	public HitResult hitResult;
	
	@Shadow
	@Nullable
	public ClientLevel level;
	
	@Inject(
			method = "continueAttack",
			at = @At("HEAD")
	)
	public void continueAttack_HL(boolean p_91387_, CallbackInfo ci)
	{
		hitResult = InputListener.alterHitResult(hitResult, level);
	}
}