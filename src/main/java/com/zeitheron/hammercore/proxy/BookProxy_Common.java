package com.zeitheron.hammercore.proxy;

import com.zeitheron.hammercore.bookAPI.Book;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BookProxy_Common
{
	public Object getBookInstanceById(String id)
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBookInstance(Book book)
	{
	}
	
	public void openBookGui(String bookId)
	{
	}
	
	public void openNewBook()
	{
		
	}
	
	public void init()
	{
	}
}