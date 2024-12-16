package org.zeith.hammerlib.api.client.ext;

import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;

public interface IClientMobEffectExtensionHolder
{
	void initializeClient(Consumer<IClientMobEffectExtensions> consumer);
}