package com.pengu.hammercore.asm;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public class TransformerSystem
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
			HammerCoreCore.ASM_LOG.info(text);
		else
			HammerCoreCore.ASM_LOG.info(indentstr + "-" + text);
	}
	
	public void addHook(iASMHook hook)
	{
		hooks.add(hook);
	}
	
	public byte[] transform(String name, String transformedName, byte[] data)
	{
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
				
				push();
				h.transform(node, obf);
				pop();
				
				data = ObjectWebUtils.writeClassToByteArray(node);
				
				pop();
			}
		
		return data;
	}
}