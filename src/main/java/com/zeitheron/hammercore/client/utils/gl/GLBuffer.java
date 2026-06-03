package com.zeitheron.hammercore.client.utils.gl;

import org.lwjgl.BufferUtils;
import sun.nio.ch.DirectBuffer;

import java.nio.*;

import static com.zeitheron.hammercore.client.utils.RenderUtil.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.*;


/**
 * Buffers are handy for transfering a lot of data between program and GPU.
 * This is used in Colored Lux to allow more lights to be passed to the shader.
 */
public class GLBuffer
{
	public final int buffer;
	private boolean disposed = false;
	
	public int drawType = GL_STREAM_DRAW;
	public int bufferKind = GL_UNIFORM_BUFFER;
	
	/**
	 * Constructs a new OpenGL buffer. It is not yet bound to any draw type or buffer kind.
	 * The variables in this class may help with
	 */
	public GLBuffer()
	{
		buffer = glTask(() -> glGenBuffers());
		disposed = false;
	}
	
	/**
	 * Disposes this buffer for good. This frees GPU memory usage after your buffer is no longer used.
	 */
	private void dispose()
	{
		if(disposed) return;
		glTaskAsync(() -> glDeleteBuffers(buffer));
		disposed = true;
	}
	
	/**
	 * Binds the current buffer to the specified buffer kind.
	 */
	public void bindBuffer()
	{
		glBindBuffer(bufferKind, buffer);
	}
	
	/**
	 * Uploads raw buffer data to the GPU.
	 */
	public void bufferData(FloatBuffer data)
	{
		glTask(() ->
		{
			bindBuffer();
			glBufferData(bufferKind, data, drawType);
		});
	}
	
	public void bufferDataAndFree(FloatBuffer data)
	{
		glTask(() ->
		{
			try
			{
				bindBuffer();
				glBufferData(bufferKind, data, drawType);
			} finally
			{
				// Deallocate the buffer after uploading
				freeBuffer(data);
			}
		});
	}
	
	/**
	 * Uploads the given units of GL-writable data into this buffer to the GPU.
	 * The data is serialized by order, and the resulting buffer data is a sequence
	 * of all objects serialized into the buffer one after another.
	 * <p>
	 * Also, do NOT pass objects with different float count, since you may get a buffer overflow exception thrown.
	 * Be careful with this code!
	 */
	public <T extends IGLWritable> void bufferData(T... writables)
	{
		int size = writables[0].getFloatSize() * writables.length;
		
		FloatBuffer buf = BufferUtils.createFloatBuffer(size);
		IGLFloatBufferStream wr = IGLFloatBufferStream.forBuffer(buf);
		for(T w : writables) w.writeFloats(wr);
		buf.flip();
		
		bufferDataAndFree(buf);
	}
	
	public static void freeBuffer(Buffer buf)
	{
		if(buf instanceof DirectBuffer)
		{
			DirectBuffer db = (DirectBuffer) buf;
			if(db.cleaner() != null) db.cleaner().clean();
			Object a = db.attachment();
			if(a instanceof Buffer) freeBuffer((Buffer) a);
		}
	}
	
	/**
	 * Binds this buffer to a shader program.
	 * For this to function, your shader can (should) use <code>#version 330 compatibility</code>
	 * To add the buffer block to shader, refer to this GLSL code:
	 * <code>layout(std140) uniform bufBlockName
	 * {
	 * float data[512]; // block binding 0
	 * };</code>
	 * <p>
	 * The data filled into the block is sequential, if you want to use
	 * custom structures, check out {@link GLBuffer#bufferData(IGLWritable[])}.
	 */
	public void bindToShader(int program, int blockBinding, String bufBlockName)
	{
		glUniformBlockBinding(program, glGetUniformBlockIndex(program, bufBlockName), blockBinding);
		glBindBufferBase(bufferKind, 0, buffer);
	}
	
	@Override
	protected void finalize()
			throws Throwable
	{
		dispose();
		super.finalize();
	}
}