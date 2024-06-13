package org.zeith.hammerlib.mixins.languages;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.zeith.hammerlib.api.LanguageHelper;

import java.util.*;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin
{
	@ModifyVariable(
			method = "loadFrom",
			at = @At(
					value = "INVOKE",
					target = "Lcom/google/common/collect/ImmutableMap;copyOf(Ljava/util/Map;)Lcom/google/common/collect/ImmutableMap;"
			)
	)
	private static Map<String, String> map(Map<String, String> v, ResourceManager resources, List<String> languages)
	{
		LanguageHelper.reloadLanguage(v, resources, languages);
		return v;
	}
}