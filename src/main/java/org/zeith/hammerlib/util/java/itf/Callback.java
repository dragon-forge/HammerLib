package org.zeith.hammerlib.util.java.itf;

public interface Callback
{
	Callback NOTHING = args -> null;
	
	Object invoke(Object... args);
}