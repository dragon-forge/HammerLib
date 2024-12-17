package org.zeith.hammerlib.client.flowgui.reader;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;

import static org.zeith.hammerlib.client.flowgui.reader.FlowguiRegistry.GUI_ROOT;

final class RootReader
		extends GuiReader<GuiRootObject>
{
	static final RootReader INSTANCE = new RootReader();
	
	public static final String KEY_DEBUG = "debug";
	
	@Override
	protected GuiRootObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		var root = GuiRootObject.root();
		root.debugBoundaries = attributes.getBoolean(KEY_DEBUG);
		context.put(GUI_ROOT, root);
		return root;
	}
}