package com.pengu.hammercore.utils;

public class OnetimeCaller
{
	private Runnable call;
	
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