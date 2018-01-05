package com.pengu.hammercore.hlang;

import com.pengu.hammercore.utils.IndexedMap;

public class HammerLanguage
{
	public static iHLDefiner createDefiner()
	{
		return new HLDefinerImpl();
	}
	
	public static iHLScript parse(iHLDefiner definer, String script)
	{
		HLDefinerImpl impl = (HLDefinerImpl) definer;
		
		return null;
	}
	
	private static final class HLDefinerImpl implements iHLDefiner
	{
		private final IndexedMap<Class, String> imports = new IndexedMap<>();
		private final IndexedMap<String, Object> vars = new IndexedMap<>();
		private final IndexedMap<String, Class[]> hooks = new IndexedMap<>();
		
		@Override
		public void defineAccessibleClass(Class cl, String hlName)
		{
			imports.put(cl, hlName);
		}
		
		@Override
		public void defineHook(String hlName, Class... args)
		{
			hooks.put(hlName, args);
		}
		
		@Override
		public void defineVariable(String hlName, Object value)
		{
			vars.put(hlName, value);
		}
	}
}