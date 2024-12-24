package org.zeith.hammerlib.client.flowgui.readers;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiSpriteButtonObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.mcf.Resources;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("sprite_button")
public class FlowguiSpriteButtonReader
		extends GuiReader<GuiSpriteButtonObject>
{
	@FileReference(
			regex = "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$",
			value = "resources/assets/%modid%/%path%"
	)
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Required("minecraft:textures/gui/container/furnace.png") String KEY_TEXTURE = "src";
	
	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("1") String KEY_ALPHA = "alpha";
	
	@AllowJS
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#FFFFFF") String KEY_BUTTON_COLOR = "button-color";
	
	@AllowJS
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#FFFFFF") String KEY_TEXT_COLOR = "text-color";
	
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final String KEY_PRESS_SOUND = "press-sound";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final String KEY_PRESS_SOUND_PITCH = "press-sound-pitch";
	
	@AllowJS
	public static final @Default("") String KEY_LABEL = "label";
	
	@AllowJS
	public static final @Default("(q, self) => {}") String KEY_CALLBACK = "callback";
	
	@AllowJS
	public static final @Default("(q, self) => true") String KEY_ENABLED = "enabled";
	
	@Override
	protected GuiSpriteButtonObject readObject(KeyMap context, String name, IDataNode node)
	{
		var query = context.get(FlowguiRegistry.QUERY);
		
		var builder = GuiSpriteButtonObject.of(name)
				.customTexture(Resources.location(node.getString(KEY_TEXTURE)));
		
		var keys = node.keys();
		if(keys.contains(KEY_PRESS_SOUND))
			builder.pressSound(Holder.direct(SoundEvent.createVariableRangeEvent(Resources.location(node.getString(KEY_PRESS_SOUND)))));
		
		var button = builder.build();
		var jsc = getJSContext(context);
		
		var cbq = readCallback(jsc, button, node, query, KEY_CALLBACK, false);
		button.callback = b -> cbq.run();
		
		driveComponent(jsc, button, query, node, KEY_LABEL, Component.empty(), false, button::setMessage);
		driveColor(jsc, button, query, node, KEY_TEXT_COLOR, 0xFFFFFF, false, button::setPackedFGColor);
		driveColor(jsc, button, query, node, KEY_BUTTON_COLOR, 0xFFFFFF, false, rgb -> button.color = Vec3.fromRGB24(rgb));
		driveBool(jsc, button, query, node, KEY_ENABLED, true, false, button::setEnabled);
		driveFloat(jsc, button, query, node, KEY_ALPHA, 1F, false, alpha -> button.setAlpha(Mth.clamp(alpha, 0F, 1F)));
		driveFloat(jsc, button, query, node, KEY_PRESS_SOUND_PITCH, 1F, false, alpha -> button.setPressSoundPitch(Mth.clamp(alpha, 0F, 2F)));
		
		return button;
	}
}
