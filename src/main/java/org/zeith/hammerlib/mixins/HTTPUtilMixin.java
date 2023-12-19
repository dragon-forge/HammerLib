package org.zeith.hammerlib.mixins;

import net.minecraft.util.HttpUtil;
import net.neoforged.fml.LogicalSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.ConfigHL;
import org.zeith.hammerlib.core.adapter.ConfigAdapter;
import org.zeith.hammerlib.event.GetSuitableLanPortEvent;
import org.zeith.hammerlib.util.java.NumberUtils;

import java.io.IOException;
import java.net.ServerSocket;

@Mixin(HttpUtil.class)
public class HTTPUtilMixin
{
	/**
	 * @author Zeitheron @ HammerLib
	 * @reason GetSuitableLanPortEvent
	 */
	@Inject(
			method = "getAvailablePort",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getAvailablePort_HammerLib(CallbackInfoReturnable<Integer> cir)
	{
		GetSuitableLanPortEvent event = new GetSuitableLanPortEvent();
		NumberUtils.tryParse(System.getProperty("hammerlib.lanport"), NumberUtils.EnumNumberType.SHORT).ifPresent(n -> event.setNewPort(n.intValue()));

		ConfigAdapter.getConfigForSide(LogicalSide.CLIENT, ConfigHL.class).ifPresent(cfg ->
		{
			try(ServerSocket ss = new ServerSocket(cfg.clientSide.lanPort))
			{
				if(ss.getLocalPort() == cfg.clientSide.lanPort)
					event.setNewPort(ss.getLocalPort());
			} catch(IOException e)
			{
			}
		});

		HammerLib.postEvent(event);
		Integer i = event.getNewPort();

		if(i != null)
		{
			try(ServerSocket ss = new ServerSocket(i))
			{
			} catch(IOException e)
			{
				HammerLib.LOG.error("Failed to set port to " + i + ". Defaulting to vanilla behavior.");
				return;
			}

			cir.setReturnValue(i);
		}
	}
}