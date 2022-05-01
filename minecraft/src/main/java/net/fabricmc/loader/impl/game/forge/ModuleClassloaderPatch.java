package net.fabricmc.loader.impl.game.forge;

import java.util.function.Consumer;
import java.util.function.Function;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.fabricmc.loader.impl.game.patch.GamePatch;
import net.fabricmc.loader.impl.launch.FabricLauncher;

public class ModuleClassloaderPatch extends GamePatch {

	@Override
	public void process(FabricLauncher launcher, Function<String, ClassReader> classSource, Consumer<ClassNode> classEmitter) {
		ClassNode moduleClassLoader = readClass(classSource.apply("cpw.mods.cl.ModuleClassLoader"));

		if (moduleClassLoader != null) {
			if (applyClassloaderParentPatch(moduleClassLoader)) {
				classEmitter.accept(moduleClassLoader);
			}
		}
	}

	private boolean applyClassloaderParentPatch(ClassNode classNode) {
		boolean applied = false;

		for (MethodNode method : classNode.methods) {
			if (method.name.equals("<init>")) {
				AbstractInsnNode nullTarget = method.instructions.get(4);
				InsnList instructions = new InsnList();
				instructions.add(new LdcInsnNode(Type.getType("Lcpw/mods/cl/ModuleClassLoader;")));
				instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getClassLoader", "()Ljava/lang/ClassLoader;", false));
				method.instructions.insert(nullTarget, instructions);
				method.instructions.remove(nullTarget);
				applied = true;
			}
		}

		return applied;
	}
}
