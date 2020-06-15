package com.zeitheron.hammercore.lib.nashorn;

import javax.annotation.Nonnull;

public class JSCallbackInfo
{
	public final Object returned;
	public final boolean functionExists, callSuccessful;
	public final Throwable error;
	
	public JSCallbackInfo(Object returned)
	{
		this.returned = returned;
		this.functionExists = true;
		this.callSuccessful = true;
		this.error = null;
	}
	
	public JSCallbackInfo(boolean functionExists, boolean callSuccessful, @Nonnull Throwable e)
	{
		this.returned = null;
		this.functionExists = functionExists;
		this.callSuccessful = callSuccessful;
		this.error = e;
	}
	
	@Override
	public String toString()
	{
		return "JSCallbackInfo{" +
				"returned=" + returned +
				", functionExists=" + functionExists +
				", callSuccessful=" + callSuccessful +
				", error=" + error +
				'}';
	}
}