package org.zeith.hammerlib.mixins.world.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.util.mcf.Resources;

import javax.annotation.Nullable;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin
{
	@Shadow
	@Nullable
	private ResourceKey<Item> id;
	
	@Inject(
			method = "effectiveDescriptionId",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_effectiveDescriptionId(CallbackInfoReturnable<String> cir)
	{
		if(id == null) cir.setReturnValue("unlocalized");
	}
	
	@Inject(
			method = "effectiveModel",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_effectiveModel(CallbackInfoReturnable<ResourceLocation> cir)
	{
		if(id == null)
			cir.setReturnValue(Resources.location("missing"));
	}
}