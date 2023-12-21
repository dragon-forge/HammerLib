package org.zeith.hammerlib.mixins;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagManager;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
		var reg = ((RegistryManagerAccessor) RegistryManager.ACTIVE)
				.getRegistries()
				.values()
				.stream()
				.filter(t -> TagManager.getTagDir(t.getRegistryKey()).equals(directory))
				.findFirst()
				.orElse(null);
		
		if(reg != null)
			HammerLib.postEvent(new BuildTagsEvent(reg, directory, value));
		else
			HammerLib.LOG.warn("Unable to find registry for tag directory " + directory);
	}
}