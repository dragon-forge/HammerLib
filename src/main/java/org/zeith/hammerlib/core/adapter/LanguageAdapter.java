package org.zeith.hammerlib.core.adapter;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.LanguageReloadEvent;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class LanguageAdapter
{
	private static final Set<String> modids = Sets.newConcurrentHashSet();
	
	static
	{
		HammerLib.EVENT_BUS.addListener(LanguageAdapter::reloadLangs);
	}
	
	/**
	 * Register in mod's constructor.
	 */
	public static void registerMod(String modid)
	{
		modids.add(modid);
	}
	
	public static void reloadLangs(LanguageReloadEvent e)
	{
		ReloadableResourceManager mgr = HammerLib.PROXY.getResourceManager();
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
						
						List<Resource> resources = mgr.getResourceStack(langFile);
						
						try
						{
							for(Resource res : resources)
							{
								try(Scanner in = new Scanner(res.open(), StandardCharsets.UTF_8))
								{
									while(in.hasNextLine())
									{
										String line = in.nextLine();
										if(line.startsWith("#")) continue;
										String[] kv = line.split("=", 2);
										if(kv.length == 2) e.translate(kv[0], kv[1]);
									}
								}
							}
						} catch(Throwable ex)
						{
							HammerLib.LOG.error("Failed to load language file located at " + langFile, ex);
						}
					});
	}
	
	private static Optional<ResourceLocation> findFirstExisting(ResourceManager mgr, ResourceLocation... paths)
	{
		for(ResourceLocation path : paths)
			if(mgr.getResource(path).isPresent())
				return Optional.of(path);
		return Optional.empty();
	}
}