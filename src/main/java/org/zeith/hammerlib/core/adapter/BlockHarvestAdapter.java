package org.zeith.hammerlib.core.adapter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.zeith.hammerlib.event.recipe.PopulateTagsEvent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlockHarvestAdapter
{
	public static final Map<ResourceLocation, Set<Block>> toolTypes = new ConcurrentHashMap<>();

	public static void bindToolType(MineableType tool, Block... blocks)
	{
		Set<Block> set = toolTypes.computeIfAbsent(tool.blockTag.getName(), b -> new HashSet<>());
		set.addAll(Arrays.asList(blocks));
	}

	@SubscribeEvent
	public static void applyTags(PopulateTagsEvent<Block> evt)
	{
		if(toolTypes.containsKey(evt.id))
		{
			toolTypes.get(evt.id).forEach(evt::add);
		}
	}

	public record MineableType(@Nonnull Tag.Named<Block> blockTag)
	{
		public static final MineableType AXE = new MineableType(BlockTags.MINEABLE_WITH_AXE);
		public static final MineableType HOE = new MineableType(BlockTags.MINEABLE_WITH_HOE);
		public static final MineableType PICKAXE = new MineableType(BlockTags.MINEABLE_WITH_PICKAXE);
		public static final MineableType SHOVEL = new MineableType(BlockTags.MINEABLE_WITH_SHOVEL);

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			MineableType that = (MineableType) o;
			return blockTag.equals(that.blockTag);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(blockTag);
		}
	}
}