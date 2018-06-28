package com.zeitheron.hammercore.bookAPI.pages;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.bookAPI.BookEntry;
import com.zeitheron.hammercore.bookAPI.BookPage;
import com.zeitheron.hammercore.client.utils.UV;

import net.minecraft.util.math.Vec2f;

public class BookPageCustom extends BookPage
{
	private final List<UV> uvs = new ArrayList<>();
	private final List<Vec2f> pos = new ArrayList<>();
	
	public BookPageCustom(BookEntry entry)
	{
		super(entry);
	}
	
	public BookPageCustom addElement(UV element, double x, double y)
	{
		uvs.add(element);
		pos.add(new Vec2f((float) x, (float) y));
		return this;
	}
	
	@Override
	public void render(int mouseX, int mouseY)
	{
		for(int i = 0; i < uvs.size(); ++i)
		{
			UV element = uvs.get(i);
			Vec2f pos = this.pos.get(i);
			element.render(pos.x, pos.y);
		}
	}
}