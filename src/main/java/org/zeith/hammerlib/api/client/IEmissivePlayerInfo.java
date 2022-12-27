package org.zeith.hammerlib.api.client;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.Cast;

/**
 * Interface to be applied to the vanilla {@link PlayerInfo} class to allow for access to custom player skin data.
 */
public interface IEmissivePlayerInfo
{
	/**
	 * Casts a {@link PlayerInfo} instance to an {@link IEmissivePlayerInfo} instance if possible.
	 *
	 * @param info
	 * 		PlayerInfo instance to cast
	 *
	 * @return Casted IEmissivePlayerInfo instance, or null if the provided PlayerInfo instance does not implement
	 * IEmissivePlayerInfo
	 */
	static IEmissivePlayerInfo get(PlayerInfo info)
	{
		return Cast.cast(info);
	}
	
	/**
	 * Gets the location of the emissive skin overlay for the player.
	 *
	 * @return Location of the emissive skin overlay for the player, or null if no emissive skin is set
	 */
	@Nullable
	ResourceLocation getEmissiveSkinLocation();
	
	/**
	 * Gets the location of the emissive cape overlay for the player.
	 *
	 * @return Location of the emissive cape overlay for the player, or null if no emissive cape is set
	 */
	@Nullable
	ResourceLocation getEmissiveCapeLocation();
	
	/**
	 * Gets the location of the emissive elytra overlay for the player.
	 *
	 * @return Location of the emissive elytra overlay for the player, or null if no emissive elytra is set
	 */
	@Nullable
	ResourceLocation getEmissiveElytraLocation();
}