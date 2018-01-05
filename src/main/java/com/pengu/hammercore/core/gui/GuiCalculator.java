package com.pengu.hammercore.core.gui;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.UV;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.common.utils.Chars;
import com.pengu.hammercore.math.ExpressionEvaluator;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiCalculator extends GuiCentered
{
	private Set<Btn> btns = new HashSet<>();
	public static final UV bg = new UV(new ResourceLocation("hammercore", "textures/gui/gui_calculator.png"), 0, 0, 147, 147);
	
	private String expression = "";
	
	private int lastBtn = -1;
	private float closeMod = 0, btnMod = 0;
	
	public GuiCalculator()
	{
		xSize = 147;
		ySize = 147;
	}
	
	{
		btns.add(new Btn("Cl", 87, 60, btns.size(), 18, 14, () ->
		{
			expression = "";
		}));
		
		btns.add(new Btn(",", 116, 124, btns.size(), 18, 14, () ->
		{
			expression += ".";
		}));
		
		btns.add(new Btn("0", 96, 124, btns.size(), 18, 14, () ->
		{
			expression += "0";
		}));
		
		btns.add(new Btn("1", 87, 108, btns.size(), 18, 14, () ->
		{
			expression += "1";
		}));
		
		btns.add(new Btn("2", 106, 108, btns.size(), 18, 14, () ->
		{
			expression += "2";
		}));
		
		btns.add(new Btn("3", 125, 108, btns.size(), 18, 14, () ->
		{
			expression += "3";
		}));
		
		btns.add(new Btn("4", 87, 92, btns.size(), 18, 14, () ->
		{
			expression += "4";
		}));
		
		btns.add(new Btn("5", 106, 92, btns.size(), 18, 14, () ->
		{
			expression += "5";
		}));
		
		btns.add(new Btn("6", 125, 92, btns.size(), 18, 14, () ->
		{
			expression += "6";
		}));
		
		btns.add(new Btn("7", 87, 76, btns.size(), 18, 14, () ->
		{
			expression += "7";
		}));
		
		btns.add(new Btn("8", 106, 76, btns.size(), 18, 14, () ->
		{
			expression += "8";
		}));
		
		btns.add(new Btn("9", 125, 76, btns.size(), 18, 14, () ->
		{
			expression += "9";
		}));
		
		btns.add(new Btn(Chars.SQRT + "", 14, 76, btns.size(), 18, 14, () ->
		{
			expression += "sqrt(";
		}));
		
		btns.add(new Btn("sin", 34, 76, btns.size(), 18, 14, () ->
		{
			expression += "sin(";
		}));
		
		btns.add(new Btn("cos", 14, 92, btns.size(), 18, 14, () ->
		{
			expression += "cos(";
		}));
		
		btns.add(new Btn("tan", 34, 92, btns.size(), 18, 14, () ->
		{
			expression += "tan(";
		}));
		
		btns.add(new Btn("log", 14, 108, btns.size(), 18, 14, () ->
		{
			expression += "log(";
		}));
		
		btns.add(new Btn("abs", 34, 108, btns.size(), 18, 14, () ->
		{
			expression += "abs(";
		}));
		
		btns.add(new Btn(Chars.PI + "", 14, 124, btns.size(), 18, 14, () ->
		{
			expression += Chars.PI;
		}));
		
		btns.add(new Btn("E", 34, 124, btns.size(), 18, 14, () ->
		{
			expression += "E";
		}));
		
		btns.add(new Btn("+", 54, 76, btns.size(), 18, 14, () ->
		{
			expression += "+";
		}));
		
		btns.add(new Btn("-", 54, 92, btns.size(), 18, 14, () ->
		{
			expression += "-";
		}));
		
		btns.add(new Btn("*", 54, 108, btns.size(), 18, 14, () ->
		{
			expression += "*";
		}));
		
		btns.add(new Btn("/", 54, 124, btns.size(), 18, 14, () ->
		{
			expression += "/";
		}));
		
		btns.add(new Btn("(", 24, 60, btns.size(), 18, 14, () ->
		{
			expression += "(";
		}));
		
		btns.add(new Btn(")", 44, 60, btns.size(), 18, 14, () ->
		{
			expression += ")";
		}));
		
		btns.add(new Btn("=", 75, 130, btns.size(), 18, 14, () ->
		{
			if(expression.isEmpty())
				return;
			try
			{
				expression = ExpressionEvaluator.evaluateDouble(expression) + ";";
			} catch(Throwable err)
			{
				expression = "Error!";
			}
		}));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		bg.render(guiLeft, guiTop);
		
		if(mouseX >= guiLeft + 125 && mouseY >= guiTop + 1 && mouseX < guiLeft + 144 && mouseY < guiTop + 14)
			closeMod += (1 - closeMod) / 2F;
		else
			closeMod = 0;
		
		int btn = getButton(mouseX, mouseY);
		if(btn != lastBtn)
		{
			btnMod = 0;
			lastBtn = btn;
		}
		
		if(lastBtn != -1)
			btnMod += (1 - btnMod) / 16F;
		else
			btnMod = 0;
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, btnMod);
		RenderUtil.drawTexturedModalRect(guiLeft + getButtonX(lastBtn), guiTop + getButtonY(lastBtn), 147, 13, 18, 14);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, closeMod);
		RenderUtil.drawTexturedModalRect(guiLeft + 125, guiTop + 1, 147, 0, 19, 13);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_BLEND);
		
		for(Btn b : btns)
			b.draw(fontRenderer, guiLeft + b.x, guiTop + b.y);
		
		fontRenderer.drawString(I18n.translateToLocal("gui.hammercore:calculatron").replaceAll("vVERSION", "v@VERSION@"), (int) guiLeft + 14, (int) guiTop + 4, 0xFFFFFF, false);
		
		String expression = this.expression;
		if(expression.endsWith(";"))
			expression = expression.substring(0, expression.length() - 1);
		GL11.glPushMatrix();
		double maxLen = 132;
		int w = fontRenderer.getStringWidth(expression);
		GL11.glTranslated(guiLeft + 7.5, guiTop + 31 + ((31 - 14) / 2D) + (w > maxLen ? 1D - maxLen / w : 0), 0);
		if(w > 133)
			GL11.glScaled(133D / w, 133D / w, 1);
		fontRenderer.drawString(expression, 0, 0, 0, false);
		GL11.glPopMatrix();
	}
	
	private int getButton(int mouseX, int mouseY)
	{
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		for(Btn b : btns)
			if(mouseX >= b.x && mouseY >= b.y && mouseX < b.x + b.w && mouseY < b.y + b.h)
				return b.id;
		return -1;
	}
	
	private int getButtonX(int btn)
	{
		for(Btn b : btns)
			if(btn == b.id)
				return b.x;
		return 0;
	}
	
	private int getButtonY(int btn)
	{
		for(Btn b : btns)
			if(btn == b.id)
				return b.y;
		return 0;
	}
	
	private void click(int btn)
	{
		for(Btn b : btns)
			if(btn == b.id && b.onClick != null)
			{
				if(expression.equals("Error!") || expression.endsWith(";"))
					expression = "";
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
				b.onClick.run();
			}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		int btn = getButton(mouseX, mouseY);
		if(btn != -1)
			click(btn);
		
		if(mouseX >= guiLeft + 125 && mouseY >= guiTop + 1 && mouseX < guiLeft + 144 && mouseY < guiTop + 14)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
	}
	
	private static class Btn
	{
		private int x, y, id, w, h;
		private Runnable onClick;
		private String c;
		
		public Btn(String c, int x, int y, int id, int w, int h, Runnable onClick)
		{
			this.c = c;
			this.onClick = onClick;
			this.x = x;
			this.y = y;
			this.id = id;
			this.w = w;
			this.h = h;
		}
		
		public void draw(FontRenderer fr, double x, double y)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(x + (w - fr.getStringWidth(c)) / 2D, y + (h - 6.5) / 2, 0);
			fr.drawString(c, 0, 0, 0, false);
			GL11.glPopMatrix();
		}
	}
}