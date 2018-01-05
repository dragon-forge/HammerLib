package com.pengu.hammercore.bookAPI;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class BookEntry
{
	protected final List<BookPage> pages = new ArrayList<>();
	
	public final BookCategory category;
	public final String entryId;
	public String title;
	
	public BookEntry(BookCategory category, String id, String title)
	{
		this.title = title;
		this.entryId = id;
		this.category = category;
		category.entries.add(this);
	}
	
	public int getPageCount()
	{
		return pages.size();
	}
	
	public String getTitle()
	{
		return I18n.translateToLocal(title);
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
	
	@Nullable
	public BookPage getPageAt(int index)
	{
		return index < 0 || index >= pages.size() ? null : pages.get(index);
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