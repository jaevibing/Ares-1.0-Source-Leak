package org.spongepowered.asm.mixin.transformer;

import java.util.Iterator;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.List;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.commons.ClassRemapper;
import java.io.IOException;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.lib.commons.Remapper;
import org.apache.logging.log4j.LogManager;
import java.util.UUID;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassWriter;
import org.spongepowered.asm.lib.ClassReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;

final class InnerClassGenerator implements IClassGenerator
{
    private static final Logger logger;
    private final Map<String, String> innerClassNames;
    private final Map<String, InnerClassInfo> innerClasses;
    
    InnerClassGenerator() {
        this.innerClassNames = new HashMap<String, String>();
        this.innerClasses = new HashMap<String, InnerClassInfo>();
    }
    
    public String registerInnerClass(final MixinInfo a1, final String a2, final MixinTargetContext a3) {
        final String v1 = /*EL:230*/String.format("%s%s", a2, a3);
        String v2 = /*EL:231*/this.innerClassNames.get(v1);
        /*SL:232*/if (v2 == null) {
            /*SL:233*/v2 = getUniqueReference(a2, a3);
            /*SL:234*/this.innerClassNames.put(v1, v2);
            /*SL:235*/this.innerClasses.put(v2, new InnerClassInfo(v2, a2, a1, a3));
            InnerClassGenerator.logger.debug(/*EL:236*/"Inner class {} in {} on {} gets unique name {}", new Object[] { a2, a1.getClassRef(), a3.getTargetClassRef(), /*EL:237*/v2 });
        }
        /*SL:239*/return v2;
    }
    
    @Override
    public byte[] generate(final String a1) {
        final String v1 = /*EL:248*/a1.replace('.', '/');
        final InnerClassInfo v2 = /*EL:249*/this.innerClasses.get(v1);
        /*SL:250*/if (v2 != null) {
            /*SL:251*/return this.generate(v2);
        }
        /*SL:253*/return null;
    }
    
    private byte[] generate(final InnerClassInfo v-1) {
        try {
            InnerClassGenerator.logger.debug(/*EL:265*/"Generating mapped inner class {} (originally {})", new Object[] { v-1.getName(), v-1.getOriginalName() });
            final ClassReader a1 = /*EL:266*/new ClassReader(v-1.getClassBytes());
            final ClassWriter v1 = /*EL:267*/new MixinClassWriter(a1, 0);
            /*SL:268*/a1.accept(new InnerClassAdapter(v1, v-1), 8);
            /*SL:269*/return v1.toByteArray();
        }
        catch (InvalidMixinException v2) {
            /*SL:271*/throw v2;
        }
        catch (Exception v3) {
            InnerClassGenerator.logger.catching(/*EL:273*/(Throwable)v3);
            /*SL:276*/return null;
        }
    }
    
    private static String getUniqueReference(final String a1, final MixinTargetContext a2) {
        String v1 = /*EL:288*/a1.substring(a1.lastIndexOf(36) + 1);
        /*SL:289*/if (v1.matches("^[0-9]+$")) {
            /*SL:290*/v1 = "Anonymous";
        }
        /*SL:292*/return String.format("%s$%s$%s", a2.getTargetClassRef(), v1, UUID.randomUUID().toString().replace("-", ""));
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    static class InnerClassInfo extends Remapper
    {
        private final String name;
        private final String originalName;
        private final MixinInfo owner;
        private final MixinTargetContext target;
        private final String ownerName;
        private final String targetName;
        
        InnerClassInfo(final String a1, final String a2, final MixinInfo a3, final MixinTargetContext a4) {
            this.name = a1;
            this.originalName = a2;
            this.owner = a3;
            this.ownerName = a3.getClassRef();
            this.target = a4;
            this.targetName = a4.getTargetClassRef();
        }
        
        String getName() {
            /*SL:99*/return this.name;
        }
        
        String getOriginalName() {
            /*SL:103*/return this.originalName;
        }
        
        MixinInfo getOwner() {
            /*SL:107*/return this.owner;
        }
        
        MixinTargetContext getTarget() {
            /*SL:111*/return this.target;
        }
        
        String getOwnerName() {
            /*SL:115*/return this.ownerName;
        }
        
        String getTargetName() {
            /*SL:119*/return this.targetName;
        }
        
        byte[] getClassBytes() throws ClassNotFoundException, IOException {
            /*SL:123*/return MixinService.getService().getBytecodeProvider().getClassBytes(this.originalName, true);
        }
        
        @Override
        public String mapMethodName(final String a3, final String v1, final String v2) {
            /*SL:132*/if (this.ownerName.equalsIgnoreCase(a3)) {
                final ClassInfo.Method a4 = /*EL:133*/this.owner.getClassInfo().findMethod(v1, v2, 10);
                /*SL:134*/if (a4 != null) {
                    /*SL:135*/return a4.getName();
                }
            }
            /*SL:138*/return super.mapMethodName(a3, v1, v2);
        }
        
        @Override
        public String map(final String a1) {
            /*SL:146*/if (this.originalName.equals(a1)) {
                /*SL:147*/return this.name;
            }
            /*SL:148*/if (this.ownerName.equals(a1)) {
                /*SL:149*/return this.targetName;
            }
            /*SL:151*/return a1;
        }
        
        @Override
        public String toString() {
            /*SL:159*/return this.name;
        }
    }
    
    static class InnerClassAdapter extends ClassRemapper
    {
        private final InnerClassInfo info;
        
        public InnerClassAdapter(final ClassVisitor a1, final InnerClassInfo a2) {
            super(327680, a1, a2);
            this.info = a2;
        }
        
        @Override
        public void visitSource(final String a1, final String a2) {
            /*SL:183*/super.visitSource(a1, a2);
            final AnnotationVisitor v1 = /*EL:184*/this.cv.visitAnnotation("Lorg/spongepowered/asm/mixin/transformer/meta/MixinInner;", false);
            /*SL:185*/v1.visit("mixin", this.info.getOwner().toString());
            /*SL:186*/v1.visit("name", this.info.getOriginalName().substring(this.info.getOriginalName().lastIndexOf(47) + 1));
            /*SL:187*/v1.visitEnd();
        }
        
        @Override
        public void visitInnerClass(final String a1, final String a2, final String a3, final int a4) {
            /*SL:197*/if (a1.startsWith(this.info.getOriginalName() + "$")) {
                /*SL:198*/throw new InvalidMixinException(this.info.getOwner(), "Found unsupported nested inner class " + a1 + " in " + this.info.getOriginalName());
            }
            /*SL:202*/super.visitInnerClass(a1, a2, a3, a4);
        }
    }
}
