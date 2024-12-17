package org.zeith.hammerlib.client.flowgui.reader;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.*;
import java.util.function.Supplier;

public abstract class GuiReader<T extends GuiObject>
{
	@AllowedValues({ AllowedValues.BOOLEAN, "^int$" })
	public static final String KEY_CENTERED = "centered";
	
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_PIVOT_CENTER = "pivot-centered";
	
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("0") String
			KEY_X = "x",
			KEY_Y = "y";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final String
			KEY_WIDTH = "width",
			KEY_HEIGHT = "height";
	
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("0") String KEY_ROTATION = "rotation";
	
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("1") String KEY_SCALE_X = "scale-x";
	
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("1") String KEY_SCALE_Y = "scale-y";
	
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("1") String KEY_SCALE_Z = "scale-z";
	
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final String KEY_PIVOT_X = "pivot-x";
	
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final String KEY_PIVOT_Y = "pivot-y";
	
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Required String KEY_CLASS = "class";
	
	@AllowedValues("^[^/]+$")
	public static final String KEY_ID = "id";
	
	protected abstract T readObject(KeyMap context, String name, IDataNode attributes);
	
	/**
	 * Special handling of non-standard nodes.
	 * <p>
	 * The standard nodes are: root, com, script, import
	 */
	protected void handleExtraNodes(T object, KeyMap context, IDataNode node, float parentWidth, float parentHeight)
	{
	}
	
	protected Supplier<RuntimeException> invalidField(IDataNode node, String fieldName)
	{
		return () -> new IllegalArgumentException("Field " + fieldName + " has invalid data (" + node.getString(fieldName) + ")!");
	}
	
	public final T read(KeyMap context, IDataNode attributes, float parentWidth, float parentHeight)
	{
		String id = attributes.getString(KEY_ID);
		if(id == null || id.isBlank()) id = UUID.randomUUID().toString();
		Set<String> keys = attributes.keys();
		
		var obj = readObject(context, id, attributes);
		if(obj == null) return null;
		
		if(keys.contains(KEY_X) && keys.contains(KEY_Y))
		{
			var x = attributes.getFloat(KEY_X);
			var y = attributes.getFloat(KEY_Y);
			if(x.isPresent() && y.isPresent()) obj.pos(x.getAsFloat(), y.getAsFloat());
		}
		
		if(keys.contains(KEY_WIDTH)) attributes.getFloat(KEY_WIDTH).ifPresent(obj.elementWidth::set);
		if(keys.contains(KEY_HEIGHT)) attributes.getFloat(KEY_HEIGHT).ifPresent(obj.elementHeight::set);
		
		if(keys.contains(KEY_ROTATION)) attributes.getFloat(KEY_ROTATION).ifPresent(obj.elementRotation::set);
		
		Vector3d scale = new Vector3d(1);
		if(keys.contains(KEY_SCALE_X)) attributes.getFloat(KEY_SCALE_X).ifPresent(f -> scale.x = f);
		if(keys.contains(KEY_SCALE_Y)) attributes.getFloat(KEY_SCALE_Y).ifPresent(f -> scale.y = f);
		if(keys.contains(KEY_SCALE_Z)) attributes.getFloat(KEY_SCALE_Z).ifPresent(f -> scale.z = f);
		obj.elementScale.set(new Vec3(scale.x, scale.y, scale.z));
		
		if(attributes.getBoolean(KEY_CENTERED)) obj.centered(parentWidth, parentHeight);
		else if("int".equalsIgnoreCase(attributes.getString(KEY_CENTERED))) obj.centered((int) parentWidth, (int) parentHeight);
		if(attributes.getBoolean(KEY_PIVOT_CENTER)) obj.pivotAtCenter();
		
		int childCount = attributes.length();
		for(int i = 0; i < childCount; i++)
		{
			var attr = attributes.get(i);
			if(!(attr instanceof IDataNode node)) continue;
			
			var cName = node.getMyName().toLowerCase(Locale.ROOT);
			
			switch(cName)
			{
				case "com" ->
				{
					var child = FlowguiRegistry.read(context, node, obj.elementWidth.get(), obj.elementHeight.get());
					if(child != null) obj.addChild(child);
					else HammerLib.LOG.warn("Failed to read Flowgui component: {}", readableName(node));
				}
				case "import" ->
				{
					var from = Resources.locationOrNull(node.getString("from"));
					var child = from != null ? FlowguiRegistry.readRoot(from, context, obj.elementWidth.get(), obj.elementHeight.get()) : null;
					if(child != null) obj.addChild(child);
					else HammerLib.LOG.warn("Failed to read Flowgui import: {}", readableName(node));
				}
				case "root" ->
				{
					HammerLib.LOG.error("Attempted to insert {} inside of component {}. This is not allowed!", readableName(node), readableName(attributes));
				}
				case "script" ->
				{
					// TODO
				}
				default -> handleExtraNodes(obj, context, node, parentWidth, parentHeight);
			}
		}
		
		return obj;
	}
	
	public static String readableName(IDataNode mess)
	{
		var keys = mess.keys();
		return "<%s %s %s/>"
				.formatted(mess.getMyName(),
						keys.contains(KEY_CLASS) ? "class=%s".formatted(JSONObject.quote(mess.getString(KEY_CLASS))) : "",
						keys.contains(KEY_ID) ? "id=%s".formatted(JSONObject.quote(mess.getString(KEY_ID))) : ""
				).trim();
	}
}