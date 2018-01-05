package com.pengu.hammercore.core.gui.modbrowser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.common.utils.TextCoding;
import com.pengu.hammercore.core.modbrowser.ModBrowser;
import com.pengu.hammercore.core.modbrowser.ModBrowser.LoadedMod;
import com.pengu.hammercore.core.modbrowser.ModBrowser.Mod;
import com.pengu.hammercore.core.modbrowser.ModBrowser.Version;
import com.pengu.hammercore.core.modbrowser.ModBrowserUVs;
import com.pengu.hammercore.math.MathHelper;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

public class GuiModBrowser extends GuiScreen
{
	public final ModBrowser browser;
	private int offset = 0, beginIndex = 0;
	
	boolean hasInstalled = false;
	
	public GuiModBrowser(ModBrowser browser)
	{
		this.browser = browser;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		
		GLRenderState.BLEND.captureState();
		GLRenderState.BLEND.on();
		
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		
		for(int i = beginIndex; i < browser.mods.size(); ++i)
		{
			Mod mod = browser.mods.get(i);
			int y = (i - beginIndex) * 64 + offset + 4;
			
			mod.icon.render(0, y, 64, 64);
			
			int verX = 0, verY = 0;
			
			for(Version ver : mod.supportedVersions.keySet())
			{
				if(mouseX >= 68 + verX && mouseY >= y + 14 + verY && mouseX < 68 + verX + 18 && mouseY < y + 14 + verY + 18)
					GL11.glColor3f(1, 1, 1);
				else
					GL11.glColor3f(.6F, .6F, .6F);
				
				ver.icon.render(68 + verX, y + 14 + verY, 18, 18);
				
				GL11.glColor3f(1, 1, 1);
				
				if(mouseX >= 68 + verX && mouseY >= y + 14 + verY && mouseX < 68 + verX + 18 && mouseY < y + 14 + verY + 18)
					if(browser.loadedMods.get(mod.modid) == null)
						ModBrowserUVs.INSTALL.render(0, y, 64, 64);
					else
						ModBrowserUVs.INSTALLED.render(0, y, 64, 64);
				verY += 24;
				if(verY == 48)
				{
					verY = 0;
					verX += 24;
				}
			}
		}
		
		for(int i = beginIndex; i < browser.mods.size(); ++i)
		{
			Mod mod = browser.mods.get(i);
			int y = (i - beginIndex) * 64 + offset + 4;
			
			fontRenderer.drawString(mod.modName, 68, y, browser.loadedMods.get(mod.modid) == null ? 0xFFFFFF : 0x66FF66, true);
			
			if(mouseX >= 0 && mouseY >= y && mouseX < 64 && mouseY < y + 64)
			{
				drawHoveringText(Arrays.asList(mod.modName, mod.description + "", "by " + mod.authors), 56, y + 12);
				GlStateManager.disableLighting();
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
			}
			
			int verX = 0, verY = 0;
			
			for(Version ver : mod.supportedVersions.keySet())
			{
				if(mouseX >= 68 + verX && mouseY >= y + 14 + verY && mouseX < 68 + verX + 18 && mouseY < y + 14 + verY + 18)
				{
					LoadedMod lmod = browser.loadedMods.get(mod.modid);
					drawHoveringText(Arrays.asList("Latest: " + mod.supportedVersions.get(ver), "Version type: " + ver.getId(), lmod != null ? "Current Version: " + lmod.version + "" : "Not installed. Press to install"), 78 + verX, y + 16 + verY);
					GlStateManager.disableLighting();
					GlStateManager.enableAlpha();
					GlStateManager.enableBlend();
				}
				
				verY += 24;
				if(verY == 48)
				{
					verY = 0;
					verX += 24;
				}
			}
		}
		
		GlStateManager.disableAlpha();
		
		GLRenderState.BLEND.reset();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == 1)
			mc.displayGuiScreen(hasInstalled ? new GuiModBrowserRebootGame() : null);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		gg: for(int i = beginIndex; i < browser.mods.size(); ++i)
		{
			Mod mod = browser.mods.get(i);
			int y = (i - beginIndex) * 64 + offset + 4;
			
			int verX = 0, verY = 0;
			
			for(Version ver : mod.supportedVersions.keySet())
			{
				if(mouseX >= 68 + verX && mouseY >= y + 14 + verY && mouseX < 68 + verX + 18 && mouseY < y + 14 + verY + 18)
				{
					String url = IOUtils.followRedirects(mod.fileVersions.get(mod.supportedVersions.get(ver)));
					if(!url.startsWith("https") && url.startsWith("http"))
						url = url.replaceFirst("http", "https");
					
					URL u = new URL(url);
					
					String filename = u.getFile().substring(u.getFile().lastIndexOf("/"));
					File dest;
					
					mc.displayGuiScreen(new GuiDownloading(this, mouseX, mouseY, dest = new File("mods", TextCoding.urlDecode(filename)), browser.loadedMods.get(mod.modid) != null ? browser.loadedMods.get(mod.modid).associated_file : null, u, mod.fileSizes.get(mod.supportedVersions.get(ver))));
					
					if(ModsDownloaded.files.get(mod.modid) != null)
						ModsDownloaded.files.get(mod.modid).delete();
					ModsDownloaded.files.put(mod.modid, dest);
					
					mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
					break gg;
				}
				
				verY += 24;
				if(verY == 48)
				{
					verY = 0;
					verX += 24;
				}
			}
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		
		int degress = Mouse.getDWheel();
		
		if(degress != 0)
		{
			int delta = degress / -12;
			offset = (int) MathHelper.clip(offset - delta, (browser.mods.size() - 3) * -64, 0);
			beginIndex = 0;
		}
	}
}