package org.zeith.hammerlib.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
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

	public String getLanguage(PlayerEntity player)
	{
		if(player instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity ent = (ServerPlayerEntity) player;
			return HLConstants.PLAYER_LANGUAGE_MAP.getOrDefault(ent.getGameProfile().getId(), "en_us");
		}
		return getLanguage();
	}

	public PlayerEntity getClientPlayer()
	{
		return null;
	}

	public IReloadableResourceManager getResourceManager()
	{
		MinecraftServer serv = ServerLifecycleHooks.getCurrentServer();
		if(serv != null) return (IReloadableResourceManager) serv.getDataPackRegistries().getResourceManager();
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

	public Consumer<FMLClientSetupEvent> addTESR(Type owner, String member, Type tesr)
	{
		return null;
	}
}