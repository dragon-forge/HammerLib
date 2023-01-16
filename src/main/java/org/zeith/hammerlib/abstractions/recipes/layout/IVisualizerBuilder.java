package org.zeith.hammerlib.abstractions.recipes.layout;

public interface IVisualizerBuilder
{
	ISlotBuilder<?> addSlot(ISlotBuilder.SlotRole role, int x, int y);
}