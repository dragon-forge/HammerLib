package org.zeith.hammerlib.compat.jei.absimpl;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import org.zeith.hammerlib.abstractions.recipes.layout.ISlotBuilder;
import org.zeith.hammerlib.abstractions.recipes.layout.IVisualizerBuilder;
import org.zeith.hammerlib.client.utils.UV;

public class VisualizerBuilderJEI
		implements IVisualizerBuilder
{
	final IRecipeLayoutBuilder builder;
	
	public VisualizerBuilderJEI(IRecipeLayoutBuilder builder)
	{
		this.builder = builder;
	}
	
	@Override
	public ISlotBuilder<?> addSlot(ISlotBuilder.SlotRole role, int x, int y)
	{
		return new SlotBuilderJEI(builder.addSlot(SlotBuilderJEI.fromSlotRole(role), x, y));
	}
	
	public static IDrawable fromUV(UV uv)
	{
		return new IDrawable()
		{
			@Override
			public int getWidth()
			{
				return (int) uv.width;
			}
			
			@Override
			public int getHeight()
			{
				return (int) uv.height;
			}
			
			@Override
			public void draw(PoseStack poseStack, int x, int y)
			{
				uv.render(poseStack, x, y);
			}
		};
	}
}