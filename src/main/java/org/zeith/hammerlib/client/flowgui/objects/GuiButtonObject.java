package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.utils.GLStencil;
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
	public Supplier<SoundEvent> pressSound;
	public float pressSoundPitch = 1F;
	
	@Builder(builderClassName = "ButtonBuilder")
	public GuiButtonObject(@NotNull String name,
						   float alpha,
						   int packedFGColor,
						   boolean enabled,
						   @NotNull Component message,
						   @NotNull OnPress callback,
						   Supplier<SoundEvent> pressSound,
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
		renderButtonBg(gfx, pos);
		int i = getFGColor();
		this.renderString(gfx.gfx(), minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
	}
	
	protected void renderButtonBg(Graphics pGfx, MousePos pos)
	{
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		pGfx.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		pGfx.blitNineSliced(AbstractWidget.WIDGETS_LOCATION, 0, 0, (int) width, (int) height, 20, 4, 200, 20, 0, this.getTextureY(pos.isMouseWithin(this)));
		pGfx.setColor(1.0F, 1.0F, 1.0F, 1.0F);
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
		{
			var snd = pressSound.get();
			if(snd != null) pHandler.play(SimpleSoundInstance.forUI(snd, pressSoundPitch));
		}
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
			
			if(Minecraft.getInstance().getMainRenderTarget().isStencilEnabled())
				try(var stencil = GLStencil.of())
				{
					stencil.populateStencil(pGuiGraphics, gfx ->
							gfx.fill(pMinX, pMinY, pMaxX, pMaxY, 0xFFFFFFFF)
					);
					
					stencil.renderWithStencil(() ->
					{
						pGuiGraphics.drawString(pFont, pText, pMinX - (int) d3, j, pColor);
					});
				}
			else
			{
				Matrix4f mat = pGuiGraphics.pose().last().pose();
				Vector3f v1Pos = mat.transformPosition(pMinX, pMinY, 0, new Vector3f());
				Vector3f v2Pos = mat.transformPosition(pMaxX, pMinY, 0, new Vector3f());
				Vector3f v3Pos = mat.transformPosition(pMaxX, pMaxY, 0, new Vector3f());
				Vector3f v4Pos = mat.transformPosition(pMinX, pMaxY, 0, new Vector3f());
				float minX = Math.min(v1Pos.x, Math.min(v2Pos.x, Math.min(v3Pos.x, v4Pos.x)));
				float minY = Math.min(v1Pos.y, Math.min(v2Pos.y, Math.min(v3Pos.y, v4Pos.y)));
				float maxX = Math.max(v1Pos.x, Math.max(v2Pos.x, Math.max(v3Pos.x, v4Pos.x)));
				float maxY = Math.max(v1Pos.y, Math.max(v2Pos.y, Math.max(v3Pos.y, v4Pos.y)));
				pGuiGraphics.enableScissor((int) minX, (int) minY, (int) maxX, (int) maxY);
				pGuiGraphics.drawString(pFont, pText, pMinX - (int) d3, j, pColor);
				pGuiGraphics.disableScissor();
			}
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