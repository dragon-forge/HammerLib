package org.zeith.hammerlib.api.client.ext;

import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public interface IClientItemExtensionHolder
{
	void initializeClient(Consumer<IClientItemExtensions> consumer);
}