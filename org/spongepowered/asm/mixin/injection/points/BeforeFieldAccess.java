package org.spongepowered.asm.mixin.injection.points;

import java.util.Iterator;
import org.spongepowered.asm.util.Bytecode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("FIELD")
public class BeforeFieldAccess extends BeforeInvoke
{
    private static final String ARRAY_GET = "get";
    private static final String ARRAY_SET = "set";
    private static final String ARRAY_LENGTH = "length";
    public static final int ARRAY_SEARCH_FUZZ_DEFAULT = 8;
    private final int opcode;
    private final int arrOpcode;
    private final int fuzzFactor;
    
    public BeforeFieldAccess(final InjectionPointData a1) {
        super(a1);
        this.opcode = a1.getOpcode(-1, 180, 181, 178, 179, -1);
        final String v1 = a1.get("array", "");
        this.arrOpcode = ("get".equalsIgnoreCase(v1) ? 46 : ("set".equalsIgnoreCase(v1) ? 79 : ("length".equalsIgnoreCase(v1) ? 190 : 0)));
        this.fuzzFactor = Math.min(Math.max(a1.get("fuzz", 8), 1), 32);
    }
    
    public int getFuzzFactor() {
        /*SL:127*/return this.fuzzFactor;
    }
    
    public int getArrayOpcode() {
        /*SL:131*/return this.arrOpcode;
    }
    
    private int getArrayOpcode(final String a1) {
        /*SL:135*/if (this.arrOpcode != 190) {
            /*SL:136*/return Type.getType(a1).getElementType().getOpcode(this.arrOpcode);
        }
        /*SL:138*/return this.arrOpcode;
    }
    
    @Override
    protected boolean matchesInsn(final AbstractInsnNode a1) {
        /*SL:143*/return a1 instanceof FieldInsnNode && (((FieldInsnNode)a1).getOpcode() == this.opcode || this.opcode == -1) && /*EL:144*/(this.arrOpcode == 0 || /*EL:148*/((a1.getOpcode() == 178 || a1.getOpcode() == 180) && /*EL:152*/Type.getType(((FieldInsnNode)a1).desc).getSort() == 9));
    }
    
    @Override
    protected boolean addInsn(final InsnList v1, final Collection<AbstractInsnNode> v2, final AbstractInsnNode v3) {
        /*SL:160*/if (this.arrOpcode > 0) {
            final FieldInsnNode a1 = /*EL:161*/(FieldInsnNode)v3;
            final int a2 = /*EL:162*/this.getArrayOpcode(a1.desc);
            /*SL:163*/this.log("{} > > > > searching for array access opcode {} fuzz={}", this.className, Bytecode.getOpcodeName(a2), this.fuzzFactor);
            /*SL:165*/if (findArrayNode(v1, a1, a2, this.fuzzFactor) == null) {
                /*SL:166*/this.log("{} > > > > > failed to locate matching insn", this.className);
                /*SL:167*/return false;
            }
        }
        /*SL:171*/this.log("{} > > > > > adding matching insn", this.className);
        /*SL:173*/return super.addInsn(v1, v2, v3);
    }
    
    public static AbstractInsnNode findArrayNode(final InsnList a4, final FieldInsnNode v1, final int v2, final int v3) {
        int v4 = /*EL:191*/0;
        final Iterator<AbstractInsnNode> a5 = /*EL:192*/a4.iterator(a4.indexOf(v1) + 1);
        while (a5.hasNext()) {
            final AbstractInsnNode a6 = /*EL:193*/a5.next();
            /*SL:194*/if (a6.getOpcode() == v2) {
                /*SL:195*/return a6;
            }
            /*SL:196*/if (a6.getOpcode() == 190 && v4 == 0) {
                /*SL:197*/return null;
            }
            /*SL:198*/if (a6 instanceof FieldInsnNode) {
                final FieldInsnNode a7 = /*EL:199*/(FieldInsnNode)a6;
                /*SL:200*/if (a7.desc.equals(v1.desc) && a7.name.equals(v1.name) && a7.owner.equals(v1.owner)) {
                    /*SL:201*/return null;
                }
            }
            /*SL:204*/if (v4++ > v3) {
                /*SL:205*/return null;
            }
        }
        /*SL:208*/return null;
    }
}
