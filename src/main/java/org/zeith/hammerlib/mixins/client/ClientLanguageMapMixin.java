package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.resources.ClientLanguageMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientLanguageMap.class)
public abstract class ClientLanguageMapMixin
{
	@ModifyArg(
			method = "loadFrom",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/ClientLanguageMap;<init>(Ljava/util/Map;Z)V"),
			index = 0
	)
	private static Map<String, String> inject(Map<String, String> map)
	{
		return new HashMap<>(map);
	}
}