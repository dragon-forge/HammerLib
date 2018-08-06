package com.zeitheron.hammercore.api;

/**
 * Represents API to be used. You need to use @{@link HammerCoreAPI} to class
 * this interface is applied. Test for presence in {@link APILoader}
 */
public interface IHammerCoreAPI
{
	/**
	 * Initializes this API.
	 * 
	 * @param log
	 *            The log
	 * @param version
	 *            The version of this API
	 */
	public void init(ILog log, String version);
}