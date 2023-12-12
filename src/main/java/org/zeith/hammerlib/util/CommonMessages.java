package org.zeith.hammerlib.util;

import net.minecraft.util.text.*;
import net.minecraft.util.text.event.*;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.client.adapter.ChatMessageAdapter;
import org.zeith.hammerlib.core.adapter.ModSourceAdapter;

import java.net.URL;
import java.util.function.Supplier;

public class CommonMessages
{
	public static final Supplier<ITextComponent> CRAFTING_MATERIAL = () -> new TranslationTextComponent("info.hammerlib.material").withStyle(TextFormatting.GRAY);
	
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
			
			ITextComponent illegalUri = new StringTextComponent(illegalSourceNotice.referrerDomain())
					.withStyle(s -> s.withColor(TextFormatting.RED));
			ITextComponent smrUri = new StringTextComponent("stopmodreposts.org")
					.withStyle(s -> s.withColor(TextFormatting.BLUE)
							.withUnderlined(true)
							.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://stopmodreposts.org/"))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to open webpage."))));
			
			String host = downloadUrl;
			try
			{
				host = new URL(downloadUrl).getAuthority();
			} catch(Exception err)
			{
			}
			
			ITextComponent curseforgeUri = new StringTextComponent(host)
					.withStyle(s -> s.withColor(TextFormatting.BLUE)
							.withUnderlined(true)
							.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to open webpage."))));
			
			ChatMessageAdapter.sendOnFirstWorldLoad(new StringTextComponent("WARNING: " + modName + " was downloaded from ")
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
	
	public enum CheckResult
	{
		OK,
		VIOLATION_FOUND;
	}
}