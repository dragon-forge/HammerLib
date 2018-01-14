package com.pengu.hammercore.client.texture.gui.theme;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.json.io.Jsonable;

public class GuiTheme implements Jsonable
{
	public static final List<GuiTheme> THEMES = new ArrayList<>();
	public static GuiTheme CURRENT_THEME;
	
	static
	{
		Scanner sc = new Scanner(HammerCore.class.getResourceAsStream("/assets/hammercore/themes/themes.txt"));
		while(sc.hasNext())
			makeTheme(sc.next());
		sc.close();
		CURRENT_THEME = getTheme("Vanilla");
	}
	
	public static GuiTheme makeTheme(String theme)
	{
		try
		{
			InputStream in;
			GuiTheme t = loadTheme(theme, ImageIO.read(in = HammerCore.class.getResourceAsStream("/assets/hammercore/themes/" + theme + ".png")));
			in.close();
			if(!THEMES.stream().filter(h -> h.name.equalsIgnoreCase(t.name)).findFirst().isPresent())
				THEMES.add(t);
			return t;
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	public static GuiTheme getTheme(String id)
	{
		Optional<GuiTheme> th = THEMES.stream().filter(h -> h.name.equalsIgnoreCase(id)).findFirst();
		return th.isPresent() ? th.get() : null;
	}
	
	public final String name;
	
	/* Body colors */
	public int bodyCover = 0x000000;
	public int bodyLayerLU = 0xFFFFFF;
	public int bodyLayerRD = 0x555555;
	public int bodyColor = 0xC6C6C6;
	
	/* Slot colors */
	public int slotCoverLU = 0xFFFFFF;
	public int slotCoverRD = 0x373737;
	public int slotColor = 0x8B8B8B;
	
	/* Text colors */
	public int textColor = 0xFFFFFF;
	public int textShadeColor = 0x555555;
	
	public GuiTheme(String name)
	{
		this.name = name;
	}
	
	public int size()
	{
		return 9;
	}
	
	private void applyColor(int id, int rgb)
	{
		if(id == 0)
			bodyCover = rgb;
		if(id == 1)
			bodyLayerLU = rgb;
		if(id == 2)
			bodyLayerRD = rgb;
		if(id == 3)
			bodyColor = rgb;
		if(id == 4)
			slotCoverLU = rgb;
		if(id == 5)
			slotCoverRD = rgb;
		if(id == 6)
			slotColor = rgb;
		if(id == 7)
			textColor = rgb;
		if(id == 8)
			textShadeColor = rgb;
	}
	
	public int getColor(int id)
	{
		if(id == 0)
			return setAlpha(bodyCover);
		if(id == 1)
			return setAlpha(bodyLayerLU);
		if(id == 2)
			return setAlpha(bodyLayerRD);
		if(id == 3)
			return setAlpha(bodyColor);
		if(id == 4)
			return setAlpha(slotCoverLU);
		if(id == 5)
			return setAlpha(slotCoverRD);
		if(id == 6)
			return setAlpha(slotColor);
		if(id == 7)
			return setAlpha(textColor);
		if(id == 8)
			return setAlpha(textShadeColor);
		
		return 0xFFFFFFFF;
	}
	
	public static GuiTheme current()
	{
		return CURRENT_THEME;
	}
	
	public static int setAlpha(int col)
	{
		return 255 << 24 | col;
	}
	
	public BufferedImage exportTheme()
	{
		BufferedImage img = new BufferedImage(3, 4, BufferedImage.TYPE_INT_ARGB);
		
		img.setRGB(0, 0, getColor(0));
		img.setRGB(0, 1, getColor(1));
		img.setRGB(0, 2, getColor(2));
		img.setRGB(0, 3, getColor(3));
		img.setRGB(1, 0, getColor(4));
		img.setRGB(1, 1, getColor(5));
		img.setRGB(1, 2, getColor(6));
		img.setRGB(2, 0, getColor(7));
		img.setRGB(2, 1, getColor(8));
		
		return img;
	}
	
	public void importTheme(BufferedImage img)
	{
		applyColor(0, img.getRGB(0, 0));
		applyColor(1, img.getRGB(0, 1));
		applyColor(2, img.getRGB(0, 2));
		applyColor(3, img.getRGB(0, 3));
		applyColor(4, img.getRGB(1, 0));
		applyColor(5, img.getRGB(1, 1));
		applyColor(6, img.getRGB(1, 2));
		applyColor(7, img.getRGB(2, 0));
		applyColor(8, img.getRGB(2, 1));
	}
	
	public static GuiTheme createTheme(String name, int... colors)
	{
		GuiTheme t = new GuiTheme(name);
		for(int i = 0; i < colors.length; ++i)
			t.applyColor(i, colors[i]);
		return t;
	}
	
	public static GuiTheme loadTheme(String name, BufferedImage colors)
	{
		GuiTheme t = new GuiTheme(name);
		t.importTheme(colors);
		return t;
	}
}