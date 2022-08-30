package org.zeith.hammerlib.client.pipelines.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.util.java.IOUtils;
import org.zeith.hammerlib.util.java.itf.IThrowableSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ShaderSource
{
	String toString;
	IThrowableSupplier<InputStream, IOException> ioGenerator;

	public ShaderSource(ResourceLocation path)
	{
		this(() -> Minecraft.getInstance().getResourceManager().open(path));
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