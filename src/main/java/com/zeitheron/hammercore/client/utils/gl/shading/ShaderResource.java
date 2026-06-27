package com.zeitheron.hammercore.client.utils.gl.shading;

import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create a static final instance of this resource to avoid loading same resource path for every shader that needs a variable.
 */
public class ShaderResource
{
	protected final AtomicBoolean hasLoaded = new AtomicBoolean(false);
	
	public final ResourceLocation location;
	protected String code;
	
	public ShaderResource(ResourceLocation location)
	{
		this.location = location;
	}
	
	public void prepareReload()
	{
		hasLoaded.set(false);
	}
	
	public void reload(IResourceManager resources)
			throws IOException
	{
		if(!hasLoaded.compareAndSet(false, true)) return;
		try(IResource res = resources.getResource(location); InputStream in = res.getInputStream())
		{
			this.code = new String(IOUtils.pipeOut(in), StandardCharsets.UTF_8);
		} catch(IOException e)
		{
			hasLoaded.set(false);
			throw e;
		}
	}
	
	public String getCode()
	{
		return code;
	}
}