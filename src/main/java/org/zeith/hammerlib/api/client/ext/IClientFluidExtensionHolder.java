package org.zeith.hammerlib.api.client.ext;

import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.function.Consumer;

public interface IClientFluidExtensionHolder
{
	void initializeClient(Consumer<IClientFluidTypeExtensions> consumer);
}