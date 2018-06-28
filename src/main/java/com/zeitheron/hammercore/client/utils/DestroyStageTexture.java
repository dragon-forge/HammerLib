package com.zeitheron.hammercore.client.utils;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class DestroyStageTexture
{
	private static final ResourceLocation[] DESTROY_STAGES_SPRITED = new ResourceLocation[] { new ResourceLocation("blocks/destroy_stage_0"), new ResourceLocation("blocks/destroy_stage_1"), new ResourceLocation("blocks/destroy_stage_2"), new ResourceLocation("blocks/destroy_stage_3"), new ResourceLocation("blocks/destroy_stage_4"), new ResourceLocation("blocks/destroy_stage_5"), new ResourceLocation("blocks/destroy_stage_6"), new ResourceLocation("blocks/destroy_stage_7"), new ResourceLocation("blocks/destroy_stage_8"), new ResourceLocation("blocks/destroy_stage_9") };
	private static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[] { new ResourceLocation("hammercore", "textures/models/destroy_stage_0.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_1.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_2.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_3.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_4.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_5.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_6.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_7.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_8.png"), new ResourceLocation("hammercore", "textures/models/destroy_stage_9.png") };
	
	@Nullable
	public static ResourceLocation getByProgress(float progress)
	{
		if(progress == 0)
			return null;
		return DESTROY_STAGES[Math.round(progress * (DESTROY_STAGES.length - 1))];
	}
	
	@Nullable
	public static TextureAtlasSprite getAsSprite(float progress)
	{
		if(progress == 0)
			return null;
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(DESTROY_STAGES_SPRITED[Math.round(progress * (DESTROY_STAGES_SPRITED.length - 1))].toString());
	}
}