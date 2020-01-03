package com.zeitheron.hammercore.client.utils.rendering;

import javax.annotation.Nullable;
import java.util.Map;

public interface ISimpleRenderable
{
	void render(@Nullable Map<String, Object> properties);

	default ISimpleRenderable withSurprocessor(ISimpleRenderable pre, ISimpleRenderable post)
	{
		return props ->
		{
			pre.render(props);
			render(props);
			post.render(props);
		};
	}

	default ISimpleRenderable withPreprocessor(ISimpleRenderable pre)
	{
		return props ->
		{
			pre.render(props);
			render(props);
		};
	}

	default ISimpleRenderable withPostprocessor(ISimpleRenderable post)
	{
		return props ->
		{
			render(props);
			post.render(props);
		};
	}
}