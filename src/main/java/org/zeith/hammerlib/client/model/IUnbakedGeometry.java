package org.zeith.hammerlib.client.model;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.neoforged.neoforge.client.model.ExtendedUnbakedModel;

import java.util.List;

public interface IUnbakedGeometry
		extends ExtendedUnbakedModel
{
	static List<BakedQuad> bakeFace(BlockElement el, SpriteGetter spriteGetter, TextureSlots slots, ModelState modelState)
	{
		return el.faces.entrySet().stream()
				.map(ent ->
				{
					BlockElementFace face = ent.getValue();
					TextureAtlasSprite sprite = IUnbakedGeometry.findSprite(spriteGetter, slots, face.texture());
					return SimpleBakedModel.bakeFace(el, face, sprite, ent.getKey(), modelState);
				})
				.toList();
	}
	
	static TextureAtlasSprite findSprite(SpriteGetter spriteGetter, TextureSlots slots, String texture)
	{
		Material material = slots.getMaterial(texture);
		return material != null ? spriteGetter.get(material) : spriteGetter.reportMissingReference(texture);
	}
}