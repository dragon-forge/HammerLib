package org.zeith.api.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.numbers.NumberFormatType;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for mapping registry types to their corresponding Forge registry and vice versa.
 * Provides methods for reporting registry mappings, getting the registry for a given registry type,
 * getting the registry type for a given registry, and checking if a registry is non-intrusive.
 */
public class RegistryMapping
{
	private static final BiMap<Class<?>, Registry<?>> REG_BY_TYPE = HashBiMap.create();
	private static final BiMap<ResourceKey<?>, Class<?>> TYPE_BY_REG = HashBiMap.create();
	private static final Set<ResourceKey<?>> NON_INTRUSIVE_REGISTRIES = new HashSet<>();
	
	static
	{
		report(Block.class, BuiltInRegistries.BLOCK);
		report(Fluid.class, BuiltInRegistries.FLUID);
		report(Item.class, BuiltInRegistries.ITEM);
		report(MobEffect.class, BuiltInRegistries.MOB_EFFECT, false);
		report(SoundEvent.class, BuiltInRegistries.SOUND_EVENT, false);
		report(Potion.class, BuiltInRegistries.POTION, false);
		report(Attribute.class, BuiltInRegistries.ATTRIBUTE, false);
		report(VillagerType.class, BuiltInRegistries.VILLAGER_TYPE, false);
		report(VillagerProfession.class, BuiltInRegistries.VILLAGER_PROFESSION, false);
		report(PoiType.class, BuiltInRegistries.POINT_OF_INTEREST_TYPE, false);
		report(Schedule.class, BuiltInRegistries.SCHEDULE, false);
		report(Activity.class, BuiltInRegistries.ACTIVITY, false);
		report(ChunkStatus.class, BuiltInRegistries.CHUNK_STATUS, false);
		report(ArmorMaterial.class, BuiltInRegistries.ARMOR_MATERIAL, false);
		report(Instrument.class, BuiltInRegistries.INSTRUMENT, false);
		report(CatVariant.class, BuiltInRegistries.CAT_VARIANT, false);
		report(FrogVariant.class, BuiltInRegistries.FROG_VARIANT, false);
		reportRaw(MapDecorationType.class, BuiltInRegistries.MAP_DECORATION_TYPE);
		reportRaw(StructurePieceType.class, BuiltInRegistries.STRUCTURE_PIECE);
		report(LootItemConditionType.class, BuiltInRegistries.LOOT_CONDITION_TYPE, false);
		
		// NeoForge stuff here.
		report(FluidType.class, NeoForgeRegistries.FLUID_TYPES, false);
		report(HolderSetType.class, NeoForgeRegistries.HOLDER_SET_TYPES, false);
		
		// NF generics
		reportRaw(AttachmentType.class, NeoForgeRegistries.ATTACHMENT_TYPES, false);
		reportRaw(FluidIngredientType.class, NeoForgeRegistries.FLUID_INGREDIENT_TYPES, false);
		reportRaw(IngredientType.class, NeoForgeRegistries.INGREDIENT_TYPES, false);
		reportRaw(EntityDataSerializer.class, NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, false);
		
		// Generics.
		reportRaw(EntityType.class, BuiltInRegistries.ENTITY_TYPE);
		reportRaw(BlockEntityType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE);
		reportRaw(ParticleType.class, BuiltInRegistries.PARTICLE_TYPE, false);
		reportRaw(MenuType.class, BuiltInRegistries.MENU, false);
		reportRaw(RecipeType.class, BuiltInRegistries.RECIPE_TYPE, false);
		reportRaw(RecipeSerializer.class, BuiltInRegistries.RECIPE_SERIALIZER, false);
		reportRaw(StatType.class, BuiltInRegistries.STAT_TYPE, false);
		reportRaw(MemoryModuleType.class, BuiltInRegistries.MEMORY_MODULE_TYPE, false);
		reportRaw(SensorType.class, BuiltInRegistries.SENSOR_TYPE, false);
		reportRaw(WorldCarver.class, BuiltInRegistries.CARVER, false);
		reportRaw(Feature.class, BuiltInRegistries.FEATURE, false);
		reportRaw(StructureType.class, BuiltInRegistries.STRUCTURE_TYPE, false);
		reportRaw(BlockStateProviderType.class, BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE, false);
		reportRaw(PlacementModifierType.class, BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, false);
		reportRaw(FoliagePlacerType.class, BuiltInRegistries.FOLIAGE_PLACER_TYPE, false);
		reportRaw(TrunkPlacerType.class, BuiltInRegistries.TRUNK_PLACER_TYPE, false);
		reportRaw(TreeDecoratorType.class, BuiltInRegistries.TREE_DECORATOR_TYPE, false);
		reportRaw(RootPlacerType.class, BuiltInRegistries.ROOT_PLACER_TYPE, false);
		reportRaw(FeatureSizeType.class, BuiltInRegistries.FEATURE_SIZE_TYPE, false);
		reportRaw(ArgumentTypeInfo.class, BuiltInRegistries.COMMAND_ARGUMENT_TYPE, false);
		reportRaw(CriterionTrigger.class, BuiltInRegistries.TRIGGER_TYPES, false);
		reportRaw(NumberFormatType.class, BuiltInRegistries.NUMBER_FORMAT_TYPE, false);
		reportRaw(StructureProcessorType.class, BuiltInRegistries.STRUCTURE_PROCESSOR, false);
		reportRaw(StructurePoolElementType.class, BuiltInRegistries.STRUCTURE_POOL_ELEMENT, false);
		reportRaw(StructurePlacementType.class, BuiltInRegistries.STRUCTURE_PLACEMENT, false);
		reportRaw(ItemSubPredicate.Type.class, BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE, false);
	}
	
