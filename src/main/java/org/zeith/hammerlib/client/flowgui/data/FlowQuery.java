package org.zeith.hammerlib.client.flowgui.data;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.HashMap;

public class FlowQuery
		extends HashMap<String, Object>
{
	public final Screen gui;
	public final AbstractContainerMenu container;
	public final GuiSlotHelper slots;
	
	public FlowQuery(Screen gui)
	{
		this(gui, gui instanceof AbstractContainerScreen<?> c ? c.getMenu() : null);
	}
	
	public FlowQuery(Screen gui, AbstractContainerMenu container)
	{
		this.gui = gui;
		this.container = container;
		this.slots = container == null ? null : new GuiSlotHelper(container);
	}
}