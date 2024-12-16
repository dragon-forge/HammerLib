package org.zeith.hammerlib.core.adapter;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagAdapter
{
	static final Object2FloatOpenHashMap<ToolMaterial> MATERIAL_TIERS = new Object2FloatOpenHashMap<>();
	static final List<SortedToolMaterial> SORTED_MATERIALS = new ArrayList<>();
	static final Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, Set<?>>> staticTags = new ConcurrentHashMap<>();
	static final Map<Block, ToolMaterial> minMiningTiers = new HashMap<>();
	
	static
	{
		HammerLib.EVENT_BUS.addListener(TagAdapter::applyTags);
		registerToolMaterial(1F, ToolMaterial.WOOD);
		registerToolMaterial(1F, ToolMaterial.GOLD);
		registerToolMaterial(2F, ToolMaterial.STONE);
		registerToolMaterial(3F, ToolMaterial.IRON);
		registerToolMaterial(4F, ToolMaterial.DIAMOND);
		registerToolMaterial(5F, ToolMaterial.NETHERITE);
	}
	
	public static ToolMaterial registerToolMaterial(float order, ToolMaterial material)
	{
		if(!Float.isFinite(order)) throw new IllegalArgumentException("Can not use non-finite numbers for sorting tool materials!");
		
		// Was already added!
		if(MATERIAL_TIERS.containsKey(material)) return material;
		MATERIAL_TIERS.put(material, order);
		SORTED_MATERIALS.add(new SortedToolMaterial(material, order));
		SORTED_MATERIALS.sort(Comparator.comparingDouble(SortedToolMaterial::sortOrder));
		return material;
	}
	
	public static Stream<SortedToolMaterial> getAllWorseMaterials(ToolMaterial material)
	{
		float order = MATERIAL_TIERS.getOrDefault(material, Float.NaN);
		if(!Float.isFinite(order)) return Stream.empty();
		return SORTED_MATERIALS.stream().takeWhile(s -> s.sortOrder() < order);
	}
	
	private static <T> Map<TagKey<T>, Set<T>> getTagsFor(ResourceKey<? extends Registry<T>> registry)
	{
		return Cast.cast(staticTags.computeIfAbsent(registry, r -> new ConcurrentHashMap<>()));
	}
	
	public static synchronized <T> void bind(TagKey<T> tag, T... values)
	{
		var tags = getTagsFor(tag.registry());
		tags.computeIfAbsent(tag, b -> new HashSet<>()).addAll(List.of(values));
	}
	
	public static synchronized void bindTier(ToolMaterial material, Block... values)
	{
		for(Block value : values)
		{
			minMiningTiers.put(value, material);
		}
	}
	
	@SuppressWarnings({
			"rawtypes",
			"Convert2MethodRef"
	})
	public static void applyTags(BuildTagsEvent evt)
	{
		Map<TagKey, Set> tags = getTagsFor(evt.reg.key());
		tags.forEach((tag, values) -> evt.addAllToTag(tag, values));
		
		if(Registries.BLOCK.equals(evt.reg.key()))
		{
			Map<ToolMaterial, Set<TagKey<Block>>> cachedTierData = new HashMap<>();
			for(Map.Entry<Block, ToolMaterial> entry : minMiningTiers.entrySet())
			{
				var b = entry.getKey();
				for(TagKey<Block> denyDrop : cachedTierData.computeIfAbsent(entry.getValue(), tm -> getAllWorseMaterials(tm).map(SortedToolMaterial::material).map(ToolMaterial::incorrectBlocksForDrops).collect(Collectors.toSet())))
					evt.addToTag(denyDrop, b);
			}
		}
	}
	
	public record SortedToolMaterial(ToolMaterial material, float sortOrder)
	{
	}
}