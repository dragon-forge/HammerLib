package org.zeith.hammerlib.mixins;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.core.adapter.CreativeTabAdapter;

@Mixin(Item.class)
public abstract class ItemMixin
		implements IForgeItem
{
	@Inject(
			method = "allowedIn",
			at = @At("HEAD"),
			cancellable = true
	)
	private void allowedIn_HL(CreativeModeTab tab, CallbackInfoReturnable<Boolean> cir)
	{
		if(CreativeTabAdapter.getTabOverrides((Item) (Object) this).stream().anyMatch(t -> t == tab))
		{
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}