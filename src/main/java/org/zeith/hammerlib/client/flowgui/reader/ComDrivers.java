package org.zeith.hammerlib.client.flowgui.reader;

import com.google.common.base.MoreObjects;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.LowerCaseEnumTypeAdapterFactory;
import org.jetbrains.annotations.NotNull;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.zeith.hammerlib.annotations.ide.AllowedValues;
import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.core.js.CallerSpec;
import org.zeith.hammerlib.core.js.ObjectMirrorConverter;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.java.cbqs.cbq3.*;
import org.zeith.hammerlib.util.java.itf.BooleanConsumer;
import org.zeith.hammerlib.util.java.itf.FloatConsumer;
import org.zeith.hammerlib.util.shaded.json.*;

import java.util.*;
import java.util.function.*;

@Slf4j
public class ComDrivers
{
	public static boolean driveComponent(DriverContext ctx, String name, Component defaultValue, boolean alwaysDrive, Consumer<Component> driver)
	{
		var parsed = readComponent(ctx, name);
		
		var fallback = Cast.constant(Optional.ofNullable(defaultValue));
		
		boolean dynamic = alwaysDrive || !isConstant(parsed);
		
		// Non-Constant lambda
		if(dynamic)
			ctx.onPreRender((time, mouse) ->
					Optional.ofNullable(parsed.get()).or(fallback).ifPresent(driver)
			);
		
		Optional.ofNullable(parsed.get()).or(fallback).ifPresent(driver);
		
		return dynamic;
	}
	
	public static boolean driveString(DriverContext ctx, String name, String defaultValue, boolean alwaysDrive, Consumer<String> driver)
	{
		var parsed = readString(ctx, name);
		
		var fallback = Cast.constant(Optional.ofNullable(defaultValue));
		
		boolean dynamic = alwaysDrive || !isConstant(parsed);
		
		// Non-Constant lambda
		if(dynamic)
			ctx.onPreRender((time, mouse) ->
					Optional.ofNullable(parsed.get()).or(fallback).ifPresent(driver)
			);
		
		Optional.ofNullable(parsed.get()).or(fallback).ifPresent(driver);
		
		return dynamic;
	}
	
	public static boolean driveBool(DriverContext ctx, String name, Boolean defaultValue, boolean alwaysDrive, BooleanConsumer driver)
	{
		var parsed = readBoolean(ctx, name);
		
		var fallback = defaultValue != null ? OptionalBoolean.of(defaultValue) : OptionalBoolean.empty();
		
		boolean dynamic = alwaysDrive || !isConstant(parsed);
		
		// Non-Constant lambda
		if(dynamic)
			ctx.onPreRender((time, mouse) ->
					parsed.get().or(fallback).ifPresent(driver)
			);
		
		parsed.get().or(fallback).ifPresent(driver);
		
		return dynamic;
	}
	
	public static boolean driveFloat(DriverContext ctx, String name, Float defaultValue, boolean alwaysDrive, FloatConsumer driver)
	{
		var parsed = readFloat(ctx, name);
		
		var fallback = defaultValue != null ? OptionalFloat.of(defaultValue) : OptionalFloat.empty();
		
		boolean dynamic = alwaysDrive || !isConstant(parsed);
		
		// Non-Constant lambda
		if(dynamic)
			ctx.onPreRender((time, mouse) ->
					parsed.get().or(fallback).ifPresent(driver)
			);
		
		parsed.get().or(fallback).ifPresent(driver);
		
		return dynamic;
	}
	
