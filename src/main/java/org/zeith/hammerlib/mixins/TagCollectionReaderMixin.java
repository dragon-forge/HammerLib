package org.zeith.hammerlib.mixins;

import com.google.common.collect.ImmutableSet;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionReader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.zeith.hammerlib.event.recipe.PopulateTagsEvent;

import java.util.ArrayList;
import java.util.List;

@Mixin(TagCollectionReader.class)
public class TagCollectionReaderMixin
{
	@ModifyArgs(
			method = "load",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
			)
	)
	public void putTag(Args args)
	{
		ResourceLocation id = args.get(0);
		ITag<?> theTag = args.get(1);
		{
			List objects = new ArrayList(theTag.getValues());
			MinecraftForge.EVENT_BUS.post(new PopulateTagsEvent<>(id, theTag, objects));
			theTag = ITag.fromSet(ImmutableSet.copyOf(objects));
		}
		args.set(1, theTag);
	}
}