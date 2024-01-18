package org.zeith.hammerlib.api.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
}