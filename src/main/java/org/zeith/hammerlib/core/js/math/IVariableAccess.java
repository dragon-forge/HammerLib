package org.zeith.hammerlib.core.js.math;

import java.util.function.BiConsumer;

public interface IVariableAccess
{
	void putObjects(BiConsumer<String, Object> storage);
}