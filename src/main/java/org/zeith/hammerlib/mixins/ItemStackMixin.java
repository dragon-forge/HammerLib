package org.zeith.hammerlib.mixins;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.api.items.IDynamicallyTaggedItem;
import org.zeith.hammerlib.util.java.Cast;

import java.util.stream.Stream;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin
{
	@Shadow
	public abstract Item getItem();
	
	@Inject(
			method = "is(Lnet/minecraft/tags/TagKey;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	public void is_HL(TagKey<Item> tag, CallbackInfoReturnable<Boolean> cir)
	{
		if(getItem() instanceof IDynamicallyTaggedItem tagged && tagged.is(Cast.cast(this), (Item) tagged, tag))
			cir.setReturnValue(true);
	}
	
	@Inject(
			method = "getTags",
			at = @At("HEAD"),
			cancellable = true
	)
	public void getTags_HL(CallbackInfoReturnable<Stream<TagKey<Item>>> cir)
	{
		if(getItem() instanceof IDynamicallyTaggedItem tagged)
			cir.setReturnValue(tagged.tags(Cast.cast(this), (Item) tagged));
	}
}