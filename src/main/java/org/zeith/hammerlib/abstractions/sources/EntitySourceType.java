package org.zeith.hammerlib.abstractions.sources;

import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.zeith.hammerlib.core.init.SourceTypesHL;

public class EntitySourceType
		extends BaseObjectSourceEntry
{
	@Override
	public IObjectSource<Entity> readSource(NBTTagCompound tag)
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
		
		public EntitySource(NBTTagCompound nbt)
		{
			this.id = nbt.getInteger("id");
		}
		
		@Override
		public NBTTagCompound writeSource()
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("id", id);
			return tag;
		}
		
		@Override
		public Entity get(World world)
		{
			return Cast.cast(world.getEntityByID(id), Entity.class);
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
		
		@Override
		public String toString()
		{
			return "EntitySource{" +
					"id=" + id +
					'}';
		}
	}
}