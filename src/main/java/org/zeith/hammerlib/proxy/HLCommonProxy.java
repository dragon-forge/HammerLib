package org.zeith.hammerlib.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.api.LanguageHelper.LangMap;
import org.zeith.hammerlib.api.lighting.ColoredLight;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class HLCommonProxy
{
	public void commonSetup()
	{
	}

	public void clientSetup()
	{
	}

	public void applyLang(LangMap map)
	{
	}

	public String getLanguage()
	{
		return "en_us";
	}

	public String getLanguage(Player player)
	{
		if(player instanceof ServerPlayer ent)
			return ent.getLanguage();
		return getLanguage();
	}

	public Player getClientPlayer()
	{
		return null;
	}

	public ReloadableResourceManager getResourceManager()
	{
		MinecraftServer serv = ServerLifecycleHooks.getCurrentServer();
		if(serv != null) return (ReloadableResourceManager) serv.getServerResources().getResourceManager();
		return null;
	}

	public Stream<ColoredLight> getGlowingParticles(float partialTicks)
	{
		return Stream.empty();
	}

	private boolean finishedLoading;

	public void finishLoading()
	{
		finishedLoading = true;
	}

	public boolean hasFinishedLoading()
	{
		return finishedLoading;
	}

	public Consumer<FMLClientSetupEvent> addTESR(Class<?> owner, String member, Type tesr)
	{
		return null;
	}
}