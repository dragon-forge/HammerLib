package com.zeitheron.hammercore.lib.nashorn;

import com.google.common.base.Strings;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.function.BiPredicate;

public interface JSSource
{
	String read();
	
	default boolean exists()
	{
		return !Strings.isNullOrEmpty(read());
	}
	
	default JSSource copy()
	{
		JSSource root = this;
		
		return new JSSource()
		{
			@Override
			public String read()
			{
				return root.read();
			}
			
			@Override
			public boolean exists()
			{
				return root.exists();
			}
		};
	}
	
	default JSSource processImports()
	{
		final JSSource main = this;
		return new JSSource()
		{
			@Override
			public String read()
			{
				StringBuilder ns = new StringBuilder();
				String script = main.read();
				try(Scanner scan = new Scanner(script))
				{
					while(scan.hasNextLine())
					{
						String ln = scan.nextLine();
						String trimmed = ln.trim();
						if(trimmed.startsWith("import "))
						{
							if(trimmed.endsWith(";")) ln = trimmed.substring(7, trimmed.length() - 1);
							else ln = trimmed.substring(7);
							String simpleName = ln.substring(ln.lastIndexOf('.') + 1);
							ln = "var " + simpleName + " = Java.type(\"" + ln + "\");";
						}
						ns.append(ln).append(scan.hasNextLine() ? "\n" : "");
					}
				}
				return ns.toString();
			}
			
			@Override
			public boolean exists()
			{
				return main.exists();
			}
		};
	}
	
	default JSSource addClassPointer(Class<?> type, String reference)
	{
		final JSSource main = this;
		return new JSSource()
		{
			@Override
			public String read()
			{
				return "var " + reference + " = Java.type(\"" + type.getCanonicalName() + "\");" + main.read();
			}
			
			@Override
			public boolean exists()
			{
				return main.exists();
			}
		};
	}
	
	default JSSource inheritClassMethods(Class<?> type)
	{
		return inheritClassMethods(type, JSImporter.alwaysTrue);
	}
	
	default JSSource inheritClassMethods(Class<?> type, BiPredicate<String, Method> allow)
	{
		final JSSource main = this;
		return new JSSource()
		{
			@Override
			public String read()
			{
				return JSImporter.createJSWrap(type, allow) + main.read();
			}
			
			@Override
			public boolean exists()
			{
				return main.exists();
			}
		};
	}
	
	static JSSource fromLocalResource(Class<?> from, String path)
	{
		String fp = path.startsWith("/") ? path : "/" + path;
		return () ->
		{
			byte[] data = new byte[0];
			try(InputStream in = from.getResourceAsStream(fp))
			{
				if(in != null) data = IOUtils.pipeOut(in);
			} catch(IOException ignored)
			{
			}
			return data.length == 0 ? null : new String(data);
		};
	}
	
	@SideOnly(Side.CLIENT)
	static JSSource fromResourceManager(IResourceManager manager, ResourceLocation path)
	{
		return () ->
		{
			byte[] data = new byte[0];
			try(IResource res = manager.getResource(path))
			{
				data = IOUtils.pipeOut(res.getInputStream());
			} catch(IOException ignored)
			{
			}
			return data.length == 0 ? null : new String(data);
		};
	}
}