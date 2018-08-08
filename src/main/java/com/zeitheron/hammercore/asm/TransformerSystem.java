package com.zeitheron.hammercore.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.zeitheron.hammercore.lib.zlib.utils.TaskedThread;

/**
 * Internal use only!
 */
class TransformerSystem
{
	public static interface iASMHook
	{
		boolean accepts(String name);
		
		String opName();
		
		void transform(ClassNode node, boolean obf);
	}
	
	private String indentstr = "";
	private int indents = 0;
	private final List<iASMHook> hooks = new ArrayList<>();
	
	private final TaskedThread SAVE_THREAD = new TaskedThread();
	private final File CLASS_SAVE_DIR = new File("HammerCore", "asm_classes");
	
	private void saveClass(byte[] original, byte[] modified, String clazz)
	{
		final byte[] o = original.clone();
		final byte[] m = modified.clone();
		
		SAVE_THREAD.addTask(() ->
		{
			if(!CLASS_SAVE_DIR.isDirectory())
				CLASS_SAVE_DIR.mkdirs();
			try
			{
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(CLASS_SAVE_DIR, clazz.replaceAll("/", "_").replaceAll("[.]", "_") + ".zip")));
				
				zos.putNextEntry(new ZipEntry("origin.class"));
				zos.write(original);
				zos.closeEntry();
				
				if(!Arrays.equals(modified, original))
				{
					zos.putNextEntry(new ZipEntry("modified.class"));
					zos.write(modified);
					zos.closeEntry();
				}
				
				zos.putNextEntry(new ZipEntry("methods.txt"));
				{
					ClassNode cn = ObjectWebUtils.loadClass(m);
					for(MethodNode mn : cn.methods)
						zos.write((mn.name + " " + mn.desc + "\n").getBytes());
				}
				zos.closeEntry();
				
				zos.close();
			} catch(IOException e)
			{
				info("Failed to save ASM class for " + clazz);
				e.printStackTrace();
			}
		});
	}
	
	public void push()
	{
		if(indents < 15)
			++indents;
		indentstr = "";
		for(int i = 0; i < indents; ++i)
			indentstr += "  ";
	}
	
	public void pop()
	{
		if(indents > 0)
			--indents;
		indentstr = "";
		for(int i = 0; i < indents; ++i)
			indentstr += "  ";
	}
	
	public void info(String text)
	{
		if(indents == 0)
			HCASM.ASM_LOG.info(text);
		else
			HCASM.ASM_LOG.info(indentstr + "-" + text);
	}
	
	public void addHook(iASMHook hook)
	{
		hooks.add(hook);
	}
	
	String currentClass, transformedCurrentClass;
	
	public String getCurrentClass()
	{
		return currentClass;
	}
	
	public String getTransformedCurrentClass()
	{
		return transformedCurrentClass;
	}
	
	public byte[] transform(String name, String transformedName, byte[] data)
	{
		currentClass = name;
		transformedCurrentClass = transformedName;
		
		byte[] origin = data;
		boolean l = false;
		for(iASMHook h : hooks)
			if(h.accepts(transformedName) || h.accepts(name))
			{
				if(!l)
				{
					l = true;
					info("Transforming " + transformedName + " (" + name + ")...");
				}
				
				push();
				
				if(h.opName() != null)
					info(h.opName());
				
				boolean obf = !name.equals(transformedName);
				ClassNode node = ObjectWebUtils.loadClass(data);
				
				currentClass = name;
				transformedCurrentClass = transformedName;
				
				push();
				h.transform(node, obf);
				pop();
				
				data = ObjectWebUtils.writeClassToByteArray(node);
				
				pop();
			}
		
		// Not interesting
		/** Save classes that we are interested in. */
		// if(l || HammerCoreTransformer.CLASS_MAPPINGS.containsKey("L" +
		// transformedName.replaceAll("[.]", "/") + ";"))
		// saveClass(origin, data, transformedName);
		
		currentClass = null;
		transformedCurrentClass = null;
		
		return data;
	}
}