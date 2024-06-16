package org.zeith.hammerlib.core.glints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.zeith.hammerlib.api.forge.CodecsHL;
import org.zeith.hammerlib.util.colors.ColorHelper;

import java.util.List;

public record GradientGlintData(List<Integer> colors, long loopbackDurationMS)
{
	public static final Codec<GradientGlintData> CODEC = RecordCodecBuilder.create(inst ->
			inst.group(
					CodecsHL.HEX_INT_CODEC.listOf().fieldOf("colors").forGetter(GradientGlintData::colors),
					Codec.LONG.fieldOf("duration").forGetter(GradientGlintData::loopbackDurationMS)
			).apply(inst, GradientGlintData::new)
	);
	
	public static final StreamCodec<ByteBuf, GradientGlintData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.<ByteBuf, Integer>list().apply(ByteBufCodecs.INT), GradientGlintData::colors,
			ByteBufCodecs.VAR_LONG, GradientGlintData::loopbackDurationMS,
			GradientGlintData::new
	);
	
	public int get(long ms)
	{
		ms %= loopbackDurationMS;
		
		int t = colors.size();
		long per = loopbackDurationMS / t;
		int idx = (int) Math.max(0, Math.min(t - 1, ms / per));
		long startMs = idx * per;
		
		long cur = ms - startMs;
		
		// Very unlikely tbh.
		if(cur == 0L) return colors.get(idx);
		
		int nextIdx = idx + 1;
		
		// Shift back if we're lagging behind
		while(cur < 0)
		{
			idx--;
			nextIdx--;
			cur += per;
			if(idx < 0 || nextIdx < 0)
			{
				idx += t;
				nextIdx += t;
			}
		}
		
		idx %= t;
		nextIdx %= t;
		
		float prog = Math.clamp(cur / (float) per, 0, 1);
		
		return ColorHelper.interpolate(
				colors.get(idx),
				colors.get(nextIdx),
				prog
		);
	}
}