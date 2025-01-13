package org.zeith.hammerlib.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuadTransformingBakedModel
		extends DelegateBakedModel
{
	protected final IQuadTransformer transformer;
	
	public QuadTransformingBakedModel(BakedModel originalModel, IQuadTransformer transformer)
	{
		super(originalModel);
		this.transformer = transformer;
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand)
	{
		return transformer.process(super.getQuads(state, side, rand));
	}
	
	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType)
	{
		return transformer.process(super.getQuads(state, side, rand, extraData, renderType));
	}
	
	@Override
	public List<BakedModel> getRenderPasses(ItemStack itemStack)
	{
		return List.of(this);
	}
}
