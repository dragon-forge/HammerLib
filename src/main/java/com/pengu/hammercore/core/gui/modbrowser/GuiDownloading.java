package com.pengu.hammercore.core.gui.modbrowser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.common.utils.FileSizeMetric;

import net.minecraft.client.gui.GuiScreen;

public class GuiDownloading extends GuiScreen
{
	public final GuiModBrowser browser;
	private final int mouseX, mouseY;
	public final File dest, opt_toRemove;
	public final URL url;
	public long expected_size;
	private boolean openBrowserBack, started;
	private long downloaded;
	
	public GuiDownloading(GuiModBrowser browser, int mouseX, int mouseY, File dest, File opt_toRemove, URL url, long expected_size)
	{
		this.browser = browser;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.dest = dest;
		this.opt_toRemove = opt_toRemove;
		this.url = url;
		this.expected_size = expected_size;
	}
	
	private void doDownload()
	{
		if(opt_toRemove != null && opt_toRemove.isFile() && !opt_toRemove.delete()) // try
		                                                                            // to
		                                                                            // delete
		                                                                            // by
		                                                                            // normal
		                                                                            // method
			opt_toRemove.deleteOnExit(); // if we can't do it, delete the file
			                             // on app exit
			
		try
		{
			URLConnection conn = url.openConnection();
			InputStream in = conn.getInputStream();
			OutputStream out = new FileOutputStream(dest);
			long len = conn.getContentLengthLong();
			if(len > 0)
				expected_size = len;
			
			byte[] buffer = new byte[867]; // not ram intense, just 1 KiB
			int read = 0;
			
			while((read = in.read(buffer)) > 0)
			{
				downloaded += read;
				out.write(buffer, 0, read);
			}
			
			out.close();
			in.close();
			
			openBrowserBack = true;
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		browser.drawScreen(this.mouseX, this.mouseY, partialTicks);
		drawGradientRect(0, 0, width, height, 0x44999999, 0x44999999);
		
		String str = "Downloaded: " + FileSizeMetric.toMaxSize(downloaded) + " / " + FileSizeMetric.toMaxSize(expected_size);
		drawCenteredString(fontRenderer, str, width / 2, height / 2 + fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		str = "                         ";
		
		RenderUtil.drawGradientRect(width / 2 - fontRenderer.getStringWidth(str), height / 2 - fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth(str) * 2, fontRenderer.FONT_HEIGHT * 2, 0xFF000000, 0xFF000000);
		
		double progress = ((double) downloaded) / (double) expected_size;
		
		GL11.glPushMatrix();
		GL11.glTranslated(width / 2 - fontRenderer.getStringWidth(str), 0, 0);
		GL11.glScaled(fontRenderer.getStringWidth(str) * 2 * progress, 1, 1);
		RenderUtil.drawGradientRect(0, height / 2 - fontRenderer.FONT_HEIGHT, 1, fontRenderer.FONT_HEIGHT * 2, 0xFF00FF00, 0xFF008800);
		GL11.glPopMatrix();
		
		Mouse.getDWheel();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if(!started)
		{
			started = true;
			new Thread(() ->
			{
				doDownload();
			}).start();
		}
		if(openBrowserBack)
		{
			browser.hasInstalled = true;
			mc.displayGuiScreen(browser);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
	}
}