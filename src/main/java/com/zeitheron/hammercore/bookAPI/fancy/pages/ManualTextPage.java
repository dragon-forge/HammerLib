package com.zeitheron.hammercore.bookAPI.fancy.pages;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.bookAPI.fancy.HCFontRenderer;
import com.zeitheron.hammercore.bookAPI.fancy.ManualPage;
import com.zeitheron.hammercore.lib.zlib.utils.Joiner;

/**
 * A text page that could be wrapped into multiple pages.
 */
public class ManualTextPage extends ManualPage
{
	public ManualTextPage(String text)
	{
		super(text);
	}
	
	@Override
	public void addPage(HCFontRenderer fr, int maxWidth, int maxHeight, List<ManualPage> pages)
	{
		List<String> lines = fr.listFormattedStringToWidth(getTranslatedText(), maxWidth);
		int lnheight = fr.FONT_HEIGHT;
		
		if(lines.size() * lnheight > maxHeight)
		{
			List<String> cur = new ArrayList<>();
			for(String ln : lines)
			{
				int splitLim = (maxHeight - (pages.isEmpty() ? 20 : 0)) / lnheight;
				
				cur.add(ln);
				if(cur.size() >= splitLim)
				{
					pages.add(new ManualRawTextPage(Joiner.on(" ").join(cur)));
					cur.clear();
				}
			}
			if(!cur.isEmpty())
			{
				pages.add(new ManualRawTextPage(Joiner.on(" ").join(cur)));
				cur.clear();
			}
		} else
			pages.add(this);
	}
}