package org.zeith.hammerlib.client.flowgui.objects;

import lombok.Builder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.MousePos;
import org.zeith.hammerlib.client.render.texture.GuiTexture;

import java.util.function.Supplier;

public class GuiSpriteButtonObject
		extends GuiButtonObject
{
	public Supplier<GuiTexture> texture;
	public Vec3 color = new Vec3(1, 1, 1);
	
	@Builder(builderClassName = "SpriteBtnBuilder")
	public GuiSpriteButtonObject(
			@NotNull String name,
			float alpha,
			int packedFGColor,
			boolean enabled,
			@NotNull Component message,
			@NotNull OnPress callback,
			Supplier<SoundEvent> pressSound,
			Float pressSoundPitch,
			@NotNull Supplier<GuiTexture> customTexture,
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
		var tx = texture.get().with(gfx);
		tx.state().setColor(ARGB.colorFromFloat(alpha, (float) color.x, (float) color.y, (float) color.z));
		tx.blitSegment(
				0, 0,
				0, this.getTextureY(pos.isMouseWithin(this)),
				width, height,
				width, height * 3
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
				.pressSound(SoundEvents.UI_BUTTON_CLICK::value);
	}
}