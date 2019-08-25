package com.zeitheron.hammercore.api.inconnect;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBlockConnectable
{
	/**
	 * Please represent texture path to your texture. <br>
	 * Example: <code>new ResourceLocation("minecraft", "blocks/bedrock")</code>
	 */
	@SideOnly(Side.CLIENT)
	ResourceLocation getTxMap();
	
	/**
	 * Please represent texture sprite of your particle texture. <br>
	 * Example: <code>return getSprite("minecraft:blocks/bedrock")</code>
	 */
	@SideOnly(Side.CLIENT)
	default TextureAtlasSprite getParticleTexture(IBlockState state)
	{
		return null;
	}
	
	AxisAlignedBB getBlockShape(IBlockAccess world, BlockPos pos, IBlockState state);
	
	default EnumConnTexVersion getConnectTextureVersion()
	{
		return EnumConnTexVersion.V1;
	}
	
	@SideOnly(Side.CLIENT)
	static TextureAtlasSprite getSprite(String path)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(path);
	}
}