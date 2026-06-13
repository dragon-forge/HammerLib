package org.zeith.hammerlib.abstractions.props;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class Key<T>
{
	private final ResourceLocation name;
	private final Class<T> type;
	
	public Key(ResourceLocation name, Class<T> type)
	{
		this.name = name;
		this.type = type;
	}
	
	public static <T> Key<T> of(ResourceLocation id, Class<T> type)
	{
		return new Key<>(id, type);
	}
	
	@Override
	public @Nonnull String toString()
	{
		return "Key<" + type.getSimpleName() + ">(" + name + ")";
	}
	
	public ResourceLocation name() {return name;}
	
	public Class<T> type() {return type;}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		Key that = (Key) obj;
		return Objects.equals(this.name, that.name) &&
				Objects.equals(this.type, that.type);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(name, type);
	}
}