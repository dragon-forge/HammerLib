package org.zeith.hammerlib.net.properties;

public interface IBasePropertyHolder
{
	PropertyDispatcher getProperties();
	
	/**
	 * Call this each tick to detect potential changes and send them to everyone
	 * around.
	 */
	void syncProperties();
	
	void syncPropertiesNow();
}