package org.zeith.hammerlib.client.flowgui.readers;

import com.google.common.base.MoreObjects;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.core.Holder;
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

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("button")
public class FlowguiButtonReader
		extends GuiReader<GuiButtonObject>
{
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("1") String KEY_ALPHA = "alpha";
	
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#FFFFFF") String KEY_TEXT_COLOR = "text-color";
	
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final String KEY_PRESS_SOUND = "press-sound";
	
	public static final @Default("") String KEY_LABEL = "label";
	
	@AllowJS
	public static final @Default("") String KEY_CALLBACK = "callback";
	
	@AllowJS
	public static final @Default("true") String KEY_ENABLED = "enabled";
	
	@FileReference(
			regex = "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$",
			value = "resources/assets/%modid%/textures/gui/sprites/%path%.png"
	)
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Default("widget/button") String KEY_SPRITES_ENABLED = "sprite-enabled";
	
	@FileReference(
			regex = "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$",
			value = "resources/assets/%modid%/textures/gui/sprites/%path%.png"
	)
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Default("widget/button_disabled") String KEY_SPRITES_DISABLED = "sprite-disabled";
	
	@FileReference(
			regex = "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$",
			value = "resources/assets/%modid%/textures/gui/sprites/%path%.png"
	)
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Default("widget/button_highlighted") String KEY_SPRITES_ENABLED_HOVER = "sprite-enabled-hover";
	
	@FileReference(
			regex = "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$",
			value = "resources/assets/%modid%/textures/gui/sprites/%path%.png"
	)
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Default("widget/button_disabled") String KEY_SPRITES_DISABLED_HOVER = "sprite-disabled-hover";
	
	@Override
	protected GuiButtonObject readObject(KeyMap context, String name, IDataNode node)
	{
		var query = context.get(FlowguiRegistry.QUERY);
		
		var builder = GuiButtonObject.builder(name);
		
		var keys = node.keys();
		
		String textColor = node.getString(KEY_TEXT_COLOR);
		if(textColor != null && textColor.matches(AllowedValues.HEX_COLOR))
			builder.packedFGColor(Integer.parseInt(textColor.substring(1), 16));
		
		if(keys.contains(KEY_PRESS_SOUND))
			builder.pressSound(Holder.direct(SoundEvent.createVariableRangeEvent(Resources.location(node.getString(KEY_PRESS_SOUND)))));
		
		Component com = Component.empty();
		if(keys.contains(KEY_LABEL))
		{
			var s = node.getString(KEY_LABEL);
			com = MoreObjects.firstNonNull(Component.Serializer.fromJsonLenient(s, TagsUpdateListener.getRegistryAccess()), Component.translatable(s));
		}
		builder.message(com);
		
		var button = builder.build();
		var jsc = getJSContext(context);
		
		var cbq = readCallback(jsc, KEY_CALLBACK, node, query, button, false);
		button.callback = b -> cbq.run();
		
		driveBool(jsc, button, query, node, KEY_ENABLED, true, false, button::setEnabled);
		driveFloat(jsc, button, query, node, KEY_ALPHA, 1F, false, alpha -> button.setAlpha(Math.clamp(alpha, 0F, 1F)));
		
		var disabled = Resources.locationOrNull(node.getString(KEY_SPRITES_DISABLED));
		button.sprites = new WidgetSprites(
				MoreObjects.firstNonNull(Resources.locationOrNull(node.getString(KEY_SPRITES_ENABLED)), button.sprites.enabled()),
				MoreObjects.firstNonNull(disabled, button.sprites.disabled()),
				MoreObjects.firstNonNull(Resources.locationOrNull(node.getString(KEY_SPRITES_ENABLED_HOVER)), button.sprites.enabledFocused()),
				MoreObjects.firstNonNull(
						MoreObjects.firstNonNull(Resources.locationOrNull(node.getString(KEY_SPRITES_DISABLED_HOVER)), disabled),
						button.sprites.disabledFocused()
				)
		);
		
		return button;
	}
}
