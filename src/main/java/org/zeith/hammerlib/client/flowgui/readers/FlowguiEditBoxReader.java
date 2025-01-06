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

import java.util.concurrent.atomic.AtomicReference;

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
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_BORDERED = "bordered";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_CAN_LOSE_FOCUS = "can-lose-focus";
	
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_TEXT_SHADOW = "text-shadow";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_EDITABLE = "editable";
	
	@AllowJS
	public static final String KEY_HINT = "hint";
	
	@AllowJS
	@AllowedValues({ })
	public static final @Default("(q, str) => {}") String KEY_ONCHANGED = "on-changed";
	
	public static final CallerSpec CONSUMER_SPEC = new CallerSpec("accept", false);
	
	@Override
	protected GuiEditBoxObject readObject(KeyMap map, String name, IDataNode node)
	{
		AtomicReference<GuiEditBoxObject> self = new AtomicReference<>();
		var ctx = getDriverContext(map, node, self);
		
		GuiEditBoxObject.EditBoxBuilder builder = GuiEditBoxObject.builder(name);
		
		String str = node.getString(KEY_ONCHANGED);
		StringConsumer eval = ctx.eval(StringConsumer.class, str, CONSUMER_SPEC);
		if(eval != null) builder.responder(s -> eval.accept(ctx.query(), s));
		
		GuiEditBoxObject box = builder.build();
		self.set(box);
		
		driveBool(ctx, KEY_BORDERED, true, false, box::bordered);
		driveBool(ctx, KEY_CAN_LOSE_FOCUS, true, false, box::canLoseFocus);
		driveInt(ctx, KEY_MAX_LENGTH, 50, false, box::maxLength);
		driveBool(ctx, KEY_EDITABLE, true, false, box::editable);
		driveComponent(ctx, KEY_HINT, Component.empty(), false, box::hint);
		driveColor(ctx, KEY_TEXT_COLOR, 0xFFFFFF, false, box::textColor);
		driveColor(ctx, KEY_UNEDITABLE_TEXT_COLOR, 0xFFFFFF, false, box::textColorUneditable);
		
		return box;
	}
	
	@Override
	protected void finishBuilding(GuiEditBoxObject object, KeyMap map)
	{
		GuiRootObject pr = map.get(FlowguiRegistry.PREVIOUS_ROOT);
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