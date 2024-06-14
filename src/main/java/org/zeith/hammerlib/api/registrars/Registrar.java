package org.zeith.hammerlib.api.registrars;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.StructureModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Use this whenever you want to register something that either links back to multiple registries to filter things out.
 * <p>
 * Although it works for more granular registration with {@link org.zeith.hammerlib.annotations.SimplyRegister}
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class Registrar<T>
		implements ICustomRegistrar, Supplier<T>
{
	protected final ResourceKey<? extends Registry<T>> registryKey;
	protected final Supplier<T> value;
	
	protected Registrar(ResourceKey<? extends Registry<T>> registryKey, T value)
	{
		this.registryKey = registryKey;
		this.value = Cast.constant(value);
	}
	
	protected Registrar(ResourceKey<? extends Registry<T>> registryKey, Supplier<T> value)
	{
		this.registryKey = registryKey;
		this.value = Suppliers.memoize(value::get);
	}
	
	@Override
	public void performRegister(RegisterEvent event, ResourceLocation id)
	{
		if(!event.getRegistryKey().equals(registryKey)) return;
		T v = value.get();
		if(v instanceof IRegisterListener l) l.onPreRegistered(id);
		event.register(registryKey, id, Cast.constant(v));
		if(v instanceof IRegisterListener l) l.onPostRegistered(id);
	}
	
	@Override
	public T get()
	{
		return value.get();
	}
	
	public static <T> Registrar<T> of(ResourceKey<? extends Registry<T>> registryKey, T value)
	{
		return new Registrar(registryKey, value);
	}
	
	public static <T> Registrar<T> of(ResourceKey<? extends Registry<T>> registryKey, Supplier<T> value)
	{
		return new Registrar(registryKey, value);
	}
	
	public static <T extends PoolAliasBinding> Registrar<MapCodec<T>> poolAliasBinding(MapCodec<? super T> type)
	{
		return new Registrar(Registries.POOL_ALIAS_BINDING, type);
	}
	
	public static <T extends BiomeSource> Registrar<MapCodec<T>> biomeSource(MapCodec<? super T> type)
	{
		return new Registrar(Registries.BIOME_SOURCE, type);
	}
	
	public static <T extends ChunkGenerator> Registrar<MapCodec<T>> chunkGenerator(MapCodec<? super ChunkGenerator> type)
	{
		return new Registrar(Registries.CHUNK_GENERATOR, type);
	}
	
	public static <T extends SurfaceRules.ConditionSource> Registrar<MapCodec<T>> materialCondition(MapCodec<? super T> type)
	{
		return new Registrar(Registries.MATERIAL_CONDITION, type);
	}
	
	public static <T extends SurfaceRules.RuleSource> Registrar<MapCodec<T>> materialRule(MapCodec<? super T> type)
	{
		return new Registrar(Registries.MATERIAL_RULE, type);
	}
	
	public static <T extends DensityFunction> Registrar<MapCodec<T>> densityFunction(MapCodec<? super T> type)
	{
		return new Registrar(Registries.DENSITY_FUNCTION_TYPE, type);
	}
	
	public static <T extends Block> Registrar<MapCodec<T>> blockType(MapCodec<? super T> type)
	{
		return new Registrar(Registries.BLOCK_TYPE, type);
	}
	
	public static <T> Registrar<DataComponentType<T>> dataComponentType(DataComponentType.Builder<T> type)
	{
		return Cast.cast(new Registrar(Registries.DATA_COMPONENT_TYPE, type.build()));
	}
	
	public static <T> Registrar<DataComponentType<T>> dataComponentType(UnaryOperator<DataComponentType.Builder<T>> type)
	{
		return Cast.cast(new Registrar(Registries.DATA_COMPONENT_TYPE, type.apply(DataComponentType.builder()).build()));
	}
	
	public static <T> Registrar<DataComponentType<T>> enchantmentEffectDataComponentType(DataComponentType.Builder<T> type)
	{
		return Cast.cast(new Registrar(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, type.build()));
	}
	
	public static <T> Registrar<DataComponentType<T>> enchantmentEffectDataComponentType(UnaryOperator<DataComponentType.Builder<T>> type)
	{
		return Cast.cast(new Registrar(Registries.DATA_COMPONENT_TYPE, type.apply(DataComponentType.builder()).build()));
	}
	
	public static <T extends EntitySubPredicate> Registrar<MapCodec<T>> entitySubPredicateType(MapCodec<? super T> type)
	{
		return new Registrar(Registries.ENTITY_SUB_PREDICATE_TYPE, type);
	}
	
	public static <T extends LevelBasedValue> Registrar<MapCodec<T>> enchantmentLevelBasedValueType(MapCodec<? super T> type)
	{
		return new Registrar(Registries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE, type);
	}
	
	public static <T extends EnchantmentEntityEffect> Registrar<MapCodec<T>> enchantmentEntityBasedValueType(MapCodec<? super T> type)
	{
		return new Registrar(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, type);
	}
	
	public static <T extends EnchantmentLocationBasedEffect> Registrar<MapCodec<T>> enchantmentLocationBasedValueType(MapCodec<? super T> type)
	{
		return new Registrar(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, type);
	}
	
	public static <T extends EnchantmentValueEffect> Registrar<MapCodec<T>> enchantmentValueBasedValueType(MapCodec<? super T> type)
	{
		return new Registrar(Registries.ENCHANTMENT_VALUE_EFFECT_TYPE, type);
	}
	
	public static <T extends EnchantmentProvider> Registrar<MapCodec<T>> enchantmentProviderType(MapCodec<? super T> type)
	{
		return new Registrar(Registries.ENCHANTMENT_PROVIDER_TYPE, type);
	}
	
	public static <T extends IGlobalLootModifier> Registrar<MapCodec<T>> globalLootModifier(MapCodec<? super T> type)
	{
		return new Registrar(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, type);
	}
	
	public static <T extends BiomeModifier> Registrar<MapCodec<T>> biomeModifierSerializer(MapCodec<? super T> type)
	{
		return new Registrar(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, type);
	}
	
	public static <T extends StructureModifier> Registrar<MapCodec<T>> structureModifierSerializer(MapCodec<? super T> type)
	{
		return new Registrar(NeoForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, type);
	}
	
	public static <T extends ICondition> Registrar<MapCodec<T>> condition(MapCodec<? super T> type)
	{
		return new Registrar(NeoForgeRegistries.Keys.CONDITION_CODECS, type);
	}
}