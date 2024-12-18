package org.zeith.hammerlib.client.flowgui.readers;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiSlotLinkObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.proxy.HLConstants;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("slot")
public class FlowguiSlotReader
		extends GuiReader<GuiSlotLinkObject>
{
	@AllowedValues(AllowedValues.POSITIVE_INTEGERS)
	public static final @Required String KEY_INDEX = "index";
	
	@Override
	protected GuiSlotLinkObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		var query = context.get(FlowguiRegistry.QUERY);
		if(query.container == null || query.slots == null) return null;
		var allSlots = query.container.slots;
		
		var error = invalidField(attributes, KEY_INDEX);
		var slot = readInt(KEY_INDEX, query, attributes).get().orElseThrow(error);
		if(slot < 0 || slot >= allSlots.size()) throw error.get();
		
		return GuiObject.create(name).slot(allSlots.get(slot));
	}
}