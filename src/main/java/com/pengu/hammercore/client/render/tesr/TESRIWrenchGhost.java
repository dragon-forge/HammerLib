package com.pengu.hammercore.client.render.tesr;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.annotations.AtTESR;
import com.pengu.hammercore.client.render.shader.ShaderProgram;
import com.pengu.hammercore.client.render.shader.impl.ShaderEnderField;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.tile.TileIWrenchGhost;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

@AtTESR(TileIWrenchGhost.class)
public class TESRIWrenchGhost extends TESR<TileIWrenchGhost>
{
	@Override
	public void renderTileEntityAt(TileIWrenchGhost te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		alpha = (te.timeLeft / (float) te.timeTotal) * alpha;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .3, y + .3, z + .3);
		GL11.glScaled(.4, .4, .4);
		
		SimpleBlockRendering sbr = RenderBlocks.getInstance().simpleRenderer;
		sbr.begin();
		sbr.drawBlock(0, 0, 0);
		
		if(ShaderEnderField.useShaders() && ShaderEnderField.endShader == null)
			ShaderEnderField.reloadShader();
		if(ShaderEnderField.useShaders() && ShaderEnderField.endShader != null)
		{
			ShaderEnderField.endShader.freeBindShader();
			ARBShaderObjects.glUniform4fARB(ShaderEnderField.endShader.getUniformLoc("color"), 0.044F, 0.036F, 0.063F, alpha);
		}
		UtilsFX.bindTexture("minecraft", "textures/entity/end_portal.png");
		Tessellator.getInstance().draw();
		if(ShaderEnderField.useShaders())
			ShaderProgram.unbindShader();
		
		GL11.glPopMatrix();
	}
}