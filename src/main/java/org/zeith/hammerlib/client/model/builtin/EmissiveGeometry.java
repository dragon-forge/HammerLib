package org.zeith.hammerlib.client.model.builtin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.zeith.hammerlib.client.model.*;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@LoadUnbakedGeometry(path = "emissive")
public class EmissiveGeometry
		implements IUnbakedGeometry<EmissiveGeometry>
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
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
	{
		if(parent == null && parentLocation != null)
			parent = modelGetter.apply(parentLocation);
		else if(parent != null)
			parent.resolveParents(modelGetter);
	}
	
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
	{
		var res = parent.bake(baker, mat ->
				{
					var dTex = textures.getOrDefault(mat.texture(), mat.texture());
					if(dTex != null && !dTex.equals(mat.texture()))
						mat = new Material(mat.atlasLocation(), dTex);
					return spriteGetter.apply(mat);
				}, modelState, parentLocation != null ? parentLocation : modelLocation
		);
		return new QuadTransformingBakedModel(res, QuadTransformers.settingEmissivity(emissivity));
	}
}