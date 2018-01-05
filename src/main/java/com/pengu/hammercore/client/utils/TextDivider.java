package com.pengu.hammercore.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextDivider
{
	public static String[] divideByLenghtLimitOrChars(FontRenderer fontRendererObj, String line, int maxWidth, String keyChar)
	{
		return Joiner.on('\n').join(divideByLenghtLimit(fontRendererObj, line, maxWidth)).replaceAll(keyChar, "\n").split("\n");
	}
	
	public static String[] divideByLenghtLimit(FontRenderer fontRendererObj, String line, int maxWidth)
	{
		return divideByLenghtLimit_List(fontRendererObj, line, maxWidth).toArray(new String[0]);
	}
	
	public static List<String> divideByLenghtLimit_List(FontRenderer fontRendererObj, String line, int maxWidth)
	{
		List<String> lns = new ArrayList<>();
		String[] lines = line.split(" ");
		String currentLn = "";
		
		for(int i = 0; i < lines.length; ++i)
		{
			if(fontRendererObj.getStringWidth(currentLn + lines[i]) >= maxWidth)
			{
				if(currentLn.endsWith(" "))
					currentLn = currentLn.substring(0, currentLn.length() - 1);
				lns.add(currentLn);
				currentLn = "";
			}
			
			currentLn += lines[i] + " ";
		}
		
		if(!currentLn.isEmpty())
			lns.add(currentLn);
		return lns;
	}
}