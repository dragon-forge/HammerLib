package org.zeith.hammerlib.mixins;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;

import java.util.*;

@Mixin(TagLoader.class)
public class TagLoaderMixin
{
	@Shadow
	@Final
	private String directory;
	
	@Inject(
			method = "build(Ljava/util/Map;)Ljava/util/Map;",
			at = @At(value = "HEAD")
	)
	private void load_HammerLib(Map<ResourceLocation, List<TagLoader.EntryWithSource>> value, CallbackInfoReturnable<Map<ResourceLocation, Collection>> cir)
	{
		var reg = BuiltInRegistries.REGISTRY
				.stream()
				.filter(t -> TagManager.getTagDir(t.key()).equals(directory))
				.findFirst()
				.orElse(null);
		
		if(reg != null)
		{
			HammerLib.postEvent(new BuildTagsEvent(reg, directory, value));
			HammerLib.LOG.info("Built tags for registry {}", reg.key().location());
		}
		else
			HammerLib.LOG.debug("Unable to find registry for tag directory {}.", directory);
	}
}