package org.zeith.hammerlib.compat.jei.absimpl;

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.zeith.hammerlib.abstractions.recipes.layout.IIngredientReceiver;
import org.zeith.hammerlib.abstractions.recipes.layout.ISlotBuilder;
import org.zeith.hammerlib.client.utils.UV;

public class SlotBuilderJEI
		extends IngredientReceiverJEI<SlotBuilderJEI>
		implements ISlotBuilder<SlotBuilderJEI>, IIngredientReceiver<SlotBuilderJEI>
{
	final IRecipeSlotBuilder slot;
	
	public SlotBuilderJEI(IRecipeSlotBuilder slot)
	{
		super(slot);
		this.slot = slot;
	}
	
	@Override
	public SlotBuilderJEI setFluidRenderer(long capacity, boolean showCapacity, int width, int height)
	{
		slot.setFluidRenderer(capacity, showCapacity, width, height);
		return this;
	}
	
	@Override
	public SlotBuilderJEI setBackground(UV background, int xOffset, int yOffset)
	{
		slot.setBackground(VisualizerBuilderJEI.fromUV(background), xOffset, yOffset);
		return this;
	}
	
	@Override
	public SlotBuilderJEI setOverlay(UV overlay, int xOffset, int yOffset)
	{
		slot.setOverlay(VisualizerBuilderJEI.fromUV(overlay), xOffset, yOffset);
		return this;
	}
	
	@Override
	public SlotBuilderJEI setName(String name)
	{
		slot.setSlotName(name);
		return this;
	}
	
	static RecipeIngredientRole fromSlotRole(SlotRole role)
	{
		return switch(role)
				{
					case INPUT -> RecipeIngredientRole.INPUT;
					case OUTPUT -> RecipeIngredientRole.OUTPUT;
					case CATALYST -> RecipeIngredientRole.CATALYST;
					case RENDER_ONLY -> RecipeIngredientRole.RENDER_ONLY;
				};
	}
}