package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.FrameNode;
import java.util.ArrayList;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Unique;
import java.util.List;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.FieldVisitor;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassWriter;
import org.spongepowered.asm.lib.ClassReader;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.spongepowered.asm.transformers.TreeTransformer;

class MixinPostProcessor extends TreeTransformer implements MixinConfig.IListener
{
    private final Set<String> syntheticInnerClasses;
    private final Map<String, MixinInfo> accessorMixins;
    private final Set<String> loadable;
    
    MixinPostProcessor() {
        this.syntheticInnerClasses = new HashSet<String>();
        this.accessorMixins = new HashMap<String, MixinInfo>();
        this.loadable = new HashSet<String>();
    }
    
    @Override
    public void onInit(final MixinInfo v2) {
        /*SL:80*/for (final String a1 : v2.getSyntheticInnerClasses()) {
            /*SL:81*/this.registerSyntheticInner(a1.replace('/', '.'));
        }
    }
    
    @Override
    public void onPrepare(final MixinInfo a1) {
        final String v1 = /*EL:87*/a1.getClassName();
        /*SL:89*/if (a1.isLoadable()) {
            /*SL:90*/this.registerLoadable(v1);
        }
        /*SL:93*/if (a1.isAccessor()) {
            /*SL:94*/this.registerAccessor(a1);
        }
    }
    
    void registerSyntheticInner(final String a1) {
        /*SL:99*/this.syntheticInnerClasses.add(a1);
    }
    
    void registerLoadable(final String a1) {
        /*SL:103*/this.loadable.add(a1);
    }
    
    void registerAccessor(final MixinInfo a1) {
        /*SL:107*/this.registerLoadable(a1.getClassName());
        /*SL:108*/this.accessorMixins.put(a1.getClassName(), a1);
    }
    
    boolean canTransform(final String a1) {
        /*SL:120*/return this.syntheticInnerClasses.contains(a1) || this.loadable.contains(a1);
    }
    
    @Override
    public String getName() {
        /*SL:128*/return this.getClass().getName();
    }
    
    @Override
    public boolean isDelegationExcluded() {
        /*SL:137*/return true;
    }
    
    @Override
    public byte[] transformClassBytes(final String a3, final String v1, final byte[] v2) {
        /*SL:146*/if (this.syntheticInnerClasses.contains(v1)) {
            /*SL:147*/return this.processSyntheticInner(v2);
        }
        /*SL:150*/if (this.accessorMixins.containsKey(v1)) {
            final MixinInfo a4 = /*EL:151*/this.accessorMixins.get(v1);
            /*SL:152*/return this.processAccessor(v2, a4);
        }
        /*SL:155*/return v2;
    }
    
    private byte[] processSyntheticInner(final byte[] a1) {
        final ClassReader v1 = /*EL:164*/new ClassReader(a1);
        final ClassWriter v2 = /*EL:165*/new MixinClassWriter(v1, 0);
        final ClassVisitor v3 = /*EL:167*/new ClassVisitor(327680, v2) {
            @Override
            public void visit(final int a1, final int a2, final String a3, final String a4, final String a5, final String[] a6) {
                /*SL:171*/super.visit(a1, a2 | 0x1, a3, a4, a5, a6);
            }
            
            @Override
            public FieldVisitor visitField(int a1, final String a2, final String a3, final String a4, final Object a5) {
                /*SL:177*/if ((a1 & 0x6) == 0x0) {
                    /*SL:178*/a1 |= 0x1;
                }
                /*SL:180*/return super.visitField(a1, a2, a3, a4, a5);
            }
            
            @Override
            public MethodVisitor visitMethod(int a1, final String a2, final String a3, final String a4, final String[] a5) {
                /*SL:185*/if ((a1 & 0x6) == 0x0) {
                    /*SL:186*/a1 |= 0x1;
                }
                /*SL:188*/return super.visitMethod(a1, a2, a3, a4, a5);
            }
        };
        /*SL:193*/v1.accept(v3, 8);
        /*SL:194*/return v2.toByteArray();
    }
    
    private byte[] processAccessor(final byte[] v-6, final MixinInfo v-5) {
        /*SL:198*/if (!MixinEnvironment.getCompatibilityLevel().isAtLeast(MixinEnvironment.CompatibilityLevel.JAVA_8)) {
            /*SL:199*/return v-6;
        }
        boolean b = /*EL:202*/false;
        final MixinInfo.MixinClassNode classNode = /*EL:203*/v-5.getClassNode(0);
        final ClassInfo classInfo = /*EL:204*/v-5.getTargets().get(0);
        /*SL:206*/for (ClassInfo.Method a2 : classNode.mixinMethods) {
            /*SL:208*/if (!Bytecode.hasFlag(a2, 8)) {
                /*SL:209*/continue;
            }
            final AnnotationNode v1 = /*EL:212*/a2.getVisibleAnnotation(Accessor.class);
            final AnnotationNode v2 = /*EL:213*/a2.getVisibleAnnotation(Invoker.class);
            /*SL:214*/if (v1 == null && v2 == null) {
                continue;
            }
            /*SL:215*/a2 = getAccessorMethod(v-5, a2, classInfo);
            createProxy(/*EL:216*/a2, classInfo, a2);
            /*SL:217*/b = true;
        }
        /*SL:221*/if (b) {
            /*SL:222*/return this.writeClass(classNode);
        }
        /*SL:225*/return v-6;
    }
    
    private static ClassInfo.Method getAccessorMethod(final MixinInfo a1, final MethodNode a2, final ClassInfo a3) throws MixinTransformerError {
        final ClassInfo.Method v1 = /*EL:229*/a1.getClassInfo().findMethod(a2, 10);
        /*SL:233*/if (!v1.isRenamed()) {
            /*SL:234*/throw new MixinTransformerError("Unexpected state: " + a1 + " loaded before " + a3 + " was conformed");
        }
        /*SL:237*/return v1;
    }
    
    private static void createProxy(final MethodNode a1, final ClassInfo a2, final ClassInfo.Method a3) {
        /*SL:241*/a1.instructions.clear();
        final Type[] v1 = /*EL:242*/Type.getArgumentTypes(a1.desc);
        final Type v2 = /*EL:243*/Type.getReturnType(a1.desc);
        /*SL:244*/Bytecode.loadArgs(v1, a1.instructions, 0);
        /*SL:245*/a1.instructions.add(new MethodInsnNode(184, a2.getName(), a3.getName(), a1.desc, false));
        /*SL:246*/a1.instructions.add(new InsnNode(v2.getOpcode(172)));
        /*SL:247*/a1.maxStack = Bytecode.getFirstNonArgLocalIndex(v1, false);
        /*SL:248*/a1.maxLocals = 0;
    }
}
