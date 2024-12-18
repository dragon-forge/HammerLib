package org.zeith.hammerlib.client.flowgui.reader;

import lombok.Getter;
import net.minecraft.client.gui.screens.Screen;
import org.zeith.hammerlib.core.js.math.IVariableAccess;

import java.util.HashMap;
import java.util.function.BiConsumer;

@Getter
public class FlowQuery
		extends HashMap<String, Object>
		implements IVariableAccess
{
	public final Screen gui;
	
	public FlowQuery(Screen gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void putObjects(BiConsumer<String, Object> storage)
	{
		storage.accept("q", this);
		storage.accept("query", this);
	}
}