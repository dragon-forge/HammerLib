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
		return CheckResult.OK;
	}
	
	public enum CheckResult
	{
		OK,
		VIOLATION_FOUND;
	}
}
