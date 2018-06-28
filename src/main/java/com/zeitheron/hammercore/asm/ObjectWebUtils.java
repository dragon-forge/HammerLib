package com.zeitheron.hammercore.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

/**
 * Useful utilities to handle class IO and transformation
 */
class ObjectWebUtils
{
	public static ClassNode loadClass(byte[] data)
	{
		ClassReader reader = new ClassReader(data);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		return node;
	}
	
	public static ClassNode loadClass(InputStream stream) throws IOException
	{
		ClassReader reader = new ClassReader(stream);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		return node;
	}
	
	public static ClassNode loadClass(File file) throws IOException
	{
		FileInputStream stream = new FileInputStream(file);
		ClassNode node = loadClass(stream);
		stream.close();
		return node;
	}
	
	public static ClassNode loadClass(URL url) throws IOException
	{
		URLConnection conn = url.openConnection();
		try
		{
			conn.setDoInput(true);
			conn.connect();
		} catch(Throwable er)
		{
		}
		InputStream stream = conn.getInputStream();
		ClassNode node = loadClass(stream);
		stream.close();
		return node;
	}
	
	public static byte[] writeClassToByteArray(ClassNode node)
	{
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}
	
	public static void writeClassToOutputStream(ClassNode node, OutputStream stream) throws IOException
	{
		stream.write(writeClassToByteArray(node));
	}
	
	public static void writeClassToFile(ClassNode node, File file) throws IOException
	{
		FileOutputStream os = new FileOutputStream(file);
		writeClassToOutputStream(node, os);
		os.close();
	}
	
	public static void writeClassToURL(ClassNode node, URL url) throws IOException
	{
		URLConnection conn = url.openConnection();
		try
		{
			conn.setDoOutput(true);
			conn.connect();
		} catch(Throwable er)
		{
		}
		OutputStream os = conn.getOutputStream();
		writeClassToOutputStream(node, os);
		os.close();
	}
}