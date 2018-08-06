package com.zeitheron.hammercore.api.multipart;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves all custom-rendered multipart signatures by their class.
 */
public final class MultipartRenderingRegistry
{
	private MultipartRenderingRegistry()
	{
	}
	
	private static final IMultipartRender DEFAULT_RENDER = new BlockStateMultipartRender();
	private static final Map<Class, IMultipartRender> renders = new HashMap<>();
	
	/**
	 * Sets a renderer for a multipart's class.
	 */
	public static <T extends MultipartSignature> void bindSpecialMultipartRender(Class<T> signature, IMultipartRender<T> render)
	{
		renders.put(signature, render);
	}
	
	/**
	 * Gets the renderer for passed signature.
	 */
	public static <T extends MultipartSignature> IMultipartRender<T> getRender(T signature)
	{
		IMultipartRender<T> render = renders.get(signature.getClass());
		return render == null ? DEFAULT_RENDER : render;
	}
}