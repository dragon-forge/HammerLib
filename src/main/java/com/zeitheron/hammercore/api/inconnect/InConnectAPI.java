package com.zeitheron.hammercore.api.inconnect;

import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InConnectAPI
{
	public static final BiFunction<IBlockAccess, Pair<BlockPos, IBlockState>, IBlockState> extendedState = (w, p) -> p.getValue();
	
	public static IBlockState makeExtendedPositionedState(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return extendedState.apply(world, Pair.of(pos, state));
	}
	
	public static interface IPartialSprite
	{
	}
	
	@SideOnly(Side.CLIENT)
	public static class TextureAtlasSpritePartial extends TextureAtlasSprite implements IPartialSprite
	{
		float sx, sy, sw, sh;
		
		protected TextureAtlasSpritePartial(String spriteName, float subX, float subY, float subWidth, float subHeight)
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