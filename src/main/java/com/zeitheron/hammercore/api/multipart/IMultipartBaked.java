package com.zeitheron.hammercore.api.multipart;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IMultipartBaked
{
	default boolean acceptsFacing(EnumFacing facing)
	{
		return facing != null;
	}

	@SideOnly(Side.CLIENT)
	default void generateBakedQuads(Consumer<BakedQuad> quadConsumer, Function<String, TextureAtlasSprite> spriteFunction, FaceBakery bakery, @Nullable EnumFacing side, long rand)
	{
	}
}