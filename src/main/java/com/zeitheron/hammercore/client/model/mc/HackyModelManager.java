package com.zeitheron.hammercore.client.model.mc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Failed experiment, I think.
 */
public class HackyModelManager
{
	private static List<Pair<ModelResourceLocation, ISimplifiedModel>> models = new ArrayList<>();
	
	public static void bind(ModelResourceLocation model, ISimplifiedModel imodel)
	{
		models.add(Pair.of(model, imodel));
	}
	
	@SubscribeEvent
	public void modelEvt(ModelBakeEvent evt)
	{
		for(Pair<ModelResourceLocation, ISimplifiedModel> mo : models)
			evt.getModelRegistry().putObject(mo.getKey(), new HackyBakedModel(mo.getValue()));
	}
}