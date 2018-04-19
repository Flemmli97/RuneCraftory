package com.flemmli97.runecraftory.asm;

import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Maps;

import net.minecraft.launchwrapper.IClassTransformer;

public class ASMTransformer implements IClassTransformer{

	private static Map<String, Transform> patches = Maps.newHashMap();
	private static Map<String, Method> classMethod = Maps.newHashMap();
	private static Logger logger = LogManager.getLogger("RuneCraftory/ASM");
	static
	{
		patches.put("net.minecraft.client.renderer.entity.layers.LayerHeldItem", layerHeldItem());
		classMethod.put("net.minecraft.client.renderer.entity.layers.LayerHeldItem", 
				new Method("doRenderLayer","func_177141_a","a", "(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V", "(L"+"vp"+";FFFFFFF)V"));
	
		patches.put("net.minecraft.client.renderer.RenderItem", removeGloveOverride());
		classMethod.put("net.minecraft.client.renderer.RenderItem", 
				new Method("renderItemAndEffectIntoGUI","func_180450_b","b", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", "(L"+"vp"+";L"+"aip"+";II)V"));
	
		patches.put("net.minecraft.client.renderer.entity.RenderPlayer", patchingRenderPlayer());
		classMethod.put("net.minecraft.client.renderer.entity.RenderPlayer", 
				new Method("setModelVisibilities","func_177137_d","d", "(Lnet/minecraft/client/entity/AbstractClientPlayer;)V", "(L"+"bua"+";)V"));
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(patches.containsKey(transformedName))
		{
			return transform(basicClass, classMethod.get(transformedName), patches.get(transformedName));
		}
		return basicClass;
	}
		
	private static byte[] transform(byte[] clss, Method m, Transform transform)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(clss);
		classReader.accept(classNode, 0);
		for(MethodNode method : classNode.methods)
		{
			if( (method.name.equals(m.name)||method.name.equals(m.srgName)||method.name.equals(m.obfName) ) && (method.desc.equals(m.desc)||method.desc.equals(m.obfDesc)))
			{
				transform.apply(classNode, method);
				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
				classNode.accept(writer);
				return writer.toByteArray();
			}
		}
		return clss;
	}
	
	private static Transform layerHeldItem()
	{
		Transform t = new Transform() {
			@Override
			public void apply(ClassNode clss, MethodNode method) {
					debug("Patching LayerHeldItem");
					Iterator<AbstractInsnNode> it = method.instructions.iterator();
					AbstractInsnNode node = null;
					while(it.hasNext())
					{
						node = it.next();
						if(node.getOpcode()==Opcodes.INVOKESTATIC)
						{
							MethodInsnNode m = (MethodInsnNode) node;
							if(m.name.equals("pushMatrix") || m.name.equals("func_179094_E"))
								break;
						}
					}
					InsnList inject = new InsnList();
					inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
					inject.add(new VarInsnNode(Opcodes.ALOAD, 10));
					inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/flemmli97/runecraftory/asm/ASMMethods", "renderDualWeapons", 
					"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false));
					inject.add(new VarInsnNode(Opcodes.ASTORE, 10));
					method.instructions.insertBefore(node, inject);
			}};
		return t;
	}
	
	private static Transform removeGloveOverride()
	{
		Transform t = new Transform() {
			@Override
			public void apply(ClassNode clss, MethodNode method) {
					debug("Patching RenderItem");
					Iterator<AbstractInsnNode> it = method.instructions.iterator();
					AbstractInsnNode node = null;
					while(it.hasNext())
					{
						node = it.next();
						if(node.getOpcode()==Opcodes.PUTFIELD)
						{
							FieldInsnNode m = (FieldInsnNode) node;
							if(m.name.equals("zLevel") || m.name.equals("field_77023_b"))
								break;
						}
					}
					InsnList inject = new InsnList();
					inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
					inject.add(new VarInsnNode(Opcodes.ALOAD, 2));
					inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/flemmli97/runecraftory/asm/ASMMethods", "ignoreGloveModel", 
					"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/EntityLivingBase;", false));
					inject.add(new VarInsnNode(Opcodes.ASTORE, 1));
					method.instructions.insert(node, inject);
			}};
		return t;
	}
	
	private static Transform patchingRenderPlayer()
	{
		Transform t = new Transform() {
			@Override
			public void apply(ClassNode clss, MethodNode method) {
					debug("Patching RenderPlayer");
					Iterator<AbstractInsnNode> it = method.instructions.iterator();
					AbstractInsnNode node = null;
					while(it.hasNext())
					{
						node = it.next();
						if(node.getOpcode()==Opcodes.RETURN)
							break;
					}
					InsnList inject = new InsnList();
					inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
					inject.add(new VarInsnNode(Opcodes.ALOAD, 2));
					inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/flemmli97/runecraftory/asm/ASMMethods", "playerAnim", 
					"(Lnet/minecraft/client/entity/AbstractClientPlayer;Lnet/minecraft/client/model/ModelPlayer;)Lnet/minecraft/client/model/ModelPlayer;", false));
					inject.add(new VarInsnNode(Opcodes.ASTORE, 2));
					method.instructions.insertBefore(node, inject);
			}};
		return t;
	}
	
	private interface Transform
	{
		public void apply(ClassNode clss, MethodNode method);
	}
	
	static void debug(String debug)
	{
		logger.debug("[RuneCraftoy/ASM]: " + debug);
	}

	private static class Method
	{
		final String name, srgName, obfName, desc, obfDesc;
		
		Method(String name, String srgName, String obfName, String desc, String obfDesc)
		{
			this.name = name;
			this.srgName=srgName;
			this.obfName=obfName;
			this.desc=desc;
			this.obfDesc=obfDesc;
		}
	}
}
