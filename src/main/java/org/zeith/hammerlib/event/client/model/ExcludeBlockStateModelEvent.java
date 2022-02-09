package org.zeith.hammerlib.event.client.model;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
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