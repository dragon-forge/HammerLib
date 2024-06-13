package org.zeith.hammerlib.api.items.coms;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.forge.StreamCodecs;
import org.zeith.hammerlib.api.items.IColoredFoilItem;
import org.zeith.hammerlib.api.items.glint.IGlintProviderType;
import org.zeith.hammerlib.core.RegistriesHL;
import org.zeith.hammerlib.core.init.GlintProviderTypesHL;

public record CustomGlintComponent<T>(Holder<IGlintProviderType<T>> type, T data)
		implements IColoredFoilItem
{
	public static final Codec<CustomGlintComponent<?>> CODEC = RecordCodecBuilder.create(inst ->
			inst.group(
					RegistryFixedCodec.create(RegistriesHL.Keys.GLINT_PROVIDERS)
							.fieldOf("type")
							.forGetter(CustomGlintComponent::holder),
					ExtraCodecs.JSON.fieldOf("data").forGetter(CustomGlintComponent::serializeData)
			).apply(inst, CustomGlintComponent::create)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CustomGlintComponent<?>> STREAM_CODEC = StreamCodecs.createRegistryAwareStreamCodec(CODEC);
	
	public CustomGlintComponent(IGlintProviderType<T> type, T data)
	{
		this(Holder.direct(type), data);
	}
	
	public CustomGlintComponent(Holder<IGlintProviderType<T>> type, JsonElement data)
	{
		this(type, type.value().codec().decode(JsonOps.INSTANCE, data).result().map(Pair::getFirst).orElse(null));
	}
	
	public Holder<IGlintProviderType<?>> holder()
	{
		return (Holder) type;
	}
	
	private JsonElement serializeData()
	{
		return type.value().codec().encodeStart(JsonOps.INSTANCE, data).result().orElseThrow();
	}
	
	@Override
	public int getFoilColor(@NotNull ItemStack stack)
	{
		return type.value().getGlint(stack, data);
	}
	
	public static CustomGlintComponent<?> fixedColor(int color)
	{
		return new CustomGlintComponent<>(GlintProviderTypesHL.CONSTANT, color);
	}
	
	public static CustomGlintComponent<?> rainbow(long fullCycleMS)
	{
		return new CustomGlintComponent<>(GlintProviderTypesHL.RAINBOW, fullCycleMS);
	}
	
	public static CustomGlintComponent<?> create(Holder<IGlintProviderType<?>> holder, JsonElement data)
	{
		return new CustomGlintComponent(holder, data);
	}
}