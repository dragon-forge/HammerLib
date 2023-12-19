package org.zeith.hammerlib.core.recipes.replacers;

import org.zeith.hammerlib.annotations.*;

@SimplyRegister
public interface RemainingReplacerHL
{
	@RegistryName("none")
	IRemainingItemReplacer NONE = (container, slot, item) -> item;
}