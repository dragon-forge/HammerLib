package org.zeith.hammerlib.client.flowgui.objects;

import org.zeith.hammerlib.client.flowgui.MousePos;

import java.util.ArrayList;
import java.util.List;

public interface RenderHook
{
	RenderHook NOOP = (partialTime, mouse) ->
	{
	};
	
	void hook(float partialTime, MousePos mouse);
	
	default RenderHook andThen(RenderHook task)
	{
		return (partialTime, mouse) ->
		{
			hook(partialTime, mouse);
			task.hook(partialTime, mouse);
		};
	}
	
	static RenderHook list()
	{
		return new ListOf();
	}
	
	class ListOf
			implements RenderHook
	{
		List<RenderHook> hooks = new ArrayList<>();
		
		@Override
		public void hook(float partialTime, MousePos mouse)
		{
			for(RenderHook hook : hooks)
			{
				hook.hook(partialTime, mouse);
			}
		}
		
		@Override
		public RenderHook andThen(RenderHook task)
		{
			hooks.add(task);
			return this;
		}
	}
}