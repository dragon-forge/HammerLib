package org.zeith.hammerlib.api.client.ext;

import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

import java.util.function.Consumer;

public interface IClientBlockExtensionHolder
{
	void initializeClient(Consumer<IClientBlockExtensions> consumer);
}