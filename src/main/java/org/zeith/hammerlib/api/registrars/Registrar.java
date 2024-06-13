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
import net.neoforged.neoforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.fml.ICustomRegistrar;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;

/**
 * Use this whenever you want to register something that either links back to multiple registries to filter things out.
 * <p>
 * Although it works for more granular registration with {@link org.zeith.hammerlib.annotations.SimplyRegister}
 */
@SuppressWarnings("unused")
public class Registrar<T>
		implements ICustomRegistrar
{
	protected final ResourceKey<? extends Registry<T>> registryKey;
	protected final Supplier<T> value;
	
	public Registrar(ResourceKey<? extends Registry<T>> registryKey, T value)
	{
		this.registryKey = registryKey;
		this.value = Cast.constant(value);
	}
	
	public Registrar(ResourceKey<? extends Registry<T>> registryKey, Supplier<T> value)
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
	
	public T get()
	{
		return value.get();
	}
	
	public static Registrar<MapCodec<? extends PoolAliasBinding>> poolAliasBinding(MapCodec<? extends PoolAliasBinding> type)
	{
		return new Registrar<>(Registries.POOL_ALIAS_BINDING, type);
	}
	
	public static Registrar<MapCodec<? extends BiomeSource>> biomeSource(MapCodec<? extends BiomeSource> type)
	{
		return new Registrar<>(Registries.BIOME_SOURCE, type);
	}
	
	public static Registrar<MapCodec<? extends ChunkGenerator>> chunkGenerator(MapCodec<? extends ChunkGenerator> type)
	{
		return new Registrar<>(Registries.CHUNK_GENERATOR, type);
	}
	
	public static Registrar<MapCodec<? extends SurfaceRules.ConditionSource>> materialCondition(MapCodec<? extends SurfaceRules.ConditionSource> type)
	{
		return new Registrar<>(Registries.MATERIAL_CONDITION, type);
	}
	
	public static Registrar<MapCodec<? extends SurfaceRules.RuleSource>> materialRule(MapCodec<? extends SurfaceRules.RuleSource> type)
	{
		return new Registrar<>(Registries.MATERIAL_RULE, type);
	}
	
	public static Registrar<MapCodec<? extends DensityFunction>> densityFunction(MapCodec<? extends DensityFunction> type)
	{
		return new Registrar<>(Registries.DENSITY_FUNCTION_TYPE, type);
	}
	
	public static Registrar<MapCodec<? extends Block>> blockType(MapCodec<? extends Block> type)
	{
		return new Registrar<>(Registries.BLOCK_TYPE, type);
	}
	
	public static <T> Registrar<DataComponentType<T>> dataComponentType(DataComponentType.Builder<T> type)
	{
		return Cast.cast(new Registrar<>(Registries.DATA_COMPONENT_TYPE, type.build()));
	}
	
	public static Registrar<DataComponentType<?>> enchantmentEffectDataComponentType(DataComponentType.Builder<?> type)
	{
		return new Registrar<>(Registries.DATA_COMPONENT_TYPE, type.build());
	}
	
	public static Registrar<MapCodec<? extends EntitySubPredicate>> entitySubPredicateType(MapCodec<? extends EntitySubPredicate> type)
	{
		return new Registrar<>(Registries.ENTITY_SUB_PREDICATE_TYPE, type);
	}
	
	public static Registrar<MapCodec<? extends LevelBasedValue>> enchantmentLevelBasedValueType(MapCodec<? extends LevelBasedValue> type)
	{
		return new Registrar<>(Registries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE, type);
	}
	
	public static Registrar<MapCodec<? extends EnchantmentEntityEffect>> enchantmentEntityBasedValueType(MapCodec<? extends EnchantmentEntityEffect> type)
	{
		return new Registrar<>(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, type);
	}
	
	public static Registrar<MapCodec<? extends EnchantmentLocationBasedEffect>> enchantmentLocationBasedValueType(MapCodec<? extends EnchantmentLocationBasedEffect> type)
	{
		return new Registrar<>(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, type);
	}
	
	public static Registrar<MapCodec<? extends EnchantmentValueEffect>> enchantmentValueBasedValueType(MapCodec<? extends EnchantmentValueEffect> type)
	{
		return new Registrar<>(Registries.ENCHANTMENT_VALUE_EFFECT_TYPE, type);
	}
	
	public static Registrar<MapCodec<? extends EnchantmentProvider>> enchantmentProviderType(MapCodec<? extends EnchantmentProvider> type)
	{
		return new Registrar<>(Registries.ENCHANTMENT_PROVIDER_TYPE, type);
	}
}