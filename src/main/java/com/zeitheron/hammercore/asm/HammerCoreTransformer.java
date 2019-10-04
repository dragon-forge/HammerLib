package com.zeitheron.hammercore.asm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.zeitheron.hammercore.asm.TransformerSystem.iASMHook;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Transforms classes. Internal use only!
 */
public class HammerCoreTransformer implements IClassTransformer
{
	public static final ClassnameMap CLASS_MAPPINGS = new ClassnameMap("net/minecraft/entity/Entity", "vg", "net/minecraft/item/ItemStack", "aip", "net/minecraft/client/renderer/block/model/IBakedModel", "cfy", "net/minecraft/entity/EntityLivingBase", "vp", "net/minecraft/inventory/EntityEquipmentSlot", "vl", "net/minecraft/client/renderer/entity/RenderLivingBase", "caa", "net/minecraft/client/model/ModelBase", "bqf", "net/minecraft/util/DamageSource", "ur", "net/minecraft/entity/item/EntityBoat", "afd", "net/minecraft/world/World", "amu", "net/minecraft/util/math/BlockPos", "et", "net/minecraft/util/EnumFacing", "fa", "net/minecraft/entity/player/EntityPlayer", "aed", "net/minecraft/block/state/IBlockState", "awt", "net/minecraft/client/renderer/BufferBuilder", "buk", "net/minecraft/world/IBlockAccess", "amy", "net/minecraft/client/renderer/block/model/BakedQuad", "bvp", "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType", "bwc$b");
	
	public static final TransformerSystem asm = new TransformerSystem();
	
