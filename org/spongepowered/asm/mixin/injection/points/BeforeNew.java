package org.spongepowered.asm.mixin.injection.points;

import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.Iterator;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import java.util.ArrayList;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("NEW")
public class BeforeNew extends InjectionPoint
{
    private final String target;
    private final String desc;
    private final int ordinal;
    
    public BeforeNew(final InjectionPointData a1) {
        super(a1);
        this.ordinal = a1.getOrdinal();
        final String v1 = Strings.emptyToNull(a1.get("class", a1.get("target", "")).replace('.', '/'));
        final MemberInfo v2 = MemberInfo.parseAndValidate(v1, a1.getContext());
        this.target = v2.toCtorType();
        this.desc = v2.toCtorDesc();
    }
    
    public boolean hasDescriptor() {
        /*SL:120*/return this.desc != null;
    }
    
    @Override
    public boolean find(final String v1, final InsnList v2, final Collection<AbstractInsnNode> v3) {
        boolean v4 = /*EL:126*/false;
        int v5 = /*EL:127*/0;
        final Collection<TypeInsnNode> v6 = /*EL:129*/new ArrayList<TypeInsnNode>();
        final Collection<AbstractInsnNode> v7 = /*EL:130*/(Collection<AbstractInsnNode>)((this.desc != null) ? v6 : v3);
        /*SL:131*/for (final AbstractInsnNode a1 : v2) {
            /*SL:135*/if (a1 instanceof TypeInsnNode && a1.getOpcode() == 187 && this.matchesOwner((TypeInsnNode)a1)) {
                /*SL:136*/if (this.ordinal == -1 || this.ordinal == v5) {
                    /*SL:137*/v7.add(a1);
                    /*SL:138*/v4 = (this.desc == null);
                }
                /*SL:141*/++v5;
            }
        }
        /*SL:145*/if (this.desc != null) {
            /*SL:146*/for (final TypeInsnNode a2 : v6) {
                /*SL:147*/if (this.findCtor(v2, a2)) {
                    /*SL:148*/v3.add(a2);
                    /*SL:149*/v4 = true;
                }
            }
        }
        /*SL:154*/return v4;
    }
    
    protected boolean findCtor(final InsnList v-2, final TypeInsnNode v-1) {
        final int v0 = /*EL:158*/v-2.indexOf(v-1);
        final Iterator<AbstractInsnNode> v = /*EL:159*/v-2.iterator(v0);
        while (v.hasNext()) {
            AbstractInsnNode a2 = /*EL:160*/v.next();
            /*SL:161*/if (a2 instanceof MethodInsnNode && a2.getOpcode() == 183) {
                /*SL:162*/a2 = (MethodInsnNode)a2;
                /*SL:163*/if ("<init>".equals(a2.name) && a2.owner.equals(v-1.desc) && a2.desc.equals(this.desc)) {
                    /*SL:164*/return true;
                }
                continue;
            }
        }
        /*SL:168*/return false;
    }
    
    private boolean matchesOwner(final TypeInsnNode a1) {
        /*SL:172*/return this.target == null || this.target.equals(a1.desc);
    }
}
