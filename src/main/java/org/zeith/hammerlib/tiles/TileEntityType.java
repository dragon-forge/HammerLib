package org.zeith.hammerlib.tiles;

import com.zeitheron.hammercore.utils.forge.ICustomRegistrar;
import com.zeitheron.hammercore.utils.forge.RegisterEvent;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityType<T extends TileEntity>
		implements ICustomRegistrar
{
	public final Class<T> type;
	public final ITileFactory<T> factory;
	
	public TileEntityType(Class<T> type, ITileFactory<T> factory)
	{
		this.type = type;
		this.factory = factory;
	}
	
	@Override
	public void register(ResourceLocation id, RegisterEvent event)
	{
		if(event.is(Block.class))
		{
			TileEntity.register(id.toString(), type);
		}
	}
	
	public interface ITileFactory<T extends TileEntity>
	{
		T create();
	}
}