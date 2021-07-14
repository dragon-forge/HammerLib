package org.zeith.hammerlib.core.test.machine;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.api.forge.ContainerAPI;
import org.zeith.hammerlib.api.inv.IScreenContainer;

public class ContainerTestMachine
		extends Container
		implements IScreenContainer
{
	public TileTestMachine tile;

	public ContainerTestMachine(PlayerEntity player, int windowId, TileTestMachine tile)
	{
		super(ContainerAPI.TILE_CONTAINER, windowId);
		this.tile = tile;

		int x;
		for(x = 0; x < 3; ++x)
			for(int y = 0; y < 9; ++y)
				this.addSlot(new Slot(player.inventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));

		for(x = 0; x < 9; ++x)
			this.addSlot(new Slot(player.inventory, x, 8 + x * 18, 142));

		addSlot(new Slot(tile.inventory, 0, 56, 17));
		addSlot(new Slot(tile.inventory, 1, 56, 53));
		addSlot(new Slot(tile.inventory, 2, 116, 35));
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn)
	{
		return tile.getBlockPos().distSqr(playerIn.position(), false) < 64D && !tile.isRemoved();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Screen openScreen(PlayerInventory inv, ITextComponent label)
	{
		return new ScreenTestMachine(this, inv, label);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int p_82846_2_)
	{
		return ItemStack.EMPTY;
	}
}