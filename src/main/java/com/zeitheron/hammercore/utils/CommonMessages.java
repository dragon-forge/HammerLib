package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.client.adapter.ChatMessageAdapter;
import com.zeitheron.hammercore.utils.java.io.win32.ModSourceAdapter;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.*;
import org.apache.logging.log4j.Logger;

import java.net.URL;

public class CommonMessages
{
	public static final ITextComponent CRAFTING_MATERIAL = new TextComponentTranslation("info.hammerlib.material").setStyle(new Style().setColor(TextFormatting.GRAY));
	
	public static CheckResult printMessageOnIllegalRedistribution(Class<?> modClass, Logger log, String modName, String downloadUrl)
	{
		ModSourceAdapter.ModSource illegalSourceNotice = ModSourceAdapter.getModSource(modClass)
				.filter(ModSourceAdapter.ModSource::wasDownloadedIllegally)
				.orElse(null);
		
		if(illegalSourceNotice != null)
		{
			log.fatal("====================================================");
			log.fatal("== WARNING: " + modName + " was downloaded from " + illegalSourceNotice.referrerDomain() +
					", which has been marked as illegal site over at stopmodreposts.org.");
			log.fatal("== Please download the mod from " + downloadUrl);
			log.fatal("====================================================");
			
			ITextComponent illegalUri = new TextComponentString(illegalSourceNotice.referrerDomain())
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
			} catch(Exception err)
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
		}
		
		return CheckResult.OK;
	}
	
	public enum CheckResult
	{
		OK,
		VIOLATION_FOUND;
	}
}