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
import org.zeith.hammerlib.core.js.CallerSpec;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.java.cbqs.cbq3.*;
import org.zeith.hammerlib.util.java.itf.*;
import org.zeith.hammerlib.util.mcf.Resources;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

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
		if(readBoolean(jsc, KEY_IF, query, attributes, null).get() == OptionalBoolean.OPTIONAL_FALSE)
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
	
	protected boolean driveBool(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String name, Boolean defaultValue, boolean alwaysDrive, BooleanConsumer driver)
	{
		var parsed = readBoolean(jsc, name, query, attributes, self);
		
		var fallback = defaultValue != null ? OptionalBoolean.of(defaultValue) : OptionalBoolean.empty();
		
		boolean dynamic = alwaysDrive || !parsed.getClass().getName().contains(Cast.class.getName());
		
		// Non-Constant lambda
		if(dynamic)
			self.onPreRender((time, mouse) ->
					parsed.get().or(fallback).ifPresent(driver)
			);
		
		parsed.get().or(fallback).ifPresent(driver);
		
		return dynamic;
	}
	
	protected boolean driveFloat(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String name, Float defaultValue, boolean alwaysDrive, FloatConsumer driver)
	{
		var parsed = readFloat(jsc, name, query, attributes, self);
		
		var fallback = defaultValue != null ? OptionalFloat.of(defaultValue) : OptionalFloat.empty();
		
		boolean dynamic = alwaysDrive || !parsed.getClass().getName().contains(Cast.class.getName());
		
		// Non-Constant lambda
		if(dynamic)
			self.onPreRender((time, mouse) ->
					parsed.get().or(fallback).ifPresent(driver)
			);
		
		parsed.get().or(fallback).ifPresent(driver);
		
		return dynamic;
	}
	
	private static final CallerSpec CBQ_SPEC = new CallerSpec("invoke", false);
	private static final CallerSpec CBQ_RET_SPEC = new CallerSpec("invoke", true);
	
	protected Supplier<OptionalBoolean> readBoolean(JsContext jsc, String from, FlowQuery query, IDataNode attributes, GuiObject self)
	{
		var expression = attributes.getString(from);
		if("true".equalsIgnoreCase(expression) || "false".equalsIgnoreCase(expression))
			return Cast.constant(OptionalBoolean.of(Boolean.parseBoolean(expression)));
		if(expression == null) return Cast.constant(OptionalBoolean.empty());
		
		var str = attributes.getString(from);
		if(str == null) return Cast.constant(OptionalBoolean.empty());
		
		var cbq = jsc.eval(BoolCallback3.class, str, CBQ_RET_SPEC);
		if(cbq == null) return Cast.constant(OptionalBoolean.empty());
		
		return () ->
		{
			try
			{
				return OptionalBoolean.of(cbq.invoke(query, self, null));
			} catch(Exception e)
			{
				return OptionalBoolean.empty();
			}
		};
	}
	
	protected Supplier<OptionalFloat> readFloat(JsContext jsc, String from, FlowQuery query, IDataNode attributes, GuiObject self)
	{
		var of = attributes.getFloat(from);
		if(of.isPresent()) return Cast.constant(of);
		
		var str = attributes.getString(from);
		if(str == null) return Cast.constant(OptionalFloat.empty());
		
		var cbq = jsc.eval(DoubleCallback3.class, str, CBQ_RET_SPEC);
		if(cbq == null) return Cast.constant(OptionalFloat.empty());
		
		return () ->
		{
			try
			{
				return OptionalFloat.of((float) cbq.invoke(query, self, null));
			} catch(Exception e)
			{
				return OptionalFloat.empty();
			}
		};
	}
	
	protected Supplier<OptionalInt> readInt(JsContext jsc, String from, FlowQuery query, IDataNode attributes, GuiObject self)
	{
		var of = attributes.getInt(from);
		if(of.isPresent()) return Cast.constant(of);
		
		var str = attributes.getString(from);
		if(str == null) return Cast.constant(OptionalInt.empty());
		
		var cbq = jsc.eval(IntCallback3.class, str, CBQ_RET_SPEC);
		if(cbq == null) return Cast.constant(OptionalInt.empty());
		
		return () ->
		{
			try
			{
				return OptionalInt.of(cbq.invoke(query, self, null));
			} catch(Exception e)
			{
				return OptionalInt.empty();
			}
		};
	}
	
	protected Runnable readCallback(JsContext jsc, String from, IDataNode node, FlowQuery query, GuiObject self, boolean returns)
	{
		var expression = node.getString(from);
		if(expression == null || expression.isBlank()) return () ->
		{
		};
		
		var cbq = jsc.eval(Callback3.class, expression, returns ? CBQ_RET_SPEC : CBQ_SPEC);
		if(cbq == null) return () ->
		{
		};
		
		return () ->
		{
			try
			{
				cbq.invoke(query, self, null);
			} catch(RuntimeException e)
			{
				log.error("Failed to invoke callback {} (code: {})", readableName(node), expression);
			}
		};
	}
	
	protected JsContext getJSContext(KeyMap map)
	{
		return map.get(FlowguiRegistry.JS_CONTEXT);
	}
	
	public static String readableName(IDataNode node)
	{
		var keys = node.keys();
		return "<%s %s %s/>"
				.formatted(node.getMyName(),
						keys.contains(KEY_CLASS) ? "class=%s".formatted(JSONObject.quote(node.getString(KEY_CLASS))) : "",
						keys.contains(KEY_ID) ? "id=%s".formatted(JSONObject.quote(node.getString(KEY_ID))) : ""
				).trim();
	}
}