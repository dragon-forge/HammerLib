package org.zeith.hammerlib.client.flowgui.readers;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.Namespace;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiReader;
import org.zeith.hammerlib.client.flowgui.reader.GuiReader;
import org.zeith.hammerlib.proxy.HLConstants;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("empty")
public class FlowguiEmptyReader
		extends GuiReader<GuiObject>
{
	public static final FlowguiEmptyReader INSTANCE = new FlowguiEmptyReader();
	
	@Override
	protected GuiObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		return new GuiObject(name);
	}
}