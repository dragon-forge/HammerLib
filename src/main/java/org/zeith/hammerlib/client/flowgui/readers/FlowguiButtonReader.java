package org.zeith.hammerlib.client.flowgui.readers;

import com.google.common.base.MoreObjects;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiButtonObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiEditBoxObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.event.listeners.TagsUpdateListener;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.Suppliers2;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("button")
public class FlowguiButtonReader
		extends GuiReader<GuiButtonObject>
{
	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("1") String KEY_ALPHA = "alpha";
	
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
	public static final @Default("") String KEY_CALLBACK = "callback";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
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
	protected GuiButtonObject readObject(KeyMap map, String name, IDataNode node)
	{
		AtomicReference<GuiButtonObject> self = new AtomicReference<>();
		var ctx = getDriverContext(map, node, self);
		
		var builder = GuiButtonObject.builder(name);
		
		var keys = node.keys();
		
		if(keys.contains(KEY_PRESS_SOUND))
		{
			Supplier<String> fac = readString(ctx, KEY_PRESS_SOUND);
			if(isConstant(fac))
				builder.pressSound(Cast.constant(SoundEvent.createVariableRangeEvent(Resources.location(fac.get()))));
			else
				builder.pressSound(Suppliers2.map(fac, s -> SoundEvent.createVariableRangeEvent(Resources.location(s))));
		}
		
		var button = builder.build();
		self.set(button);
		
		var cbq = readCallback(ctx, KEY_CALLBACK, false);
		button.callback = b -> cbq.run();
		
		driveComponent(ctx, KEY_LABEL, Component.empty(), false, button::setMessage);
		driveColor(ctx, KEY_TEXT_COLOR, 0xFFFFFF, false, button::setPackedFGColor);
		driveBool(ctx, KEY_ENABLED, true, false, button::setEnabled);
		driveFloat(ctx, KEY_ALPHA, 1F, false, alpha -> button.setAlpha(Mth.clamp(alpha, 0F, 1F)));
		driveFloat(ctx, KEY_PRESS_SOUND_PITCH, 1F, false, alpha -> button.setPressSoundPitch(Mth.clamp(alpha, 0F, 2F)));
		driveFloat(ctx, KEY_ALPHA, 1F, false, alpha -> button.setAlpha(Math.clamp(alpha, 0F, 1F)));
		
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
