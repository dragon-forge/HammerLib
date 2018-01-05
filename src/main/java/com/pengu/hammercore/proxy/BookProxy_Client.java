package com.pengu.hammercore.proxy;

import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.bookAPI.Book;
import com.pengu.hammercore.bookAPI.BookCategory;
import com.pengu.hammercore.bookAPI.BookEntry;
import com.pengu.hammercore.bookAPI.fancy.GuiHammerManual;
import com.pengu.hammercore.bookAPI.pages.BookPageTextPlain;
import com.pengu.hammercore.core.gui.book.GuiBook;
import com.pengu.hammercore.core.init.ItemsHC;
import com.pengu.hammercore.event.RegisterBookEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
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
			HammerCore.LOG.bigWarn("A mod tried to register book with already used id: " + id + ", registered: " + books.get(id) + ", failed to register: " + book);
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
		registerManual();
		
		MinecraftForge.EVENT_BUS.post(new RegisterBookEvent());
	}
	
	private void registerManual()
	{
		Book hammerCoreManual = new Book("hammercore:manual");
		
		BookCategory items = new BookCategory(hammerCoreManual, "items");
		items.setIcon(new ItemStack(ItemsHC.manual));
		
		BookCategory blocks = new BookCategory(hammerCoreManual, "blocks");
		
		{
			BookEntry entry = new BookEntry(items, "calculatron", "gui.hammercore:manual/items/calculatron.title");
			entry.setIcon(new ItemStack(ItemsHC.calculatron));
			
			new BookPageTextPlain(entry, "gui.hammercore:manual/items/calculatron.desc");
		}
		
		{
			BookEntry entry = new BookEntry(items, "battery", "gui.hammercore:manual/items/battery.title");
			entry.setIcon(new ItemStack(ItemsHC.battery));
			
			new BookPageTextPlain(entry, "gui.hammercore:manual/items/battery.desc");
		}
		
		{
			BookEntry entry = new BookEntry(items, "wrench", "gui.hammercore:manual/items/wrench.title");
			entry.setIcon(new ItemStack(ItemsHC.wrench));
			
			new BookPageTextPlain(entry, "gui.hammercore:manual/items/wrench.desc");
		}
		
		registerBookInstance(hammerCoreManual);
	}
}