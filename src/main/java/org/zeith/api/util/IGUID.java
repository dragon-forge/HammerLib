package org.zeith.api.util;

import java.util.*;

public interface IGUID
{
	/**
	 * Get the GUID of this object.
	 * This may be any UUID that has to be generated (lazily) ONCE per instance.
	 * The generation method does not matter. IE it may be randomly generated, or from any seed(data).
	 */
	UUID getGUID();
	
	static boolean same(Object a, Object b)
	{
		if(a instanceof IGUID ag && b instanceof IGUID bg)
			return Objects.equals(ag.getGUID(), bg.getGUID());
		return Objects.equals(a, b);
	}
}