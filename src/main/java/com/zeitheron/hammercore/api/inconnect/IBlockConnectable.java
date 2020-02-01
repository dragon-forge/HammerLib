package com.zeitheron.hammercore.api.inconnect;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.api.blocks.INoBlockstate;
import com.zeitheron.hammercore.api.inconnect.InConnectAPI.TextureAtlasSpritePartial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

public interface IBlockConnectable
		extends INoBlockstate
{
	/**
	 * Please represent texture path to your texture. <br>
	 * Example: <code>new ResourceLocation("minecraft", "blocks/bedrock")</code>
	 */
	@SideOnly(Side.CLIENT)
	ResourceLocation getTxMap();

	/**
	 * New way of representing multiple texture paths to the texture. Starting
	 * from zero. If your texture doesn't have specified texture, please return
	 * null to prevent texture duplication. <br>
	 * Example: <code>new ResourceLocation("minecraft", "blocks/bedrock")</code>
	 */
	default ResourceLocation getTx(int layer, @Nullable IBlockAccess world, BlockPos pos)
	{
		return layer == 0 ? getTxMap() : layer == 1 ? new ResourceLocation(getTxMap().getNamespace(), getTxMap().getPath() + "2") : null;
	}

	default Stream<ResourceLocation> getSprites()
	{
		Stream.Builder<ResourceLocation> builder = Stream.builder();
		int ls = getConnectTextureVersion().getLayers();
		for(int i = 0; i < ls; ++i)
			builder.add(getTx(i, null, BlockPos.ORIGIN));
		return builder.build().filter(Predicates.notNull());
	}

	/**
	 * Please represent texture sprite of your particle texture. <br>
	 * Example: <code>return getSprite("minecraft:blocks/bedrock")</code>
	 */
	@SideOnly(Side.CLIENT)
	default TextureAtlasSprite getParticleTexture(IBlockState state)
	{
		EnumConnTexVersion ctv = getConnectTextureVersion();
		if(ctv == EnumConnTexVersion.V1)
		{
			TextureAtlasSprite sprite = getSprite(Objects.toString(getTx(0, null, BlockPos.ORIGIN)));
			TextureAtlasSpritePartial partial = new TextureAtlasSpritePartial(sprite.getIconName(), 0.375F, 0.375F, 0.25F, 0.25F);
			partial.copyFrom(sprite);
		} else if(ctv == EnumConnTexVersion.V2)
		{
			TextureAtlasSprite sprite = getSprite(Objects.toString(getTx(0, null, BlockPos.ORIGIN)));
			TextureAtlasSpritePartial partial = new TextureAtlasSpritePartial(sprite.getIconName(), 0, 0, 0.25F, 0.25F);
			partial.copyFrom(sprite);
		} else if(ctv == EnumConnTexVersion.V3)
		{
			TextureAtlasSprite sprite = getSprite(Objects.toString(getTx(0, null, BlockPos.ORIGIN)));
			TextureAtlasSpritePartial partial = new TextureAtlasSpritePartial(sprite.getIconName(), 0.25F, 0.5F, 0.25F, 0.25F);
			partial.copyFrom(sprite);
		}
		return null;
	}

	AxisAlignedBB getBlockShape(IBlockAccess world, BlockPos pos, IBlockState state);

	default EnumConnTexVersion getConnectTextureVersion()
	{
		return EnumConnTexVersion.V1;
	}

	@SideOnly(Side.CLIENT)
	default TextureAtlasSprite getSpriteForFace(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing face, long rand, int layer)
	{
		return getSprite(getTx(layer, world, pos).toString());
	}

	@SideOnly(Side.CLIENT)
	static TextureAtlasSprite getSprite(String path)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(path);
	}
}