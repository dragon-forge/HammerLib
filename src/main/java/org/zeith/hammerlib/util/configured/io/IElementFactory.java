package org.zeith.hammerlib.util.configured.io;

import org.zeith.hammerlib.util.configured.ConfigToken;
import org.zeith.hammerlib.util.configured.types.ConfigElement;

public interface IElementFactory<T extends ConfigElement<T>>
{
	T create(Runnable onChanged, ConfigToken<T> token, String name);
}