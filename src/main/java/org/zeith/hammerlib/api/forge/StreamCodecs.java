package org.zeith.hammerlib.api.forge;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.*;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.GsonHelper;

/**
 * This is a realm of non-efficient codecs. Use carefully.
 */
public class StreamCodecs
{
	private static final Gson GSON = new Gson();
	
	public static <T> StreamCodec<RegistryFriendlyByteBuf, T> createRegistryAwareStreamCodec(Codec<T> codec)
	{
		return StreamCodec.of(
				(buf, t) ->
				{
					var res = codec.encodeStart(RegistryOps.create(JsonOps.INSTANCE, buf.registryAccess()), t);
					buf.writeUtf(GSON.toJson(res.getOrThrow(comp -> new EncoderException("Failed to encode: " + comp + " " + t))));
				},
				(buf) ->
				{
					JsonElement jsonelement = GsonHelper.fromJson(GSON, buf.readUtf(), JsonElement.class);
					DataResult<T> dataresult = codec.parse(RegistryOps.create(JsonOps.INSTANCE, buf.registryAccess()), jsonelement);
					return dataresult.getOrThrow(comp -> new DecoderException("Failed to decode json: " + comp));
				}
		);
	}
}