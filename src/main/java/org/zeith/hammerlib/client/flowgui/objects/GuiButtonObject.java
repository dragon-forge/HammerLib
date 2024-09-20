package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.util.math.Point;

public class GuiButtonObject
		extends GuiObject
{
	public static final int UNSET_FG_COLOR = -1;
	
	public float alpha;
	protected int packedFGColor;
	public boolean enabled;
	public Component message;
	public OnPress callback;
	
	@Builder
	public GuiButtonObject(@NotNull String name,
						   float alpha,
						   int packedFGColor,
						   boolean enabled,
						   @NotNull Component message,
						   @NotNull OnPress callback
	)
	{
		super(name);
		this.alpha = alpha;
		this.packedFGColor = packedFGColor;
		this.enabled = enabled;
		this.message = message;
		this.callback = callback;
	}
	
	public GuiButtonObject setAlpha(float alpha)
	{
		this.alpha = alpha;
		return this;
	}
	
	public GuiButtonObject setPackedFGColor(int packedFGColor)
	{
		this.packedFGColor = packedFGColor;
		return this;
	}
	
	public GuiButtonObject setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		return this;
	}
	
	public GuiButtonObject setMessage(Component message)
	{
		this.message = message;
		return this;
	}
	
	public static GuiButtonObjectBuilder builder(String name)
	{
		return new GuiButtonObjectBuilder()
				.name(name)
				.alpha(1F)
				.packedFGColor(UNSET_FG_COLOR)
				.enabled(true)
				.message(Component.empty())
				.callback(OnPress.NONE);
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		Minecraft minecraft = Minecraft.getInstance();
		
		var pGuiGraphics = gfx.gfx();
		
		pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		pGuiGraphics.blitNineSliced(AbstractWidget.WIDGETS_LOCATION, 0, 0, (int) width, (int) height, 20, 4, 200, 20, 0, this.getTextureY(pos.isMouseWithin(this)));
		pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		int i = getFGColor();
		this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
	}
	
	public void onPress()
	{
		this.callback.onPress(this);
		playDownSound(Minecraft.getInstance().getSoundManager());
	}
	
	@Override
	protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button)
	{
		if(button == 0 && enabled && pos.isMouseWithin(this))
		{
			onPress();
			return true;
		}
		
		return false;
	}
	
	public void playDownSound(SoundManager pHandler)
	{
		pHandler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	private int getTextureY(boolean hovered)
	{
		int i = 1;
		if(!this.enabled)
		{
			i = 0;
		} else if(hovered)
		{
			i = 2;
		}
		
		return 46 + i * 20;
	}
	
	public int getFGColor()
	{
		if(packedFGColor != UNSET_FG_COLOR) return packedFGColor;
		return this.enabled ? 16777215 : 10526880; // White : Light Grey
	}
	
	public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor)
	{
		this.renderScrollingString(pGuiGraphics, pFont, 2, pColor);
	}
	
	protected void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, int pWidth, int pColor)
	{
		int i = pWidth;
		int j = (int) (width - pWidth);
		renderScrollingString(pGuiGraphics, pFont, message, i, 0, j, (int) height, pColor);
	}
	
	protected static void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, Component pText, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor)
	{
		int i = pFont.width(pText);
		int j = (pMinY + pMaxY - 9) / 2 + 1;
		int k = pMaxX - pMinX;
		if(i > k)
		{
			int l = i - k;
			double d0 = (double) Util.getMillis() / 1000.0D;
			double d1 = Math.max((double) l * 0.5D, 3.0D);
			double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
			double d3 = Mth.lerp(d2, 0.0D, (double) l);
			pGuiGraphics.enableScissor(pMinX, pMinY, pMaxX, pMaxY);
			pGuiGraphics.drawString(pFont, pText, pMinX - (int) d3, j, pColor);
			pGuiGraphics.disableScissor();
		} else
		{
			pGuiGraphics.drawCenteredString(pFont, pText, (pMinX + pMaxX) / 2, j, pColor);
		}
	}
	
	public static class GuiButtonObjectBuilder
	{
		private GuiButtonObjectBuilder name(String name)
		{
			this.name = name;
			return this;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public interface OnPress
	{
		OnPress NONE = (b) ->
		{
		};
		
		void onPress(GuiButtonObject button);
	}
}