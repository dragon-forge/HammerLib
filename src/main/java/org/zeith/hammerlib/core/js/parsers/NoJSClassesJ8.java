package org.zeith.hammerlib.core.js.parsers;

import org.openjdk.nashorn.api.scripting.ClassFilter;

class NoJSClassesJ8
		implements ClassFilter
{
	@Override
	public boolean exposeToScripts(String s)
	{
		return false;
	}
}
