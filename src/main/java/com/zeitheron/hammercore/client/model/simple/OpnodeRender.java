package com.zeitheron.hammercore.client.model.simple;

import java.util.Arrays;
import java.util.List;

import com.zeitheron.hammercore.client.render.vertex.SimpleBlockRendering;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;

/**
 * A renderer class for opnodes.
 */
public class OpnodeRender
{
	/**
	 * Renders opnodes to {@link SimpleBlockRendering}.
	 * 
	 * @param sbr
	 *            The renderer class.
	 * @param nodes
	 *            The opnode list. Get it from {@link SimpleModelParser}
	 * @param bright
	 *            The brightness of the model
	 * @param newTessellation
	 *            If we should call {@link SimpleBlockRendering#begin()} and
	 *            {@link SimpleBlockRendering#end()}
	 */
	public static void renderOpnodes(SimpleBlockRendering sbr, List<int[]> nodes, int bright, boolean newTessellation)
	{
		if(newTessellation)
			sbr.begin();
		for(int[] node : nodes)
			renderOpnode(sbr, node, bright, false);
		if(newTessellation)
			sbr.end();
	}
	
	/**
	 * Renders a single opnode to {@link SimpleBlockRendering}.
	 * 
	 * @param sbr
	 *            The renderer class.
	 * @param opnode
	 *            The opnode itself. Get it from {@link SimpleModelParser}
	 * @param bright
	 *            The brightness of the model
	 * @param newTessellation
	 *            If we should call {@link SimpleBlockRendering#begin()} and
	 *            {@link SimpleBlockRendering#end()}
	 */
	public static void renderOpnode(SimpleBlockRendering sbr, int[] opnode, int bright, boolean newTessellation)
	{
		if(newTessellation)
			sbr.begin();
		sbr.setBrightness(bright);
		for(int i = 0; i < opnode.length; ++i)
		{
			int code = opnode[i];
			
			if(code == ModelOpcodes.DFACE)
			{
				i++;
				EnumFacing f = EnumFacing.VALUES[opnode[i]];
				sbr.disableFace(f);
				continue;
			} else if(code == ModelOpcodes.EFACE)
			{
				i++;
				EnumFacing f = EnumFacing.VALUES[opnode[i]];
				sbr.enableFace(f);
				continue;
			} else if(code == ModelOpcodes.COLOR)
			{
				i++;
				int r = opnode[i];
				i++;
				int g = opnode[i];
				i++;
				int b = opnode[i];
				i++;
				int a = opnode[i];
				Arrays.fill(sbr.rgb, (r >> 16) | (g >> 8) | b);
				sbr.rb.renderAlpha = a / 255F;
				continue;
			} else if(code == ModelOpcodes.BOUNDS)
			{
				double m1 = SimpleModelParser.getDouble(opnode, i + 1);
				double m2 = SimpleModelParser.getDouble(opnode, i + 3);
				double m3 = SimpleModelParser.getDouble(opnode, i + 5);
				double m4 = SimpleModelParser.getDouble(opnode, i + 7);
				double m5 = SimpleModelParser.getDouble(opnode, i + 9);
				double m6 = SimpleModelParser.getDouble(opnode, i + 11);
				sbr.setRenderBounds(m1, m2, m3, m4, m5, m6);
				i += 12;
				continue;
			} else if(code == ModelOpcodes.EFACES)
				sbr.enableFaces();
			else if(code == ModelOpcodes.DFACES)
				sbr.disableFaces();
			else if(code == ModelOpcodes.DRAW)
				sbr.drawBlock(0, 0, 0);
			else if(code == ModelOpcodes.INAME)
			{
				i++;
				i += opnode[i];
			} else if(code == ModelOpcodes.ITEX)
			{
				i++;
				int len = opnode[i];
				i++;
				int face = opnode[i];
				i++;
				byte[] buf = new byte[len];
				for(int j = 0; j < len; ++j)
					buf[j] = (byte) opnode[i + j];
				i += len - 1;
				sbr.setSpriteForSide(EnumFacing.VALUES[face], Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(new String(buf)));
			}
		}
		if(newTessellation)
			sbr.end();
		Arrays.fill(sbr.rgb, 0xFFFFFF);
		sbr.rb.renderAlpha = 1F;
	}
}