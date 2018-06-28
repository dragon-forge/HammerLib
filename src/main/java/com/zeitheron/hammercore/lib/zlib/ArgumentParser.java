package com.zeitheron.hammercore.lib.zlib;

import java.util.HashMap;
import java.util.Map;

import com.zeitheron.hammercore.lib.zlib.tuple.TwoTuple;

public class ArgumentParser
{
	public static TwoTuple<Map<String, String>, Map<String, Boolean>> parse(String... args)
	{
		Map<String, String> sargs = new HashMap<>();
		Map<String, Boolean> bargs = new HashMap<>();
		
		String carg = "";
		String past = "";
		
		for(String s : args)
			if(s.startsWith("-"))
			{
				if(!past.isEmpty())
				{
					past = past.substring(0, past.length() - 1);
					if(past.equalsIgnoreCase("true") || past.equalsIgnoreCase("false"))
						bargs.put(carg, past.equalsIgnoreCase("true"));
					else
						sargs.put(carg, past);
				} else if(!carg.isEmpty())
					bargs.put(carg, true);
				
				carg = s.substring(1);
				past = "";
			} else
				past += s + " ";
			
		if(!carg.isEmpty() && !past.isEmpty())
		{
			past = past.substring(0, past.length() - 1);
			if(past.equalsIgnoreCase("true") || past.equalsIgnoreCase("false"))
				bargs.put(carg, past.equalsIgnoreCase("true"));
			else
				sargs.put(carg, past);
		} else if(!carg.isEmpty())
			bargs.put(carg, true);
		
		return new TwoTuple<>(sargs, bargs);
	}
}