package com.zeitheron.hammercore.bookAPI.fancy.pages;

public class ManualRawTextPage extends ManualTextPage
{
	public ManualRawTextPage(String text)
	{
		super(text);
	}
	
	@Override
	public String getTranslatedText()
	{
		return text;
	}
}