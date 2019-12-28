package org.spongepowered.asm.transformers;

import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.service.ILegacyClassTransformer;

public abstract class TreeTransformer implements ILegacyClassTransformer
{
    private ClassReader classReader;
    private ClassNode classNode;
    
    protected final ClassNode readClass(final byte[] a1) {
        /*SL:45*/return this.readClass(a1, true);
    }
    
    protected final ClassNode readClass(final byte[] a1, final boolean a2) {
        final ClassReader v1 = /*EL:55*/new ClassReader(a1);
        /*SL:56*/if (a2) {
            /*SL:57*/this.classReader = v1;
        }
        final ClassNode v2 = /*EL:60*/new ClassNode();
        /*SL:61*/v1.accept(v2, 8);
        /*SL:62*/return v2;
    }
    
    protected final byte[] writeClass(final ClassNode v2) {
        /*SL:71*/if (this.classReader != null && this.classNode == v2) {
            /*SL:72*/this.classNode = null;
            final ClassWriter a1 = /*EL:73*/new MixinClassWriter(this.classReader, 3);
            /*SL:74*/this.classReader = null;
            /*SL:75*/v2.accept(a1);
            /*SL:76*/return a1.toByteArray();
        }
        /*SL:79*/this.classNode = null;
        final ClassWriter v3 = /*EL:81*/new MixinClassWriter(3);
        /*SL:82*/v2.accept(v3);
        /*SL:83*/return v3.toByteArray();
    }
}
