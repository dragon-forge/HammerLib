package org.zeith.hammerlib.core.adapter;

import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.LanguageReloadEvent;
import org.zeith.hammerlib.util.java.CloseableArrayList;

import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

@Mod.EventBusSubscriber
public class LanguageAdapter
{
	private static final Set<String> modids = new HashSet<>();

	/**
	 * Register in mod's constructor.
	 */
	public static void registerMod(String modid)
	{
		modids.add(modid);
	}

	@SubscribeEvent
	public static void reloadLangs(LanguageReloadEvent e)
	{
		IReloadableResourceManager mgr = HammerLib.PROXY.getResourceManager();
		if(mgr == null)
		{
			HammerLib.LOG.warn("Failed to reload HammerLib languages due to lack of available ResourceManager");
			return;
		}
		HammerLib.LOG.debug("Reloading HammerLib-enabled language namespaces: " + mgr.getNamespaces());
		for(String modId : modids)
			findFirstExisting(mgr,
					new ResourceLocation(modId, "langs/" + e.getLang().toLowerCase() + ".hl"),
					new ResourceLocation(modId, "lang/" + e.getLang().toLowerCase() + ".hl"),
					new ResourceLocation(modId, "langs/" + e.getLang().toLowerCase() + ".lang"),
					new ResourceLocation(modId, "lang/" + e.getLang().toLowerCase() + ".lang"))
					.ifPresent(langFile ->
					{
						HammerLib.LOG.debug("Hooking HammerLib language adapter for namespace " + modId + ": " + langFile);
						try(CloseableArrayList<IResource> resources = new CloseableArrayList<>(mgr.getResources(langFile)))
						{
							for(IResource res : resources)
							{
								Scanner in = new Scanner(res.getInputStream());
								while(in.hasNextLine())
								{
									String line = in.nextLine();
									if(line.startsWith("#")) continue;
									String[] kv = line.split("=", 2);
									if(kv.length == 2) e.translate(kv[0], kv[1]);
								}
							}
						} catch(Throwable ex)
						{
							HammerLib.LOG.error("Failed to load language file located at " + langFile, ex);
						}
					});
	}

	private static Optional<ResourceLocation> findFirstExisting(IResourceManager mgr, ResourceLocation... paths)
	{
		for(ResourceLocation path : paths)
			if(mgr.hasResource(path))
				return Optional.of(path);
		return Optional.empty();
	}
}