package org.zeith.hammerlib.client.model.builtin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.QuadTransformers;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.model.*;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.HashMap;
import java.util.Map;

@LoadUnbakedGeometry(path = "emissive")
public class EmissiveGeometry
		implements IUnbakedGeometry
{
	protected ResourceLocation parentLocation;
	protected UnbakedModel parent;
	protected int emissivity;
	
	protected final Map<ResourceLocation, ResourceLocation> textures = new HashMap<>();
	
	public EmissiveGeometry(JsonObject obj, JsonDeserializationContext context)
	{
		if(obj.has("inline_parent") && obj.get("inline_parent").isJsonObject())
			parent = context.deserialize(obj.getAsJsonObject("inline_parent"), BlockModel.class);
		else
			parentLocation = ResourceLocation.tryParse(GsonHelper.getAsString(obj, "parent"));
		emissivity = GsonHelper.getAsInt(obj, "emissivity", 15);
		if(obj.has("textures"))
		{
			var arr = obj.getAsJsonObject("textures");
			for(String id : arr.keySet())
				textures.put(Resources.location(id), Resources.location(GsonHelper.getAsString(arr, id)));
		}
	}
	
	@Override
	public void resolveDependencies(Resolver modelGetter)
	{
		if(parentLocation != null)
			parent = modelGetter.resolve(parentLocation);
		else if(parent != null)
			parent.resolveDependencies(modelGetter);
	}
	
	@Override
	public void fillAdditionalProperties(ContextMap.Builder propertiesBuilder)
	{
		if(parent != null)
			parent.fillAdditionalProperties(propertiesBuilder);
	}
	
	@Override
	public @Nullable UnbakedModel getParent()
	{
		return parent;
	}
	
	@Override
	public @Nullable ItemTransforms getTransforms()
	{
		return parent.getTransforms();
	}
	
	@Override
	public TextureSlots.Data getTextureSlots()
	{
		return parent.getTextureSlots();
	}
	
	@Override
	public @Nullable Boolean getAmbientOcclusion()
	{
		return parent.getAmbientOcclusion();
	}
	
	@Override
	public @Nullable GuiLight getGuiLight()
	{
		return parent.getGuiLight();
	}
	
	@Override
	public BakedModel bake(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties)
	{
		BakedModel res = parent.bake(TextureSlotsHelper.replacing(textures, (id, mat) ->
						{
							if(mat == null) return null;
							var dTex = EmissiveGeometry.this.textures.getOrDefault(mat.texture(), mat.texture());
							if(dTex != null && !dTex.equals(mat.texture()))
								mat = new Material(mat.atlasLocation(), dTex);
							return mat;
						}
				), baker, modelState, useAmbientOcclusion, usesBlockLight, itemTransforms, additionalProperties
		);
		return new QuadTransformingBakedModel(res, QuadTransformers.settingEmissivity(emissivity));
	}
}