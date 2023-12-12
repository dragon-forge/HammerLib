package org.zeith.hammerlib.abstractions.sources;

import lombok.var;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.*;
import java.util.*;
import java.util.function.Supplier;

public interface IObjectSource<T>
{
	CompoundNBT writeSource();
	
	IObjectSourceType getType();
	
	Class<T> getBaseType();
	
	T get(World world);
	
	default <R> Optional<R> get(Class<R> base, World level)
	{
		return Cast.optionally(get(level), base);
	}
	
	static Supplier<EntitySourceType.EntitySource> ofEntity(@Nonnull Entity entity)
	{
		return () -> new EntitySourceType.EntitySource(entity.getId());
	}
	
	static Supplier<TileSourceType.TileSource> ofTile(@Nonnull TileEntity tile)
	{
		return () -> new TileSourceType.TileSource(tile.getBlockPos());
	}
	
	static CompoundNBT writeSource(IObjectSource<?> src)
	{
		var tag = new CompoundNBT();
		tag.put("Src", src.writeSource());
		tag.putString("Type", Objects.toString(src.getType().getRegistryKey()));
		return tag;
	}
	
	static Optional<IObjectSource<?>> readSource(@Nullable CompoundNBT tag)
	{
		if(tag == null) return Optional.empty();
		IObjectSourceType type = RegistriesHL.animationSources().getValue(new ResourceLocation(tag.getString("Type")));
		if(type == null) return Optional.empty();
		return Optional.ofNullable(type.readSource(tag.getCompound("Src")));
	}
}