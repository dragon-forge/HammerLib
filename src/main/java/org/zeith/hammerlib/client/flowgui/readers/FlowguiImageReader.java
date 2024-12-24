package org.zeith.hammerlib.client.flowgui.readers;

import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.reader.*;
import org.zeith.hammerlib.client.render.texture.GuiTexture;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.mcf.Resources;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.driveFloat;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("image")
public class FlowguiImageReader
		extends GuiReader<GuiImageObject>
{
	@FileReference(
			regex = "^(?<modid>[a-z0-9_.-]+):(?<path>[a-z0-9_./-]+)$",
			value = "resources/assets/%modid%/%path%"
	)
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Required("minecraft:textures/gui/container/furnace.png") String KEY_TEXTURE = "src";

	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required("0") String KEY_U_COORD = "u-coord";

	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required("0") String KEY_V_COORD = "v-coord";

	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required("176") String KEY_IMAGE_WIDTH = "image-width";

	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required("166") String KEY_IMAGE_HEIGHT = "image-height";

	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("256") String KEY_FILE_WIDTH = "file-width";

	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Default("256") String KEY_FILE_HEIGHT = "file-height";
	
	@Override
	protected GuiImageObject readObject(KeyMap context, String name, IDataNode attributes)
	{
		var root = context.get(FlowguiRegistry.GUI_ROOT);
		var query = context.get(FlowguiRegistry.QUERY);
		
		GuiTexture texture = GuiTexture.of(Resources.location(attributes.getString(KEY_TEXTURE)));
		
		var image = new GuiImageObject(name, texture, 0, 0, 0, 0, 256, 256);
		
		var jsc = getJSContext(context);
		driveFloat(jsc, image, query, attributes, KEY_U_COORD, null, false, image.textureUOffset::set);
		driveFloat(jsc, image, query, attributes, KEY_V_COORD, null, false, image.textureVOffset::set);
		driveFloat(jsc, image, query, attributes, KEY_IMAGE_WIDTH, null, false, image.imageWidth::set);
		driveFloat(jsc, image, query, attributes, KEY_IMAGE_HEIGHT, null, false, image.imageHeight::set);
		driveFloat(jsc, image, query, attributes, KEY_FILE_WIDTH, 256F, false, image.fileWidth::set);
		driveFloat(jsc, image, query, attributes, KEY_FILE_HEIGHT, 256F, false, image.fileHeight::set);
		
		image.size(image.imgWidth, image.imgHeight);
		
		return image;
	}
}