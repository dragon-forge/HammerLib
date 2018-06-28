package com.zeitheron.hammercore.bookAPI;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class BookCategory
{
	public final List<BookEntry> entries = new ArrayList<>();
	
	public final Book book;
	public final String categoryId;
	
	public BookCategory(Book book, String id)
	{
		this.categoryId = id;
		this.book = book;
		book.categories.add(this);
	}
	
	public String getTitle()
	{
		return I18n.translateToLocal("bookapi." + book.bookId + ":" + categoryId);
	}
	
	public boolean isHidden()
	{
		return false;
	}
	
	public boolean isDisabled()
	{
		return false;
	}
	
	public int getHoverColor()
	{
		return isDisabled() ? 0xFF0000 : 0xFFFFFF;
	}
	
	protected ItemStack icon;
	
	public ItemStack getIcon()
	{
		return icon != null ? icon : ItemStack.EMPTY;
	}
	
	public void setIcon(ItemStack icon)
	{
		this.icon = icon;
	}
}