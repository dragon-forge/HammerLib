package org.zeith.hammerlib.mixins;

import net.minecraft.resources.IAsyncReloader;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.client.adapter.ResourcePackAdapter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class SimpleReloadableResourceManagerMixin
{
	@Shadow
	public abstract void add(IResourcePack pack);

	@Inject(
			method = "createFullReload",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/resources/SimpleReloadableResourceManager;createReload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/List;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/resources/IAsyncReloader;"
			)
	)
	public void injectResourcePacks(Executor p_219537_1_, Executor p_219537_2_, CompletableFuture<Unit> p_219537_3_, List<IResourcePack> p_219537_4_, CallbackInfoReturnable<IAsyncReloader> cir)
	{
		ResourcePackAdapter.BUILTIN_PACKS.forEach(this::add);
	}
}