package org.zeith.hammerlib.client.flowgui.reader;

import com.google.common.base.MoreObjects;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.zeith.hammerlib.annotations.ide.AllowedValues;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.core.js.CallerSpec;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.java.cbqs.cbq3.*;
import org.zeith.hammerlib.util.java.itf.BooleanConsumer;
import org.zeith.hammerlib.util.java.itf.FloatConsumer;
import org.zeith.hammerlib.util.shaded.json.JSONObject;

import java.util.*;
import java.util.function.*;

@Slf4j
public class ComDrivers
{
	public static boolean driveComponent(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String name, Component defaultValue, boolean alwaysDrive, Consumer<Component> driver)
	{
		var parsed = readComponent(jsc, name, query, attributes, self);
		
		var fallback = Cast.constant(Optional.ofNullable(defaultValue));
		
		boolean dynamic = alwaysDrive || !parsed.getClass().getName().contains(Cast.class.getName());
		
		// Non-Constant lambda
		if(dynamic)
			self.onPreRender((time, mouse) ->
					Optional.ofNullable(parsed.get()).or(fallback).ifPresent(driver)
			);
		
		Optional.ofNullable(parsed.get()).or(fallback).ifPresent(driver);
		
		return dynamic;
	}
	
	public static boolean driveBool(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String name, Boolean defaultValue, boolean alwaysDrive, BooleanConsumer driver)
	{
		var parsed = readBoolean(jsc, self, query, attributes, name);
		
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
	
	public static boolean driveFloat(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String name, Float defaultValue, boolean alwaysDrive, FloatConsumer driver)
	{
		var parsed = readFloat(jsc, self, query, attributes, name);
		
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
	
	public static boolean driveInt(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String name, Integer defaultValue, boolean alwaysDrive, IntConsumer driver)
	{
		var parsed = readInt(jsc, self, query, attributes, name);
		
		var fallback = defaultValue != null ? OptionalInt.of(defaultValue) : OptionalInt.empty();
		
		boolean dynamic = alwaysDrive || !parsed.getClass().getName().contains(Cast.class.getName());
		
		// Non-Constant lambda
		if(dynamic)
			self.onPreRender((time, mouse) ->
					parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver))
			);
		
		parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver));
		
		return dynamic;
	}
	
	public static boolean driveColor(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String name, Integer defaultValue, boolean alwaysDrive, IntConsumer driver)
	{
		var parsed = readColor(jsc, self, query, attributes, name);
		
		var fallback = defaultValue != null ? OptionalInt.of(defaultValue) : OptionalInt.empty();
		
		boolean dynamic = alwaysDrive || !parsed.getClass().getName().contains(Cast.class.getName());
		
		// Non-Constant lambda
		if(dynamic)
			self.onPreRender((time, mouse) ->
					parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver))
			);
		
		parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver));
		
		return dynamic;
	}
	
	private static final CallerSpec CBQ_SPEC = new CallerSpec("invoke", false);
	private static final CallerSpec CBQ_RET_SPEC = new CallerSpec("invoke", true);
	
	public static Supplier<Component> readComponent(JsContext jsc, String from, FlowQuery query, IDataNode attributes, GuiObject self)
	{
		var str = attributes.getString(from);
		if(str == null || str.isBlank()) return Cast.constant(Component.empty());
		
		var cbq = JsContext.isScript(str) ? jsc.eval(Callback3.class, str, CBQ_RET_SPEC) : null;
		if(cbq != null)
		{
			return () ->
			{
				try
				{
					Object s = cbq.invoke(query, self, null);
					return s instanceof Component com ? com : componentFromString(Objects.toString(s));
				} catch(Exception e)
				{
					return Component.literal("Error: " + e)
							.setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
				}
			};
		}
		
		return Cast.constant(componentFromString(str));
	}
	
	public static Component componentFromString(String str){
		try
		{
			return Component.Serializer.fromJson(str);
		} catch(Exception e)
		{
		}
		
		return Component.translatable(str);
	}
	
	public static Supplier<OptionalBoolean> readBoolean(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String from)
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
	
	public static Supplier<OptionalFloat> readFloat(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String from)
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
	
	public static Supplier<OptionalInt> readInt(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String from)
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
	
	public static Supplier<OptionalInt> readColor(JsContext jsc, GuiObject self, FlowQuery query, IDataNode attributes, String from)
	{
		var of = attributes.getInt(from);
		if(of.isPresent()) return Cast.constant(of);
		
		String str = attributes.getString(from);
		if(str == null || str.isBlank()) return Cast.constant(OptionalInt.empty());
		if(str != null && str.matches(AllowedValues.HEX_COLOR))
			return Cast.constant(OptionalInt.of(Integer.parseInt(str.substring(1), 16)));
		
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
	
	public static Runnable readCallback(JsContext jsc, GuiObject self, IDataNode node, FlowQuery query, String from, boolean returns)
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
	
	public static String readableName(IDataNode node)
	{
		var keys = node.keys();
		return "<%s %s %s/>"
				.formatted(node.getMyName(),
						keys.contains(GuiReader.KEY_CLASS) ? "class=%s".formatted(JSONObject.quote(node.getString(GuiReader.KEY_CLASS))) : "",
						keys.contains(GuiReader.KEY_ID) ? "id=%s".formatted(JSONObject.quote(node.getString(GuiReader.KEY_ID))) : ""
				).trim();
	}
}