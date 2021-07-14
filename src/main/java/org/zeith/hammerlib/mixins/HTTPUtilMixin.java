package org.zeith.hammerlib.mixins;

import net.minecraft.util.HTTPUtil;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.zeith.hammerlib.event.GetSuitableLanPortEvent;
import org.zeith.hammerlib.util.java.NumberUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(HTTPUtil.class)
public class HTTPUtilMixin
{
	/**
	 * @author Zeitheron @ HammerLib
	 * @reason GetSuitableLanPortEvent
	 */
	@Overwrite
	public static int getAvailablePort()
	{
		AtomicInteger port = new AtomicInteger(25564);
		try(final ServerSocket ss = new ServerSocket(0))
		{
			port.set(ss.getLocalPort());
		} catch(IOException ioe)
		{
			port.set(25564);
		}
		NumberUtils.tryParse(System.getProperty("hammerlib.lanport"), NumberUtils.EnumNumberType.SHORT).ifPresent(n -> port.set(n.intValue()));
		GetSuitableLanPortEvent event = new GetSuitableLanPortEvent(port.get());
		MinecraftForge.EVENT_BUS.post(event);
		return event.getNewPort();
	}
}