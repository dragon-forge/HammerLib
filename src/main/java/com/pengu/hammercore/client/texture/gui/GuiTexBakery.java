package com.pengu.hammercore.client.texture.gui;

import java.awt.Rectangle;

import com.endie.lib.tuple.TwoTuple;

public class GuiTexBakery
{
	public GuiTexBakery()
	{
	}
	
	private final DynGuiTex tex = new DynGuiTex();
	
	public static GuiTexBakery start()
	{
		return new GuiTexBakery();
	}
	
	public DynGuiTex bake()
	{
		return tex;
	}
	
	public GuiTexBakery body(int x, int y, int w, int h)
	{
		TexElement e = new TexElement(tex);
		e.parts.add(new TwoTuple<>(new Rectangle(x + 2, y + 2, w - 3, h - 3), 3)); // 0
		e.parts.add(new TwoTuple<>(new Rectangle(x + 1, y + 1, 1, 1), 0)); // 1
		e.parts.add(new TwoTuple<>(new Rectangle(x + 1, y + h - 3, 1, 1), 0)); // 2
		e.parts.add(new TwoTuple<>(new Rectangle(x + 2, y + h - 2, 1, 1), 0)); // 3
		e.parts.add(new TwoTuple<>(new Rectangle(x + w - 2, y + h - 2, 1, 1), 0)); // 4
		e.parts.add(new TwoTuple<>(new Rectangle(x + w - 3, y + 1, 1, 1), 0)); // 5
		e.parts.add(new TwoTuple<>(new Rectangle(x + w - 2, y + 2, 1, 1), 0)); // 6
		e.parts.add(new TwoTuple<>(new Rectangle(x + 2, y, w - 5, 1), 0)); // 7
		e.parts.add(new TwoTuple<>(new Rectangle(x, y + 2, 1, h - 5), 0)); // 8
		e.parts.add(new TwoTuple<>(new Rectangle(x + 3, y + h - 1, w - 5, 1), 0)); // 9
		e.parts.add(new TwoTuple<>(new Rectangle(x + w - 1, y + 3, 1, h - 5), 0)); // 10
		e.parts.add(new TwoTuple<>(new Rectangle(x + 3, y + 3, 1, 1), 1)); // 11
		e.parts.add(new TwoTuple<>(new Rectangle(x + 2, y + 1, w - 5, 2), 1)); // 12
		e.parts.add(new TwoTuple<>(new Rectangle(x + 1, y + 2, 2, h - 5), 1)); // 13
		e.parts.add(new TwoTuple<>(new Rectangle(x + w - 4, y + h - 4, 1, 1), 2)); // 14
		e.parts.add(new TwoTuple<>(new Rectangle(x + 3, y + h - 3, w - 5, 2), 2)); // 15
		e.parts.add(new TwoTuple<>(new Rectangle(x + w - 3, y + 3, 2, h - 5), 2)); // 16
		tex.elements.add(0, e);
		return this;
	}
	
	public GuiTexBakery slot(int x, int y)
	{
		return slot(x, y, 18, 18);
	}
	
	public GuiTexBakery slot(int x, int y, int w, int h)
	{
		TexElement e = null;
		
		for(int i = 0; i < tex.elements.size(); ++i)
		{
			TexElement te = tex.elements.get(i);
			if(te.intersects(x, y, w, h))
			{
				e = te;
				break;
			}
		}
		
		if(e == null)
			e = new TexElement(tex);
		e.parts.add(new TwoTuple<>(new Rectangle(x + w - 1, y, 1, 1), 6)); // 0
		e.parts.add(new TwoTuple<>(new Rectangle(x, y + h - 1, 1, 1), 6)); // 1
		e.parts.add(new TwoTuple<>(new Rectangle(x, y, w - 1, h - 1), 5)); // 2
		e.parts.add(new TwoTuple<>(new Rectangle(x + 1, y + 1, w - 1, h - 1), 4)); // 3
		e.parts.add(new TwoTuple<>(new Rectangle(x + 1, y + 1, w - 2, h - 2), 6)); // 4
		if(!tex.elements.contains(e))
			tex.elements.add(e);
		return this;
	}
}