package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;

public class GuiSpriteButtonObject
		extends GuiButtonObject
{
	public ResourceLocation texture;
	public Vec3 color = new Vec3(1, 1, 1);
	
	@Builder(builderClassName = "SpriteBtnBuilder")
	public GuiSpriteButtonObject(
			@NotNull String name,
			float alpha,
			int packedFGColor,
			boolean enabled,
			@NotNull Component message,
			@NotNull OnPress callback,
			Holder<SoundEvent> pressSound,
			Float pressSoundPitch,
			@NotNull ResourceLocation customTexture,
			Vec3 color
	)
	{
		super(name, alpha, packedFGColor, enabled, message, callback, pressSound, SPRITES, pressSoundPitch);
		texture = customTexture;
		if(color != null) this.color = color;
	}
	
	@Override
	protected void renderButtonBg(Graphics gfx, MousePos pos)
	{
		
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		gfx.drawSpecial((mbs) ->
		{
			RenderSystem.setShaderTexture(0, texture);
			GuiImageObject.blitWithBlend(
					CoreShaders.POSITION_TEX_COLOR,
					gfx.gfx(),
					0, getTextureY(pos.isMouseWithin(this)),
					width, height,
					width, height * 3,
					alpha, color
			);
		});
		
		gfx.blit(texture,
				0, 0, (int) width, (int) height,
				0, this.getTextureY(pos.isMouseWithin(this)),
				(int) width, (int) height,
				(int) width, (int) height * 3,
				ARGB.color(Math.round(alpha * 255F), ARGB.color(color))
		);
	}
	
	@Override
	protected int getTextureY(boolean hovered)
	{
		int state = 1; // default
		if(!this.enabled) state = 0; // disabled
		else if(hovered) state = 2; // hovered
		return (int) (state * height);
	}
	
	public static SpriteBtnBuilder of(String name)
	{
		return new SpriteBtnBuilder()
				.name(name)
				.alpha(1F)
				.packedFGColor(UNSET_FG_COLOR)
				.enabled(true)
				.message(Component.empty())
				.callback(OnPress.NONE)
				.pressSound(SoundEvents.UI_BUTTON_CLICK);
	}
}