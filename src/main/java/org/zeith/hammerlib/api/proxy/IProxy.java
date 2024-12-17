package org.zeith.hammerlib.api.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;

public interface IProxy
{
	default Player getClientPlayer()
	{
		return null;
	}
	
	default Level getClientLevel()
	{
		return null;
	}
	
	static <T extends IProxy> T create(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget)
	{
		return switch(FMLEnvironment.dist)
		{
			case CLIENT -> Cast.cast(((Supplier) clientTarget.get()).get());
			case DEDICATED_SERVER -> Cast.cast(((Supplier) serverTarget.get()).get());
			default -> throw new IllegalArgumentException("UNSIDED?");
		};
	}
	
	static <T> T createSided(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget)
	{
		return switch(FMLEnvironment.dist)
		{
			case CLIENT -> Cast.cast(((Supplier) clientTarget.get()).get());
			case DEDICATED_SERVER -> Cast.cast(((Supplier) serverTarget.get()).get());
			default -> throw new IllegalArgumentException("UNSIDED?");
		};
	}
	
	static void runSided(Supplier<Runnable> clientTarget, Supplier<Runnable> serverTarget)
	{
		switch(FMLEnvironment.dist)
		{
			case CLIENT -> clientTarget.get().run();
			case DEDICATED_SERVER -> serverTarget.get().run();
			default -> throw new IllegalArgumentException("UNSIDED?");
		}
	}
	
	static <T> T createOn(Dist expect, Supplier<Supplier<T>> clientTarget)
	{
		return FMLEnvironment.dist == expect ? Cast.cast(((Supplier) clientTarget.get()).get()) : null;
	}
	
	static void runOn(Dist expect, Supplier<Runnable> clientTarget)
	{
		if(FMLEnvironment.dist == expect)
			clientTarget.get().run();
	}
}