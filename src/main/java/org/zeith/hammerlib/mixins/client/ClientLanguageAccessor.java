package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientLanguage.class)
public interface ClientLanguageAccessor
{
	@Accessor
	Map<String, String> getStorage();
	
	@Mutable
	@Accessor
	void setStorage(Map<String, String> storage);
}
