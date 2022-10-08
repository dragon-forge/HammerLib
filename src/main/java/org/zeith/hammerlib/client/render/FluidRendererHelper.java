package org.zeith.hammerlib.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.FluidTextureType;
import org.zeith.hammerlib.util.colors.ColorHelper;

public class FluidRendererHelper
{
	public static void renderFluidInGui(PoseStack pose, FluidStack fluid, FluidTextureType type, float full, float x, float y, float width, float height)
	{
		if(!fluid.isEmpty())
		{
			var sprite = getFluidTexture(fluid, type);
			int argb = getColorARGB(fluid);
			
			float a = ColorHelper.getAlpha(argb), r = ColorHelper.getRed(argb), g = ColorHelper.getGreen(argb), b = ColorHelper.getBlue(argb);
			
			// The height of the fluid
			float heightF = height * full;
			
			if(heightF > 0)
			{
				FXUtils.bindTexture(InventoryMenu.BLOCK_ATLAS);
				
				Tesselator tess = Tesselator.getInstance();
				BufferBuilder bb = tess.getBuilder();
				bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				
				float[] colors = RenderSystem.getShaderColor().clone();
				RenderSystem.setShaderColor(r, g, b, a);
				
				Matrix4f pose4f = pose.last().pose();
				
				float minX = sprite.getU(0);
				float maxX = sprite.getU(16);
				float maxY = sprite.getV(16);
				
				float ys = 0;
				while(heightF > 0)
				{
					float ch = Math.min(width, heightF);
					{
						float yCoord = y + height - ch - ys;
						float minY = sprite.getV(16 - Math.min(16, heightF * 16F / width));
						
						bb.vertex(pose4f, x, yCoord + ch, 0).uv(minX, maxY).endVertex();
						bb.vertex(pose4f, x + width, yCoord + ch, 0).uv(maxX, maxY).endVertex();
						bb.vertex(pose4f, x + width, yCoord, 0).uv(maxX, minY).endVertex();
						bb.vertex(pose4f, x, yCoord, 0).uv(minX, minY).endVertex();
					}
					heightF -= ch;
					ys += ch;
				}
				
				tess.end();
				
				RenderSystem.setShaderColor(colors[0], colors[1], colors[2], colors[3]);
			}
		}
	}
	
	public static TextureAtlasSprite getBaseFluidTexture(@NotNull Fluid fluid, @NotNull FluidTextureType type)
	{
		IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluid);
		ResourceLocation spriteLocation;
		if(type == FluidTextureType.STILL)
		{
			spriteLocation = properties.getStillTexture();
		} else
		{
			spriteLocation = properties.getFlowingTexture();
		}
		
		return getSprite(spriteLocation);
	}
	
	public static int getColorARGB(@NotNull FluidStack fluidStack)
	{
		return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
	}
	
	public static TextureAtlasSprite getFluidTexture(@NotNull FluidStack fluidStack, @NotNull FluidTextureType type)
	{
		IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
		return getSprite(type == FluidTextureType.STILL ? properties.getStillTexture(fluidStack) : properties.getFlowingTexture(fluidStack));
	}
	
	public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation)
	{
		return Minecraft.getInstance()
				.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
				.apply(spriteLocation);
	}
	
	public static int calculateGlowLight(int combinedLight, @NotNull FluidStack fluid)
	{
		return fluid.isEmpty() ? combinedLight : calculateGlowLight(combinedLight, fluid.getFluid().getFluidType().getLightLevel(fluid));
	}
	
	public static int calculateGlowLight(int combinedLight, int glow)
	{
		return combinedLight & -65536 | Math.max(Math.min(glow, 15) << 4, combinedLight & '\uffff');
	}
}