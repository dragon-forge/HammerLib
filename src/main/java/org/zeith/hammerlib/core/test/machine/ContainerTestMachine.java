package org.zeith.hammerlib.core.test.machine;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.inv.ComplexProgressHandler;
import org.zeith.hammerlib.api.inv.IScreenContainer;
import org.zeith.hammerlib.client.screen.MenuWithProgressBars;

import java.util.List;

public class ContainerTestMachine
		extends MenuWithProgressBars
		implements IScreenContainer
{
	public TileTestMachine tile;
	
	public ContainerTestMachine(Player player, int windowId, TileTestMachine tile)
	{
		super(ContainerAPI.TILE_CONTAINER, windowId,
				ComplexProgressHandler.withProperties(List.of(
						tile.progress,
						tile.maxProgress
				))
		);
		
		this.tile = tile;
		
		int x;
		for(x = 0; x < 3; ++x)
			for(int y = 0; y < 9; ++y)
				this.addSlot(new Slot(player.getInventory(), y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
		
		for(x = 0; x < 9; ++x)
			this.addSlot(new Slot(player.getInventory(), x, 8 + x * 18, 142));
		
		addSlot(new Slot(tile.inventory, 0, 56, 17));
		addSlot(new Slot(tile.inventory, 1, 56, 53));
		addSlot(new Slot(tile.inventory, 2, 116, 35));
	}
	
	@Override
	public boolean stillValid(Player playerIn)
	{
		return tile.getBlockPos().closerToCenterThan(playerIn.position(), 64) && !tile.isRemoved();
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public Screen openScreen(Inventory inv, Component label)
	{
		return new ScreenTestMachine(this, inv, label);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int slot)
	{
		return ItemStack.EMPTY;
	}
}