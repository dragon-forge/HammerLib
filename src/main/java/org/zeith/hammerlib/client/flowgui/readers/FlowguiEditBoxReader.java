package org.zeith.hammerlib.client.flowgui.readers;

import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.client.flowgui.objects.GuiEditBoxObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiRootObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.core.js.CallerSpec;
import org.zeith.hammerlib.proxy.HLConstants;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("input")
public class FlowguiEditBoxReader
		extends GuiReader<GuiEditBoxObject>
{
	@AllowJS
	@AllowedValues(AllowedValues.POSITIVE_INTEGERS)
	public static final @Default("50") String KEY_MAX_LENGTH = "max-length";
	
	@AllowJS
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#e0e0e0") String KEY_TEXT_COLOR = "text-color";
	
	@AllowJS
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#707070") String KEY_UNEDITABLE_TEXT_COLOR = "uneditable-text-color";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_BORDERED = "bordered";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_CAN_LOSE_FOCUS = "can-lose-focus";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_EDITABLE = "editable";
	
	@AllowJS
	public static final String KEY_HINT = "hint";
	
	@AllowJS
	@AllowedValues({ })
	public static final @Default("(q, str) => {}") String KEY_ONCHANGED = "on-changed";
	
	public static final CallerSpec CONSUMER_SPEC = new CallerSpec("accept", false);
	
	@Override
	protected GuiEditBoxObject readObject(KeyMap context, String name, IDataNode node)
	{
		var query = context.get(FlowguiRegistry.QUERY);
		var jsc = getJSContext(context);
		
		GuiEditBoxObject.EditBoxBuilder builder = GuiEditBoxObject.builder(name);
		
		String str = node.getString(KEY_ONCHANGED);
		StringConsumer eval = jsc.eval(StringConsumer.class, str, CONSUMER_SPEC);
		if(eval != null) builder.responder(s -> eval.accept(query, s));
		
		GuiEditBoxObject box = builder.build();
		
		driveBool(jsc, box, query, node, KEY_BORDERED, true, false, box::bordered);
		driveBool(jsc, box, query, node, KEY_CAN_LOSE_FOCUS, true, false, box::canLoseFocus);
		driveInt(jsc, box, query, node, KEY_MAX_LENGTH, 50, false, box::maxLength);
		driveBool(jsc, box, query, node, KEY_EDITABLE, true, true, box::editable);
		driveComponent(jsc, box, query, node, KEY_HINT, Component.empty(), false, box::hint);
		driveColor(jsc, box, query, node, KEY_TEXT_COLOR, 0xFFFFFF, false, box::textColor);
		driveColor(jsc, box, query, node, KEY_UNEDITABLE_TEXT_COLOR, 0xFFFFFF, false, box::textColorUneditable);
		
		return box;
	}
	
	@Override
	protected void finishBuilding(GuiEditBoxObject object, KeyMap context)
	{
		GuiRootObject pr = context.get(FlowguiRegistry.PREVIOUS_ROOT);
		if(pr == null) return;
		String mp = object.getMyPath();
		GuiEditBoxObject prev = pr.findByPath(mp, GuiEditBoxObject.class);
		if(object.wrapped != null && prev != null && prev.wrapped != null)
			object.wrapped.setValue(prev.wrapped.getValue());
	}
	
	@FunctionalInterface
	public interface StringConsumer
	{
		void accept(FlowQuery q, String s);
	}
}