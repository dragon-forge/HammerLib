package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.math.Point;

import java.util.function.Supplier;

public class GuiButtonObject
		extends GuiObject
{
	public static final int UNSET_FG_COLOR = -1;
	
	public float alpha;
	protected int packedFGColor;
	public boolean enabled;
	public Component message;
	public OnPress callback;
	public Supplier<Holder<SoundEvent>> pressSound;
	public float pressSoundPitch = 1F;
	
	protected static final WidgetSprites SPRITES = new WidgetSprites(
			ResourceLocation.withDefaultNamespace("widget/button"),
			ResourceLocation.withDefaultNamespace("widget/button_disabled"),
			ResourceLocation.withDefaultNamespace("widget/button_highlighted")
	);
	
	@Builder
	public GuiButtonObject(@NotNull String name,
	                       float alpha,
	                       int packedFGColor,
	                       boolean enabled,
	                       @NotNull Component message,
	                       @NotNull OnPress callback,
	                       Supplier<Holder<SoundEvent>> pressSound,
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
	
	public static GuiButtonObjectBuilder builder(String name)
	{
		return new GuiButtonObjectBuilder()
				.name(name)
				.alpha(1F)
				.packedFGColor(UNSET_FG_COLOR)
				.enabled(true)
				.message(Component.empty())
				.callback(OnPress.NONE)
				.pressSound(Cast.constant(SoundEvents.UI_BUTTON_CLICK));
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		Minecraft minecraft = Minecraft.getInstance();
		renderButtonBg(gfx, pos);
		var pGuiGraphics = gfx.gfx();
		int i = getFGColor();
		this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
	}
	
	protected void renderButtonBg(Graphics gfx, MousePos pos)
	{
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		var gg = gfx.gfx();
		gg.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		gg.blitSprite(getSprite(enabled, pos.isMouseWithin(this)), 0, 0, (int) width, (int) height);
		gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);
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
			onPress();
			return true;
		}
		
		return false;
	}
	
	public void playDownSound(SoundManager pHandler)
	{
		if(pressSound != null)
		{
			var s = pressSound.get();
			if(s == null) return;
			pHandler.play(SimpleSoundInstance.forUI(s, pressSoundPitch));
		}
	}
	
	private ResourceLocation getSprite(boolean enabled, boolean hovered)
	{
		return SPRITES.get(enabled, hovered);
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