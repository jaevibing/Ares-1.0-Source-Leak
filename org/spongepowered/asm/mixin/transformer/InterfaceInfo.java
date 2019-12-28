package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.List;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.Unique;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Iterator;
import java.util.HashSet;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import java.util.Set;
import org.spongepowered.asm.lib.Type;

public final class InterfaceInfo
{
    private final MixinInfo mixin;
    private final String prefix;
    private final Type iface;
    private final boolean unique;
    private Set<String> methods;
    
    private InterfaceInfo(final MixinInfo a1, final String a2, final Type a3, final boolean a4) {
        if (a2 == null || a2.length() < 2 || !a2.endsWith("$")) {
            throw new InvalidMixinException(a1, String.format("Prefix %s for iface %s is not valid", a2, a3.toString()));
        }
        this.mixin = a1;
        this.prefix = a2;
        this.iface = a3;
        this.unique = a4;
    }
    
    private void initMethods() {
        /*SL:96*/this.methods = new HashSet<String>();
        /*SL:97*/this.readInterface(this.iface.getInternalName());
    }
    
    private void readInterface(final String v-2) {
        final ClassInfo forName = /*EL:107*/ClassInfo.forName(v-2);
        /*SL:109*/for (final ClassInfo.Method a1 : forName.getMethods()) {
            /*SL:110*/this.methods.add(a1.toString());
        }
        /*SL:113*/for (final String v1 : forName.getInterfaces()) {
            /*SL:114*/this.readInterface(v1);
        }
    }
    
    public String getPrefix() {
        /*SL:124*/return this.prefix;
    }
    
    public Type getIface() {
        /*SL:133*/return this.iface;
    }
    
    public String getName() {
        /*SL:142*/return this.iface.getClassName();
    }
    
    public String getInternalName() {
        /*SL:151*/return this.iface.getInternalName();
    }
    
    public boolean isUnique() {
        /*SL:160*/return this.unique;
    }
    
    public boolean renameMethod(final MethodNode a1) {
        /*SL:173*/if (this.methods == null) {
            /*SL:174*/this.initMethods();
        }
        /*SL:177*/if (!a1.name.startsWith(this.prefix)) {
            /*SL:178*/if (this.methods.contains(a1.name + a1.desc)) {
                /*SL:179*/this.decorateUniqueMethod(a1);
            }
            /*SL:181*/return false;
        }
        final String v1 = /*EL:184*/a1.name.substring(this.prefix.length());
        final String v2 = /*EL:185*/v1 + a1.desc;
        /*SL:187*/if (!this.methods.contains(v2)) {
            /*SL:188*/throw new InvalidMixinException(this.mixin, String.format("%s does not exist in target interface %s", v1, this.getName()));
        }
        /*SL:191*/if ((a1.access & 0x1) == 0x0) {
            /*SL:192*/throw new InvalidMixinException(this.mixin, String.format("%s cannot implement %s because it is not visible", v1, this.getName()));
        }
        /*SL:195*/Annotations.setVisible(a1, MixinRenamed.class, "originalName", a1.name, "isInterfaceMember", true);
        /*SL:196*/this.decorateUniqueMethod(a1);
        /*SL:197*/a1.name = v1;
        /*SL:198*/return true;
    }
    
    private void decorateUniqueMethod(final MethodNode a1) {
        /*SL:208*/if (!this.unique) {
            /*SL:209*/return;
        }
        /*SL:212*/if (Annotations.getVisible(a1, Unique.class) == null) {
            /*SL:213*/Annotations.setVisible(a1, Unique.class, new Object[0]);
            /*SL:214*/this.mixin.getClassInfo().findMethod(a1).setUnique(true);
        }
    }
    
    static InterfaceInfo fromAnnotation(final MixinInfo a1, final AnnotationNode a2) {
        final String v1 = /*EL:227*/Annotations.<String>getValue(a2, "prefix");
        final Type v2 = /*EL:228*/Annotations.<Type>getValue(a2, "iface");
        final Boolean v3 = /*EL:229*/Annotations.<Boolean>getValue(a2, "unique");
        /*SL:231*/if (v1 == null || v2 == null) {
            /*SL:232*/throw new InvalidMixinException(a1, String.format("@Interface annotation on %s is missing a required parameter", a1));
        }
        /*SL:235*/return new InterfaceInfo(a1, v1, v2, v3 != null && v3);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:240*/if (this == a1) {
            /*SL:241*/return true;
        }
        /*SL:243*/if (a1 == null || this.getClass() != a1.getClass()) {
            /*SL:244*/return false;
        }
        final InterfaceInfo v1 = /*EL:247*/(InterfaceInfo)a1;
        /*SL:249*/return this.mixin.equals(v1.mixin) && this.prefix.equals(v1.prefix) && this.iface.equals(v1.iface);
    }
    
    @Override
    public int hashCode() {
        int v1 = /*EL:254*/this.mixin.hashCode();
        /*SL:255*/v1 = 31 * v1 + this.prefix.hashCode();
        /*SL:256*/v1 = 31 * v1 + this.iface.hashCode();
        /*SL:257*/return v1;
    }
}
