package com.zeitheron.hammercore.proxy;

import java.util.HashMap;
import java.util.Map;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.bookAPI.Book;
import com.zeitheron.hammercore.bookAPI.fancy.GuiHammerManual;
import com.zeitheron.hammercore.client.gui.impl.book.GuiBook;
import com.zeitheron.hammercore.event.RegisterBookEvent;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BookProxy_Client extends BookProxy_Common
{
	private final Map<String, Book> books = new HashMap<>();
	
	@Override
	public Object getBookInstanceById(String id)
	{
		return books.get(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBookInstance(Book book)
	{
		String id = book.bookId;
		if(books.putIfAbsent(id, book) != null)
			HammerCore.LOG.warn("A mod tried to register book with already used id: " + id + ", registered: " + books.get(id) + ", failed to register: " + book);
	}
	
	@Override
	public void openBookGui(String bookId)
	{
		if(getBookInstanceById(bookId) != null)
			Minecraft.getMinecraft().displayGuiScreen(new GuiBook((Book) getBookInstanceById(bookId)));
	}
	
	@Override
	public void openNewBook()
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiHammerManual());
	}
	
	@Override
	public void init()
	{
		MinecraftForge.EVENT_BUS.post(new RegisterBookEvent());
	}
}