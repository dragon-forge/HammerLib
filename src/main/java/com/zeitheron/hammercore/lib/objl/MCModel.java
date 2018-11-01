package com.zeitheron.hammercore.lib.objl;

import java.nio.FloatBuffer;
import java.util.function.BooleanSupplier;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

/**
 * This part was written by Zeitheron in order to render congard's models into
 * lwjgl. Untested, may be unstable!
 */
public class MCModel
{
	/** Is render thread? */
	public static final BooleanSupplier rt = () -> Minecraft.getMinecraft().isCallingFromMinecraftThread();
	
	public final Model obj;
	
	private final FloatBuffer vertices, texCoords, normals;
	
	/* Pls supply only loaded model! */
	public MCModel(Model obj)
	{
		this.vertices = FloatBuffer.wrap(obj.vertices);
		this.texCoords = FloatBuffer.wrap(obj.texCoords);
		this.normals = FloatBuffer.wrap(obj.normals);
		this.obj = obj;
	}
	
	/* Bind textures yourself, please. */
	public void render()
	{
		if(!rt.getAsBoolean())
			return;
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glVertexPointer(3, 0, vertices);
		GL11.glTexCoordPointer(2, 0, texCoords);
		GL11.glNormalPointer(0, normals);
		for(Model.VerticesDescriptor vd : this.obj.vd)
			GL11.glDrawArrays(vd.POLYTYPE, vd.START, vd.END);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
	}
	
	public void cleanup()
	{
		vertices.clear();
		texCoords.clear();
		normals.clear();
		obj.cleanup();
	}
}