package com.zeitheron.hammercore.bookAPI;

public abstract class BookPage
{
	public final BookEntry entry;
	
	public BookPage(BookEntry entry)
	{
		this.entry = entry;
		entry.pages.add(this);
	}
	
	public abstract void render(int mouseX, int mouseY);
	
	public void prepare()
	{
	};
}