	static
	{
		hook((node, obf) ->
		{
			MethodSignature sig1 = new MethodSignature("renderItem", "func_180454_a", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
			MethodSignature sig2 = new MethodSignature("renderEffect", "func_191966_a", "a", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;)V");
			MethodSignature sig3 = new MethodSignature("renderItemModel", "func_184394_a", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V");
			MethodSignature sig4 = new MethodSignature("renderItemModelIntoGUI", "func_191962_a", "a", "(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V");
			
			for(MethodNode method : node.methods)
			{
				if((!method.name.equals(sig1.funcName) && !method.name.equals(sig1.obfName) && !method.name.equals(sig1.srgName)) || (!method.desc.equals(sig1.funcDesc) && !method.desc.equals(sig1.obfDesc)))
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
				if((!method.name.equals(sig2.funcName) && !method.name.equals(sig2.obfName) && !method.name.equals(sig2.srgName)) || (!method.desc.equals(sig2.funcDesc) && !method.desc.equals(sig2.obfDesc)))
					continue;
				
				InsnList insn = method.instructions;
				
				boolean worked = false;
				
				for(int i = 0; i < insn.size(); ++i)
				{
					AbstractInsnNode n = insn.get(i);
					
					if(n.getOpcode() == 18 && ((LdcInsnNode) n).cst.equals(-8372020))
					{
						InsnList newInstructions = new InsnList();
						newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/utils/ItemColorHelper", "getCustomColor", "()I", false));
						insn.insertBefore(n, newInstructions);
						insn.remove(n);
						worked = true;
					}
				}
				
				if(worked)
					asm.info("Sending instructions to RenderItem for function renderEffect");
			}
			
			for(MethodNode method : node.methods)
			{
				if((!method.desc.equals("(Laip;Lcfy;Lbwc$b;Z)V") || !method.name.equals("a")) && ((!method.name.equals(sig3.funcName) && !method.name.equals(sig3.obfName) && !method.name.equals(sig3.srgName)) || (!method.desc.equals(sig3.funcDesc) && !method.desc.equals(sig3.obfDesc))))
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
				if((!method.name.equals(sig4.funcName) && !method.name.equals(sig4.obfName) && !method.name.equals(sig4.srgName)) || (!method.desc.equals(sig4.funcDesc) && !method.desc.equals(sig4.obfDesc)))
					continue;
				
				InsnList insn = method.instructions;
				InsnList newInstructions = new InsnList();
				newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
				newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
				newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
				newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
				newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/zeitheron/hammercore/client/utils/ItemColorHelper", "renderItemModelIntoGUI", method.desc, false));
				insn.insert(newInstructions);
				asm.info("Sending instructions to RenderItem for function renderItemModelIntoGUI");
			}
		}, "Coloring Item Glint...", cv("net.minecraft.client.renderer.RenderItem"));
		
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
		}, "Patching ForgeHooksClient", cv("net.minecraftforge.client.ForgeHooksClient"));
		
		// Apparently Forge now has this feature. I'll leave this here just in
		// case users use old Forge (that didn't add this feature)
		hook((node, obf) ->
		{
			MethodSignature fillWithLoot = new MethodSignature("fillWithLoot", "func_184281_d", "d", "(Lnet/minecraft/entity/player/EntityPlayer;)V");
			
			for(int i = 0; i < node.methods.size(); ++i)
			{
				MethodNode mn = node.methods.get(i);
				
				if(fillWithLoot.isThisMethod(mn))
				{
					String desc = "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)V";
					if(obf)
						desc = MethodSignature.obfuscate(desc);
					InsnList fwl = mn.instructions;
					
					MethodInsnNode nd = fwl.get(62) instanceof MethodInsnNode ? (MethodInsnNode) fwl.get(62) : null;
					
					if(nd != null)
					{
						InsnList list = new InsnList();
						
						MethodInsnNode nnnd;
						list.add(new VarInsnNode(Opcodes.ALOAD, 4));
						list.add(new VarInsnNode(Opcodes.ALOAD, 1));
						String dsc = obf ? fillWithLoot.obfDesc : fillWithLoot.funcDesc;
						dsc = dsc.substring(0, dsc.length() - 2) + ")L" + nd.owner + ";";
						list.add(nnnd = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, nd.owner, obf ? "a" : "withPlayer", dsc, false));
						list.add(new InsnNode(Opcodes.POP));
						fwl.insertBefore(fwl.get(64), list);
					}
					
					asm.info("Modified method 'fillWithLoot': added 'withPlayer(player)' after 'withLuck(player.getLuck())'");
				}
			}
		}, "Patching TileEntityLockableLoot", cv("net.minecraft.tileentity.TileEntityLockableLoot"));
		
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
		}, "Patching EntityLivingBase", cv("net.minecraft.entity.EntityLivingBase"));
	}
	
	final GlASM gl = new GlASM(asm);
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(transformedName.equals("net.minecraft.client.renderer.ChunkRenderContainer"))
			return gl.patchRenderChunkASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.equals("net.minecraft.client.renderer.entity.RenderManager"))
			return gl.patchRenderManagerASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.equals("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher"))
			return gl.patchTERendererASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.equals("net.minecraft.client.renderer.GlStateManager"))
			return gl.patchGlStateManagerASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.equals("net.minecraft.profiler.Profiler"))
			return gl.patchProfilerASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		if(transformedName.compareTo("net.minecraftforge.client.ForgeHooksClient") == 0)
			return gl.patchForgeHooksASM(name, basicClass, name.compareTo(transformedName) != 0, transformedName);
		return asm.transform(name, transformedName, basicClass);
	}
	
	private static String opcodeName(int ops)
	{
		Field[] f = Opcodes.class.getDeclaredFields();
		
		for(Field g : f)
			try
			{
				if(g.getInt(null) == ops)
					return g.getName();
			} catch(Throwable err)
			{
			}
		
		return "?";
	}
	
	public static Predicate<String> cv(String c)
	{
		return s -> c.equalsIgnoreCase(s);
	}
	
	public static void hook(BiConsumer<ClassNode, Boolean> handle, String desc, Predicate<String> acceptor)
	{
		asm.addHook(new iASMHook()
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
	
	private static class MethodSignature
	{
		String funcName;
		String srgName;
		String obfName;
		String funcDesc;
		String obfDesc;
		
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
		
		public boolean isThisMethod(MethodNode node)
		{
			return (node.name.equals(funcName) || node.name.equals(obfName) || node.name.equals(srgName)) && (node.desc.equals(funcDesc) || node.desc.equals(obfDesc));
		}
	}
}