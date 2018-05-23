package com.pengu.hammercore.utils;

public class OnetimeCaller
{
	private Runnable call;
	
	public static OnetimeCaller of(Runnable r)
	{
		return new OnetimeCaller(r);
	}
	
	public OnetimeCaller(Runnable run)
	{
		call = run;
	}
	
	public void call()
	{
		if(call != null)
			call.run();
		call = null;
	}
}