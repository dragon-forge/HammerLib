package org.zeith.hammerlib.client.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public interface IUnbakedGeometry<T extends IUnbakedGeometry<T>>
		extends net.minecraftforge.client.model.geometry.IUnbakedGeometry<T>
{
	Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors);
}