package org.zeith.hammerlib.mixins;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagManager;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;
import org.zeith.hammerlib.event.recipe.PopulateTagsEvent;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(TagLoader.class)
public class TagLoaderMixin
{
	@Shadow
	@Final
	private String directory;

	@ModifyVariable(
			method = "load",
			at = @At(value = "RETURN"),
			index = 2
	)
	private Map<ResourceLocation, Tag.Builder> load_HammerLib(Map<ResourceLocation, Tag.Builder> value)
	{
		var reg = ((RegistryManagerAccessor) RegistryManager.ACTIVE).getRegistries().values().stream().filter(t -> TagManager.getTagDir(t.getRegistryKey()).equals(directory)).findFirst().orElse(null);
		if(reg != null)
			HammerLib.postEvent(new BuildTagsEvent(reg, directory, value));
		else
			HammerLib.LOG.warn("Unable to find registry for tag directory " + directory);
		return value;
	}

	/*@ModifyVariable(
			method = "build",
			at = @At(value = "RETURN"),
			index = 2
	)*/
	private Map build_HammerLib(Map value)
	{
		value.forEach((name, theTag) ->
		{
			if(theTag instanceof Tag nt)
			{
				var objects = new ArrayList<Holder.Reference<?>>(nt.getValues());
				PopulateTagsEvent evt;

				var closestCommonSuperType = ReflectionUtil.findCommonSuperClass(objects, r -> r.value().getClass());

				var type = PopulateTagsEvent.TAG_BASE_TYPES.stream()
						.filter(base -> base.isAssignableFrom(closestCommonSuperType))
						.findAny()
						.orElse(closestCommonSuperType);

				HammerLib.postEvent(evt = new PopulateTagsEvent(o ->
				{
//					Holder.Reference ref = RegistryAdapter.findReference(Cast.cast(o));
//					if(ref != null)
//						objects.add(ref);
				}, o ->
				{
//					Holder.Reference ref = RegistryAdapter.findReference(Cast.cast(o));
//					if(ref != null)
//						objects.remove(ref);
				}, (ResourceLocation) name, nt, type));

				if(evt.hasChanged())
				{
					nt.elements = List.copyOf(Set.copyOf(objects));
				}
			}
		});

		return value;
	}
}