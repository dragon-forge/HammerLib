package org.zeith.hammerlib.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DelegateBakedModel
		implements BakedModel
{
	protected final BakedModel delegate;
	
	public DelegateBakedModel(BakedModel delegate)
	{
		this.delegate = delegate;
	}
	
	@Override
	public ItemTransforms getTransforms()
	{
		return delegate.getTransforms();
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom)
	{
		return delegate.getQuads(pState, pDirection, pRandom);
	}
	
	@Override
	public boolean useAmbientOcclusion()
	{
		return delegate.useAmbientOcclusion();
	}
	
	@Override
	public boolean isGui3d()
	{
		return delegate.isGui3d();
	}
	
	@Override
	public boolean usesBlockLight()
	{
		return delegate.usesBlockLight();
	}
	
	@Override
	public boolean isCustomRenderer()
	{
		return delegate.isCustomRenderer();
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return delegate.getParticleIcon();
	}
	
	@Override
	public ItemOverrides getOverrides()
	{
		return delegate.getOverrides();
	}
	
	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)
	{
		return delegate.getQuads(state, side, rand, data, renderType);
	}
	
	@Override
	public boolean useAmbientOcclusion(BlockState state)
	{
		return delegate.useAmbientOcclusion(state);
	}
	
	@Override
	public boolean useAmbientOcclusion(BlockState state, RenderType renderType)
	{
		return delegate.useAmbientOcclusion(state, renderType);
	}
	
	@Override
	public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform)
	{
		delegate.applyTransform(transformType, poseStack, applyLeftHandTransform);
		return this;
	}
	
	@Override
	public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData)
	{
		return delegate.getModelData(level, pos, state, modelData);
	}
	
	@Override
	public TextureAtlasSprite getParticleIcon(@NotNull ModelData data)
	{
		return delegate.getParticleIcon(data);
	}
	
	@Override
	public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data)
	{
		return delegate.getRenderTypes(state, rand, data);
	}
	
	@Override
	public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous)
	{
		return delegate.getRenderTypes(itemStack, fabulous);
	}
	
	@Override
	public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous)
	{
		return delegate.getRenderPasses(itemStack, fabulous);
	}
}