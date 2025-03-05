package org.zeith.hammerlib.client.flowgui.readers;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiHoverObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.proxy.HLConstants;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("hover")
public class GuiHoverReader
		extends GuiReader<GuiHoverObject>
{
	@AllowJS
	@Default("false")
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_ALWAYS_RENDER_CHILDREN = "always-render-children";
	
	@Override
	protected GuiHoverObject readObject(KeyMap map, String name, IDataNode attributes)
	{
		var self = new GuiHoverObject(name);
		var ctx = getDriverContext(map, attributes, self);
		
		ComDrivers.driveBool(ctx, KEY_ALWAYS_RENDER_CHILDREN, false, false, self::alwaysRenderChildren);
		
		return self;
	}
}