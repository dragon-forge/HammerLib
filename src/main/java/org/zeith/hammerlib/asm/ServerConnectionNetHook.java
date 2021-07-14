package org.zeith.hammerlib.asm;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CClientSettingsPacket;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.zeith.hammerlib.event.PlayerConnectionPacketEvent;
import org.zeith.hammerlib.proxy.HLConstants;

public class ServerConnectionNetHook
{
	public static void updateTime(ServerPlayerEntity player, long gameTime, long dayTime)
	{
		player.connection.send(new SUpdateTimePacket(gameTime, dayTime, true));
	}

	public static void handle(ServerPlayNetHandler handler, Object anything)
	{
		if(anything instanceof IPacket)
		{
			MinecraftForge.EVENT_BUS.post(new PlayerConnectionPacketEvent.Handle<>(handler, (IPacket<?>) anything));
		}
	}

	public static void send(ServerPlayNetHandler handler, IPacket anything)
	{
		MinecraftForge.EVENT_BUS.post(new PlayerConnectionPacketEvent.Send<>(handler, (IPacket<?>) anything));
	}

	@EventBusSubscriber
	public static class InternalHooks
	{
		@SubscribeEvent
		public static void handleClientSettings(PlayerConnectionPacketEvent<CClientSettingsPacket> e)
		{
			ServerPlayNetHandler connection = e.getConnection();
			CClientSettingsPacket settings = e.getPacket();
			HLConstants.PLAYER_LANGUAGE_MAP.put(connection.player.getGameProfile().getId(), settings.getLanguage());
		}
	}
}