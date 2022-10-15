package org.zeith.hammerlib.client.model;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IBakedModel
		extends BakedModel
{
	ItemTransforms BLOCK_TRANSFORMS = new ItemTransforms(
			new ItemTransform(new Vector3f(75, 45, 0), new Vector3f(0, 0.15625F, 0), new Vector3f(0.375F, 0.375F, 0.375F)), // thirdPersonLeftHand
			new ItemTransform(new Vector3f(75, 45, 0), new Vector3f(0, 0.15625F, 0), new Vector3f(0.375F, 0.375F, 0.375F)), // thirdPersonRightHand
			new ItemTransform(new Vector3f(0, 45, 0), new Vector3f(), new Vector3f(0.4F, 0.4F, 0.4F)), // firstPersonLeftHand
			new ItemTransform(new Vector3f(0, 225, 0), new Vector3f(), new Vector3f(0.4F, 0.4F, 0.4F)), // firstPersonRightHand
			ItemTransform.NO_TRANSFORM, // head
			new ItemTransform(new Vector3f(30, 225, 0), new Vector3f(), new Vector3f(0.625F, 0.625F, 0.625F)), // gui
			new ItemTransform(new Vector3f(), new Vector3f(0, 0.1875F, 0), new Vector3f(0.25F, 0.25F, 0.25F)), // ground
			new ItemTransform(new Vector3f(), new Vector3f(), new Vector3f(0.5F, 0.5F, 0.5F)), // fixed
			ImmutableMap.of()
	);
	
	@Override
	@NotNull
	List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType);
	
	default boolean useBlockTransforms()
	{
		return usesBlockLight();
	}
	
	@Override
	default List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand)
	{
		return getQuads(state, side, rand, ModelData.EMPTY, RenderType.solid());
	}
	
	@Override
	default ItemTransforms getTransforms()
	{
		return useBlockTransforms() ? BLOCK_TRANSFORMS : BakedModel.super.getTransforms();
	}
	
	@Override
	default ItemOverrides getOverrides()
	{
		return ItemOverrides.EMPTY;
	}
}