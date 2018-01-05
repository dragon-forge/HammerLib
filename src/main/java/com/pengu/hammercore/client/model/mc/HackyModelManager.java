package com.pengu.hammercore.client.model.mc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HackyModelManager
{
	private static List<Pair<ModelResourceLocation, iSimplifiedModel>> models = new ArrayList<>();
	
	public static void bind(ModelResourceLocation model, iSimplifiedModel imodel)
	{
		models.add(Pair.of(model, imodel));
	}
	
	@SubscribeEvent
	public void modelEvt(ModelBakeEvent evt)
	{
		for(Pair<ModelResourceLocation, iSimplifiedModel> mo : models)
			evt.getModelRegistry().putObject(mo.getKey(), new HackyBakedModel(mo.getValue()));
	}
}