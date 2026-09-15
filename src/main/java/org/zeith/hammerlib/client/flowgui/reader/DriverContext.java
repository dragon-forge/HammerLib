package org.zeith.hammerlib.client.flowgui.reader;

import org.zeith.hammerlib.api.data.IDataNode;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.flowgui.data.FlowQuery;
import org.zeith.hammerlib.client.flowgui.objects.RenderHook;
import org.zeith.hammerlib.core.js.CallerSpec;
import org.zeith.hammerlib.util.java.OptionalFloat;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record DriverContext(
		JsContext jsc,
		FlowQuery query,
		IDataNode attributes,
		Supplier<GuiObject> selfDyn,
		Consumer<RenderHook> preRender
)
{
	public void onPreRender(RenderHook task)
	{
		if(preRender != null)
			preRender.accept(task);
		else
			Optional.ofNullable(selfDyn.get()).ifPresent(h -> h.onPreRender(task));
	}
	
	public String getString(String from)
	{
		return attributes.getString(from);
	}
	
	public OptionalFloat getFloat(String from)
	{
		return attributes.getFloat(from);
	}
	
	public OptionalInt getInt(String from)
	{
		return attributes.getInt(from);
	}
	
	public boolean getBoolean(String from)
	{
		return attributes.getBoolean(from);
	}
	
	public <T> T eval(Class<T> interfaceType, String input, CallerSpec spec)
	{
		return jsc.eval(interfaceType, input, spec);
	}
	
	public GuiObject self()
	{
		return selfDyn.get();
	}
}