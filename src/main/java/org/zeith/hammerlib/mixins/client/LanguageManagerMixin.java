package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.LanguageHelper;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
@Mixin(LanguageManager.class)
public class LanguageManagerMixin
{
	@Inject(
			method = "onResourceManagerReload",
			at = @At("TAIL")
	)
	public void onResourceManagerReload_HammerLib(ResourceManager p_118973_, CallbackInfo ci)
	{
		if(I18nAccessor.getLanguage() instanceof ClientLanguageAccessor cl)
		{
			if(!(cl.getStorage() instanceof HashMap))
				cl.setStorage(new HashMap<>(cl.getStorage()));
			LanguageHelper.reloadLanguage((HashMap<String, String>) cl.getStorage());
		}
	}
}