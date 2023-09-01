package org.zeith.hammerlib.abstractions.sources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.*;
import java.util.Optional;
import java.util.function.Supplier;

public interface IObjectSource<T>
{
	CompoundTag writeSource();
	
	IObjectSourceType getType();
	
	Class<T> getBaseType();
	
	T get(Level world);
	
	default <R> Optional<R> get(Class<R> base, Level level)
	{
		return Cast.optionally(get(level), base);
	}
	
	static Supplier<EntitySourceType.EntitySource> ofEntity(@Nonnull Entity entity)
	{
		return () -> new EntitySourceType.EntitySource(entity.getId());
	}
	
	static Supplier<TileSourceType.TileSource> ofTile(@Nonnull BlockEntity tile)
	{
		return () -> new TileSourceType.TileSource(tile.getBlockPos());
	}
	
	static CompoundTag writeSource(IObjectSource<?> src)
	{
		var tag = new CompoundTag();
		tag.put("Src", src.writeSource());
		tag.putString("Type", src.getType().getRegistryKey().toString());
		return tag;
	}
	
	static Optional<IObjectSource<?>> readSource(@Nullable CompoundTag tag)
	{
		if(tag == null) return Optional.empty();
		IObjectSourceType type = RegistriesHL.animationSources().getValue(new ResourceLocation(tag.getString("Type")));
		if(type == null) return Optional.empty();
		return Optional.ofNullable(type.readSource(tag.getCompound("Src")));
	}
}