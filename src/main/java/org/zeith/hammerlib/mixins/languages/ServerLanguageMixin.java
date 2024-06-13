package org.zeith.hammerlib.mixins.languages;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.server.LanguageHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.LanguageHelper;

import java.util.List;
import java.util.Map;

@Mixin(LanguageHook.class)
public class ServerLanguageMixin
{
	@Shadow
	private static Map<String, String> modTable;
	
	@Inject(
			method = "loadLanguage",
			at = @At("TAIL")
	)
	private static void HammerLiv_loadLanguage(String langName, MinecraftServer server, CallbackInfo ci)
	{
		// noinspection resource
		ResourceManager resourceManager = server.getServerResources().resourceManager();
		
		LanguageHelper.reloadLanguage(modTable,
				new MultiPackResourceManager(PackType.CLIENT_RESOURCES, resourceManager.listPacks().toList()),
				List.of(langName)
		);
	}
}