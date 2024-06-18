package org.zeith.hammerlib.api.items.coms;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.api.items.glint.IGlintProviderType;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.core.glints.GradientGlintData;
import org.zeith.hammerlib.core.init.GlintProviderTypesHL;
import org.zeith.hammerlib.util.java.Cast;

import java.util.stream.IntStream;

public record CustomGlintComponent<T>(IGlintProviderType<T> type, T data)
		implements IColoredFoilItem
{
	public static final Codec<CustomGlintComponent<?>> CODEC = RecordCodecBuilder.create(inst ->
			inst.group(
					RegistriesHL.GLINT_PROVIDERS.byNameCodec().fieldOf("type").forGetter(CustomGlintComponent::type),
					ExtraCodecs.JSON.fieldOf("data").forGetter(CustomGlintComponent::serializeData)
			).apply(inst, CustomGlintComponent::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CustomGlintComponent<?>> STREAM_CODEC = StreamCodec.of(CustomGlintComponent::toNetwork, CustomGlintComponent::fromNetwork);
	
	public CustomGlintComponent(IGlintProviderType<T> type, JsonElement data)
	{
		this(type, type.codec().decode(JsonOps.INSTANCE, data).result().map(Pair::getFirst).orElse(null));
	}
	
	private JsonElement serializeData()
	{
		return type.codec().encodeStart(JsonOps.INSTANCE, data).result().orElseThrow();
	}
	
	@Override
	public int getFoilColor(@NotNull ItemStack stack)
	{
		return type.getGlint(stack, data);
	}
	
	public static CustomGlintComponent<?> fixedColor(int color)
	{
		return new CustomGlintComponent<>(GlintProviderTypesHL.CONSTANT, color);
	}
	
	public static CustomGlintComponent<?> rainbow(long fullCycleMS)
	{
		return new CustomGlintComponent<>(GlintProviderTypesHL.RAINBOW, fullCycleMS);
	}
	
	public static CustomGlintComponent<?> gradient(long fullCycleMS, int... colors)
	{
		return new CustomGlintComponent<>(GlintProviderTypesHL.GRADIENT, new GradientGlintData(IntStream.of(colors).boxed().toList(), fullCycleMS));
	}
	
	public static <T> void toNetwork(RegistryFriendlyByteBuf buf, CustomGlintComponent<T> com)
	{
		IGlintProviderType<T> h = com.type();
		buf.writeResourceLocation(buf.registryAccess().registryOrThrow(RegistriesHL.Keys.GLINT_PROVIDERS).getKey(h));
		h.streamCodec().encode(Cast.cast(buf), com.data());
	}
	
	public static CustomGlintComponent<?> fromNetwork(RegistryFriendlyByteBuf buf)
	{
		IGlintProviderType<?> type = buf.registryAccess().registryOrThrow(RegistriesHL.Keys.GLINT_PROVIDERS).getOrThrow(buf.readResourceKey(RegistriesHL.Keys.GLINT_PROVIDERS));
		Object decode = type.streamCodec().decode(Cast.cast(buf));
		return new CustomGlintComponent(type, decode);
	}
}