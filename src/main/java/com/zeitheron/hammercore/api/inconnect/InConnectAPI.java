package com.zeitheron.hammercore.api.inconnect;

import com.zeitheron.hammercore.utils.PositionedStateImplementation;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiFunction;

public class InConnectAPI
{
	public static final BiFunction<IBlockAccess, Pair<BlockPos, IBlockState>, IBlockState> extendedState = (world, p) ->
	{
		BlockPos pos = p.getKey();
		IBlockState state = p.getValue();
		if(state instanceof BlockStateContainer.StateImplementation)
			return PositionedStateImplementation.convert((BlockStateContainer.StateImplementation) state).withPos(pos, world);
		return p.getValue();
	};

	public static IBlockState makeExtendedPositionedState(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return extendedState.apply(world, Pair.of(pos, state));
	}

	public interface IPartialSprite
	{
	}

	@SideOnly(Side.CLIENT)
	public static TextureAtlasSpritePartial partial(TextureAtlasSprite sprite, float subX, float subY, float subWidth, float subHeight)
	{
		TextureAtlasSpritePartial partial = new TextureAtlasSpritePartial(sprite.getIconName(), subX, subY, subWidth, subHeight);
		partial.copyFrom(sprite);
		return partial;
	}

	@SideOnly(Side.CLIENT)
	public static class TextureAtlasSpritePartial
			extends TextureAtlasSprite
			implements IPartialSprite
	{
		float sx, sy, sw, sh;

		public TextureAtlasSpritePartial(String spriteName, float subX, float subY, float subWidth, float subHeight)
		{
			super(spriteName);
			this.sx = subX;
			this.sy = subY;
			this.sw = subWidth;
			this.sh = subHeight;
		}

		@Override
		public float getMinU()
		{
			float d = super.getMaxU() - super.getMinU();
			return super.getMinU() + d * sx;
		}

		@Override
		public float getMinV()
		{
			float d = super.getMaxV() - super.getMinV();
			return super.getMinV() + d * sy;
		}

		@Override
		public float getMaxU()
		{
			float d = super.getMaxU() - super.getMinU();
			return getMinU() + d * sw;
		}

		@Override
		public float getMaxV()
		{
			float d = super.getMaxV() - super.getMinV();
			return getMinV() + d * sh;
		}

		@Override
		public float getInterpolatedU(double u)
		{
			float f = this.getMaxU() - this.getMinU();
			return this.getMinU() + f * (float) u / 16.0F;
		}

		@Override
		public float getInterpolatedV(double v)
		{
			float f = this.getMaxV() - this.getMinV();
			return this.getMinV() + f * (float) v / 16.0F;
		}
	}
}