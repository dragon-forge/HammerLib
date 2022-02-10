package org.zeith.hammerlib.core.adapter;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import org.zeith.hammerlib.HammerLib;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlockHarvestAdapter
{
	public static void bindToolType(MineableType tool, Block... blocks)
	{
		TagAdapter.bindStaticTag(tool.blockTag(), blocks);
	}

	public static void bindToolTier(Tier tier, Block... blocks)
	{
		if(tier.getTag() instanceof Tag.Named<Block> nt)
		{
			TagAdapter.bindStaticTag(nt, blocks);
		} else
			HammerLib.LOG.warn("Unable to make out tag name for tier " + tier + "! These blocks won't be assigned a harvest level: " + Arrays.stream(blocks).map(b -> "Block{" + b.getRegistryName() + "}").collect(Collectors.joining(", ", "[", "]")));
	}

	public static void bindTool(MineableType tool, Tier tier, Block... blocks)
	{
		bindToolType(tool, blocks);
		bindToolTier(tier, blocks);
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