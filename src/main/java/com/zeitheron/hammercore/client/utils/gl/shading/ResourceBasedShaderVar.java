package com.zeitheron.hammercore.client.utils.gl.shading;

import com.zeitheron.hammercore.HammerCore;
import net.minecraft.client.resources.IResourceManager;

import java.io.IOException;

public abstract class ResourceBasedShaderVar<STATE>
		extends ShaderVar<STATE>
{
	protected String code;
	protected boolean isUsingFallback;
	
	public ResourceBasedShaderVar(String key)
	{
		super(key);
	}
	
	@Override
	public void prepareReload(IResourceManager resources)
	{
		getResource().prepareReload();
	}
	
	@Override
	public void onReload(IResourceManager resources)
	{
		try
		{
			getResource().reload(resources);
			code = postProcessCode(getResource().code);
			isUsingFallback = false;
		} catch(IOException e)
		{
			HammerCore.LOG.error("Failed to reload shader resource at {} required by variable {} ({})", getResource().location, this, this.key, e);
			code = postProcessCode(getFallbackCode());
			isUsingFallback = true;
		}
	}
	
	protected String postProcessCode(String code)
	{
		return code;
	}
	
	public abstract String getFallbackCode();
	
	public abstract ShaderResource getResource();
}