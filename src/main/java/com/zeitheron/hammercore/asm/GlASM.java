package com.zeitheron.hammercore.asm;

import java.util.List;
import java.util.ListIterator;

import javax.annotation.Nullable;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class GlASM
{
	final TransformerSystem asm;
	
	public GlASM(TransformerSystem asm)
	{
		this.asm = asm;
	}
	
	public byte[] patchForgeHooksASM(String name, byte[] bytes, boolean obfuscated, String transformedName)
	{
		asm.info("Transforming " + transformedName + " (" + name + ")...");
		asm.push();
		
		String targetMethod = "";
		String transformTypeName = "";
		if(obfuscated)
		{
			targetMethod = "handleCameraTransforms";
			transformTypeName = "Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;";
		} else
		{
			targetMethod = "handleCameraTransforms";
			transformTypeName = "Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;";
		}
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		
		for(MethodNode m : methods)
		{
			if(m.name.compareTo(targetMethod) == 0)
			{
				InsnList code = m.instructions;
				List<LocalVariableNode> vars = m.localVariables;
				int paramloc = -1;
				if(!obfuscated)
					paramloc = 1;
				for(int i = 0; i < vars.size() && paramloc == -1; i++)
				{
					LocalVariableNode p = vars.get(i);
					if(p.desc.compareTo(transformTypeName) == 0)
						paramloc = i;
				}
				
				if(paramloc > -1)
				{
					MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "setTransform", "(" + transformTypeName + ")V", false);
					code.insertBefore(code.get(2), method);
					code.insertBefore(code.get(2), new VarInsnNode(Opcodes.ALOAD, paramloc));
					asm.info("Patched handleCameraTransforms!");
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		asm.pop();
		return writer.toByteArray();
	}
	
	public byte[] patchGlStateManagerASM(String name, byte[] bytes, boolean obfuscated, String transformedName)
	{
		asm.info("Transforming " + transformedName + " (" + name + ")...");
		asm.push();
		
		String enableLighting = "";
		String disableLighting = "";
		String enableFog = "";
		String disableFog = "";
		if(obfuscated)
		{
			enableLighting = "func_179145_e";
			disableLighting = "func_179140_f";
			enableFog = "func_179127_m";
			disableFog = "func_179106_n";
		} else
		{
			enableLighting = "enableLighting";
			disableLighting = "disableLighting";
			enableFog = "enableFog";
			disableFog = "disableFog";
		}
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		
		for(MethodNode m : methods)
		{
			if(m.name.compareTo(enableLighting) == 0)
			{
				InsnList code = m.instructions;
				MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "enableLighting", "()V", false);
				code.insertBefore(code.get(2), method);
				asm.info("Patched enableLighting!");
			}
			if(m.name.compareTo(disableLighting) == 0)
			{
				InsnList code = m.instructions;
				MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "disableLighting", "()V", false);
				code.insertBefore(code.get(2), method);
				asm.info("Patched disableLighting!");
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		asm.pop();
		return writer.toByteArray();
	}
	
	public byte[] patchTERendererASM(String name, byte[] bytes, boolean obfuscated, String transformedName)
	{
		asm.info("Transforming " + transformedName + " (" + name + ")...");
		asm.push();
		
		String entityName = "";
		String targetMethod = "";
		String targetDesc = "";
		if(obfuscated)
		{
			targetMethod = "func_180546_a";
			entityName = "Lnet/minecraft/tileentity/TileEntity;";
			targetDesc = "(Lnet/minecraft/tileentity/TileEntity;FI)V";
		} else
		{
			targetMethod = "render";
			entityName = "Lnet/minecraft/tileentity/TileEntity;";
			targetDesc = "(Lnet/minecraft/tileentity/TileEntity;FI)V";
		}
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		
		for(MethodNode m : methods)
		{
			if(m.name.compareTo(targetMethod) == 0 && m.desc.compareTo(targetDesc) == 0)
			{
				InsnList code = m.instructions;
				List<LocalVariableNode> vars = m.localVariables;
				int paramloc = 1;
				@Nullable
				AbstractInsnNode returnNode = null;
				for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();)
				{
					AbstractInsnNode insn = iterator.next();
					
					if(insn.getOpcode() == Opcodes.RETURN)
					{
						returnNode = insn;
						break;
					}
				}
				
				if(returnNode != null && paramloc > -1)
				{
					MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "renderTileEntity", "(" + entityName + ")V", false);
					code.insertBefore(code.get(2), method);
					code.insertBefore(code.get(2), new VarInsnNode(Opcodes.ALOAD, paramloc));
					asm.info("Patched render!");
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		asm.pop();
		return writer.toByteArray();
	}
	
	public byte[] patchRenderManagerASM(String name, byte[] bytes, boolean obfuscated, String transformedName)
	{
		asm.info("Transforming " + transformedName + " (" + name + ")...");
		asm.push();
		
		String entityName = "";
		String entityFieldName = "";
		String targetMethod = "";
		String targetDesc = "";
		if(obfuscated)
		{
			targetMethod = "func_188391_a";
			entityName = "Lnet/minecraft/entity/Entity;";
			targetDesc = "(Lnet/minecraft/entity/Entity;DDDFFZ)V";
		} else
		{
			targetMethod = "renderEntity";
			entityName = "Lnet/minecraft/entity/Entity;";
			targetDesc = "(Lnet/minecraft/entity/Entity;DDDFFZ)V";
		}
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		
		for(MethodNode m : methods)
		{
			if(m.name.compareTo(targetMethod) == 0 && m.desc.compareTo(targetDesc) == 0)
			{
				InsnList code = m.instructions;
				List<LocalVariableNode> vars = m.localVariables;
				int paramloc = 1;
				@Nullable
				AbstractInsnNode returnNode = null;
				for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();)
				{
					AbstractInsnNode insn = iterator.next();
					
					if(insn.getOpcode() == Opcodes.RETURN)
					{
						returnNode = insn;
						break;
					}
				}
				
				if(returnNode != null && paramloc > -1)
				{
					MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "renderEntity", "(" + entityName + ")V", false);
					code.insertBefore(code.get(2), method);
					code.insertBefore(code.get(2), new VarInsnNode(Opcodes.ALOAD, paramloc));
					asm.info("Patched renderEntity!");
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		asm.pop();
		return writer.toByteArray();
	}
	
	public byte[] patchProfilerASM(String name, byte[] bytes, boolean obfuscated, String transformedName)
	{
		asm.info("Transforming " + transformedName + " (" + name + ")...");
		asm.push();
		
		String targetMethod = "";
		if(obfuscated)
			targetMethod = "func_76318_c";
		else
			targetMethod = "endStartSection";
		
		String targetMethod2 = "";
		if(obfuscated)
			targetMethod2 = "func_76320_a";
		else
			targetMethod2 = "startSection";
		
		String targetMethod3 = "";
		if(obfuscated)
			targetMethod3 = "func_76319_b";
		else
			targetMethod3 = "endSection";
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		
		for(MethodNode m : methods)
		{
			if(m.name.compareTo(targetMethod2) == 0 && m.desc.compareTo("(Ljava/lang/String;)V") == 0)
			{
				InsnList code = m.instructions;
				List<LocalVariableNode> vars = m.localVariables;
				int paramloc = 1;
				@Nullable
				AbstractInsnNode returnNode = null;
				for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();)
				{
					AbstractInsnNode insn = iterator.next();
					
					if(insn.getOpcode() == Opcodes.RETURN)
					{
						returnNode = insn;
						break;
					}
				}
				
				if(paramloc > -1)
				{
					MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "profilerStart", "(Ljava/lang/String;)V", false);
					code.insertBefore(code.get(2), method);
					code.insertBefore(code.get(2), new VarInsnNode(Opcodes.ALOAD, paramloc));
					asm.info("Patched startSection!");
				}
			}
			
			if(m.name.compareTo(targetMethod) == 0 && m.desc.compareTo("(Ljava/lang/String;)V") == 0)
			{
				InsnList code = m.instructions;
				List<LocalVariableNode> vars = m.localVariables;
				int paramloc = 1;
				@Nullable
				AbstractInsnNode returnNode = null;
				for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();)
				{
					AbstractInsnNode insn = iterator.next();
					
					if(insn.getOpcode() == Opcodes.RETURN)
					{
						returnNode = insn;
						break;
					}
				}
				
				if(paramloc > -1)
				{
					MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "profilerEndStart", "(Ljava/lang/String;)V", false);
					code.insertBefore(code.get(2), method);
					code.insertBefore(code.get(2), new VarInsnNode(Opcodes.ALOAD, paramloc));
					asm.info("Patched endStartSection!");
				}
			}
			
			if(m.name.compareTo(targetMethod3) == 0 && m.desc.compareTo("()V") == 0)
			{
				InsnList code = m.instructions;
				List<LocalVariableNode> vars = m.localVariables;
				int paramloc = 1;
				@Nullable
				AbstractInsnNode returnNode = null;
				for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();)
				{
					AbstractInsnNode insn = iterator.next();
					
					if(insn.getOpcode() == Opcodes.RETURN)
					{
						returnNode = insn;
						break;
					}
				}
				
				if(paramloc > -1)
				{
					MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "profilerEnd", "()V", false);
					code.insertBefore(code.get(2), method);
					asm.info("Patched endSection!");
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		asm.pop();
		return writer.toByteArray();
	}
	
	public byte[] patchRenderChunkASM(String name, byte[] bytes, boolean obfuscated, String transformedName)
	{
		asm.info("Transforming " + transformedName + " (" + name + ")...");
		asm.push();
		
		String renderChunkName = "";
		String targetMethod = "";
		if(obfuscated)
		{
			targetMethod = "func_178003_a";
			renderChunkName = "Lnet/minecraft/client/renderer/chunk/RenderChunk;";
		} else
		{
			targetMethod = "preRenderChunk";
			renderChunkName = "Lnet/minecraft/client/renderer/chunk/RenderChunk;";
		}
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		
		for(MethodNode m : methods)
		{
			if(m.name.compareTo(targetMethod) == 0 && m.desc.compareTo("(" + renderChunkName + ")V") == 0)
			{
				InsnList code = m.instructions;
				List<LocalVariableNode> vars = m.localVariables;
				int paramloc = -1;
				if(!obfuscated)
					paramloc = 1;
				for(int i = 0; i < vars.size() && paramloc == -1; i++)
				{
					LocalVariableNode p = vars.get(i);
					if(p.desc.compareTo(renderChunkName) == 0)
						paramloc = i;
				}
				@Nullable
				AbstractInsnNode returnNode = null;
				for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();)
				{
					AbstractInsnNode insn = iterator.next();
					if(insn.getOpcode() == Opcodes.RETURN)
					{
						returnNode = insn;
						break;
					}
				}
				
				if(returnNode != null && paramloc > -1)
				{
					code.insertBefore(returnNode, new VarInsnNode(Opcodes.ALOAD, paramloc));
					MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "preRenderChunk", "(" + renderChunkName + ")V", false);
					code.insertBefore(returnNode, method);
					asm.info("Patched preRenderChunk!");
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		asm.pop();
		return writer.toByteArray();
	}
}