	public static boolean driveInt(DriverContext ctx, String name, Integer defaultValue, boolean alwaysDrive, IntConsumer driver)
	{
		var parsed = readInt(ctx, name);
		
		var fallback = defaultValue != null ? OptionalInt.of(defaultValue) : OptionalInt.empty();
		
		boolean dynamic = alwaysDrive || !isConstant(parsed);
		
		// Non-Constant lambda
		if(dynamic)
			ctx.onPreRender((time, mouse) ->
					parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver))
			);
		
		parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver));
		
		return dynamic;
	}
	
	public static boolean driveColor(DriverContext ctx, String name, Integer defaultValue, boolean alwaysDrive, IntConsumer driver)
	{
		var parsed = readColor(ctx, name);
		
		var fallback = defaultValue != null ? OptionalInt.of(defaultValue) : OptionalInt.empty();
		
		boolean dynamic = alwaysDrive || !isConstant(parsed);
		
		// Non-Constant lambda
		if(dynamic)
			ctx.onPreRender((time, mouse) ->
					parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver))
			);
		
		parsed.get().ifPresentOrElse(driver, () -> fallback.ifPresent(driver));
		
		return dynamic;
	}
	
	private static final CallerSpec CBQ_SPEC = new CallerSpec("invoke", false);
	private static final CallerSpec CBQ_RET_SPEC = new CallerSpec("invoke", true);
	
	@NotNull
	public static Supplier<Component> readComponent(DriverContext ctx, String from)
	{
		var str = ctx.getString(from);
		if(str == null || str.isBlank()) return Cast.constant(Component.empty());
		
		var cbq = JsContext.isScript(str) ? ctx.eval(Callback3.class, str, CBQ_RET_SPEC) : null;
		if(cbq != null)
		{
			return () ->
			{
				try
				{
					Object s = cbq.invoke(ctx.query(), ctx.self(), null);
					if(s instanceof ScriptObjectMirror som) s = ObjectMirrorConverter.toGson(som);
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
	
	@NotNull
	public static Supplier<List<Component>> readComponents(DriverContext ctx, String from)
	{
		var str = ctx.getString(from);
		if(str == null || str.isBlank()) return Cast.constant(List.of());
		
		var cbq = JsContext.isScript(str) ? ctx.eval(Callback3.class, str, CBQ_RET_SPEC) : null;
		if(cbq != null)
		{
			return () ->
			{
				try
				{
					Object s = cbq.invoke(ctx.query(), ctx.self(), null);
					if(s instanceof ScriptObjectMirror som) s = ObjectMirrorConverter.toGson(som);
					return s instanceof Component[] coms
						   ? List.of(coms)
						   : s instanceof Component com
							 ? List.of(com)
							 : componentsFromString(Objects.toString(s));
				} catch(Exception e)
				{
					return List.of(Component.literal("Error: " + e)
							.setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
				}
			};
		}
		
		return Cast.constant(componentsFromString(str));
	}
	
	@NotNull
	public static Supplier<String> readString(DriverContext ctx, String from)
	{
		var str = ctx.getString(from);
		if(str == null || str.isBlank()) return Cast.constant("");
		
		var cbq = JsContext.isScript(str) ? ctx.eval(Callback3.class, str, CBQ_RET_SPEC) : null;
		if(cbq != null)
		{
			return () ->
			{
				try
				{
					Object s = cbq.invoke(ctx.query(), ctx.self(), null);
					return s instanceof CharSequence com ? com.toString() : Objects.toString(s);
				} catch(Exception e)
				{
					return e.toString();
				}
			};
		}
		
		return Cast.constant(str);
	}
	
	@NotNull
	public static <T> Supplier<T> readObject(DriverContext ctx, String from, Class<T> returnType)
	{
		var str = ctx.getString(from);
		if(str == null || str.isBlank()) return Cast.constant(null);
		
		var cbq = JsContext.isScript(str) ? ctx.eval(Callback3.class, str, CBQ_RET_SPEC) : null;
		if(cbq != null)
		{
			return () ->
			{
				try
				{
					Object s = cbq.invoke(ctx.query(), ctx.self(), null);
					return returnType.isInstance(s) ? (T) s : ObjectMirrorConverter.unwrap(s, returnType);
				} catch(Exception e)
				{
					return null;
				}
			};
		}
		
		return Cast.constant(null);
	}
	
	public static Component componentFromString(String str)
	{
		try
		{
			return Component.Serializer.fromJson(str);
		} catch(Exception e)
		{
		}
		
		return Component.translatable(str);
	}
	
	private static final Gson GSON = Util.make(() ->
	{
		GsonBuilder gsonbuilder = new GsonBuilder();
		gsonbuilder.disableHtmlEscaping();
		gsonbuilder.registerTypeHierarchyAdapter(Component.class, new Component.Serializer());
		gsonbuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
		gsonbuilder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
		return gsonbuilder.create();
	});
	
	public static List<Component> componentsFromString(String str)
	{
		try
		{
			JsonElement t = GSON.fromJson(str, JsonElement.class);
			
			if(t instanceof JsonArray arr)
			{
				List<Component> coms = new ArrayList<>(arr.size());
				for(int i = 0; i < arr.size(); i++)
				{
					coms.add(Component.Serializer.fromJson(arr.get(i)));
				}
				return coms;
			} else
				return List.of(MoreObjects.firstNonNull(Component.Serializer.fromJson(t), Component.empty()));
		} catch(Exception e)
		{
		}
		
		return List.of(Component.translatable(str));
	}
	
	@NotNull
	public static Supplier<OptionalBoolean> readBoolean(DriverContext ctx, String from)
	{
		var expression = ctx.getString(from);
		if("true".equalsIgnoreCase(expression) || "false".equalsIgnoreCase(expression))
			return Cast.constant(OptionalBoolean.of(Boolean.parseBoolean(expression)));
		if(expression == null) return Cast.constant(OptionalBoolean.empty());
		
		var str = ctx.getString(from);
		if(str == null) return Cast.constant(OptionalBoolean.empty());
		
		var cbq = ctx.eval(BoolCallback3.class, str, CBQ_RET_SPEC);
		if(cbq == null) return Cast.constant(OptionalBoolean.empty());
		
		return () ->
		{
			try
			{
				return OptionalBoolean.of(cbq.invoke(ctx.query(), ctx.self(), null));
			} catch(Exception e)
			{
				return OptionalBoolean.empty();
			}
		};
	}
	
	@NotNull
	public static Supplier<OptionalFloat> readFloat(DriverContext ctx, String from)
	{
		var of = ctx.getFloat(from);
		if(of.isPresent()) return Cast.constant(of);
		
		var str = ctx.getString(from);
		if(str == null) return Cast.constant(OptionalFloat.empty());
		
		var cbq = ctx.eval(DoubleCallback3.class, str, CBQ_RET_SPEC);
		if(cbq == null) return Cast.constant(OptionalFloat.empty());
		
		return () ->
		{
			try
			{
				return OptionalFloat.of((float) cbq.invoke(ctx.query(), ctx.self(), null));
			} catch(Exception e)
			{
				return OptionalFloat.empty();
			}
		};
	}
	
	@NotNull
	public static Supplier<OptionalInt> readInt(DriverContext ctx, String from)
	{
		var of = ctx.getInt(from);
		if(of.isPresent()) return Cast.constant(of);
		
		var str = ctx.getString(from);
		if(str == null) return Cast.constant(OptionalInt.empty());
		
		var cbq = ctx.eval(IntCallback3.class, str, CBQ_RET_SPEC);
		if(cbq == null) return Cast.constant(OptionalInt.empty());
		
		return () ->
		{
			try
			{
				return OptionalInt.of(cbq.invoke(ctx.query(), ctx.self(), null));
			} catch(Exception e)
			{
				return OptionalInt.empty();
			}
		};
	}
	
	@NotNull
	public static Supplier<OptionalInt> readColor(DriverContext ctx, String from)
	{
		var of = ctx.getInt(from);
		if(of.isPresent()) return Cast.constant(of);
		
		String str = ctx.getString(from);
		if(str == null || str.isBlank()) return Cast.constant(OptionalInt.empty());
		if(str != null && str.matches(AllowedValues.HEX_COLOR))
			return Cast.constant(OptionalInt.of(Integer.parseInt(str.substring(1), 16)));
		
		var cbq = ctx.eval(IntCallback3.class, str, CBQ_RET_SPEC);
		if(cbq == null) return Cast.constant(OptionalInt.empty());
		
		return () ->
		{
			try
			{
				return OptionalInt.of(cbq.invoke(ctx.query(), ctx.self(), null));
			} catch(Exception e)
			{
				return OptionalInt.empty();
			}
		};
	}
	
	public static Runnable readCallback(DriverContext ctx, String from, boolean returns)
	{
		return readCallback(ctx, from, returns, null);
	}
	
	public static Runnable readCallback(DriverContext ctx, String from, boolean returns, Object thirdParam)
	{
		var expression = ctx.getString(from);
		if(expression == null || expression.isBlank()) return () ->
		{
		};
		
		var cbq = ctx.eval(Callback3.class, expression, returns ? CBQ_RET_SPEC : CBQ_SPEC);
		if(cbq == null) return () ->
		{
		};
		
		return () ->
		{
			try
			{
				cbq.invoke(ctx.query(), ctx.self(), thirdParam);
			} catch(RuntimeException e)
			{
				log.error("Failed to invoke callback {} (code: {})", readableName(ctx.attributes()), expression);
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
	
	private static final String CAST_NAME = Cast.class.getName();
	
	public static boolean isConstant(Object supplier)
	{
		return supplier != null && supplier.getClass().getName().startsWith(CAST_NAME);
	}
}