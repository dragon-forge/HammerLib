package org.zeith.hammerlib.api.client;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.Cast;

public interface IEmissivePlayerInfo
{
	static IEmissivePlayerInfo get(PlayerInfo info)
	{
		return Cast.cast(info);
	}
	
	@NotNull
	ResourceLocation getEmissiveSkinLocation();
	
	@Nullable
	ResourceLocation getEmissiveCapeLocation();
	
	@Nullable
	ResourceLocation getEmissiveElytraLocation();
}