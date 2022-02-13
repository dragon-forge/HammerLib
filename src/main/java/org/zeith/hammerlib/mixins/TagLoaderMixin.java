package org.zeith.hammerlib.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SetTag;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.PopulateTagsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(TagLoader.class)
public class TagLoaderMixin
{
	@ModifyVariable(
			method = "build",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/tags/TagCollection;of(Ljava/util/Map;)Lnet/minecraft/tags/TagCollection;"
			),
			index = 2
	)
	private Map putTag_HammerLib(Map value)
	{
		value.forEach((name, theTag) ->
		{
			if(theTag instanceof SetTag nt)
			{
				List objects = new ArrayList<>();
				PopulateTagsEvent evt;
				HammerLib.postEvent(evt = new PopulateTagsEvent(objects::add, (ResourceLocation) name, nt, objects, nt.closestCommonSuperType));
				if(evt.hasChanged())
				{
					objects.addAll(nt.getValues());
					nt.values = Set.copyOf(objects);
					nt.valuesList = ImmutableList.copyOf(objects);
				}
			}
		});

		return value;
	}
}