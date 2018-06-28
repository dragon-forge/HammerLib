package com.zeitheron.hammercore.api;

/**
 * Represents API to be used. You need to use @{@link HammerCoreAPI} to class
 * this interface is applied
 */
public interface IHammerCoreAPI
{
	/**
	 * Initializes this API.
	 */
	public void init(ILog log, String version);
}