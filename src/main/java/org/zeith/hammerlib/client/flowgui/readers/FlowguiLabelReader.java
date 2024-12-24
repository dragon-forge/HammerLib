package org.zeith.hammerlib.client.flowgui.readers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiTextObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiReader;
import org.zeith.hammerlib.client.flowgui.reader.GuiReader;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.OptionalBoolean;
import org.zeith.hammerlib.util.mcf.Resources;

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
	public static final @Default("true") String KEY_SHADOW = "shadow";
	
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Default("minecraft:default") String KEY_FONT = "font";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_BOLD = "bold";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_ITALIC = "italic";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_UNDERLINED = "underlined";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_STRIKETHROUGH = "strikethrough";
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("false") String KEY_OBFUSCATED = "obfuscated";
	
	@Override
	protected GuiTextObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		var query = getQuery(context);
		var jsc = getJSContext(context);
		
		var label = new GuiTextObject(name, Minecraft.getInstance().font, Component.empty().getVisualOrderText(), 0xFFFFFFFF, true);
		
		Supplier<OptionalBoolean> bold = readBoolean(jsc, label, query, attributes, KEY_BOLD);
		Supplier<OptionalBoolean> italic = readBoolean(jsc, label, query, attributes, KEY_ITALIC);
		Supplier<OptionalBoolean> underlined = readBoolean(jsc, label, query, attributes, KEY_UNDERLINED);
		Supplier<OptionalBoolean> strikethrough = readBoolean(jsc, label, query, attributes, KEY_STRIKETHROUGH);
		Supplier<OptionalBoolean> obfuscated = readBoolean(jsc, label, query, attributes, KEY_OBFUSCATED);
		
		var str = attributes.getString(KEY_FONT);
		var font = str != null && !str.isBlank() ? Resources.locationOrNull(str) : null;
		
		driveColor(jsc, label, query, attributes, KEY_COLOR, 0xFFFFFFFF, false, label::setColor);
		driveBool(jsc, label, query, attributes, KEY_SHADOW, true, false, label::setShadow);
		driveComponent(jsc, label, query, attributes, KEY_VALUE, Component.empty(), false, comp ->
				label.setText(comp.copy().withStyle(st ->
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
					if(font != null) st = st.withFont(font);
					return st;
				}))
		);
		
		return label;
	}
}