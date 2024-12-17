package org.zeith.hammerlib.client.render.texture;

import net.minecraft.client.gui.GuiGraphics;
import org.zeith.hammerlib.annotations.Assumes;

public record TextureGraphics(GuiTexture texture, GuiGraphics gfx, GraphicsState state)
{
	public void blitFull(
			float x, float y,
			float width, float height
	)
	{
		rawBlit(
				x, x + width,
				y, y + height,
				0, 1,
				0, 1,
				state.color
		);
	}
	
	@Assumes({ "256x256 texture file", "UV dimensions match with screen dimensions" })
	public void blitSegment(
			float x, float y,
			float uStart, float vStart,
			float width, float height
	)
	{
		blitSegment(x, y, uStart, vStart, width, height, 256, 256);
	}
	
	@Assumes("UV dimensions match with screen dimensions")
	public void blitSegment(
			float x, float y,
			float uStart, float vStart,
			float width, float height,
			float fileWidth, float fileHeight
	)
	{
		blitSegment(
				x, y,
				uStart, vStart, width, height,
				width, height,
				fileWidth, fileHeight
		);
	}
	
	public void blitSegment(
			float x, float y,
			float uStart, float vStart,
			float screenWidth, float screenHeight,
			float uvWidth, float uvHeight,
			float fileWidth, float fileHeight
	)
	{
		rawBlit(
				x, x + screenWidth,
				y, y + screenHeight,
				uStart / fileWidth, (uStart + uvWidth) / fileWidth,
				vStart / fileHeight, (vStart + uvHeight) / fileHeight
		);
	}
	
	public void rawBlit(
			// Screen Coords
			float pX1, float pX2,
			float pY1, float pY2,
			
			// UV Coords
			float pMinU, float pMaxU,
			float pMinV, float pMaxV
	)
	{
		rawBlit(pX1, pX2, pY1, pY2, pMinU, pMaxU, pMinV, pMaxV, state.getColor());
	}
	
	public void rawBlit(
			// Screen Coords
			float pX1, float pX2,
			float pY1, float pY2,
			
			// UV Coords
			float pMinU, float pMaxU,
			float pMinV, float pMaxV,
			
			// Tint
			int pColor
	)
	{
		gfx.drawSpecial(mbs ->
		{
			var type = texture.type();
			var matrix4f = gfx.pose().last().pose();
			var vertCons = mbs.getBuffer(type);
			float z = state.getZLevel();
			vertCons.addVertex(matrix4f, pX1, pY1, z).setUv(pMinU, pMinV).setColor(pColor);
			vertCons.addVertex(matrix4f, pX1, pY2, z).setUv(pMinU, pMaxV).setColor(pColor);
			vertCons.addVertex(matrix4f, pX2, pY2, z).setUv(pMaxU, pMaxV).setColor(pColor);
			vertCons.addVertex(matrix4f, pX2, pY1, z).setUv(pMaxU, pMinV).setColor(pColor);
		});
	}
}