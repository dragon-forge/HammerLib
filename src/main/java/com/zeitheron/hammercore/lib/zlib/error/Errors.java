/* Decompiled with CFR 0_123. */
package com.zeitheron.hammercore.lib.zlib.error;

import com.zeitheron.hammercore.lib.zlib.error.WrappedError;

public class Errors
{
	public static void propagate(Throwable err)
	{
		throw new WrappedError(err);
	}
}
