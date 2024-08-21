package org.zeith.hammerlib.api.forge;

import net.minecraft.tileentity.TileEntity;
import org.zeith.hammerlib.tiles.TileEntityType;

public class BlockAPI
{
	public static <T extends TileEntity> TileEntityType<T> createBlockEntityType(TileEntityType.ITileFactory<T> generator, Class<T> type)
	{
		return new TileEntityType<>(type, generator);
	}
}
