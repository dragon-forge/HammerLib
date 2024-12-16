package org.zeith.hammerlib.client.model.builtin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.QuadTransformers;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.model.IUnbakedGeometry;
import org.zeith.hammerlib.client.model.LoadUnbakedGeometry;

import java.util.List;

@LoadUnbakedGeometry(path = "emissive")
public class EmissiveGeometry
		implements IUnbakedGeometry
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
	public BakedModel bake(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties)
	{
		BakedModel res = baker.bake(parentLocation, modelState);
		return new EmissiveQuadApplierModel(res, QuadTransformers.settingEmissivity(emissivity));
	}
	
	@Override
	public void resolveDependencies(Resolver modelGetter)
	{
		if(parentLocation != null)
			parent = modelGetter.resolve(parentLocation);
		if(parent != null)
			parent.resolveDependencies(modelGetter);
	}
	
	public static class EmissiveQuadApplierModel
			extends DelegateBakedModel
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
		public List<BakedModel> getRenderPasses(ItemStack itemStack)
		{
			return List.of(this);
		}
	}
}