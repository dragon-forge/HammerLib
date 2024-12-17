package org.zeith.hammerlib.client.flowgui.reader;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.util.math.Point;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.*;
import java.util.function.Supplier;

public abstract class GuiReader<T extends GuiObject>
{
	@AllowedValues({ AllowedValues.BOOLEAN, "^int$" })
	public static final String KEY_CENTERED = "centered";
	
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
	public static final @Default("1") String
			KEY_SCALE_UNIFIED = "scale",
			KEY_SCALE_X = "scale-x",
			KEY_SCALE_Y = "scale-y",
			KEY_SCALE_Z = "scale-z";
	
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_PIVOT_CENTER = "pivot-centered";
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final String
			KEY_PIVOT_X = "pivot-x",
			KEY_PIVOT_Y = "pivot-y";
	
	@AllowedValues({ "^start$", "^left$", "^center$", "^right$", "^end$" })
	public static final @Default("left") String KEY_ALIGN_X = "align-x";
	
	@AllowedValues({ "^start$", "^top$", "^up$", "^center$", "^bottom$", "^down$", "^end$" })
	public static final @Default("left") String KEY_ALIGN_Y = "align-y";
	
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
		
		var obj = readObject(context, id, attributes);
		if(obj == null) return null;
		
		Set<String> keys = attributes.keys();
		
		//<editor-fold desc="Size">
		if(keys.contains(KEY_WIDTH)) attributes.getFloat(KEY_WIDTH).ifPresent(obj.elementWidth::set);
		if(keys.contains(KEY_HEIGHT)) attributes.getFloat(KEY_HEIGHT).ifPresent(obj.elementHeight::set);
		//</editor-fold>
		
		//<editor-fold desc="Scale">
		Vector3f scale = new Vector3f(1);
		if(keys.contains(KEY_SCALE_X)) attributes.getFloat(KEY_SCALE_X).ifPresent(f -> scale.x = f);
		if(keys.contains(KEY_SCALE_Y)) attributes.getFloat(KEY_SCALE_Y).ifPresent(f -> scale.y = f);
		if(keys.contains(KEY_SCALE_Z)) attributes.getFloat(KEY_SCALE_Z).ifPresent(f -> scale.z = f);
		if(keys.contains(KEY_SCALE_UNIFIED)) attributes.getFloat(KEY_SCALE_UNIFIED).ifPresent(scale::mul);
		obj.elementScale.set(new Vec3(scale.x, scale.y, scale.z));
		//</editor-fold>
		
		//<editor-fold desc="Position">
		Point pos = new Point(0F, 0F);
		if(keys.contains(KEY_X)) attributes.getFloat(KEY_X).ifPresent(pos::setX);
		if(keys.contains(KEY_Y)) attributes.getFloat(KEY_Y).ifPresent(pos::setY);
		pos.setX(Alignment.readX(attributes.getString(KEY_ALIGN_X), Alignment.START).apply(pos.x(), parentWidth, scale.x * obj.elementWidth.get()));
		pos.setY(Alignment.readY(attributes.getString(KEY_ALIGN_Y), Alignment.START).apply(pos.y(), parentHeight, scale.y * obj.elementHeight.get()));
		obj.elementPosition.set(pos);
		
		if(attributes.getBoolean(KEY_CENTERED)) obj.centered(parentWidth, parentHeight);
		else if("int".equalsIgnoreCase(attributes.getString(KEY_CENTERED))) obj.centered((int) parentWidth, (int) parentHeight);
		//</editor-fold>
		
		//<editor-fold desc="Rotation">
		if(keys.contains(KEY_ROTATION)) attributes.getFloat(KEY_ROTATION).ifPresent(obj.elementRotation::set);
		//</editor-fold>
		
		//<editor-fold desc="Rotation Point">
		if(attributes.getBoolean(KEY_PIVOT_CENTER)) obj.pivotAtCenter();
		
		attributes.getFloat(KEY_PIVOT_X).ifPresent(px -> obj.elementPivot.apply(p -> p.offset(px, 0)));
		attributes.getFloat(KEY_PIVOT_Y).ifPresent(py -> obj.elementPivot.apply(p -> p.offset(0, py)));
		//</editor-fold>
		
		//<editor-fold desc="Child elements">
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
		//</editor-fold>
		
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