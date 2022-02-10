package org.zeith.hammerlib.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SetTag;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.PopulateTagsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(TagLoader.class)
public class TagCollectionReaderMixin
{
	@Inject(
			method = "lambda$build$10",
			at = @At(
					value = "HEAD"
			)
	)
	private static void putTag_HammerLib(Map tagMap, ResourceLocation name, Tag theTag, CallbackInfo ci)
	{
		if(theTag instanceof SetTag nt)
		{
			List objects = new ArrayList<>();
			PopulateTagsEvent evt;
			HammerLib.postEvent(evt = new PopulateTagsEvent(objects::add, name, nt, objects, nt.closestCommonSuperType));
			if(evt.hasChanged())
			{
				objects.addAll(nt.getValues());
				nt.values = Set.copyOf(objects);
				nt.valuesList = ImmutableList.copyOf(objects);
			}
		}
	}
}