	/**
	 * Allows marking any registry as non-intrusive, allowing @{@link org.zeith.hammerlib.annotations.OnlyIf} to be applicable on constant fields of a registry.
	 */
	public static synchronized <T> void markRegistryAsNonIntrusive(ResourceKey<? extends Registry<T>> registryKey)
	{
		NON_INTRUSIVE_REGISTRIES.add(registryKey);
	}
	
	/**
	 * Reports the mapping of a registry type to a Forge registry.
	 *
	 * @param base
	 * 		registry type to be mapped
	 * @param registry
	 * 		Forge registry to be mapped
	 * @param <T>
	 * 		type of registry
	 */
	public static synchronized <T> void report(Class<? super T> base, Registry<T> registry)
	{
		REG_BY_TYPE.put(base, registry);
		TYPE_BY_REG.put(registry.key(), base);
	}
	
	public static synchronized <T> void report(Class<T> base, Registry<T> registry, boolean intrusive)
	{
		REG_BY_TYPE.put(base, registry);
		TYPE_BY_REG.put(registry.key(), base);
		if(!intrusive) markRegistryAsNonIntrusive(registry.key());
	}
	
	public static synchronized void reportRaw(Class base, Registry registry)
	{
		REG_BY_TYPE.put(base, registry);
		TYPE_BY_REG.put(registry.key(), base);
	}
	
	public static synchronized void reportRaw(Class base, Registry registry, boolean intrusive)
	{
		REG_BY_TYPE.put(base, registry);
		TYPE_BY_REG.put(registry.key(), base);
		if(!intrusive) markRegistryAsNonIntrusive(registry.key());
	}
	
	/**
	 * Returns the registry type for the provided Game registry.
	 *
	 * @param registry
	 * 		Game registry to get the registry type for
	 * @param <T>
	 * 		type of registry
	 *
	 * @return registry type for the provided Forge registry
	 */
	public static <T> Class<T> getSuperType(Registry<T> registry)
	{
		if(registry == null)
			return null;
		return getSuperType(registry.key());
	}
	
	public static <T> Class<T> getSuperType(ResourceKey<? extends Registry<?>> registry)
	{
		if(registry == null)
			return null;
		return Cast.cast(TYPE_BY_REG.get(registry));
	}
	
	public static <T> Registry<T> getRegistryByType(Class<T> registry)
	{
		if(registry == null)
			return null;
		return Cast.cast(REG_BY_TYPE.get(registry));
	}
	
	/**
	 * Checks if the provided Forge registry is non-intrusive, meaning it does not have a corresponding registry type.
	 *
	 * @param registry
	 * 		Forge registry to check if it is non-intrusive
	 *
	 * @return true if the provided Forge registry is non-intrusive, false otherwise
	 */
	public static boolean isNonIntrusive(ResourceKey<? extends Registry<?>> registry)
	{
		return registry != null && NON_INTRUSIVE_REGISTRIES.contains(registry);
	}
	
	private static final Map<ResourceKey<? extends Registry<?>>, Codec<?>> REGISTRY_CODECS = new ConcurrentHashMap<>();
	
	public static <T> Codec<T> registryCodec(ResourceKey<? extends Registry<T>> key)
	{
		return Cast.cast(REGISTRY_CODECS.computeIfAbsent(key, regKey -> createRegistryCodec(key)));
	}
	
	private static <T> Codec<T> createRegistryCodec(ResourceKey<? extends Registry<T>> key)
	{
		return ResourceLocation.CODEC
				.flatXmap(
						id ->
						{
							Registry<T> registry = BuiltInRegistries.REGISTRY.get((ResourceKey) key);
							return Optional.ofNullable(registry.get(id))
									.map(DataResult::success)
									.orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + key + ": " + id));
						},
						obj ->
						{
							Registry<T> registry = BuiltInRegistries.REGISTRY.get((ResourceKey) key);
							return registry.getResourceKey(obj)
									.map(ResourceKey::location)
									.map(DataResult::success)
									.orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + key + ":" + obj));
						}
				);
	}
}