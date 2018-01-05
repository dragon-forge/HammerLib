package com.pengu.hammercore.client.render.tesr;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.annotations.AtTESR;
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.shader.ShaderProgram;
import com.pengu.hammercore.client.render.shader.impl.ShaderEnderField;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.common.blocks.tesseract.BlockTesseract;
import com.pengu.hammercore.common.blocks.tesseract.TileTesseract;
import com.pengu.hammercore.core.init.BlocksHC;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

@AtTESR(TileTesseract.class)
public class TESRTesseract extends TESR<TileTesseract>
{
	@Override
	public void renderTileEntityAt(TileTesseract te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		
		if(state.getBlock() == BlocksHC.TESSERACT && state.getValue(BlockTesseract.active))
		{
			GL11.glPushMatrix();
			GL11.glTranslated(x + .1, y + .1, z + .1);
			GL11.glScaled(.8, .8, .8);
			
			SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
			sbr.begin();
			sbr.drawBlock(0, 0, 0);
			
			if(ShaderEnderField.useShaders() && ShaderEnderField.endShader == null)
				ShaderEnderField.reloadShader();
			if(ShaderEnderField.useShaders() && ShaderEnderField.endShader != null)
			{
				ShaderEnderField.endShader.freeBindShader();
				ARBShaderObjects.glUniform4fARB(ShaderEnderField.endShader.getUniformLoc("color"), 0.044F, 0.036F, 0.063F, .8F);
			}
			UtilsFX.bindTexture("minecraft", "textures/entity/end_portal.png");
			Tessellator.getInstance().draw();
			if(ShaderEnderField.useShaders())
				ShaderProgram.unbindShader();
			
			GL11.glPopMatrix();
			
			if(destroyStage != null && destroyProgress > 0F)
			{
				TextureAtlasSprite sprite = DestroyStageTexture.getAsSprite(destroyProgress);
				RenderBlocks rb = RenderBlocks.forMod("hammercore");
				
				GLRenderState blend = GLRenderState.BLEND;
				blend.captureState();
				blend.on();
				
				GlStateManager.disableLighting();
				
				int bright = getBrightnessForRB(te, rb);
				
				Tessellator tess = Tessellator.getInstance();
				
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
				
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				
				rb.renderFromInside = false;
				
				rb.setRenderBounds(3.2 / 16, 3.2 / 16, 3.2 / 16, 12.8 / 16, 12.8 / 16, 12.8 / 16);
				
				rb.renderFaceXNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceXPos(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceYNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceYPos(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceZNeg(x, y, z, sprite, 1, 1, 1, bright);
				rb.renderFaceZPos(x, y, z, sprite, 1, 1, 1, bright);
				
				tess.draw();
				
				blend.reset();
			}
		}
	}
}