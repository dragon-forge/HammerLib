package org.zeith.hammerlib.core.init;

import net.minecraft.core.component.DataComponentType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.items.coms.CustomGlintComponent;
import org.zeith.hammerlib.api.registrars.Registrar;

@SimplyRegister
public interface ComponentTypesHL
{
	@RegistryName("custom_glint")
	Registrar<DataComponentType<CustomGlintComponent<?>>> CUSTOM_GLINT = Registrar.dataComponentType(DataComponentType.<CustomGlintComponent<?>>builder()
			.persistent(CustomGlintComponent.CODEC)
			.networkSynchronized(CustomGlintComponent.STREAM_CODEC)
			.cacheEncoding()
	);
}