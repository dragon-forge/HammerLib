package org.zeith.hammerlib.abstractions.sources;

import lombok.var;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import org.zeith.hammerlib.core.init.SourceTypesHL;
import org.zeith.hammerlib.util.java.Cast;

public class EntitySourceType
		extends IObjectSourceType
{
	@Override
	public IObjectSource<Entity> readSource(CompoundNBT tag)
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
		
		public EntitySource(CompoundNBT nbt)
		{
			this.id = nbt.getInt("id");
		}
		
		@Override
		public CompoundNBT writeSource()
		{
			var tag = new CompoundNBT();
			tag.putInt("id", id);
			return tag;
		}
		
		@Override
		public Entity get(World world)
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