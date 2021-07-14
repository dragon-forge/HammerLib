var hookClass = "org/zeith/hammerlib/asm/ServerConnectionNetHook";
var hookMethod = "handle";

var sendMethod = "send";

function initializeCoreMod()
{
	return {
		'coremodone': {
			'target': { 'type': 'CLASS', 'name': 'net.minecraft.network.play.ServerPlayNetHandler' },
			'transformer': function(classNode)
			{
				var Opcodes = Java.type('org.objectweb.asm.Opcodes');
				print("[HammerLib] Transforming ServerPlayNetHandler!");
				var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
				var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

				var methods = classNode.methods;
				for(m in methods)
				{
					var method = methods[m];

					if(method.desc.startsWith("(L") && method.desc.endsWith(";)V") && !method.desc.contains(";L"))
					{
						var code = method.instructions;
						code.insertBefore(code.getFirst(), new MethodInsnNode(Opcodes.INVOKESTATIC, hookClass, hookMethod, "(L" + classNode.name + ";Ljava/lang/Object;)V", false));
						code.insertBefore(code.getFirst(), new VarInsnNode(Opcodes.ALOAD, 1));
						code.insertBefore(code.getFirst(), new VarInsnNode(Opcodes.ALOAD, 0));
					}

					if(method.desc == "(Lnet/minecraft/network/IPacket;Lio/netty/util/concurrent/GenericFutureListener;)V")
					{
						var code = method.instructions;
						code.insertBefore(code.getFirst(), new MethodInsnNode(Opcodes.INVOKESTATIC, hookClass, sendMethod, "(L" + classNode.name + ";Lnet/minecraft/network/IPacket;)V", false));
						code.insertBefore(code.getFirst(), new VarInsnNode(Opcodes.ALOAD, 1));
						code.insertBefore(code.getFirst(), new VarInsnNode(Opcodes.ALOAD, 0));

					    print("[HammerLib] Tapping into sendPacket (" + method.name + ")");
					}
				}

				return classNode;
			}
		}
	}
}