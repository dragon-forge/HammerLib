package org.zeith.hammerlib.api.io.serializers.codec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Points to a field whose type is a {@link ICodecSerializer}.
 * This is used to register a custom codec NBT serializers in an inline way.
 */
@Target(ElementType.FIELD)
public @interface CodecSerializer
{
}