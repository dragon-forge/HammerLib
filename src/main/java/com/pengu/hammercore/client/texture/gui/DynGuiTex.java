package com.pengu.hammercore.client.texture.gui;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.client.texture.gui.theme.GuiTheme;

public class DynGuiTex
{
	public GuiTheme theme = GuiTheme.current();
	public List<TexElement> elements = new ArrayList<>();
	
	boolean intersects(int x, int y, int w, int h)
	{
		for(TexElement elem : elements)
			if(elem.intersects(x, y, w, h))
				return true;
		return false;
	}
	
	public void render(int x, int y)
	{
		for(int i = 0; i < elements.size(); ++i)
			elements.get(i).render(x, y);
	}
}