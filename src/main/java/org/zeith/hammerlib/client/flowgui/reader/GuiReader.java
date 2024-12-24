package org.zeith.hammerlib.client.flowgui.reader;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.props.KeyMap;
import org.zeith.hammerlib.annotations.ide.*;
import org.zeith.hammerlib.api.data.DataNodeTransformer;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.OptionalBoolean;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.java.itf.FloatSupplier;
import org.zeith.hammerlib.util.mcf.Resources;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.zeith.hammerlib.client.flowgui.reader.ComDrivers.*;

@Slf4j
public abstract class GuiReader<T extends GuiObject>
{
	public static final Map<String, String> TAGS_TO_COMPONENTS = Util.make(new HashMap<>(), m ->
			Arrays.stream(FlowguiTags.class.getDeclaredFields())
					.filter(f -> String.class.equals(f.getType()) && f.getName().startsWith("COM_"))
					.forEach(f ->
							ReflectionUtil.fetchValue(f, null, String.class)
									.ifPresent(id -> m.put(f.getName().substring(4), id))
					)
	);
	
	@AllowJS
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final @Default("(q) => true") String KEY_IF = "if";
	
	@AllowedValues({ AllowedValues.BOOLEAN, "^int$" })
	public static final String KEY_CENTERED = "centered";
	
	@AllowJS
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("0") String
			KEY_X = "x",
			KEY_Y = "y";
	
	@AllowedValues(AllowedValues.NON_NEGATIVE_FLOAT)
	public static final String
			KEY_WIDTH = "width",
			KEY_HEIGHT = "height";
	
	@AllowJS
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("0") String KEY_ROTATION = "rotation";
	
	@AllowJS
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final @Default("1") String
			KEY_SCALE_UNIFIED = "scale",
			KEY_SCALE_X = "scale-x",
			KEY_SCALE_Y = "scale-y",
			KEY_SCALE_Z = "scale-z";
	
	@AllowedValues(AllowedValues.BOOLEAN)
	public static final String KEY_PIVOT_CENTER = "pivot-centered";
	@AllowJS
	@AllowedValues(AllowedValues.ANY_FLOAT)
	public static final String
			KEY_PIVOT_X = "pivot-x",
			KEY_PIVOT_Y = "pivot-y";
	
	@AllowedValues({ "^start$", "^left$", "^center$", "^right$", "^end$" })
	public static final @Default("left") String KEY_ALIGN_X = "align-x";
	
	@AllowedValues({ "^start$", "^top$", "^up$", "^center$", "^bottom$", "^down$", "^end$" })
	public static final @Default("left") String KEY_ALIGN_Y = "align-y";
	
	@AllowedValues(AllowedValues.RESOURCE_LOCATION)
	public static final @Required("") String KEY_CLASS = "class";
	
	@AllowedValues("^[^/]+$")
	public static final String KEY_ID = "id";
	
	protected abstract T readObject(KeyMap context, String name, IDataNode attributes);
	
	/**
	 * Special handling of non-standard nodes.
	 * <p>
	 * The standard nodes are: root, com, script, import, empty, img
	 */
	protected void handleExtraNodes(T object, KeyMap context, IDataNode node, FloatSupplier parentWidth, FloatSupplier parentHeight)
	{
	}
	
	/**
	 * Special handling for restoration of context. Called after this element has been added into the parent object, thus allowing to reconstruct full path, and using context to seek for the previous instance at given path.
	 * <p>
	 * Used by edit boxes to memoize their text values
	 */
	protected void finishBuilding(T object, KeyMap context)
	{
	}
	
	protected Supplier<RuntimeException> invalidField(IDataNode node, String fieldName)
	{
		return () -> new IllegalArgumentException("Field " + fieldName + " has invalid data (" + node.getString(fieldName) + ")!");
	}
	
