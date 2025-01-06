package org.zeith.hammerlib.client.flowgui.readers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiEditBoxObject;
import org.zeith.hammerlib.client.flowgui.objects.GuiTextObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiReader;
import org.zeith.hammerlib.client.flowgui.reader.GuiReader;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.OptionalBoolean;
import org.zeith.hammerlib.util.java.Suppliers2;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("label")
public class FlowguiLabelReader
		extends GuiReader<GuiTextObject>
{
	@AllowJS
	public static final @Required("{\"translate\":\"block.minecraft.stone\"}") String KEY_VALUE = "value";
	
	@AllowJS
	public static final @Default("#FFFFFF") String KEY_COLOR = "color";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("true") String KEY_SHADOW = "shadow";
	
	@AllowJS
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Default("minecraft:default") String KEY_FONT = "font";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_BOLD = "bold";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_ITALIC = "italic";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_UNDERLINED = "underlined";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_STRIKETHROUGH = "strikethrough";
	
	@AllowJS
	@Suggestions({ "true", "false" })
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_OBFUSCATED = "obfuscated";
	
	@Override
	protected GuiTextObject readObject(KeyMap map, String name, IDataNode node)
	{
		var self = new GuiTextObject(name, Minecraft.getInstance().font, Component.empty().getVisualOrderText(), 0xFFFFFFFF, true);
		var ctx = getDriverContext(map, node, self);
		
		Supplier<OptionalBoolean> bold = readBoolean(ctx, KEY_BOLD);
		Supplier<OptionalBoolean> italic = readBoolean(ctx, KEY_ITALIC);
		Supplier<OptionalBoolean> underlined = readBoolean(ctx, KEY_UNDERLINED);
		Supplier<OptionalBoolean> strikethrough = readBoolean(ctx, KEY_STRIKETHROUGH);
		Supplier<OptionalBoolean> obfuscated = readBoolean(ctx, KEY_OBFUSCATED);
		Supplier<ResourceLocation> font = Suppliers2.map(readString(ctx, KEY_FONT), s -> s.isBlank() ? null : Resources.locationOrNull(s));
		
		driveColor(ctx, KEY_COLOR, 0xFFFFFFFF, false, self::setColor);
		driveBool(ctx, KEY_SHADOW, true, false, self::setShadow);
		driveComponent(ctx, KEY_VALUE, Component.empty(), false, comp ->
				self.setText(comp.copy().withStyle(st ->
				{
					var b = bold.get();
					if(b != null && b.isPresent()) st = st.withBold(b.orElse(false));
					b = italic.get();
					if(b != null && b.isPresent()) st = st.withItalic(b.orElse(false));
					b = underlined.get();
					if(b != null && b.isPresent()) st = st.withUnderlined(b.orElse(false));
					b = strikethrough.get();
					if(b != null && b.isPresent()) st = st.withStrikethrough(b.orElse(false));
					b = obfuscated.get();
					if(b != null && b.isPresent()) st = st.withObfuscated(b.orElse(false));
					var f = font.get();
					if(f != null) st = st.withFont(f);
					return st;
				}))
		);
		
		return self;
	}
}