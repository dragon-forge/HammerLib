package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Builder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.flowgui.*;

import java.util.function.Supplier;

public class GuiSpriteButtonObject
		extends GuiButtonObject
{
	public ResourceLocation texture;
	public Vec3 color = new Vec3(1, 1, 1);
	
	public Supplier<ShaderInstance> shader = FlowguiShaderRegistry.GUI_SHADER;
	public int shaderTexture = 0;
	
	@Builder(builderClassName = "SpriteButtonBuilder")
	public GuiSpriteButtonObject(
			@NotNull String name,
			float alpha,
			int packedFGColor,
			boolean enabled,
			@NotNull Component message,
			@NotNull OnPress callback,
			Supplier<SoundEvent> pressSound,
			Float pressSoundPitch,
			@NotNull ResourceLocation customTexture,
			Vec3 color
	)
	{
		super(name, alpha, packedFGColor, enabled, message, callback, pressSound, pressSoundPitch);
		texture = customTexture;
		if(color != null) this.color = color;
	}
	
	public GuiSpriteButtonObject shader(Supplier<ShaderInstance> shader)
	{
		this.shader = shader;
		return this;
	}
	
	public GuiSpriteButtonObject shaderTexture(int shaderTexture)
	{
		this.shaderTexture = shaderTexture;
		return this;
	}
	
	@Override
	protected void renderButtonBg(Graphics gfx, MousePos pos)
	{
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		gfx.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		gfx.drawManaged(() ->
		{
			RenderSystem.setShaderTexture(shaderTexture, texture);
			GuiImageObject.blitWithBlend(
					shader,
					gfx.gfx(),
					0, getTextureY(pos.isMouseWithin(this)),
					width, height,
					width, height * 3,
					alpha, color
			);
		});
		gfx.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	protected int getTextureY(boolean hovered)
	{
		int state = 1; // default
		if(!this.enabled) state = 0; // disabled
		else if(hovered) state = 2; // hovered
		return (int) (state * height);
	}
	
	public static SpriteButtonBuilder of(String name)
	{
		return new SpriteButtonBuilder()
				.name(name)
				.alpha(1F)
				.packedFGColor(UNSET_FG_COLOR)
				.enabled(true)
				.message(Component.empty())
				.callback(OnPress.NONE)
				.pressSound(SoundEvents.UI_BUTTON_CLICK);
	}
}