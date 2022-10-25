package org.zeith.api.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.zeith.hammerlib.util.java.Cast;

import java.util.HashSet;
import java.util.Set;

public class RegistryMapping
{
	private static final BiMap<Class<?>, IForgeRegistry<?>> REG_BY_TYPE = HashBiMap.create();
	private static final BiMap<ResourceKey<?>, Class<?>> TYPE_BY_REG = HashBiMap.create();
	private static final Set<ResourceKey<?>> NON_INTRUSIVE_REGISTRIES = new HashSet<>();
	
	static
	{
		report(Block.class, ForgeRegistries.BLOCKS);
		report(Fluid.class, ForgeRegistries.FLUIDS);
		report(Item.class, ForgeRegistries.ITEMS);
		report(MobEffect.class, ForgeRegistries.MOB_EFFECTS);
		report(SoundEvent.class, ForgeRegistries.SOUND_EVENTS);
		report(Potion.class, ForgeRegistries.POTIONS);
		report(Enchantment.class, ForgeRegistries.ENCHANTMENTS);
		report(PaintingVariant.class, ForgeRegistries.PAINTING_VARIANTS);
		report(Attribute.class, ForgeRegistries.ATTRIBUTES);
		report(VillagerProfession.class, ForgeRegistries.VILLAGER_PROFESSIONS);
		report(PoiType.class, ForgeRegistries.POI_TYPES);
		report(Schedule.class, ForgeRegistries.SCHEDULES);
		report(Activity.class, ForgeRegistries.ACTIVITIES);
		report(ChunkStatus.class, ForgeRegistries.CHUNK_STATUS);
		report(Biome.class, ForgeRegistries.BIOMES);
		
		// Generics.
		reportRaw(EntityType.class, ForgeRegistries.ENTITY_TYPES);
		reportRaw(BlockEntityType.class, ForgeRegistries.BLOCK_ENTITY_TYPES);
		reportRaw(ParticleType.class, ForgeRegistries.PARTICLE_TYPES);
		reportRaw(MenuType.class, ForgeRegistries.MENU_TYPES);
		reportRaw(RecipeType.class, ForgeRegistries.RECIPE_TYPES);
		reportRaw(RecipeSerializer.class, ForgeRegistries.RECIPE_SERIALIZERS);
		reportRaw(StatType.class, ForgeRegistries.STAT_TYPES);
		reportRaw(MemoryModuleType.class, ForgeRegistries.MEMORY_MODULE_TYPES);
		reportRaw(SensorType.class, ForgeRegistries.SENSOR_TYPES);
		reportRaw(WorldCarver.class, ForgeRegistries.WORLD_CARVERS);
		reportRaw(Feature.class, ForgeRegistries.FEATURES);
		reportRaw(BlockStateProviderType.class, ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES);
		reportRaw(FoliagePlacerType.class, ForgeRegistries.FOLIAGE_PLACER_TYPES);
		reportRaw(TreeDecoratorType.class, ForgeRegistries.TREE_DECORATOR_TYPES);
		reportRaw(ArgumentTypeInfo.class, ForgeRegistries.COMMAND_ARGUMENT_TYPES);
	}
	
	/**
	 * Allows marking any registry as non-intrusive, allowing @{@link org.zeith.hammerlib.annotations.OnlyIf} to be applicable on constant fields of a registry.
	 */
	public static synchronized <T> void markRegistryAsNonIntrusive(ResourceKey<Registry<T>> registryKey)
	{
		NON_INTRUSIVE_REGISTRIES.add(registryKey);
	}
	
	public static synchronized <T> void report(Class<T> base, IForgeRegistry<T> registry)
	{
		REG_BY_TYPE.put(base, registry);
		TYPE_BY_REG.put(registry.getRegistryKey(), base);
	}
	
	public static synchronized <T> void report(Class<T> base, IForgeRegistry<T> registry, boolean intrusive)
	{
		REG_BY_TYPE.put(base, registry);
		TYPE_BY_REG.put(registry.getRegistryKey(), base);
		if(!intrusive) markRegistryAsNonIntrusive(registry.getRegistryKey());
	}
	
	public static synchronized void reportRaw(Class base, IForgeRegistry registry)
	{
		REG_BY_TYPE.put(base, registry);
		TYPE_BY_REG.put(registry.getRegistryKey(), base);
	}
	
	public static <T> Class<T> getSuperType(IForgeRegistry<T> registry)
	{
		if(registry == null)
			return null;
		return getSuperType(registry.getRegistryKey());
	}
	
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
	
	public static <T> IForgeRegistry<T> getRegistryByType(Class<T> registry)
	{
		if(registry == null)
			return null;
		return Cast.cast(REG_BY_TYPE.get(registry));
	}
	
	public static boolean isNonIntrusive(IForgeRegistry<?> registry)
	{
		return NON_INTRUSIVE_REGISTRIES.contains(registry.getRegistryKey());
	}
}