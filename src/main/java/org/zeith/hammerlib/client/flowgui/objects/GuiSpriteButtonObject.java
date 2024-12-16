package org.zeith.hammerlib.client.flowgui.objects;

import lombok.Builder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;

public class GuiSpriteButtonObject
		extends GuiButtonObject
{
	public ResourceLocation texture;
	
	@Builder(builderClassName = "SpriteBtnBuilder")
	public GuiSpriteButtonObject(
			@NotNull String name,
			float alpha,
			int packedFGColor,
			boolean enabled,
			@NotNull Component message,
			@NotNull OnPress callback,
			Holder<SoundEvent> pressSound,
			@NotNull ResourceLocation customTexture
	)
	{
		super(name, alpha, packedFGColor, enabled, message, callback, pressSound, SPRITES);
		texture = customTexture;
	}
	
	@Override
	protected void renderButtonBg(Graphics gfx, MousePos pos)
	{
		gfx.blit(texture,
				0, 0, (int) width, (int) height,
				0, this.getTextureY(pos.isMouseWithin(this)),
				(int) width, (int) height,
				(int) width, (int) height * 3,
				ARGB.white(alpha)
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