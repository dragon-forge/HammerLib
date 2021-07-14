package org.zeith.hammerlib.event.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class ExcludeBlockStateModelEvent
		extends Event
{
	final ModelBakery bakery;
	final Consumer<ModelResourceLocation> add;

	public ExcludeBlockStateModelEvent(ModelBakery bakery, Consumer<ModelResourceLocation> add)
	{
		this.bakery = bakery;
		this.add = add;
	}

	public ModelBakery getModelBakery()
	{
		return bakery;
	}

	public void add(BlockState state)
	{

	}

	public void add(ModelResourceLocation mrl)
	{
		add.accept(mrl);
	}
}