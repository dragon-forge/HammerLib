package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.neoforged.api.distmarker.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@OnlyIn(Dist.CLIENT)
@Mixin(I18n.class)
public interface I18nAccessor
{
	@Accessor
	static Language getLanguage()
	{
		throw new UnsupportedOperationException();
	}
}
