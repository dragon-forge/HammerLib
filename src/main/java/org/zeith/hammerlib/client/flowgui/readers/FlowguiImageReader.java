package org.zeith.hammerlib.client.flowgui.readers;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.client.render.texture.GuiTexture;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.mcf.Resources;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("image")
public class FlowguiImageReader
		extends GuiReader<GuiImageObject>
{
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Required String KEY_TEXTURE = "texture";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required String KEY_U_COORD = "u-coord";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required String KEY_V_COORD = "v-coord";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required String KEY_IMAGE_WIDTH = "image-width";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required String KEY_IMAGE_HEIGHT = "image-height";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("256") String KEY_FILE_WIDTH = "file-width";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("256") String KEY_FILE_HEIGHT = "file-height";
	
	@Override
	protected GuiImageObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		GuiTexture texture = GuiTexture.of(Resources.location(attributes.getString(KEY_TEXTURE)));
		
		float uOffset = attributes.getFloat(KEY_U_COORD).filter(f -> f >= 0).orElseThrow(invalidField(attributes, KEY_U_COORD));
		float vOffset = attributes.getFloat(KEY_V_COORD).filter(f -> f >= 0).orElseThrow(invalidField(attributes, KEY_V_COORD));
		float width = attributes.getInt(KEY_IMAGE_WIDTH).stream().filter(i -> i > 0).findFirst().orElseThrow(invalidField(attributes, KEY_IMAGE_WIDTH));
		float height = attributes.getInt(KEY_IMAGE_HEIGHT).stream().filter(i -> i > 0).findFirst().orElseThrow(invalidField(attributes, KEY_IMAGE_HEIGHT));
		float txWidth = attributes.getInt(KEY_FILE_WIDTH).stream().filter(i -> i > 0).findFirst().orElse(256);
		float txHeight = attributes.getInt(KEY_FILE_HEIGHT).stream().filter(i -> i > 0).findFirst().orElse(256);
		
		return new GuiImageObject(name, texture, uOffset, vOffset, width, height, txWidth, txHeight);
	}
}