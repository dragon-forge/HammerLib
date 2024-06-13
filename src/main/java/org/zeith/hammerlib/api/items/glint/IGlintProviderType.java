package org.zeith.hammerlib.api.items.glint;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;

import java.util.function.ToIntBiFunction;

public interface IGlintProviderType<T>
{
	int getGlint(ItemStack stack, T data);
	
	Codec<T> codec();
	
	static <V> IGlintProviderType<V> simple(ToIntBiFunction<ItemStack, V> getGlint, Codec<V> codec)
	{
		return new IGlintProviderType<V>()
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
		};
	}
}