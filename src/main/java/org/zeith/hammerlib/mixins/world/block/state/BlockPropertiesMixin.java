package org.zeith.hammerlib.mixins.world.block.state;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(BlockBehaviour.Properties.class)
public class BlockPropertiesMixin
{
	@Shadow
	@Nullable
	private ResourceKey<Block> id;
	
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
			method = "effectiveDrops",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_effectiveDrops(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> cir)
	{
		if(id == null) cir.setReturnValue(Optional.empty());
	}
}
