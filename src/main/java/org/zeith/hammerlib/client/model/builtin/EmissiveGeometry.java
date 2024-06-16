package org.zeith.hammerlib.client.model.builtin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.*;
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
	protected int emissivity;
	
	public EmissiveGeometry(JsonObject obj, JsonDeserializationContext context)
	{
		if(obj.has("inline_parent") && obj.get("inline_parent").isJsonObject())
			parent = context.deserialize(obj.getAsJsonObject("inline_parent"), BlockModel.class);
		else
			parentLocation = ResourceLocation.tryParse(GsonHelper.getAsString(obj, "parent"));
		emissivity = GsonHelper.getAsInt(obj, "emissivity", 15);
	}
	
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides)
	{
		BakedModel res = parent != null && parentLocation == null
						 ? baker.bakeUncached(parent, modelState, spriteGetter)
						 : baker.bake(parentLocation, modelState, spriteGetter);
		return res == null ? null : new EmissiveQuadApplierModel(res, QuadTransformers.settingEmissivity(emissivity));
	}
	
	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
	{
		if(parentLocation != null)
			parent = modelGetter.apply(parentLocation);
		if(parent != null)
			parent.resolveParents(modelGetter);
	}
	
	public static class EmissiveQuadApplierModel
			extends BakedModelWrapper<BakedModel>
	{
		protected final IQuadTransformer transformer;
		
		public EmissiveQuadApplierModel(BakedModel originalModel, IQuadTransformer transformer)
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
		public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform)
		{
			var bm = super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
			if(bm == originalModel) return this;
			return new EmissiveQuadApplierModel(bm, transformer);
		}
		
		@Override
		public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous)
		{
			return List.of(this);
		}
	}
}