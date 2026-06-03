package com.zeitheron.hammercore.utils;

import com.google.common.base.Stopwatch;
import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.adapter.ChatMessageAdapter;
import com.zeitheron.hammercore.utils.java.io.win32.ModSourceAdapter;
import com.zeitheron.hammercore.utils.java.tuples.*;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.*;
import net.minecraftforge.fml.common.ProgressManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CommonMessages
{
	private static final List<Tuple2<String, CompletableFuture<CheckResult>>> PENDING_CHECKS = new ArrayList<>();
	public static final ITextComponent CRAFTING_MATERIAL = new TextComponentTranslation("info.hammerlib.material").setStyle(new Style().setColor(TextFormatting.GRAY));
	
	// Backwards compat.
	@Deprecated
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, Logger log, String modName, String downloadUrl)
	{
		checkModSource(modClass, log, modName, downloadUrl);
		return CheckResult.OK;
	}
	
	public static CompletableFuture<CheckResult> checkModSource(Class<?> modClass, Logger log, String modName, String downloadUrl)
	{
		ModSourceAdapter.ModSource src = ModSourceAdapter.getModSource(modClass).orElse(null);
		if(src == null) return CompletableFuture.completedFuture(CheckResult.OK);
		CompletableFuture<CheckResult> f = src.wasDownloadedIllegally().thenApply(illegal ->
		{
			if(!illegal) return CheckResult.OK;
			
			log.fatal("====================================================");
			log.fatal("== WARNING: {} was downloaded from {}, which has been marked as illegal site over at stopmodreposts.org.", modName, src.referrerDomain());
			log.fatal("== Please download the mod from {}", downloadUrl);
			log.fatal("====================================================");
			
			ITextComponent illegalUri = new TextComponentString(src.referrerDomain())
					.setStyle(new Style().setColor(TextFormatting.RED));
			
			ITextComponent smrUri = new TextComponentString("stopmodreposts.org")
					.setStyle(new Style()
							.setColor(TextFormatting.BLUE)
							.setUnderlined(true)
							.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://stopmodreposts.org/"))
							.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open webpage."))));
			
			String host = downloadUrl;
			try
			{
				host = new URL(downloadUrl).getAuthority();
			} catch(Exception ignored)
			{
			}
			
			ITextComponent curseforgeUri = new TextComponentString(host)
					.setStyle(new Style()
							.setColor(TextFormatting.BLUE)
							.setUnderlined(true)
							.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl))
							.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to open webpage."))));
			
			ChatMessageAdapter.sendOnFirstWorldLoad(new TextComponentString(
					"WARNING: " + modName + " was downloaded from ")
					.appendSibling(illegalUri)
					.appendText(", which has been marked as illegal site over at ")
					.appendSibling(smrUri)
					.appendText(". Please download the mod from ")
					.appendSibling(curseforgeUri)
					.appendText(".")
			);
			
			return CheckResult.VIOLATION_FOUND;
		});
		synchronized(PENDING_CHECKS)
		{
			PENDING_CHECKS.add(Tuples.immutable(modName, f));
		}
		return f;
	}
	
	public static void gameLoaded()
	{
		Stopwatch sw = Stopwatch.createStarted();
		ProgressManager.ProgressBar pg = ProgressManager.push("Await mod source checks...", PENDING_CHECKS.size());
		for(Tuple2<String, CompletableFuture<CheckResult>> pc : PENDING_CHECKS)
		{
			pg.step("Check " + pc.a());
			pc.b().join();
		}
		ProgressManager.pop(pg);
		HammerCore.LOG.info("Checked {} mod sources in {}.", PENDING_CHECKS.size(), sw.stop());
		PENDING_CHECKS.clear();
	}
	
	public enum CheckResult
	{
		OK,
		VIOLATION_FOUND;
	}
}