package org.zeith.hammerlib.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.stream.Stream;

public interface IGroupedQuadModel
{
	Stream<BakedQuad> getGroupedUnculledQuads(String group);
}