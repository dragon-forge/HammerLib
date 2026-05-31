package com.zeitheron.hammercore.client.render.shader;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Function;

import static org.lwjgl.opengl.ARBShaderObjects.*;

@Deprecated
public class ShaderProgram
{
	private int programID;
	private final ArrayList<IShaderOperation> ops = new ArrayList<>();
	
	public ShaderProgram()
	{
		programID = OpenGlHelper.glCreateProgram();
		if(programID == 0)
			throw new RuntimeException("Unable to allocate shader program object.");
	}
	
	public void attachShaderOperation(IShaderOperation operation)
	{
		ops.add(operation);
	}
	
	public void bindShader()
	{
		OpenGlHelper.glUseProgram(programID);
	}
	
	/**
	 * Allows you to bind the shader for use outside an IShaderOperation. You
	 * can still pass variables to the shader using an IShaderOperation.
	 * <p>
	 * Call this before you do your rendering. Then call
	 * ShaderProgram.unbindShader() when you have finished rendering.
	 */
	public void freeBindShader()
	{
		OpenGlHelper.glUseProgram(programID);
		for(IShaderOperation op : ops) op.operate(this);
	}
	
	public static void unbindShader()
	{
		OpenGlHelper.glUseProgram(0);
	}
	
	public ShaderProgram attachVert(String resource)
	{
		return attach(ARBVertexShader.GL_VERTEX_SHADER_ARB, resource);
	}
	
	public ShaderProgram attachFrag(String resource)
	{
		return attach(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB, resource);
	}
	
	public ShaderProgram attach(int shaderType, String resource)
	{
		InputStream stream = ShaderProgram.class.getResourceAsStream(resource);
		if(stream == null)
			throw new RuntimeException("Unable to locate resource: " + resource);
		return attach(shaderType, stream);
	}
	
	public ShaderProgram attach(int shaderType, InputStream stream)
	{
		if(stream == null)
			throw new RuntimeException("Invalid shader inputstream");
		
		int shaderID = 0;
		try
		{
			shaderID = OpenGlHelper.glCreateShader(shaderType);
			if(shaderID == 0)
				throw new RuntimeException("Unable to allocate shader object.");
			
			try
			{
				byte[] abyte = asString(stream).getBytes(StandardCharsets.UTF_8);
				ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
				bytebuffer.put(abyte);
				bytebuffer.position(0);
				OpenGlHelper.glShaderSource(shaderID, bytebuffer);
			} catch(IOException e)
			{
				throw new RuntimeException("Error reading inputstream.", e);
			}
			
			OpenGlHelper.glCompileShader(shaderID);
			if(OpenGlHelper.glGetShaderi(shaderID, OpenGlHelper.GL_COMPILE_STATUS) == GL11.GL_FALSE)
				throw new RuntimeException("Error compiling shader: " + getInfoLog(shaderID));
			
			OpenGlHelper.glAttachShader(programID, shaderID);
		} catch(RuntimeException e)
		{
			OpenGlHelper.glDeleteShader(shaderID);
			throw e;
		}
		return this;
	}
	
	/**
	 * Call this once you have bound your frag and vert shader.
	 *
	 * @return The validated shader program
	 */
	public ShaderProgram validate()
	{
		OpenGlHelper.glLinkProgram(programID);
		if(glGetObjectParameteriARB(programID, GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
			throw new RuntimeException("Error linking program: " + getInfoLog(programID));
		glValidateProgramARB(programID);
		if(glGetObjectParameteriARB(programID, GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
			throw new RuntimeException("Error validating program: " + getInfoLog(programID));
		return this;
	}
	
	public static String asString(InputStream stream)
			throws IOException
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader bin = new BufferedReader(new InputStreamReader(stream));
		String line;
		while((line = bin.readLine()) != null)
			sb.append(line).append('\n');
		stream.close();
		return sb.toString();
	}
	
	private static String getInfoLog(int shaderID)
	{
		return glGetInfoLogARB(shaderID, glGetObjectParameteriARB(shaderID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	Object2IntMap<String> uniforms = new Object2IntOpenHashMap<>(), attribs = new Object2IntOpenHashMap<>();
	Function<String, Integer> getUniform = name -> OpenGlHelper.glGetUniformLocation(programID, name);
	Function<String, Integer> getAttrib = name -> OpenGlHelper.glGetAttribLocation(programID, name);
	
	public int getUniformLoc(String name)
	{
		return uniforms.computeIfAbsent(name, getUniform);
	}
	
	public int getAttribLoc(String name)
	{
		return attribs.computeIfAbsent(name, getAttrib);
	}
	
	public void uniformTexture(String name, int textureIndex)
	{
		OpenGlHelper.glUniform1i(getUniformLoc(name), textureIndex);
	}
	
	public void glVertexAttributeMat4(int loc, Matrix4f matrix)
	{
		ARBVertexShader.glVertexAttrib4fARB(loc, matrix.m00, matrix.m01, matrix.m02, matrix.m03);
		ARBVertexShader.glVertexAttrib4fARB(loc + 1, matrix.m10, matrix.m11, matrix.m12, matrix.m13);
		ARBVertexShader.glVertexAttrib4fARB(loc + 2, matrix.m20, matrix.m21, matrix.m22, matrix.m23);
		ARBVertexShader.glVertexAttrib4fARB(loc + 3, matrix.m30, matrix.m31, matrix.m32, matrix.m33);
	}
	
	/**
	 * This method will completely remove the shader.
	 */
	public void cleanup()
	{
		ops.clear();
		ARBShaderObjects.glDeleteObjectARB(programID);
	}
	
	@Override
	protected void finalize()
			throws Throwable
	{
		cleanup();
		super.finalize();
	}
}