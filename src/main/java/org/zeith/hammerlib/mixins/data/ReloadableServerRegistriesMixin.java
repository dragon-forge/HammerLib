package org.zeith.hammerlib.mixins.data;

import com.google.gson.JsonElement;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.data.DataPackRegistryLoadEvent;

@Mixin(ReloadableServerRegistries.class)
public class ReloadableServerRegistriesMixin
{
	@Inject(
			method = "lambda$scheduleRegistryLoad$5",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/tags/TagLoader;loadTagsForRegistry(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/core/WritableRegistry;)V",
					shift = At.Shift.BEFORE
			)
	)
	private static <T> void HammerLib_hookIntoReading(LootDataType<T> type, RegistryOps<JsonElement> ops, ResourceManager resources, CallbackInfoReturnable<WritableRegistry<T>> cir)
	{
		WritableRegistry<T> reg = cir.getReturnValue();
		HammerLib.postNeoEvent(new DataPackRegistryLoadEvent(DataPackRegistryLoadEvent.Source.RELOADABLE_SERVER_REGISTRIES, reg, resources, ops.lookupProvider));
	}
}