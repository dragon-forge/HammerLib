package org.zeith.hammerlib.api.forge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.NetworkHooks;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.tiles.IContainerTile;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.Nullable;

@SimplyRegister
public class ContainerAPI
{
	@RegistryName("tile_container")
	public static final ContainerType<Container> TILE_CONTAINER = IForgeContainerType.create((windowId, playerInv, extraData) ->
	{
		TileEntity tile = playerInv.player.level.getBlockEntity(extraData.readBlockPos());
		return Cast
				.optionally(tile, IContainerTile.class)
				.map(ict -> ict.openContainer(playerInv.player, windowId))
				.orElse(null);
	});

	public static INamedContainerProvider forTile(TileEntity tile)
	{
		return new INamedContainerProvider()
		{
			@Override
			public ITextComponent getDisplayName()
			{
				return Cast
						.optionally(tile, IContainerTile.class)
						.map(IContainerTile::getDisplayName)
						.orElseGet(() -> tile.getBlockState().getBlock().getName());
			}

			@Nullable
			@Override
			public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player)
			{
				return Cast
						.optionally(tile, IContainerTile.class)
						.map(ict -> ict.openContainer(player, windowId))
						.orElse(null);
			}
		};
	}

	public static <T extends TileEntity & IContainerTile> void openContainerTile(PlayerEntity player, T tile)
	{
		if(player instanceof ServerPlayerEntity && tile != null)
			NetworkHooks.openGui((ServerPlayerEntity) player, forTile(tile), buf -> buf.writeBlockPos(tile.getBlockPos()));
	}
}