package org.zeith.hammerlib.client.flowgui.readers;

import com.google.common.base.MoreObjects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiButtonObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.event.listeners.TagsUpdateListener;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.Map;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("button")
public class ButtonReader
		extends GuiReader<GuiButtonObject>
{
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required String KEY_ALPHA = "alpha";
	
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#FFFFFF") String KEY_TEXT_COLOR = "text-color";
	
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final String KEY_PRESS_SOUND = "press-sound";
	
	public static final @Default("") String KEY_LABEL = "label";
	
	@AllowJS
	public static final @Default("") String KEY_CALLBACK = "callback";
	
	@AllowJS
	public static final @Default("true") String KEY_ENABLED = "enabled";
	
	@Override
	protected GuiButtonObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		var root = context.get(FlowguiRegistry.GUI_ROOT);
		var query = context.get(FlowguiRegistry.QUERY);
		
		var builder = GuiButtonObject.builder(name);
		
		var keys = attributes.keys();
		
		String textColor = attributes.getString(KEY_TEXT_COLOR);
		if(textColor != null && textColor.matches(AllowedValues.HEX_COLOR))
			builder.packedFGColor(Integer.parseInt(textColor.substring(1), 16));
		
		if(keys.contains(KEY_PRESS_SOUND))
			builder.pressSound(Holder.direct(SoundEvent.createVariableRangeEvent(Resources.location(attributes.getString(KEY_PRESS_SOUND)))));
		
		Component com = Component.empty();
		if(keys.contains(KEY_LABEL))
		{
			var s = attributes.getString(KEY_LABEL);
			com = MoreObjects.firstNonNull(Component.Serializer.fromJsonLenient(s, TagsUpdateListener.getRegistryAccess()), Component.translatable(s));
		}
		builder.message(com);
		
		var button = builder.build();
		
		var cbq = readCallback(KEY_CALLBACK, attributes, query, button, false, Map.of());
		button.callback = cbq::invoke;
		
		driveBool(root, query, attributes, KEY_ENABLED, true, false, button::setEnabled);
		driveFloat(root, query, attributes, KEY_ALPHA, 1F, false, alpha -> button.setAlpha(Math.clamp(alpha, 0F, 1F)));
		
		return button;
	}
}
