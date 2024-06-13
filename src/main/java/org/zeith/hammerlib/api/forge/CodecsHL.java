package org.zeith.hammerlib.api.forge;

import com.mojang.serialization.Codec;

public class CodecsHL
{
	public static final Codec<Integer> HEX_INT_CODEC = Codec.STRING.xmap(
			str -> Integer.parseUnsignedInt(str, 16),
			i -> Integer.toUnsignedString(i, 16)
	);
}