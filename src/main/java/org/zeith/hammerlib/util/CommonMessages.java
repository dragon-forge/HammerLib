package org.zeith.hammerlib.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.client.adapter.ChatMessageAdapter;
import org.zeith.hammerlib.core.adapter.ModSourceAdapter;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.proxy.HLConstants;

import java.net.URL;
import java.util.List;
import java.util.function.Supplier;

public class CommonMessages
{
	private static final String EMPTY_KEY = HLConstants.id("empty").toLanguageKey("info");
	public static final Component CRAFTING_MATERIAL = Component.translatable("info.hammerlib.material").withStyle(ChatFormatting.GRAY);
	public static final Supplier<Component> EMPTY = () -> Component.translatable(EMPTY_KEY);
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, Logger log, String modName, String downloadUrl)
	{
		return printMessageOnIllegalRedistribution(modClass, AbstractLogger.ofLog4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, org.slf4j.Logger log, String modName, String downloadUrl)
	{
		return printMessageOnIllegalRedistribution(modClass, AbstractLogger.ofSlf4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, Logger log, String modName, List<String> downloadUrl)
	{
		return printMessageOnIllegalRedistribution(modClass, AbstractLogger.ofLog4j(log), modName, List.copyOf(downloadUrl));
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, org.slf4j.Logger log, String modName, List<String> downloadUrl)
	{
		return printMessageOnIllegalRedistribution(modClass, AbstractLogger.ofSlf4j(log), modName, List.copyOf(downloadUrl));
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, Logger log, String modName, String downloadUrl)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, AbstractLogger.ofLog4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, org.slf4j.Logger log, String modName, String downloadUrl)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, AbstractLogger.ofSlf4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, Logger log, String modName, List<String> downloadUrls)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, AbstractLogger.ofLog4j(log), modName, downloadUrls);
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, org.slf4j.Logger log, String modName, List<String> downloadUrls)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, AbstractLogger.ofSlf4j(log), modName, downloadUrls);
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, AbstractLogger log, String modName, String downloadUrl)
	{
		return printMessageOnIllegalRedistribution(modClass, log, modName, List.of(downloadUrl));
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, AbstractLogger log, String modName, List<String> downloadUrls)
	{
		return printMessageOnIllegalRedistribution(modClass, log, modName, String.join(" or ", downloadUrls), createDownloadComponent(downloadUrls));
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, AbstractLogger log, String modName, String downloadUrl, Component downloadComponent)
	{
		if(!FMLEnvironment.production)
			return CheckResult.DEV_ENV;
		
		var illegalSourceNotice = ModSourceAdapter
				.getModSource(modClass)
				.filter(ModSourceAdapter.ModSource::wasDownloadedIllegally)
				.orElse(null);
		
		if(illegalSourceNotice != null)
		{
			log.error("=".repeat(52));
			log.error("== WARNING: " + modName + " was downloaded from " + illegalSourceNotice.referrerDomain() +
					", which has been marked as illegal site over at stopmodreposts.org.");
			log.error("== Please download the mod from " + downloadUrl);
			log.error("=".repeat(52));
			
			final String smr = "https://stopmodreposts.org/";
			var illegalUri = Component
					.literal(illegalSourceNotice.referrerDomain())
					.withStyle(s -> s.withColor(ChatFormatting.RED));
			var smrUri = Component
					.literal(getWebsiteName(smr))
					.withStyle(s -> s
							.withColor(ChatFormatting.BLUE)
							.withUnderlined(true)
							.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, smr))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to open webpage.")))
					);
			
			ChatMessageAdapter.sendOnFirstWorldLoad(Component
					.literal("WARNING: " + modName + " was downloaded from ")
					.append(illegalUri)
					.append(", which has been marked as illegal site over at ")
					.append(smrUri)
					.append(". Please download the mod from ")
					.append(downloadComponent)
					.append(".")
			);
			
			return CheckResult.VIOLATION_FOUND;
		}
		
		return CheckResult.OK;
	}
	
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, AbstractLogger log, String modName, String downloadUrl)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, log, modName, downloadUrl, createDownloadComponent(List.of(downloadUrl)));
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, AbstractLogger log, String modName, List<String> downloadUrls)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, log, modName, String.join(" or ", downloadUrls), createDownloadComponent(List.copyOf(downloadUrls)));
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, AbstractLogger log, String modName, String downloadUrl, Component downloadCom)
	{
		if(!FMLEnvironment.production)
			return CheckResult.DEV_ENV;
		
		var modFile = event.getModContainer().getModInfo().getOwningFile().getFile().getFileName();
		var trustData = event.trustData().map(s -> "Trust[" + s + "]").orElse("[No Trust Data]");
		
		if(!event.isViolated(expectFingerprint))
		{
			log.info("{} ({}) has passed the jar integrity check. {}", modName, modFile, trustData);
			return CheckResult.OK;
		}
		
		log.error("=".repeat(52));
		log.error("== WARNING: Somebody has been tampering with " + modName + "'s jar! (" + modFile + ")");
		log.error("== It is highly recommended that you re-download it from " + downloadUrl);
		log.error("== Expected fingerprint {}, but got {}. {}", expectFingerprint, event.fingerprint().map(s -> s.replace(":", "")).orElse(null), trustData);
		var set = event.getInvalidSignedFiles();
		if(!set.isEmpty())
		{
			log.error("== Here are " + set.size() + " files that have been found to be corrupted:");
			for(var e : set)
				log.error("== " + e);
		}
		log.error("=".repeat(52));
		
		ChatMessageAdapter.sendOnFirstWorldLoad(Component
				.literal("WARNING: " + modName + " was tampered by someone. Please download the mod from ")
				.append(downloadCom)
				.append(".")
		);
		
		return CheckResult.VIOLATION_FOUND;
	}
	
	public static MutableComponent createDownloadComponent(List<String> downloadUrls)
	{
		var downloadCom = Component.empty();
		var added = false;
		
		for(String url : downloadUrls)
		{
			var subCom = Component
					.literal(getWebsiteName(url))
					.withStyle(s -> s
							.withColor(ChatFormatting.BLUE)
							.withUnderlined(true)
							.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to open webpage.")))
					);
			if(added) downloadCom = downloadCom.append(" or ");
			downloadCom = downloadCom.append(subCom);
			added = true;
		}
		return downloadCom;
	}
	
	public static String getWebsiteName(String uri)
	{
		String host = uri;
		try
		{
			host = new URL(uri).getAuthority();
		} catch(Exception err)
		{
		}
		if(host.endsWith(".curseforge.com") || host.equals("curseforge.com")) return "CurseForge";
		if(host.equals("modrinth.com")) return "Modrinth";
		return host;
	}
	
	public enum CheckResult
	{
		OK,
		DEV_ENV,
		VIOLATION_FOUND;
	}
	
	interface AbstractLogger
	{
		void info(String text, Object... args);
		
		void error(String text, Object... args);
		
		static AbstractLogger ofSlf4j(org.slf4j.Logger log)
		{
			return new AbstractLogger()
			{
				@Override
				public void info(String text, Object... args)
				{
					log.info(text, args);
				}
				
				@Override
				public void error(String text, Object... args)
				{
					log.error(text, args);
				}
			};
		}
		
		static AbstractLogger ofLog4j(Logger log)
		{
			return new AbstractLogger()
			{
				@Override
				public void info(String text, Object... args)
				{
					log.info(text, args);
				}
				
				@Override
				public void error(String text, Object... args)
				{
					log.error(text, args);
				}
			};
		}
	}
}