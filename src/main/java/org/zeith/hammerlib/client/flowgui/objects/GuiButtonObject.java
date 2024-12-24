package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.util.math.Point;

public class GuiButtonObject
		extends GuiObject
{
	public static final int UNSET_FG_COLOR = -1;
	
	protected static final WidgetSprites SPRITES = new WidgetSprites(
			ResourceLocation.withDefaultNamespace("widget/button"),
			ResourceLocation.withDefaultNamespace("widget/button_disabled"),
			ResourceLocation.withDefaultNamespace("widget/button_highlighted")
	);
	
	public float alpha;
	protected int packedFGColor;
	public boolean enabled;
	public Component message;
	public OnPress callback;
	public Holder<SoundEvent> pressSound;
	public WidgetSprites sprites = SPRITES;
	public float pressSoundPitch = 1F;
	
	@Builder(builderClassName = "ButtonBuilder")
	public GuiButtonObject(@NotNull String name,
						   float alpha,
						   int packedFGColor,
						   boolean enabled,
						   @NotNull Component message,
						   @NotNull OnPress callback,
						   Holder<SoundEvent> pressSound,
						   @Nullable WidgetSprites sprites,
						   Float pressSoundPitch
	)
	{
		super(name);
		this.alpha = alpha;
		this.packedFGColor = packedFGColor;
		this.enabled = enabled;
		this.message = message;
		this.callback = callback;
		this.pressSound = pressSound;
		if(sprites != null) this.sprites = sprites;
		if(pressSoundPitch != null) this.pressSoundPitch = pressSoundPitch;
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
	
	public GuiButtonObject setPressSoundPitch(float pressSoundPitch)
	{
		this.pressSoundPitch = pressSoundPitch;
		return this;
	}
	
	public static ButtonBuilder builder(String name)
	{
		return new ButtonBuilder()
				.name(name)
				.alpha(1F)
				.packedFGColor(UNSET_FG_COLOR)
				.enabled(true)
				.message(Component.empty())
				.callback(OnPress.NONE)
				.pressSound(SoundEvents.UI_BUTTON_CLICK);
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		Minecraft minecraft = Minecraft.getInstance();
		
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		
		renderButtonBg(gfx, pos);
		
		int i = getFGColor();
		this.renderString(gfx.gfx(), minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
	}
	
	protected void renderButtonBg(Graphics gfx, MousePos pos)
	{
		var g = gfx.gfx();
		var p = g.pose();
		
		int wi = (int) width;
		int hi = (int) height;
		float sdX = width / wi;
		float sdY = height / hi;
		
		p.pushPose();
		p.scale(sdX, sdY, 1);
		g.blitSprite(
				RenderType::guiTextured,
				sprites.get(enabled, pos.isMouseWithin(this)),
				0,
				0,
				wi,
				hi,
				ARGB.white(this.alpha)
		);
		p.popPose();
	}
	
	public void onPress()
	{
		this.callback.onPress(this);
		playDownSound(Minecraft.getInstance().getSoundManager());
	}
	
	@Override
	protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake)
	{
		if(button == 0 && enabled && pos.isMouseWithin(this))
		{
			if(!fake) onPress();
			return true;
		}
		
		return fake && enabled && pos.isMouseWithin(this);
	}
	
	public void playDownSound(SoundManager pHandler)
	{
		if(pressSound != null)
			pHandler.play(SimpleSoundInstance.forUI(pressSound, pressSoundPitch));
	}
	
	protected int getTextureY(boolean hovered)
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
	
	public static class ButtonBuilder
	{
		private ButtonBuilder name(String name)
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