package com.zeitheron.hammercore.client.utils.gl.shading;

import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.utils.base.IThrowableSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ShaderSource
{
	String toString;
	IThrowableSupplier<InputStream, IOException> ioGenerator;

	public ShaderSource(ResourceLocation path)
	{
		this(() -> Minecraft.getMinecraft().getResourceManager().getResource(path).getInputStream());
		toString = "resource='" + path + '\'';
	}

	public ShaderSource(IThrowableSupplier<InputStream, IOException> ioGenerator)
	{
		this.ioGenerator = ioGenerator;
		toString = "ioGenerator=" + ioGenerator;
	}

	public String read(List<ShaderVar> variables)
	{
		String str = "";
		try(InputStream in = ioGenerator.get())
		{
			str = new String(IOUtils.pipeOut(in), StandardCharsets.UTF_8);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		for(ShaderVar var : variables)
			str = str.replaceAll("#variable " + var.key, var.getValue()).replaceAll("%" + var.key + "%", var.getValue());
		return str;
	}

	@Override
	public String toString()
	{
		return "ShaderSource{" + toString + "}";
	}
}