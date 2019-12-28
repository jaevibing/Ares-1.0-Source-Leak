package org.spongepowered.tools.agent;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.ClassWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;

class MixinAgentClassLoader extends ClassLoader
{
    private static final Logger logger;
    private Map<Class<?>, byte[]> mixins;
    private Map<String, byte[]> targets;
    
    MixinAgentClassLoader() {
        this.mixins = new HashMap<Class<?>, byte[]>();
        this.targets = new HashMap<String, byte[]>();
    }
    
    void addMixinClass(final String v-1) {
        MixinAgentClassLoader.logger.debug(/*EL:64*/"Mixin class {} added to class loader", new Object[] { v-1 });
        try {
            final byte[] a1 = /*EL:66*/this.materialise(v-1);
            final Class<?> v1 = /*EL:67*/this.defineClass(v-1, a1, 0, a1.length);
            /*SL:70*/v1.newInstance();
            /*SL:71*/this.mixins.put(v1, a1);
        }
        catch (Throwable v2) {
            MixinAgentClassLoader.logger.catching(/*EL:73*/v2);
        }
    }
    
    void addTargetClass(final String a1, final byte[] a2) {
        /*SL:84*/this.targets.put(a1, a2);
    }
    
    byte[] getFakeMixinBytecode(final Class<?> a1) {
        /*SL:94*/return this.mixins.get(a1);
    }
    
    byte[] getOriginalTargetBytecode(final String a1) {
        /*SL:104*/return this.targets.get(a1);
    }
    
    private byte[] materialise(final String a1) {
        final ClassWriter v1 = /*EL:114*/new ClassWriter(3);
        /*SL:115*/v1.visit(MixinEnvironment.getCompatibilityLevel().classVersion(), 1, a1.replace('.', '/'), null, /*EL:116*/Type.getInternalName(Object.class), null);
        final MethodVisitor v2 = /*EL:119*/v1.visitMethod(1, "<init>", "()V", null, null);
        /*SL:120*/v2.visitCode();
        /*SL:121*/v2.visitVarInsn(25, 0);
        /*SL:122*/v2.visitMethodInsn(183, Type.getInternalName(Object.class), "<init>", "()V", false);
        /*SL:123*/v2.visitInsn(177);
        /*SL:124*/v2.visitMaxs(1, 1);
        /*SL:125*/v2.visitEnd();
        /*SL:127*/v1.visitEnd();
        /*SL:128*/return v1.toByteArray();
    }
    
    static {
        logger = LogManager.getLogger("mixin.agent");
    }
}
