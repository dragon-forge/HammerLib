package org.zeith.hammerlib.client.model.builtin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.model.IUnbakedGeometry;
import org.zeith.hammerlib.client.model.LoadUnbakedGeometry;

import java.util.List;
import java.util.function.Function;

@LoadUnbakedGeometry(path = "emissive")
public class EmissiveGeometry
		implements IUnbakedGeometry<EmissiveGeometry>
{
	protected ResourceLocation parentLocation;
	protected UnbakedModel parent;
	
	public EmissiveGeometry(JsonObject obj, JsonDeserializationContext context)
	{
		parentLocation = ResourceLocation.tryParse(GsonHelper.getAsString(obj, "parent"));
	}
	
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides)
	{
		BakedModel res = baker.bake(parentLocation, modelState, spriteGetter);
		return res == null ? null : new BakedModelWrapper<>(res)
		{
			@Override
			public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand)
			{
				return QuadTransformers.settingMaxEmissivity().process(super.getQuads(state, side, rand));
			}
			
			@Override
			public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType)
			{
				return QuadTransformers.settingMaxEmissivity().process(super.getQuads(state, side, rand, extraData, renderType));
			}
		};
	}
	
	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
	{
		parent = modelGetter.apply(parentLocation);
	}
}