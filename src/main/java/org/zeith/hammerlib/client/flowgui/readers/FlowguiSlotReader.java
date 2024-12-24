package org.zeith.hammerlib.client.flowgui.readers;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiSlotLinkObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.proxy.HLConstants;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("slot")
public class FlowguiSlotReader
		extends GuiReader<GuiSlotLinkObject>
{
	@AllowJS
	@AllowedValues(AllowedValues.POSITIVE_INTEGERS)
	public static final @Required("(q) => q.slots.playerHotbarSlots[0]") String KEY_INDEX = "index";
	
	@Override
	protected GuiSlotLinkObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		var cachingJS = context.getOrDefault(FlowguiRegistry.IS_CACHING_JS, false);
		
		var query = context.get(FlowguiRegistry.QUERY);
		if(!cachingJS && (query.container == null || query.slots == null)) return null;
		
		GuiSlotLinkObject link = new GuiSlotLinkObject(name);
		
		var value = readInt(getJSContext(context), KEY_INDEX, query, attributes, link).get();
		
		if(cachingJS) return link;
		
		var error = invalidField(attributes, KEY_INDEX);
		var slot = value.orElseThrow(error);
		
		var allSlots = query.container.slots;
		if(slot < 0 || slot >= allSlots.size()) throw error.get();
		link.bindToSlot(allSlots.get(slot));
		return link;
	}
}