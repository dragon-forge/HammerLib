package org.zeith.hammerlib.abstractions.recipes.layout;

import org.zeith.hammerlib.client.utils.UV;

public interface ISlotBuilder<B extends ISlotBuilder<B>>
		extends IIngredientReceiver<B>
{
	B setFluidRenderer(long capacity, boolean showCapacity, int width, int height);
	
	B setBackground(UV background, int xOffset, int yOffset);
	
	B setOverlay(UV overlay, int xOffset, int yOffset);
	
	B setName(String name);
	
	default void build()
	{
	}
	
	enum SlotRole
	{
		INPUT,
		OUTPUT,
		CATALYST,
		RENDER_ONLY;
	}
}