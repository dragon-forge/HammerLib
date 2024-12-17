package org.zeith.hammerlib.util.mcf;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RunnableReloader
		extends SimplePreparableReloadListener<Object>
{
	protected final BiConsumer<ResourceManager, ProfilerFiller> r;
	
	public RunnableReloader(BiConsumer<ResourceManager, ProfilerFiller> r)
	{
		this.r = r;
	}
	
	@Override
	protected Object prepare(ResourceManager resourceManagerIn, ProfilerFiller profilerIn)
	{
		return null;
	}
	
	@Override
	protected void apply(Object objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn)
	{
		r.accept(resourceManagerIn, profilerIn);
	}
	
	public static RunnableReloader of(Runnable r)
	{
		return of((a, b) -> r.run());
	}
	
	public static RunnableReloader of(Consumer<ResourceManager> r)
	{
		return of((a, b) -> r.accept(a));
	}
	
	public static RunnableReloader of(BiConsumer<ResourceManager, ProfilerFiller> r)
	{
		return new RunnableReloader(r);
	}
}