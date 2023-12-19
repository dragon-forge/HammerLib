package org.zeith.hammerlib.core.adapter;

import net.minecraft.tags.*;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BlockHarvestAdapter
{
	public static void bindToolType(MineableType tool, Block... blocks)
	{
		TagAdapter.bind(tool.blockTag(), blocks);
	}
	
	public static void bindToolTier(Tier tier, Block... blocks)
	{
		TagAdapter.bind(Objects.requireNonNull(tier.getTag()), blocks);
	}
	
	public static void bindTool(MineableType tool, Tier tier, Block... blocks)
	{
		bindToolType(tool, blocks);
		bindToolTier(tier, blocks);
	}
	
	public record MineableType(@Nonnull TagKey<Block> blockTag)
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