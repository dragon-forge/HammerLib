package org.zeith.hammerlib.abstractions.sources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.core.init.SourceTypesHL;
import org.zeith.hammerlib.util.java.Cast;


public class EntitySourceType
		implements IObjectSourceType
{
	@Override
	public IObjectSource<Entity> readSource(CompoundTag tag)
	{
		return new EntitySource(tag);
	}
	
	public static class EntitySource
			implements IObjectSource<Entity>
	{
		public final int id;
		
		public EntitySource(int id)
		{
			this.id = id;
		}
		
		public EntitySource(CompoundTag nbt)
		{
			this.id = nbt.getInt("id");
		}
		
		@Override
		public CompoundTag writeSource()
		{
			var tag = new CompoundTag();
			tag.putInt("id", id);
			return tag;
		}
		
		@Override
		public Entity get(Level world)
		{
			return Cast.cast(world.getEntity(id), Entity.class);
		}
		
		@Override
		public IObjectSourceType getType()
		{
			return SourceTypesHL.ENTITY_TYPE;
		}
		
		@Override
		public Class<Entity> getBaseType()
		{
			return Entity.class;
		}
	}
}