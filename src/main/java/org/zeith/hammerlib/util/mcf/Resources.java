package org.zeith.hammerlib.util.mcf;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class Resources
{
	public static ResourceLocation location(String namespace, String path)
	{
		return new ResourceLocation(namespace, path);
	}
	
	public static ResourceLocation location(String location)
	{
		return new ResourceLocation(location);
	}
	
	@Nullable
	public static ResourceLocation locationOrNull(String namespace, String path)
	{
		if(isValidNamespace(namespace) && isValidPath(path))
			return location(namespace, path);
		return null;
	}
	
	@Nullable
	public static ResourceLocation locationOrNull(String location)
	{
		String[] split = location.split(":", 2);
		if(split.length == 2) return locationOrNull(split[0], split[1]);
		return isValidPath(split[0]) ? location(split[0]) : null;
	}
	
	
	private static boolean isValidPath(String pPath)
	{
		for(int i = 0; i < pPath.length(); ++i)
		{
			if(!validPathChar(pPath.charAt(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isValidNamespace(String pNamespace)
	{
		for(int i = 0; i < pNamespace.length(); ++i)
		{
			if(!validNamespaceChar(pNamespace.charAt(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean validPathChar(char pPathChar)
	{
		return pPathChar == '_' || pPathChar == '-' || pPathChar >= 'a' && pPathChar <= 'z' || pPathChar >= '0' && pPathChar <= '9' || pPathChar == '/' || pPathChar == '.';
	}
	
	private static boolean validNamespaceChar(char pNamespaceChar)
	{
		return pNamespaceChar == '_' || pNamespaceChar == '-' || pNamespaceChar >= 'a' && pNamespaceChar <= 'z' || pNamespaceChar >= '0' && pNamespaceChar <= '9' || pNamespaceChar == '.';
	}
}