package org.zeith.hammerlib.client.flowgui.data;

import lombok.Getter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.zeith.hammerlib.core.js.math.IVariableAccess;

import java.util.HashMap;
import java.util.function.BiConsumer;

@Getter
public class FlowQuery
		extends HashMap<String, Object>
		implements IVariableAccess
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
	
	@Override
	public void putObjects(BiConsumer<String, Object> storage)
	{
		storage.accept("q", this);
		storage.accept("query", this);
	}
}