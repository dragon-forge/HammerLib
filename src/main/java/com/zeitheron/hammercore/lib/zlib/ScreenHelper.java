package com.zeitheron.hammercore.lib.zlib;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class ScreenHelper
{
	public static final Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	public static final Rectangle screen = new Rectangle(0, 0, ScreenHelper.screenCenter.x * 2, ScreenHelper.screenCenter.y * 2);
	public static final Robot robot;
	
	public static void init()
	{
		
	}
	
	static
	{
		Robot robo = null;
		try
		{
			robo = new Robot();
		} catch(Throwable throwable)
		{
			// empty catch block
		}
		robot = robo;
	}
	
	public static BufferedImage genScreen()
	{
		BufferedImage img = robot.createScreenCapture(screen);
//		Graphics2D g = img.createGraphics();
		return img;
	}
}