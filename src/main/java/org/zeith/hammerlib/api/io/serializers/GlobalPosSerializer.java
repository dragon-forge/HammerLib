package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.util.math.GlobalPos;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(GlobalPos.class)
public class GlobalPosSerializer
		extends BaseCodecSerializer<GlobalPos>
{
	public GlobalPosSerializer()
	{
		super(GlobalPos.CODEC, () -> null);
	}
}