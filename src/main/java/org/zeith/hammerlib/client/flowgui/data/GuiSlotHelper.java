package org.zeith.hammerlib.client.flowgui.data;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GuiSlotHelper
{
	public static int MAIN_INVENTORY_SLOT_COUNT = 9 * 3;
	public static int HOTBAR_INVENTORY_SLOT_COUNT = Inventory.getSelectionSize();
	public static int ARMOR_INVENTORY_SLOT_COUNT = 4;
	public static int OFFHAND_INVENTORY_SLOT_COUNT = 1;
	
	public final int[] playerInventorySlots = new int[MAIN_INVENTORY_SLOT_COUNT];
	public final int[] playerHotbarSlots = new int[HOTBAR_INVENTORY_SLOT_COUNT];
	public final int[] playerArmorSlots = new int[ARMOR_INVENTORY_SLOT_COUNT];
	public final int[] playerOffhandSlots = new int[OFFHAND_INVENTORY_SLOT_COUNT];
	
	public final int[] guiSlots;
	
	public GuiSlotHelper(@NotNull AbstractContainerMenu menu)
	{
		Arrays.fill(playerInventorySlots, -1);
		Arrays.fill(playerHotbarSlots, -1);
		Arrays.fill(playerArmorSlots, -1);
		Arrays.fill(playerOffhandSlots, -1);
		
		IntList guiSlots = new IntArrayList();
		
		var sl = menu.slots;
		for(int i = 0, len = sl.size(); i < len; i++)
		{
			var slot = sl.get(i);
			if(slot.container instanceof Inventory)
			{
				int index = slot.getSlotIndex();
				int prev = 0;
				
				if(index < prev + HOTBAR_INVENTORY_SLOT_COUNT)
				{
					playerHotbarSlots[index - prev] = i;
					continue;
				}
				prev += HOTBAR_INVENTORY_SLOT_COUNT;
				
				if(index < prev + MAIN_INVENTORY_SLOT_COUNT)
				{
					playerInventorySlots[index - prev] = i;
					continue;
				}
				prev += MAIN_INVENTORY_SLOT_COUNT;
				
				if(index < prev + ARMOR_INVENTORY_SLOT_COUNT)
				{
					playerArmorSlots[index - prev] = i;
					continue;
				}
				prev += ARMOR_INVENTORY_SLOT_COUNT;
				
				if(index < prev + OFFHAND_INVENTORY_SLOT_COUNT)
				{
					playerOffhandSlots[index - prev] = i;
					continue;
				}
				prev += OFFHAND_INVENTORY_SLOT_COUNT;
			} else
			{
				guiSlots.add(i);
			}
		}
		
		this.guiSlots = guiSlots.toIntArray();
	}
}