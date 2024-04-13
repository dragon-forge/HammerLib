package org.zeith.hammerlib.abstractions.sources;

import com.zeitheron.hammercore.internal.init.RegistriesHL;
import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.*;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

public interface IObjectSource<T>
{
	NBTTagCompound writeSource();
	
	IObjectSourceType getType();
	
	Class<T> getBaseType();
	
	T get(World world);
	
	default <R> Optional<R> get(Class<R> base, World level)
	{
		return Cast.optionally(get(level), base);
	}
	
	static Supplier<EntitySourceType.EntitySource> ofEntity(@Nonnull Entity entity)
	{
		return () -> new EntitySourceType.EntitySource(entity.getEntityId());
	}
	
	static Supplier<TileSourceType.TileSource> ofTile(@Nonnull TileEntity tile)
	{
		return () -> new TileSourceType.TileSource(tile.getPos());
	}
	
	static NBTTagCompound writeSource(IObjectSource<?> src)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("Src", src.writeSource());
		tag.setString("Type", src.getType().getRegistryKey().toString());
		return tag;
	}
	
	static Optional<IObjectSource<?>> readSource(@Nullable NBTTagCompound tag)
	{
		if(tag == null) return Optional.empty();
		IObjectSourceType type = RegistriesHL.OBJECT_SOURCE().getValue(new ResourceLocation(tag.getString("Type")));
		if(type == null) return Optional.empty();
		return Optional.ofNullable(type.readSource(tag.getCompoundTag("Src")));
	}
	
	static void writeSource(@Nullable IObjectSource<?> src, @Nonnull PacketBuffer buf)
	{
		buf.writeBoolean(src != null);
		if(src == null) return;
		buf.writeResourceLocation(src.getType().getRegistryKey());
		buf.writeCompoundTag(src.writeSource());
	}
	
	static Optional<IObjectSource<?>> readSource(@Nonnull PacketBuffer buf)
			throws IOException
	{
		if(!buf.readBoolean()) return Optional.empty();
		IObjectSourceType type = RegistriesHL.OBJECT_SOURCE().getValue(buf.readResourceLocation());
		NBTTagCompound src = buf.readCompoundTag();
		if(type == null) return Optional.empty();
		return Optional.ofNullable(type.readSource(src));
	}
}