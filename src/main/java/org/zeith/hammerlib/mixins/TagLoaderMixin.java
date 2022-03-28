package org.zeith.hammerlib.mixins;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.PopulateTagsEvent;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(TagLoader.class)
public class TagLoaderMixin
{
	@ModifyVariable(
			method = "build",
			at = @At(value = "RETURN"),
			index = 2
	)
	private Map putTag_HammerLib(Map value)
	{
		value.forEach((name, theTag) ->
		{
			if(theTag instanceof Tag nt)
			{
				var objects = new ArrayList<>(nt.getValues());
				PopulateTagsEvent evt;

				var closestCommonSuperType = ReflectionUtil.findCommonSuperClass(objects);

				var type = PopulateTagsEvent.TAG_BASE_TYPES.stream()
						.filter(base -> base.isAssignableFrom(closestCommonSuperType))
						.findAny()
						.orElse(closestCommonSuperType);

				HammerLib.postEvent(evt = new PopulateTagsEvent(objects::add, objects::remove, (ResourceLocation) name, nt, type));
				if(evt.hasChanged())
				{
					nt.elements = List.copyOf(Set.copyOf(objects));
				}
			}
		});

		return value;
	}
}