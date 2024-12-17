package org.zeith.hammerlib.mixins.languages;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.zeith.hammerlib.api.LanguageHelper;

import java.util.List;
import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin
{
	@WrapOperation(
			method = "loadFrom",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Map;copyOf(Ljava/util/Map;)Ljava/util/Map;",
					ordinal = 0
			)
	)
	private static Map<String, String> HammerLib_addLangs(
			Map<String, String> map, Operation<Map<String, String>> original,
			ResourceManager resourceManager, List<String> filenames, boolean defaultRightToLeft
	)
	{
		LanguageHelper.reloadLanguage(map, resourceManager, filenames);
		return original.call(map);
	}
}