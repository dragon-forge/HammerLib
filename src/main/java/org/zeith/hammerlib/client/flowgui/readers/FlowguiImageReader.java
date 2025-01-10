package org.zeith.hammerlib.client.flowgui.readers;

import com.google.common.base.MoreObjects;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.FlowguiShaderRegistry;
import org.zeith.hammerlib.client.flowgui.objects.GuiImageObject;
import org.zeith.hammerlib.client.flowgui.reader.FlowguiReader;
import org.zeith.hammerlib.client.flowgui.reader.GuiReader;
import org.zeith.hammerlib.client.render.texture.GuiTexture;
import org.zeith.hammerlib.client.utils.IRenderTypeFactory;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Suppliers2;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Namespace(HLConstants.MOD_ID)
@FlowguiReader("image")
public class FlowguiImageReader
		extends GuiReader<GuiImageObject>
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
	public static final @Required("0") String KEY_U_COORD = "u-coord";
	
	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final @Required("0") String KEY_V_COORD = "v-coord";
	
	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final String KEY_RENDER_WIDTH = "render-width";
	
	@AllowJS
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final String KEY_RENDER_HEIGHT = "render-height";
	
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
	protected GuiImageObject readObject(KeyMap map, String name, IDataNode node)
	{
		AtomicReference<GuiImageObject> self = new AtomicReference<>();
		var ctx = getDriverContext(map, node, self);
		
		Supplier<IRenderTypeFactory> shader = Suppliers2.map(readString(ctx, KEY_SHADER), s ->
				FlowguiShaderRegistry.get(Resources.locationOrNull(MoreObjects.firstNonNull(s, "minecraft:gui")))
		);
		var texture = Suppliers2.map(readString(ctx, KEY_TEXTURE), str ->
				GuiTexture.of(shader.get(), Resources.locationOrNull(str))
		);
		
		var image = new GuiImageObject(name, texture, 0, 0, 0, 0, 256, 256);
		self.set(image);
		
		driveFloat(ctx, KEY_U_COORD, null, false, image.textureUOffset::set);
		driveFloat(ctx, KEY_V_COORD, null, false, image.textureVOffset::set);
		
		boolean drivingWidth =
				driveFloat(ctx, KEY_RENDER_WIDTH, null, false, image.elementWidth::set)
				|| node.keys().contains(KEY_RENDER_WIDTH);
		
		boolean drivingHeight =
				driveFloat(ctx, KEY_RENDER_HEIGHT, null, false, image.elementHeight::set)
				|| node.keys().contains(KEY_RENDER_HEIGHT);
		
		driveFloat(ctx, KEY_IMAGE_WIDTH, null, false, v ->
				{
					image.imageWidth.set(v);
					if(!drivingWidth) image.elementWidth.set(v);
				}
		);
		driveFloat(ctx, KEY_IMAGE_HEIGHT, null, false, v ->
				{
					image.imageHeight.set(v);
					if(!drivingHeight) image.elementHeight.set(v);
				}
		);
		
		driveFloat(ctx, KEY_FILE_WIDTH, 256F, false, image.fileWidth::set);
		driveFloat(ctx, KEY_FILE_HEIGHT, 256F, false, image.fileHeight::set);
		
		image.size(image.imgWidth, image.imgHeight);
		
		return image;
	}
}