package org.zeith.hammerlib.util.mcf;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.GsonHelper;

public class CodecHelper
{
	public static <T> DynamicOps<T> withRegistry(DynamicOps<T> ops, HolderLookup.Provider access)
	{
		return access.createSerializationContext(ops);
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
		if(dr.hasResultOrPartial())
			return Either.left(dr.resultOrPartial().orElse(null));
		if(dr.isError())
			return Either.right(dr.error().map(DataResult.Error::message).orElse(""));
		return null;
	}
}