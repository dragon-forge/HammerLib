package com.pengu.hammercore.api;

/**
 * Represents API to be used. You need to use @{@link HammerCoreAPI} to class
 * this interface is applied
 */
public interface iHammerCoreAPI
{
	/**
	 * Initializes this API.
	 */
	public void init(iLog log, String version);
}