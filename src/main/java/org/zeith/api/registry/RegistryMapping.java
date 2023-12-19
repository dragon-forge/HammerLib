package org.zeith.api.registry;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
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
		report(MobEffect.class, BuiltInRegistries.MOB_EFFECT);
		report(SoundEvent.class, BuiltInRegistries.SOUND_EVENT);
		report(Potion.class, BuiltInRegistries.POTION);
		report(Enchantment.class, BuiltInRegistries.ENCHANTMENT);
		report(PaintingVariant.class, BuiltInRegistries.PAINTING_VARIANT);
		report(Attribute.class, BuiltInRegistries.ATTRIBUTE);
		report(VillagerProfession.class, BuiltInRegistries.VILLAGER_PROFESSION);
		report(PoiType.class, BuiltInRegistries.POINT_OF_INTEREST_TYPE);
		report(Schedule.class, BuiltInRegistries.SCHEDULE);
		report(Activity.class, BuiltInRegistries.ACTIVITY);
		report(ChunkStatus.class, BuiltInRegistries.CHUNK_STATUS);
//		report(Biome.class, BuiltInRegistries.BIOME);
		
		// Forge is dumb, yes.
		report(FluidType.class, NeoForgeRegistries.FLUID_TYPES);
		
		// Generics.
		reportRaw(EntityType.class, BuiltInRegistries.ENTITY_TYPE);
		reportRaw(BlockEntityType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE);
		reportRaw(ParticleType.class, BuiltInRegistries.PARTICLE_TYPE);
		reportRaw(MenuType.class, BuiltInRegistries.MENU);
		reportRaw(RecipeType.class, BuiltInRegistries.RECIPE_TYPE);
		reportRaw(RecipeSerializer.class, BuiltInRegistries.RECIPE_SERIALIZER);
		reportRaw(StatType.class, BuiltInRegistries.STAT_TYPE);
		reportRaw(MemoryModuleType.class, BuiltInRegistries.MEMORY_MODULE_TYPE);
		reportRaw(SensorType.class, BuiltInRegistries.SENSOR_TYPE);
		reportRaw(WorldCarver.class, BuiltInRegistries.CARVER);
		reportRaw(Feature.class, BuiltInRegistries.FEATURE);
		reportRaw(BlockStateProviderType.class, BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE);
		reportRaw(FoliagePlacerType.class, BuiltInRegistries.FOLIAGE_PLACER_TYPE);
		reportRaw(TreeDecoratorType.class, BuiltInRegistries.TREE_DECORATOR_TYPE);
		reportRaw(ArgumentTypeInfo.class, BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
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
	public static synchronized <T> void report(Class<T> base, Registry<T> registry)
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