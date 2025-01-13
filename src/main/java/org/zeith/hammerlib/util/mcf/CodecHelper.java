package org.zeith.hammerlib.util.mcf;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.GsonHelper;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class CodecHelper
{
	public static <T> DynamicOps<T> withRegistry(DynamicOps<T> ops, HolderLookup.Provider access)
	{
		return ops;
	}
	
	public static <T> Either<T, String> parseRegistryJson(HolderLookup.Provider access, Codec<T> codec, String json)
	{
		return parseRegistryJson(access, codec, GsonHelper.parse(json));
	}
	
	public static <T> Either<T, String> parseRegistryJson(HolderLookup.Provider access, Codec<T> codec, JsonElement json)
	{
		return parse(codec, withRegistry(JsonOps.INSTANCE, access), json);
	}
	
	public static <T, O> Either<T, String> parse(Codec<T> codec, DynamicOps<O> ops, O op)
	{
		DataResult<T> dr = codec.parse(ops, op);
		AtomicReference<String> err = new AtomicReference<>();
		return dr.resultOrPartial(err::set)
				.<Either<T, String>>map(Either::left)
				.orElseGet(() -> Either.right(Objects.toString(err.get())));
	}
}