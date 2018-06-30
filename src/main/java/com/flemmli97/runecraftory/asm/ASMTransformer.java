package com.flemmli97.runecraftory.asm;

import net.minecraft.launchwrapper.*;
import org.objectweb.asm.*;
import java.util.*;
import org.objectweb.asm.tree.*;
import com.google.common.collect.*;

public class ASMTransformer implements IClassTransformer
{
    private static Map<String, Transform> patches = Maps.newHashMap();
    private static Map<String, Method> classMethod = Maps.newHashMap();
    
    static {
        patches.put("net.minecraft.client.renderer.entity.layers.LayerHeldItem", layerHeldItem());
        classMethod.put("net.minecraft.client.renderer.entity.layers.LayerHeldItem", 
        		new Method("doRenderLayer", "doRenderLayer", "a", "(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V", "(Lvp;FFFFFFF)V"));
        
        patches.put("net.minecraft.client.renderer.RenderItem", removeGloveOverride());
        classMethod.put("net.minecraft.client.renderer.RenderItem", 
        		new Method("renderItemAndEffectIntoGUI", "renderItemAndEffectIntoGUI", "b", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V", "(Lvp;Laip;II)V"));
        
        patches.put("net.minecraft.client.renderer.entity.RenderPlayer", patchingRenderPlayer());
        classMethod.put("net.minecraft.client.renderer.entity.RenderPlayer", 
        		new Method("setModelVisibilities", "setModelVisibilities", "d", "(Lnet/minecraft/client/entity/AbstractClientPlayer;)V", "(Lbua;)V"));
        
        patches.put("net.minecraft.client.model.ModelBiped", patchingModelBiped());
        classMethod.put("net.minecraft.client.model.ModelBiped", 
        		new Method("setRotationAngles", "setRotationAngles", "a", "(FFFFFFLnet/minecraft/entity/Entity;)V", "(FFFFFFbpt;)V"));
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) 
    {
        if (patches.containsKey(transformedName)) 
        {
            return transform(basicClass, classMethod.get(transformedName), patches.get(transformedName));
        }
        return basicClass;
    }
    
    private static byte[] transform(byte[] clss, Method m, Transform transform) 
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(clss);
        classReader.accept((ClassVisitor)classNode, 0);
        for (MethodNode method : classNode.methods) {
            if ((method.name.equals(m.name) || method.name.equals(m.srgName) || method.name.equals(m.obfName)) && (method.desc.equals(m.desc) || method.desc.equals(m.obfDesc))) {
                transform.apply(classNode, method);
                ClassWriter writer = new ClassWriter(3);
                classNode.accept((ClassVisitor)writer);
                return writer.toByteArray();
            }
        }
        return clss;
    }
    
    private static Transform layerHeldItem() {
        Transform t = new Transform() {
            @Override
            public void apply(ClassNode clss, MethodNode method) {
                debug("Patching LayerHeldItem");
                Iterator<AbstractInsnNode> it = (Iterator<AbstractInsnNode>)method.instructions.iterator();
                AbstractInsnNode node = null;
                while (it.hasNext()) {
                    node = it.next();
                    if (node.getOpcode() == 184) {
                        MethodInsnNode m = (MethodInsnNode)node;
                        if (m.name.equals("pushMatrix")) {
                            break;
                        }
                        if (m.name.equals("pushMatrix")) {
                            break;
                        }
                        continue;
                    }
                }
                InsnList inject = new InsnList();
                inject.add((AbstractInsnNode)new VarInsnNode(25, 1));
                inject.add((AbstractInsnNode)new VarInsnNode(25, 10));
                inject.add((AbstractInsnNode)new MethodInsnNode(184, "com/flemmli97/runecraftory/asm/ASMMethods", "renderDualWeapons", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false));
                inject.add((AbstractInsnNode)new VarInsnNode(58, 10));
                method.instructions.insertBefore(node, inject);
            }
        };
        return t;
    }
    
    private static Transform removeGloveOverride() {
        Transform t = new Transform() {
            @Override
            public void apply(ClassNode clss, MethodNode method) {
                debug("Patching RenderItem");
                Iterator<AbstractInsnNode> it = (Iterator<AbstractInsnNode>)method.instructions.iterator();
                AbstractInsnNode node = null;
                while (it.hasNext()) {
                    node = it.next();
                    if (node.getOpcode() == 181) {
                        FieldInsnNode m = (FieldInsnNode)node;
                        if (m.name.equals("zLevel")) {
                            break;
                        }
                        if (m.name.equals("zLevel")) {
                            break;
                        }
                        continue;
                    }
                }
                InsnList inject = new InsnList();
                inject.add((AbstractInsnNode)new VarInsnNode(25, 1));
                inject.add((AbstractInsnNode)new VarInsnNode(25, 2));
                inject.add((AbstractInsnNode)new MethodInsnNode(184, "com/flemmli97/runecraftory/asm/ASMMethods", "ignoreGloveModel", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/EntityLivingBase;", false));
                inject.add((AbstractInsnNode)new VarInsnNode(58, 1));
                method.instructions.insert(node, inject);
            }
        };
        return t;
    }
    
    private static Transform patchingRenderPlayer() {
        Transform t = new Transform() {
            @Override
            public void apply(ClassNode clss, MethodNode method) {
                debug("Patching RenderPlayer");
                Iterator<AbstractInsnNode> it = (Iterator<AbstractInsnNode>)method.instructions.iterator();
                AbstractInsnNode node = null;
                while (it.hasNext()) {
                    node = it.next();
                    if (node.getOpcode() == 177) {
                        break;
                    }
                }
                InsnList inject = new InsnList();
                inject.add((AbstractInsnNode)new VarInsnNode(25, 1));
                inject.add((AbstractInsnNode)new VarInsnNode(25, 2));
                inject.add((AbstractInsnNode)new MethodInsnNode(184, "com/flemmli97/runecraftory/asm/ASMMethods", "playerAnim", "(Lnet/minecraft/client/entity/AbstractClientPlayer;Lnet/minecraft/client/model/ModelPlayer;)Lnet/minecraft/client/model/ModelPlayer;", false));
                inject.add((AbstractInsnNode)new VarInsnNode(58, 2));
                method.instructions.insertBefore(node, inject);
            }
        };
        return t;
    }
    
    private static Transform patchingModelBiped() {
        Transform t = new Transform() {
            @Override
            public void apply(ClassNode clss, MethodNode method) {
                debug("Patching ModelBiped");
                Iterator<AbstractInsnNode> it = (Iterator<AbstractInsnNode>)method.instructions.iterator();
                AbstractInsnNode node = null;
                while (it.hasNext()) {
                    node = it.next();
                    if (node.getOpcode() == 177) {
                        break;
                    }
                }
                InsnList inject = new InsnList();
                inject.add((AbstractInsnNode)new VarInsnNode(25, 0));
                inject.add((AbstractInsnNode)new VarInsnNode(23, 1));
                inject.add((AbstractInsnNode)new VarInsnNode(23, 2));
                inject.add((AbstractInsnNode)new VarInsnNode(23, 3));
                inject.add((AbstractInsnNode)new VarInsnNode(23, 4));
                inject.add((AbstractInsnNode)new VarInsnNode(23, 5));
                inject.add((AbstractInsnNode)new VarInsnNode(23, 6));
                inject.add((AbstractInsnNode)new VarInsnNode(25, 7));
                inject.add((AbstractInsnNode)new MethodInsnNode(184, "com/flemmli97/runecraftory/asm/ASMMethods", "modelBiped", "(Lnet/minecraft/client/model/ModelBiped;FFFFFFLnet/minecraft/entity/Entity;)V", false));
                method.instructions.insertBefore(node, inject);
            }
        };
        return t;
    }
    
    static void debug(String debug) {
        System.out.println("[RuneCraftoy/ASM]: " + debug);
    }
    
    private static class Method
    {
        String name;
        String srgName;
        String obfName;
        String desc;
        String obfDesc;
        
        Method(String name, String srgName, String obfName, String desc, String obfDesc) {
            this.name = name;
            this.srgName = srgName;
            this.obfName = obfName;
            this.desc = desc;
            this.obfDesc = obfDesc;
        }
    }
    
    private interface Transform
    {
        void apply(ClassNode clssNode, MethodNode metNode);
    }
}
