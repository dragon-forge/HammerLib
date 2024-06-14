package org.zeith.hammerlib.core.init;

import com.mojang.serialization.Codec;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.CodecsHL;
import org.zeith.hammerlib.api.items.glint.IGlintProviderType;
import org.zeith.hammerlib.core.glints.GradientGlintData;
import org.zeith.hammerlib.util.colors.Rainbow;

@SimplyRegister
public interface GlintProviderTypesHL
{
	@RegistryName("constant")
	IGlintProviderType<Integer> CONSTANT = IGlintProviderType.simple((s, i) -> i, CodecsHL.HEX_INT_CODEC);
	
	@RegistryName("rainbow")
	IGlintProviderType<Long> RAINBOW = IGlintProviderType.simple((s, i) -> 255 << 24 | Rainbow.doIt(s.getPopTime(), i), Codec.LONG);
	
	@RegistryName("gradient")
	IGlintProviderType<GradientGlintData> GRADIENT = IGlintProviderType.simple((s, i) -> i.get(System.currentTimeMillis()), GradientGlintData.CODEC);
	
}