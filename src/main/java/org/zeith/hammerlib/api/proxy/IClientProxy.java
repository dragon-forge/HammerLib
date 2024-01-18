package org.zeith.hammerlib.api.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.HammerLib;

public interface IClientProxy
		extends IProxy
{
	@Override
	default Player getClientPlayer()
	{
		return HammerLib.PROXY.getClientPlayer();
	}
	
	@Override
	default Level getClientLevel()
	{
		var p = getClientPlayer();
		return p != null ? p.level : null;
	}
}