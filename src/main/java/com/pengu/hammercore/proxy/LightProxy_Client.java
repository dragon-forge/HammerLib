package com.pengu.hammercore.proxy;

import com.pengu.hammercore.api.dynlight.DynamicLightGetter;
import com.pengu.hammercore.api.dynlight.iDynlightSrc;

public class LightProxy_Client extends LightProxy_Common
{
	@Override
	public void addDynLight(iDynlightSrc src)
	{
		DynamicLightGetter.addLightSource(src);
	}
	
	@Override
	public void removeDynLight(iDynlightSrc src)
	{
		DynamicLightGetter.removeLightSource(src);
	}
}