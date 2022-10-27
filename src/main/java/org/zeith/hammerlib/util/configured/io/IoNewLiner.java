package org.zeith.hammerlib.util.configured.io;

import java.io.IOException;

@FunctionalInterface
public interface IoNewLiner
{
	void newLine(int depth) throws IOException;
	
	default void newLine() throws IOException
	{
		newLine(0);
	}
	
	default IoNewLiner push()
	{
		return depth -> newLine(depth + 1);
	}
}