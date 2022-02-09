package org.zeith.hammerlib.api.forge;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.tiles.IContainerTile;
import org.zeith.hammerlib.util.java.Cast;

import javax.annotation.Nullable;

@SimplyRegister
public class ContainerAPI
{
	@RegistryName("tile_container")
	public static final MenuType<AbstractContainerMenu> TILE_CONTAINER = IForgeMenuType.create((windowId, playerInv, extraData) ->
	{
		BlockEntity tile = playerInv.player.level.getBlockEntity(extraData.readBlockPos());
		return Cast
				.optionally(tile, IContainerTile.class)
				.map(ict -> ict.openContainer(playerInv.player, windowId))
				.orElse(null);
	});

	public static MenuProvider forTile(BlockEntity tile)
	{
		return new MenuProvider()
		{
			@Override
			public Component getDisplayName()
			{
				return Cast
						.optionally(tile, IContainerTile.class)
						.<Component> map(IContainerTile::getDisplayName)
						.orElseGet(() -> tile.getBlockState().getBlock().getName());
			}

			@Nullable
			@Override
			public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player)
			{
				return Cast
						.optionally(tile, IContainerTile.class)
						.map(ict -> ict.openContainer(player, windowId))
						.orElse(null);
			}
		};
	}

	public static <T extends BlockEntity & IContainerTile> void openContainerTile(Player player, T tile)
	{
		if(player instanceof ServerPlayer && tile != null)
			NetworkHooks.openGui((ServerPlayer) player, forTile(tile), buf -> buf.writeBlockPos(tile.getBlockPos()));
	}
}