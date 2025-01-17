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
import java.util.function.Supplier;

public class CommonMessages
{
	private static final String EMPTY_KEY = HLConstants.id("empty").toLanguageKey("info");
	public static final Component CRAFTING_MATERIAL = Component.translatable(HLConstants.id("material").toLanguageKey("info")).withStyle(ChatFormatting.GRAY);
	public static final Supplier<Component> EMPTY = () -> Component.translatable(EMPTY_KEY);
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, Logger log, String modName, String downloadUrl)
	{
		return printMessageOnIllegalRedistribution(modClass, AbstractLogger.ofLog4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, org.slf4j.Logger log, String modName, String downloadUrl)
	{
		return printMessageOnIllegalRedistribution(modClass, AbstractLogger.ofSlf4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, Logger log, String modName, String downloadUrl)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, AbstractLogger.ofLog4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, org.slf4j.Logger log, String modName, String downloadUrl)
	{
		return printMessageOnFingerprintViolation(event, expectFingerprint, AbstractLogger.ofSlf4j(log), modName, downloadUrl);
	}
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, AbstractLogger log, String modName, String downloadUrl)
	{
		if(!FMLEnvironment.production)
			return CheckResult.DEV_ENV;
		
		var illegalSourceNotice = ModSourceAdapter.getModSource(modClass)
				.filter(ModSourceAdapter.ModSource::wasDownloadedIllegally)
				.orElse(null);
		
		if(illegalSourceNotice != null)
		{
			log.error("=".repeat(52));
			log.error("== WARNING: " + modName + " was downloaded from " + illegalSourceNotice.referrerDomain() +
					  ", which has been marked as illegal site over at stopmodreposts.org.");
			log.error("== Please download the mod from " + downloadUrl);
			log.error("=".repeat(52));
			
			var illegalUri = Component.literal(illegalSourceNotice.referrerDomain())
					.withStyle(s -> s.withColor(ChatFormatting.RED));
			var smrUri = Component.literal("stopmodreposts.org")
					.withStyle(s -> s.withColor(ChatFormatting.BLUE)
							.withUnderlined(true)
							.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://stopmodreposts.org/"))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to open webpage."))));
			
			String host = downloadUrl;
			try
			{
				host = new URL(downloadUrl).getAuthority();
			} catch(Exception err)
			{
			}
			
			var curseforgeUri = Component.literal(host)
					.withStyle(s -> s.withColor(ChatFormatting.BLUE)
							.withUnderlined(true)
							.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to open webpage."))));
			
			ChatMessageAdapter.sendOnFirstWorldLoad(Component.literal("WARNING: " + modName + " was downloaded from ")
					.append(illegalUri)
					.append(", which has been marked as illegal site over at ")
					.append(smrUri)
					.append(". Please download the mod from ")
					.append(curseforgeUri)
					.append(".")
			);
			
			return CheckResult.VIOLATION_FOUND;
		}
		
		return CheckResult.OK;
	}
	
	public static CheckResult printMessageOnFingerprintViolation(FMLFingerprintCheckEvent event, String expectFingerprint, AbstractLogger log, String modName, String downloadUrl)
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
		
		String host = downloadUrl;
		try
		{
			host = new URL(downloadUrl).getAuthority();
		} catch(Exception err)
		{
		}
		
		var curseforgeUri = Component.literal(host)
				.withStyle(s -> s.withColor(ChatFormatting.BLUE)
						.withUnderlined(true)
						.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl))
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to open webpage."))));
		
		ChatMessageAdapter.sendOnFirstWorldLoad(Component.literal("WARNING: " + modName + " was tampered by someone. Please download the mod from ")
				.append(curseforgeUri)
				.append(".")
		);
		
		return CheckResult.VIOLATION_FOUND;
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