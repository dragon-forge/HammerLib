package org.zeith.hammerlib.mixins.data;

import com.mojang.serialization.Decoder;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.data.DataPackRegistryLoadEvent;

import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin
{
	@Inject(
			method = "loadContentsFromManager",
			at = @At("TAIL")
	)
	private static <E> void HammerLib_hookIntoReading(
			ResourceManager res,
			RegistryOps.RegistryInfoLookup lookup,
			WritableRegistry<E> reg,
			Decoder<E> dec,
			Map<ResourceKey<?>, Exception> errors,
			CallbackInfo ci)
	{
		HammerLib.postNeoEvent(new DataPackRegistryLoadEvent(DataPackRegistryLoadEvent.Source.REGISTRY_DATA_LOADER, reg, res, null));
	}
}