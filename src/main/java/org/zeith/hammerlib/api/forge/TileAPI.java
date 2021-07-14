package org.zeith.hammerlib.api.forge;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;

public class TileAPI
{
	public static <T extends TileEntity> TileEntityType<T> createType(Class<T> type, Block... blocks)
	{
		return createType(Cast.newInstanceSupplier(type), blocks);
	}

	public static <T extends TileEntity> TileEntityType<T> createType(Supplier<T> generator, Block... blocks)
	{
		return TileEntityType.Builder.of(generator, blocks).build(null);
	}
}