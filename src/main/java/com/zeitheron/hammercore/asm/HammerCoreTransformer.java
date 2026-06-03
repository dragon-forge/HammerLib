package com.zeitheron.hammercore.asm;

import com.zeitheron.hammercore.asm.TransformerSystem.IASMHook;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;

/**
 * Transforms classes. Internal use only!
 */
public class HammerCoreTransformer
		implements IClassTransformer
{
	public static final ClassnameMap CLASS_MAPPINGS = new ClassnameMap(
			"net/minecraft/entity/Entity", "vg",
			"net/minecraft/item/ItemStack", "aip",
			"net/minecraft/client/renderer/block/model/IBakedModel", "cfy",
			"net/minecraft/entity/EntityLivingBase", "vp",
			"net/minecraft/inventory/EntityEquipmentSlot", "vl",
			"net/minecraft/client/renderer/entity/RenderLivingBase", "caa",
			"net/minecraft/client/model/ModelBase", "bqf",
			"net/minecraft/util/DamageSource", "ur",
			"net/minecraft/entity/item/EntityBoat", "afd",
			"net/minecraft/world/World", "amu",
			"net/minecraft/util/math/BlockPos", "et",
			"net/minecraft/util/EnumFacing", "fa",
			"net/minecraft/entity/player/EntityPlayer", "aed",
			"net/minecraft/block/state/IBlockState", "awt",
			"net/minecraft/client/renderer/BufferBuilder", "buk",
			"net/minecraft/world/IBlockAccess", "amy",
			"net/minecraft/client/renderer/block/model/BakedQuad", "bvp",
			"net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType", "bwc$b"
	);
	
	static final TransformerSystem asm = new TransformerSystem();
	
	public static final boolean CFG_ASM_WORLD_ITICKABLE_OVERRIDE;
	
	static
	{
		JSONObject props = new JSONObject();
		File asmDir = new File("asm");
		if(!asmDir.isDirectory()) asmDir.mkdirs();
		new File(asmDir, "hammercore.xml").delete();
		
		long now = System.currentTimeMillis();
		final File propsJSON = new File(asmDir, "hammerlib.json");
		if(propsJSON.isFile()) try
		{
			props = new JSONObject(new String(Files.readAllBytes(propsJSON.toPath()), StandardCharsets.UTF_8));
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		
		final AtomicBoolean hasChanged = new AtomicBoolean(false);
		final Runnable setChanged = () -> hasChanged.set(true);
		
		long fLastMod = propsJSON.lastModified();
		long cfgLastMod = parsePropertyLong(props, "Config.LastModified", now, "Do not modify this field. Stores the time when the default config values have been updated.", setChanged);
		
		boolean tickSlip = parsePropertyBool(props, "World.ITickable.Override", false, "Should HammerLib transform world update method: Enhance ITickable.update method call with TickSlip technology? This is probably incompatible with Sponge.", setChanged);
		
		CFG_ASM_WORLD_ITICKABLE_OVERRIDE = tickSlip;
		
		if(hasChanged.get())
		{
			HCASM.ASM_LOG.info("HammerLib ASM configs have changed, saving.");
			
			props.put("Config.LastModified", Long.toUnsignedString(now));
			try
			{
				Files.write(propsJSON.toPath(), props.toString(2).getBytes(StandardCharsets.UTF_8));
			} catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			propsJSON.setLastModified(now);
		}
		
		hook((node, obf) ->
				{
					MethodSignature sig1 = new MethodSignature("renderItem", "func_180454_a", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
					MethodSignature sig2 = new MethodSignature("renderEffect", "func_191966_a", "a", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
					MethodSignature sig3 = new MethodSignature("renderItemModel", "func_184394_a", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V");
					MethodSignature sig4 = new MethodSignature("renderItemModelIntoGUI", "func_191962_a", "a", "(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V");
					
					for(MethodNode method : node.methods)
					{
						if((!method.name.equals(sig1.funcName) && !method.name.equals(sig1.obfName) && !method.name.equals(sig1.srgName)) ||
								(!method.desc.equals(sig1.funcDesc) && !method.desc.equals(sig1.obfDesc)))
							continue;
						
						InsnList insn = method.instructions;
						InsnList newInstructions = new InsnList();
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
						newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/utils/ItemColorHelper", "setTargetStackAndHandleRender", "(Lnet/minecraft/item/ItemStack;)V", false));
						insn.insertBefore(insn.get(0), newInstructions);
						asm.info("Sending instructions to RenderItem for function renderItem");
					}
					
					for(MethodNode method : node.methods)
					{
						if((!method.name.equals(sig2.funcName) && !method.name.equals(sig2.obfName) && !method.name.equals(sig2.srgName)) ||
								(!method.desc.equals(sig2.funcDesc) && !method.desc.equals(sig2.obfDesc)))
							continue;
						
						InsnList insn = method.instructions;
						
						boolean worked = false;
						
						for(int i = 0; i < insn.size(); ++i)
						{
							AbstractInsnNode n = insn.get(i);
							
							if(n.getOpcode() == 18 && ((LdcInsnNode) n).cst.equals(-8372020))
							{
								insn.insert(n, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/utils/ItemColorHelper", "getCustomColor", "(I)I", false));
								worked = true;
							}
						}
						
						if(worked)
							asm.info("Sending instructions to RenderItem for function renderEffect");
					}
					
					for(MethodNode method : node.methods)
					{
						if((!method.desc.equals("(Laip;Lcfy;Lbwc$b;Z)V") || !method.name.equals("a")) &&
								((!method.name.equals(sig3.funcName) && !method.name.equals(sig3.obfName) && !method.name.equals(sig3.srgName)) ||
										(!method.desc.equals(sig3.funcDesc) && !method.desc.equals(sig3.obfDesc))))
							continue;
						
						MethodSignature sigRet = new MethodSignature("renderItemModel", "func_184394_a", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V");
						String desc = method.desc.equals(sig3.obfDesc) ? sigRet.obfDesc : sigRet.funcDesc;
						
						InsnList insn = method.instructions;
						InsnList newInstructions = new InsnList();
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
						newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/utils/ItemColorHelper", "renderItemModel", desc, false));
						insn.insert(newInstructions);
						asm.info("Sending instructions to RenderItem for function renderItemModel");
					}
					
					for(MethodNode method : node.methods)
					{
						if((!method.name.equals(sig4.funcName) && !method.name.equals(sig4.obfName) && !method.name.equals(sig4.srgName)) ||
								(!method.desc.equals(sig4.funcDesc) && !method.desc.equals(sig4.obfDesc)))
							continue;
						
						InsnList insn = method.instructions;
						InsnList newInstructions = new InsnList();
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
						newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
						newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
						newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/utils/ItemColorHelper", "renderItemModelIntoGUIPre", method.desc, false));
						insn.insert(newInstructions);
						
						newInstructions = new InsnList();
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
						newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
						newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
						newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/utils/ItemColorHelper", "renderItemModelIntoGUIPost", method.desc, false));
						insn.insertBefore(insn.getLast(), newInstructions);
						
						asm.info("Sending instructions to RenderItem for function renderItemModelIntoGUI");
					}
				}, "Coloring Item Glint...", cv("net.minecraft.client.renderer.RenderItem")
		);
		
		hook((node, obf) ->
				{
					for(int i = 0; i < node.methods.size(); ++i)
					{
						MethodNode mn = node.methods.get(i);
						/* Prevent duplicate */
						if(mn.name.equalsIgnoreCase("renderMainMenu"))
						{
							InsnList list = mn.instructions;
							AbstractInsnNode n = list.get(list.size() - 2);
							
							// Handle custom splash text
							list.insertBefore(n, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/witty/SplashTextHelper", "handle", "(Ljava/lang/String;)Ljava/lang/String;", false));
						}
					}
				}, "Patching ForgeHooksClient", cv("net.minecraftforge.client.ForgeHooksClient")
		);
		
		hook((node, obf) ->
				{
					MethodSignature onItemUseFinish = new MethodSignature("onItemUseFinish", "func_71036_o", "v", "()V");
					
					for(int i = 0; i < node.methods.size(); ++i)
					{
						MethodNode mn = node.methods.get(i);
						
						if(onItemUseFinish.isThisMethod(mn))
						{
							InsnList l = new InsnList();
							l.add(new VarInsnNode(Opcodes.ALOAD, 0));
							l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/hooks/EntityHooks", "onItemUseFinish", "(L" + node.name + ";)V", false));
							mn.instructions.insert(l);
							asm.info("Modified method 'onItemUseFinish': added event call.");
						}
					}
				}, "Patching EntityLivingBase", cv("net.minecraft.entity.EntityLivingBase")
		);
		
		hook((node, obf) ->
				{
					for(MethodNode mn : node.methods)
						if((mn.name.equals("postRenderBlocks") || mn.name.equals("func_178584_a") || mn.name.equals("a")) && mn.desc.startsWith("(L") && mn.desc.contains(";FFFL") && mn.desc.endsWith(";)V"))
						{
							InsnList insns = new InsnList();
							
							insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
							insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
							insns.add(new VarInsnNode(Opcodes.FLOAD, 2));
							insns.add(new VarInsnNode(Opcodes.FLOAD, 3));
							insns.add(new VarInsnNode(Opcodes.FLOAD, 4));
							insns.add(new VarInsnNode(Opcodes.ALOAD, 5));
							insns.add(new VarInsnNode(Opcodes.ALOAD, 6));
							
							insns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "postRenderChunk", mn.desc.replaceFirst("[(L]", "(L" + node.name + ";"), false));
							
							mn.instructions.insert(insns);
							
							asm.info("Modified method 'postRenderChunk': added event call.");
						}
				}, "Patching RenderChunk", cv("net.minecraft.client.renderer.chunk.RenderChunk")
		);
		
		hook((node, obf) ->
				{
					MethodSignature atk = new MethodSignature("attackEntityFrom", "func_70097_a", "a", "(Lnet/minecraft/util/DamageSource;F)Z", "(Lur;F)Z");
					for(MethodNode mn : node.methods)
						if(atk.isThisMethod(mn))
						{
							InsnList insns = new InsnList();
							
							insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
							insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
							insns.add(new VarInsnNode(Opcodes.FLOAD, 2));
							insns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "attackEntityItem",
									"(" + (obf ? "Lacl;Lur;" : "L" + node.name + ";Lnet/minecraft/util/DamageSource;") + "F)Z", false
							));
							LabelNode n1 = new LabelNode();
							insns.add(new JumpInsnNode(Opcodes.IFEQ, n1));
							insns.add(new InsnNode(Opcodes.H_PUTFIELD));
							insns.add(new InsnNode(Opcodes.IRETURN));
							insns.add(n1);
							
							mn.instructions.insert(insns);
							
							asm.info("Modified method 'attackEntityFrom': added event call.");
						}
				}, "Patching EntityItem", cv("net.minecraft.entity.item.EntityItem")
		);
		
		if(CFG_ASM_WORLD_ITICKABLE_OVERRIDE) hook((node, obf) ->
				{
					String itickableDeobf = "net/minecraft/util/ITickable";
					String itickableObf = "nx";
					
					for(MethodNode mn : node.methods)
					{
						InsnList insns = mn.instructions;
						for(int i = 0; i < insns.size(); ++i)
						{
							AbstractInsnNode inode = insns.get(i);
							if(inode instanceof MethodInsnNode)
							{
								MethodInsnNode min = (MethodInsnNode) inode;
								if(min.owner.compareTo(itickableDeobf) == 0 || min.owner.compareTo(itickableObf) == 0)
								{
									insns.set(min, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/asm/McHooks", "tickTile",
													"(L" + (obf ? itickableObf : itickableDeobf) + ";)V", false
											)
									);
									asm.info("Modified method '" + mn.name + "', replaced " + min.owner + "." + min.name + min.desc + " with HC call.");
								}
							}
						}
					}
				}, "Patching World", cv("net.minecraft.world.World")
		);
	}
	
	public static void save(ClassNode node)
	{
		File dir = new File("HammerCore", "asm");
		dir = new File(dir, node.name + ".class");
		dir.mkdirs();
		dir.delete();
		try
		{
			ObjectWebUtils.writeClassToFile(node, dir);
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	final GlASM gl = new GlASM(asm);
	
	public static final Map<String, String> MC_CLASS_MAP = new HashMap<String, String>();
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(transformedName.startsWith("net.minecraft.") && name.compareTo(transformedName) != 0)
			MC_CLASS_MAP.put(transformedName, name);
		if(transformedName.compareTo("net.minecraft.client.renderer.ChunkRenderContainer") == 0)
			return gl.patchRenderChunkASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.compareTo("net.minecraft.client.renderer.entity.RenderManager") == 0)
			return gl.patchRenderManagerASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.compareTo("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher") == 0)
			return gl.patchTERendererASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.compareTo("net.minecraft.client.renderer.GlStateManager") == 0)
			return gl.patchGlStateManagerASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.compareTo("net.minecraft.profiler.Profiler") == 0)
			return gl.patchProfilerASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.compareTo("net.minecraftforge.client.ForgeHooksClient") == 0)
			return gl.patchForgeHooksASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		return asm.transform(name, transformedName, basicClass);
	}
	
	public static Predicate<String> cv(String c)
	{
		return s -> c.compareTo(s) == 0;
	}
	
	public static void hook(BiConsumer<ClassNode, Boolean> handle, String desc, Predicate<String> acceptor)
	{
		asm.addHook(new IASMHook()
		{
			@Override
			public void transform(ClassNode node, boolean obf)
			{
				handle.accept(node, obf);
			}
			
			@Override
			public String opName()
			{
				return desc;
			}
			
			@Override
			public boolean accepts(String name)
			{
				return acceptor.test(name);
			}
		});
	}
	
	private MethodNode getMoonPhase(String name)
	{
		MethodNode func_72853_d = new MethodNode(Opcodes.ASM5);
		func_72853_d.desc = "()I";
		func_72853_d.access = Opcodes.ACC_PUBLIC;
		func_72853_d.exceptions = new ArrayList<>();
		func_72853_d.name = name;
		InsnList list = new InsnList();
		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/utils/WorldUtil", "getMoonPhase", "(Lamu;)I", false));
		list.add(new InsnNode(Opcodes.IRETURN));
		func_72853_d.instructions = list;
		return func_72853_d;
	}
	
	public static class MethodSignature
	{
		String funcName;
		String srgName;
		String obfName;
		String funcDesc;
		String obfDesc;
		
		public MethodSignature(String funcName, String srgName, String obfName, String funcDesc, String obfDesc)
		{
			this.funcName = funcName;
			this.srgName = srgName;
			this.obfName = obfName;
			this.funcDesc = funcDesc;
			this.obfDesc = obfDesc;
		}
		
		public MethodSignature(String funcName, String srgName, String obfName, String funcDesc)
		{
			this.funcName = funcName;
			this.srgName = srgName;
			this.obfName = obfName;
			this.funcDesc = funcDesc;
			this.obfDesc = MethodSignature.obfuscate(funcDesc);
		}
		
		@Override
		public String toString()
		{
			return "Names [" + this.funcName + ", " + this.srgName + ", " + this.obfName + "] Descriptor " + this.funcDesc + " / " + this.obfDesc;
		}
		
		private static String obfuscate(String desc)
		{
			for(String s : CLASS_MAPPINGS.keySet())
			{
				if(!desc.contains(s))
					continue;
				desc = desc.replaceAll(s, CLASS_MAPPINGS.get(s));
			}
			return desc;
		}
		
		public boolean isThisDesc(String desc)
		{
			return this.obfDesc.compareTo(desc) == 0 || this.funcDesc.compareTo(desc) == 0;
		}
		
		public boolean isThisMethod(MethodNode node)
		{
			return (node.name.equals(funcName) || node.name.equals(obfName) || node.name.equals(srgName)) && (node.desc.equals(funcDesc) || node.desc.equals(obfDesc));
		}
	}
	
	public static void toString(ClassNode classNode, MethodNode $)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ClassBuilder builder = ObjectWebASM.classBuilder(\"name.to.ur.Cls\");\n\n");
		builder.append("builder.extendsClass(").append(JSONObject.quote(classNode.superName).replace("/", ".")).append(");\n");
		for(String implement : classNode.interfaces)
			builder.append("builder.implementsClass(").append(JSONObject.quote(implement).replace("/", ".")).append(");\n");
		
		for(FieldNode node : classNode.fields)
		{
			String val = "null";
			if(node.value instanceof String)
				val = JSONObject.quote(node.value.toString());
			else
				val = Objects.toString(node.value);
			builder.append("\nbuilder.field(new FieldNode(")
			       .append(node.access).append(", ")
			       .append(JSONObject.quote(node.name)).append(", ")
			       .append(JSONObject.quote(node.desc)).append(", ")
			       .append(JSONObject.quote(node.signature)).append(", ")
			       .append(val).append("));");
		}
		
		builder.append("\n");
		
		for(MethodNode node : classNode.methods)
		{
			if($ != null && node != $)
				continue;
			Map<Label, String> nodes = new HashMap<>();
			String mnodes = "new MethodNode(" + node.access + ", " + JSONObject.quote(node.name) + ", " + JSONObject.quote(node.desc) + ", " + JSONObject.quote(node.signature) + ", new String" +
					(node.exceptions.isEmpty() ? "[0]" : "[]" + node.exceptions.toString().replace("[", "{").replace("]", "}")) + ")";
			builder.append("\nbuilder.method(").append(mnodes).append(", node ->\n{");
			Consumer<String> toList = ln -> builder.append("\n\tinsn.add(").append(ln).append(");");
			builder.append("\n\tInsnList insn = node.instructions;");
			for(AbstractInsnNode i : node.instructions.toArray())
				if(i instanceof LabelNode)
					toString(classNode, i, nodes, toList, ln -> builder.append("\n\t").append(ln), true);
			for(AbstractInsnNode i : node.instructions.toArray())
				toString(classNode, i, nodes, toList, ln -> builder.append("\n\t").append(ln), false);
			builder.append("\n});\n");
		}
		
		builder.append("\nClass<?> generatedClass = builder.buildClass();");
		
		System.out.println(builder.toString());
	}
	
	public static void toString(ClassNode classNode, AbstractInsnNode node, Map<Label, String> nodes, Consumer<String> toList, Consumer<String> append, boolean prerun)
	{
		if(node instanceof LabelNode)
		{
			Label lbl = ((LabelNode) node).getLabel();
			String var = nodes.containsKey(lbl) ? nodes.get(lbl) : "l" + nodes.size();
			if(prerun)
				append.accept("LabelNode " + var + " = new LabelNode();");
			else
				toList.accept(var);
			nodes.put(lbl, var);
		} else if(node instanceof LineNumberNode)
		{
			LineNumberNode nd = (LineNumberNode) node;
			String var = nodes.get(nd.start.getLabel());
			toList.accept("new LineNumberNode(" + nd.line + ", " + (var == null ? "new LabelNode()" : var) + ")");
		} else if(node instanceof MethodInsnNode)
		{
			MethodInsnNode nd = (MethodInsnNode) node;
			toList.accept("new MethodInsnNode(" + opcodeName(nd.getOpcode()) + ", \"" + nd.owner + "\", \"" + nd.name + "\", \"" + nd.desc + "\")");
		} else if(node instanceof VarInsnNode)
		{
			VarInsnNode nd = (VarInsnNode) node;
			toList.accept("new VarInsnNode(" + opcodeName(nd.getOpcode()) + ", " + nd.var + ")");
		} else if(node instanceof TypeInsnNode)
		{
			TypeInsnNode nd = (TypeInsnNode) node;
			toList.accept("new TypeInsnNode(" + opcodeName(nd.getOpcode()) + ", \"" + nd.desc + "\")");
		} else if(node instanceof LdcInsnNode)
		{
			LdcInsnNode nd = (LdcInsnNode) node;
			Object tostr = nd.cst;
			String str = tostr.toString();
			if(tostr instanceof Type)
			{
				Type type = (Type) tostr;
				str = type.toString();
			} else if(tostr instanceof String)
				str = JSONObject.quote(tostr.toString());
			toList.accept("new LdcInsnNode(" + str + ")");
		} else if(node instanceof IntInsnNode)
		{
			IntInsnNode nd = (IntInsnNode) node;
			toList.accept("new IntInsnNode(" + opcodeName(nd.getOpcode()) + ", " + nd.operand + ")");
		} else if(node instanceof FieldInsnNode)
		{
			FieldInsnNode nd = (FieldInsnNode) node;
			String owner = nd.owner.compareTo(classNode.name) == 0 ? "builder.cname()" : JSONObject.quote(nd.owner);
			toList.accept("new FieldInsnNode(" + opcodeName(nd.getOpcode()) + ", " + owner + ", " + JSONObject.quote(nd.name) + "," + JSONObject.quote(nd.desc) + ")");
		} else if(node instanceof InsnNode)
		{
			InsnNode nd = (InsnNode) node;
			toList.accept("new InsnNode(" + opcodeName(nd.getOpcode()) + ")");
		} else if(node instanceof JumpInsnNode)
		{
			JumpInsnNode nd = (JumpInsnNode) node;
			String var = nodes.get(nd.label.getLabel());
			toList.accept("new JumpInsnNode(" + opcodeName(nd.getOpcode()) + ", " + (var == null ? "new LabelNode()" : var) + ")");
		} else
			append.accept("// Failed to parse: " + node.toString() + " @OP " + opcodeName(node.getOpcode()));
	}
	
	public static String opcodeName(int opcode)
	{
		for(Field f : Opcodes.class.getDeclaredFields())
			if(Modifier.isStatic(f.getModifiers()) && int.class.isAssignableFrom(f.getType()))
			{
				try
				{
					if(f.getInt(null) == opcode)
						return "Opcodes." + f.getName();
				} catch(IllegalArgumentException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		return "0x" + Integer.toHexString(opcode);
	}
	
	public static boolean parsePropertyBool(JSONObject props, String key, boolean defValue, String comment, Runnable onChanged)
	{
		if(comment != null) props.put(key + "_comment", comment);
		else props.remove(key + "_comment");
		if(props.opt(key) == null)
		{
			props.put(key, defValue);
			onChanged.run();
		}
		try
		{
			return props.getBoolean(key);
		} catch(Throwable err)
		{
			props.put(key, defValue);
			onChanged.run();
		}
		return defValue;
	}
	
	public static long parsePropertyLong(JSONObject props, String key, long defValue, String comment, Runnable onChanged)
	{
		if(comment != null) props.put(key + "_comment", comment);
		else props.remove(key + "_comment");
		if(props.opt(key) == null)
		{
			props.put(key, defValue);
			onChanged.run();
		}
		try
		{
			return props.getLong(key);
		} catch(Throwable err)
		{
			props.put(key, defValue);
			onChanged.run();
		}
		return defValue;
	}
}