	public final T read(KeyMap context, IDataNode attributes, FloatSupplier parentWidth, FloatSupplier parentHeight)
	{
		String id = attributes.getString(KEY_ID);
		if(id == null || id.isBlank())
		{
			var rng = context.getOrSupply(FlowguiRegistry.NAMEGEN_RANDOM, RandomSource::create);
			id = "gen:" + new UUID(rng.nextLong(), rng.nextLong());
		}
		
		var jsc = getJSContext(context);
		
		var query = context.get(FlowguiRegistry.QUERY);
		if(readBoolean(jsc, null, query, attributes, KEY_IF).get() == OptionalBoolean.OPTIONAL_FALSE)
			return null;
		
		var obj = readObject(context, id, attributes);
		if(obj == null) return null;
		
		obj.finishBuilding.add(() -> finishBuilding(obj, context));
		
		Set<String> keys = attributes.keys();
		
		//<editor-fold desc="Size">
		if(keys.contains(KEY_WIDTH)) attributes.getFloat(KEY_WIDTH).ifPresent(obj.elementWidth::set);
		if(keys.contains(KEY_HEIGHT)) attributes.getFloat(KEY_HEIGHT).ifPresent(obj.elementHeight::set);
		//</editor-fold>
		
		boolean[] scaledAxis = driveScale(jsc, query, attributes, obj);
		drivePosition(jsc, query, attributes, obj, parentWidth, parentHeight, scaledAxis);
		
		//<editor-fold desc="Rotation">
		driveFloat(jsc, obj, query, attributes, KEY_ROTATION, 0F, false, obj.elementRotation::set);
		//</editor-fold>
		
		//<editor-fold desc="Rotation Point">
		drivePivot(jsc, query, attributes, obj, scaledAxis);
		//</editor-fold>
		
		//<editor-fold desc="Child elements">
		int childCount = attributes.length();
		for(int i = 0; i < childCount; i++)
		{
			var attr = attributes.get(i);
			if(!(attr instanceof IDataNode node)) continue;
			
			var cName = node.getMyName().toLowerCase(Locale.ROOT);
			
			var tagName = TAGS_TO_COMPONENTS.get(cName);
			if(tagName != null)
			{
				var node2 = DataNodeTransformer.convertToComponent(node, Resources.location(tagName));
				var child = FlowguiRegistry.read(context, node2, obj::getUnscaledWidth, obj::getUnscaledHeight);
				if(child != null) obj.addChild(child);
				else HammerLib.LOG.warn("Failed to read Flowgui {} component: {}", tagName, readableName(node));
				continue;
			}
			
			switch(cName)
			{
				case "com" ->
				{
					var child = FlowguiRegistry.read(context, node, obj::getUnscaledWidth, obj::getUnscaledHeight);
					if(child != null) obj.addChild(child);
					else HammerLib.LOG.warn("Failed to read Flowgui component: {}", readableName(node));
				}
				case "import" ->
				{
					var node2 = DataNodeTransformer.convertToComponent(node, HLConstants.id("empty"));
					var child = FlowguiRegistry.read(context, node2, obj::getUnscaledWidth, obj::getUnscaledHeight);
					if(child != null)
					{
						var childContext = KeyMap.createHash().withAll(context);
						obj.addChild(child);
						var from = Resources.locationOrNull(node.getString("from"));
						var scene = from != null ? FlowguiRegistry.readRoot(from, childContext, child::getUnscaledWidth, child::getUnscaledHeight) : null;
						if(scene != null)
						{
							if(child.elementWidth.get() <= 0F) child.elementWidth.set(scene.getUnscaledWidth());
							if(child.elementHeight.get() <= 0F) child.elementHeight.set(scene.getUnscaledHeight());
							List<GuiObject> chs = new ArrayList<>();
							scene.getChildren().forEach(chs::add);
							for(GuiObject ch : chs)
							{
								scene.removeChild(ch.getName());
								child.addChild(ch);
							}
						} else HammerLib.LOG.warn("Failed to read Flowgui import: {}", readableName(node));
					} else HammerLib.LOG.warn("Failed to read Flowgui import as placeholder object: {}", readableName(node));
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
	
	private boolean[] driveScale(JsContext jsc, FlowQuery query, IDataNode attributes, GuiObject obj)
	{
		AtomicReference<Float> scaleX = new AtomicReference<>(1F);
		AtomicReference<Float> scaleY = new AtomicReference<>(1F);
		AtomicReference<Float> scaleZ = new AtomicReference<>(1F);
		AtomicReference<Float> mainScale = new AtomicReference<>(1F);
		
		boolean scaledAll = driveFloat(jsc, obj, query, attributes, KEY_SCALE_UNIFIED, 1F, false, s ->
				{
					mainScale.set(s);
					obj.elementScale.set(new Vec3(scaleX.get() * s, scaleY.get() * s, scaleZ.get() * s));
				}
		);
		
		boolean scaledX = driveFloat(jsc, obj, query, attributes, KEY_SCALE_X, 1F, false, s ->
				{
					scaleX.set(s);
					var es = obj.elementScale;
					var vec = es.get();
					es.set(new Vec3(s * mainScale.get(), vec.y, vec.z));
				}
		);
		boolean scaledY = driveFloat(jsc, obj, query, attributes, KEY_SCALE_Y, 1F, false, s ->
				{
					scaleY.set(s);
					var es = obj.elementScale;
					var vec = es.get();
					es.set(new Vec3(vec.x, s * mainScale.get(), vec.z));
				}
		);
		driveFloat(jsc, obj, query, attributes, KEY_SCALE_Z, 1F, false, s ->
				{
					scaleZ.set(s);
					var es = obj.elementScale;
					var vec = es.get();
					es.set(new Vec3(vec.x, vec.y, s * mainScale.get()));
				}
		);
		
		return new boolean[] { scaledAll || scaledX, scaledAll || scaledY };
	}
	
	private void drivePosition(JsContext jsc, FlowQuery query, IDataNode attributes, GuiObject obj, FloatSupplier parentWidth, FloatSupplier parentHeight, boolean[] scaledAxis)
	{
		Supplier<Alignment> alX = Alignment.readX(query, attributes.getString(KEY_ALIGN_X), Alignment.START);
		Supplier<Alignment> alY = Alignment.readY(query, attributes.getString(KEY_ALIGN_Y), Alignment.START);
		
		int centering;
		{
			if(attributes.getBoolean(KEY_CENTERED)) centering = 1;
			else if("int".equalsIgnoreCase(attributes.getString(KEY_CENTERED))) centering = 2;
			else centering = 0;
		}
		
		driveFloat(jsc, obj, query, attributes, KEY_X, 0F, scaledAxis[0], x -> obj.elementPosition.apply(pos0 ->
						pos0.withX(
								alX.get().apply(
										x,
										parentWidth.getAsFloat(),
										obj.getScaledWidth()
								)
						)
				)
		);
		
		driveFloat(jsc, obj, query, attributes, KEY_Y, 0F, scaledAxis[1], y -> obj.elementPosition.apply(pos0 ->
						pos0.withY(
								alY.get().apply(
										y,
										parentHeight.getAsFloat(),
										obj.getScaledHeight()
								)
						)
				)
		);
		
		switch(centering)
		{
			case 1 ->
			{
				obj.centered(parentWidth.getAsFloat(), parentHeight.getAsFloat());
				obj.onPreRender((f, mouse) -> obj.centered(parentWidth.getAsFloat(), parentHeight.getAsFloat()));
			}
			case 2 ->
			{
				obj.centered((int) parentWidth.getAsFloat(), (int) parentHeight.getAsFloat());
				obj.onPreRender((f, mouse) -> obj.centered((int) parentWidth.getAsFloat(), (int) parentHeight.getAsFloat()));
			}
		}
	}
	
	private void drivePivot(JsContext jsc, FlowQuery query, IDataNode attributes, GuiObject obj, boolean[] scaledAxis)
	{
		if(attributes.getBoolean(KEY_PIVOT_CENTER))
		{
			obj.pivotAtCenter();
			if(scaledAxis[0] || scaledAxis[1]) obj.onPreRender((f, mouse) -> obj.pivotAtCenter());
		}
		
		driveFloat(jsc, obj, query, attributes, KEY_PIVOT_X, null, false, x -> obj.elementPivot.apply(p -> p.withX(x)));
		driveFloat(jsc, obj, query, attributes, KEY_PIVOT_Y, null, false, y -> obj.elementPivot.apply(p -> p.withY(y)));
	}
	
	protected JsContext getJSContext(KeyMap map)
	{
		return map.get(FlowguiRegistry.JS_CONTEXT);
	}
	
	protected FlowQuery getQuery(KeyMap map)
	{
		return map.get(FlowguiRegistry.QUERY);
	}
}