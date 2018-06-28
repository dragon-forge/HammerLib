package com.zeitheron.hammercore.bookAPI;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class Book
{
	public final List<BookCategory> categories = new ArrayList<>();
	public final String bookId;
	
	public ResourceLocation customBackground = new ResourceLocation("hammercore", "textures/gui/default_book_gui.png"), customEntryBackground = new ResourceLocation("hammercore", "textures/gui/entry_book_gui.png");
	public double width = 146, height = 180, book_text_limit = 120;
	
	public Book(String id)
	{
		this.bookId = id;
	}
}