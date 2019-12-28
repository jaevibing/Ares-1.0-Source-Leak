package org.spongepowered.asm.util.asm;

import org.spongepowered.asm.mixin.transformer.ClassInfo;
import java.util.List;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.analysis.SimpleVerifier;

public class MixinVerifier extends SimpleVerifier
{
    private Type currentClass;
    private Type currentSuperClass;
    private List<Type> currentClassInterfaces;
    private boolean isInterface;
    
    public MixinVerifier(final Type a1, final Type a2, final List<Type> a3, final boolean a4) {
        super(a1, a2, a3, a4);
        this.currentClass = a1;
        this.currentSuperClass = a2;
        this.currentClassInterfaces = a3;
        this.isInterface = a4;
    }
    
    @Override
    protected boolean isInterface(final Type a1) {
        /*SL:53*/if (this.currentClass != null && a1.equals(this.currentClass)) {
            /*SL:54*/return this.isInterface;
        }
        /*SL:56*/return ClassInfo.forType(a1).isInterface();
    }
    
    @Override
    protected Type getSuperClass(final Type a1) {
        /*SL:61*/if (this.currentClass != null && a1.equals(this.currentClass)) {
            /*SL:62*/return this.currentSuperClass;
        }
        final ClassInfo v1 = /*EL:64*/ClassInfo.forType(a1).getSuperClass();
        /*SL:65*/return (v1 == null) ? null : Type.getType("L" + v1.getName() + ";");
    }
    
    @Override
    protected boolean isAssignableFrom(final Type v2, final Type v3) {
        /*SL:70*/if (v2.equals(v3)) {
            /*SL:71*/return true;
        }
        /*SL:73*/if (this.currentClass != null && v2.equals(this.currentClass)) {
            /*SL:74*/if (this.getSuperClass(v3) == null) {
                /*SL:75*/return false;
            }
            /*SL:77*/if (this.isInterface) {
                /*SL:78*/return v3.getSort() == 10 || v3.getSort() == 9;
            }
            /*SL:80*/return this.isAssignableFrom(v2, this.getSuperClass(v3));
        }
        else/*SL:82*/ if (this.currentClass != null && v3.equals(this.currentClass)) {
            /*SL:83*/if (this.isAssignableFrom(v2, this.currentSuperClass)) {
                /*SL:84*/return true;
            }
            /*SL:86*/if (this.currentClassInterfaces != null) {
                /*SL:87*/for (Type a2 = (Type)0; a2 < this.currentClassInterfaces.size(); ++a2) {
                    /*SL:88*/a2 = this.currentClassInterfaces.get(a2);
                    /*SL:89*/if (this.isAssignableFrom(v2, a2)) {
                        /*SL:90*/return true;
                    }
                }
            }
            /*SL:94*/return false;
        }
        else {
            ClassInfo v4 = /*EL:96*/ClassInfo.forType(v2);
            /*SL:97*/if (v4 == null) {
                /*SL:98*/return false;
            }
            /*SL:100*/if (v4.isInterface()) {
                /*SL:101*/v4 = ClassInfo.forName("java/lang/Object");
            }
            /*SL:103*/return ClassInfo.forType(v3).hasSuperClass(v4);
        }
    }
}
