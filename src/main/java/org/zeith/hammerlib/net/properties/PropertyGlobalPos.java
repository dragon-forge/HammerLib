package org.zeith.hammerlib.net.properties;

import net.minecraft.util.math.GlobalPos;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyGlobalPos
		extends PropertyBaseCodec<GlobalPos>
{
	public PropertyGlobalPos(DirectStorage<GlobalPos> value)
	{
		super(GlobalPos.CODEC, () -> null, GlobalPos.class, value);
	}
	
	public PropertyGlobalPos()
	{
		super(GlobalPos.CODEC, () -> null, GlobalPos.class);
	}
}