package org.zeith.hammerlib.client.flowgui.readers;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiTooltipObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.client.flowgui.util.Tooltip;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.Optional;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("tooltip")
public class FlowguiTooltipReader
		extends GuiReader<GuiTooltipObject>
{
	@AllowJS
	@Required("[{\"translate\":\"block.minecraft.stone\"},{\"text\":\"Text!\"}]")
	public static final String KEY_TEXT = "text";
	
	@Override
	protected GuiTooltipObject readObject(KeyMap map, String name, IDataNode attributes)
	{
		GuiTooltipObject self = new GuiTooltipObject(name);
		var ctx = getDriverContext(map, attributes, self);
		var tooltip = ComDrivers.readComponents(ctx, KEY_TEXT);
		Tooltip tt = (graphics, font, x, y) -> graphics.renderTooltip(font, tooltip.get(), Optional.empty(), x, y);
		return self.tooltip(tt);
	}
}