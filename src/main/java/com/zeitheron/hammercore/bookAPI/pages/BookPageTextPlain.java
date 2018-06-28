package com.zeitheron.hammercore.bookAPI.pages;

import com.zeitheron.hammercore.bookAPI.BookEntry;
import com.zeitheron.hammercore.bookAPI.BookPage;
import com.zeitheron.hammercore.client.utils.TextDivider;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;

public class BookPageTextPlain extends BookPage
{
	public final String text;
	public String[] lines;
	
	public BookPageTextPlain(BookEntry entry, String text)
	{
		super(entry);
		this.text = text;
	}
	
	@Override
	public void prepare()
	{
		// Translate, then re-split text by lines
		lines = TextDivider.divideByLenghtLimit(Minecraft.getMinecraft().fontRenderer, I18n.translateToLocal(text), 234);
	}
	
	@Override
	public void render(int mouseX, int mouseY)
	{
		if(lines != null)
			for(int i = 0; i < lines.length; ++i)
				Minecraft.getMinecraft().fontRenderer.drawString(lines[i], 9, 12 + i * (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2), 0x000000, false);
	}
}