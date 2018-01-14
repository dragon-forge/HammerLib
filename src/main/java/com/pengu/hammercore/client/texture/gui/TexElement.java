package com.pengu.hammercore.client.texture.gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.endie.lib.tuple.TwoTuple;

import net.minecraft.client.gui.Gui;

public class TexElement
{
	public final List<TwoTuple<Rectangle, Integer>> parts = new ArrayList<>();
	public final DynGuiTex tex;
	
	public TexElement(DynGuiTex tex)
	{
		this.tex = tex;
	}
	
	public boolean intersects(int x, int y, int w, int h)
	{
		Rectangle rect = new Rectangle(x, y, w, h);
		for(TwoTuple<Rectangle, Integer> p : parts)
			if(p.get1().intersects(rect))
				return true;
		return false;
	}
	
	public void render(int x, int y)
	{
		for(int i = 0; i < parts.size(); ++i)
		{
			TwoTuple<Rectangle, Integer> p = parts.get(i);
			Rectangle r = p.get1();
			Gui.drawRect(x + r.x, y + r.y, x + r.x + r.width, y + r.y + r.height, tex.theme.getColor(p.get2().intValue()));
		}
	}
}