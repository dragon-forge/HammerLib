package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.LanguageHelper;
import org.zeith.hammerlib.mixins.I18nAccessor;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
@Mixin(LanguageManager.class)
public class LanguageManagerMixin
{
	@Inject(
			method = "onResourceManagerReload",
			at = @At("TAIL")
	)
	public void onResourceManagerReloadHLHook(ResourceManager p_118973_, CallbackInfo ci)
	{
		if(I18nAccessor.getLanguage() instanceof ClientLanguage cl)
		{
			if(!(cl.storage instanceof HashMap))
				cl.storage = new HashMap<>(cl.storage);
			LanguageHelper.reloadLanguage(cl.storage::put);
		}
	}
}