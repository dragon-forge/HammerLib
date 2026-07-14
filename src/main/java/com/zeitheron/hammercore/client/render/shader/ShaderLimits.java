package com.zeitheron.hammercore.client.render.shader;

import com.zeitheron.hammercore.HammerCore;
import org.lwjgl.opengl.*;

public class ShaderLimits
{
	private static boolean resolved = false;
	private static int maxUBS;
	
	public static int getMaxUniformBlockSize()
	{
		if(resolved) return maxUBS;
		
		int ubs = GL11.glGetInteger(GL31.GL_MAX_UNIFORM_BLOCK_SIZE);
		if(ubs <= 0 || ubs == Integer.MAX_VALUE)
		{
			int p = ubs;
			ubs = 65536;
			HammerCore.LOG.warn("OpenGL GL_MAX_UNIFORM_BLOCK_SIZE reported invalid value of {}. Falling back to safer limit of {}", p, ubs);
		}
		
		resolved = true;
		maxUBS = ubs;
		return ubs;
	}
}