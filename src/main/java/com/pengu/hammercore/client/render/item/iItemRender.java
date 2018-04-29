package com.pengu.hammercore.client.render.item;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface iItemRender
{
	void renderItem(ItemStack item);
	
	default void renderItem(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform)
	{
		renderItem(stack);
	}
}