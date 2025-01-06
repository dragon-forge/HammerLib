package org.zeith.hammerlib.client.flowgui.readers;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.FlowguiShaderRegistry;
import org.zeith.hammerlib.client.flowgui.objects.GuiSpriteButtonObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Suppliers2;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("sprite_button")
public class FlowguiSpriteButtonReader
		extends GuiReader<GuiSpriteButtonObject>
{
	@AllowJS
	@FileReference(
			regex = { "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$", "^(?<path>[a-z0-9_./-]+)$" },
			value = { "assets/%modid%/%path%", "assets/minecraft/%path%" }
	)
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Required("minecraft:textures/gui/container/furnace.png") String KEY_TEXTURE = "src";
	
	@AllowJS
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Default("minecraft:gui") String KEY_SHADER = "shader";
	
	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("1") String KEY_ALPHA = "alpha";
	
	@AllowJS
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#FFFFFF") String KEY_BUTTON_COLOR = "button-color";
	
	@AllowJS
	@AllowedValues(AllowedValues.HEX_COLOR)
	public static final @Default("#FFFFFF") String KEY_TEXT_COLOR = "text-color";
	
	@AllowJS
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final String KEY_PRESS_SOUND = "press-sound";
	
	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final String KEY_PRESS_SOUND_PITCH = "press-sound-pitch";
	
	@AllowJS
	public static final @Default("") String KEY_LABEL = "label";
	
	@AllowJS
	public static final @Default("(q, self) => {}") String KEY_CALLBACK = "callback";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("(q, self) => true") String KEY_ENABLED = "enabled";
	
	@Override
	protected GuiSpriteButtonObject readObject(KeyMap map, String name, IDataNode node)
	{
		AtomicReference<GuiSpriteButtonObject> self = new AtomicReference<>();
		var ctx = getDriverContext(map, node, self);
		
		var builder = GuiSpriteButtonObject.of(name);
		
		var keys = node.keys();
		if(keys.contains(KEY_PRESS_SOUND))
		{
			Supplier<String> fac = readString(ctx, KEY_PRESS_SOUND);
			if(isConstant(fac))
				builder.pressSound(Holder.direct(SoundEvent.createVariableRangeEvent(Resources.location(fac.get()))));
			else
				builder.pressSound(Suppliers2.map(fac, s -> SoundEvent.createVariableRangeEvent(Resources.location(s))));
		}
		
		var button = builder.build();
		self.set(button);
		
		var cbq = readCallback(ctx, KEY_CALLBACK, false);
		button.callback = b -> cbq.run();
		
		driveString(ctx, KEY_TEXTURE, "minecraft:missing", false, str -> button.texture = Resources.locationOrNull(str));
		driveComponent(ctx, KEY_LABEL, Component.empty(), false, button::setMessage);
		driveColor(ctx, KEY_TEXT_COLOR, 0xFFFFFF, false, button::setPackedFGColor);
		driveColor(ctx, KEY_BUTTON_COLOR, 0xFFFFFF, false, rgb -> button.color = Vec3.fromRGB24(rgb));
		driveBool(ctx, KEY_ENABLED, true, false, button::setEnabled);
		driveFloat(ctx, KEY_ALPHA, 1F, false, alpha -> button.setAlpha(Mth.clamp(alpha, 0F, 1F)));
		driveFloat(ctx, KEY_PRESS_SOUND_PITCH, 1F, false, alpha -> button.setPressSoundPitch(Mth.clamp(alpha, 0F, 2F)));
		driveString(ctx, KEY_SHADER, "minecraft:gui", false, shader -> button.shader = FlowguiShaderRegistry.get(Resources.locationOrNull(shader)));
		
		return button;
	}
}
