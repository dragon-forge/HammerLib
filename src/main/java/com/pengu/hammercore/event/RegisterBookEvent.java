package com.pengu.hammercore.event;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.bookAPI.Book;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RegisterBookEvent extends Event
{
	public void registerBook(Book book)
	{
		HammerCore.bookProxy.registerBookInstance(book);
	}
	
	public boolean isBookRegistered(String id)
	{
		return HammerCore.bookProxy.getBookInstanceById(id) != null;
	}
}