package org.zeith.hammerlib.api.items.glint;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.api.forge.StreamCodecs;

import java.util.function.ToIntBiFunction;

public interface IGlintProviderType<T>
{
	int getGlint(ItemStack stack, T data);
	
	Codec<T> codec();
	
	StreamCodec<? extends ByteBuf, T> streamCodec();
	
	static <V> IGlintProviderType<V> simple(ToIntBiFunction<ItemStack, V> getGlint, Codec<V> codec)
	{
		return simple(getGlint, codec, StreamCodecs.createRegistryAwareStreamCodec(codec));
	}
	
	static <B extends ByteBuf, V> IGlintProviderType<V> simple(ToIntBiFunction<ItemStack, V> getGlint, Codec<V> codec, StreamCodec<B, V> streamCodec)
	{
		return new IGlintProviderType<>()
		{
			@Override
			public int getGlint(ItemStack stack, V data)
			{
				return getGlint.applyAsInt(stack, data);
			}
			
			@Override
			public Codec<V> codec()
			{
				return codec;
			}
			
			@Override
			public StreamCodec<? extends ByteBuf, V> streamCodec()
			{
				return streamCodec;
			}
		};
	}
}