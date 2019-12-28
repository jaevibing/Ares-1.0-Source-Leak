package org.spongepowered.asm.lib.tree.analysis;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import java.util.List;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.Opcodes;

public class SourceInterpreter extends Interpreter<SourceValue> implements Opcodes
{
    public SourceInterpreter() {
        super(327680);
    }
    
    protected SourceInterpreter(final int a1) {
        super(a1);
    }
    
    public SourceValue newValue(final Type a1) {
        /*SL:62*/if (a1 == Type.VOID_TYPE) {
            /*SL:63*/return null;
        }
        /*SL:65*/return new SourceValue((a1 == null) ? 1 : a1.getSize());
    }
    
    public SourceValue newOperation(final AbstractInsnNode v0) {
        int v2 = 0;
        /*SL:71*/switch (v0.getOpcode()) {
            case 9:
            case 10:
            case 14:
            case 15: {
                final int a1 = /*EL:76*/2;
                /*SL:77*/break;
            }
            case 18: {
                final Object v = /*EL:79*/((LdcInsnNode)v0).cst;
                /*SL:80*/v2 = ((v instanceof Long || v instanceof Double) ? 2 : 1);
                /*SL:81*/break;
            }
            case 178: {
                /*SL:83*/v2 = Type.getType(((FieldInsnNode)v0).desc).getSize();
                /*SL:84*/break;
            }
            default: {
                /*SL:86*/v2 = 1;
                break;
            }
        }
        /*SL:88*/return new SourceValue(v2, v0);
    }
    
    public SourceValue copyOperation(final AbstractInsnNode a1, final SourceValue a2) {
        /*SL:94*/return new SourceValue(a2.getSize(), a1);
    }
    
    public SourceValue unaryOperation(final AbstractInsnNode v2, final SourceValue v3) {
        final int v4;
        /*SL:101*/switch (v2.getOpcode()) {
            case 117:
            case 119:
            case 133:
            case 135:
            case 138:
            case 140:
            case 141:
            case 143: {
                final int a1 = /*EL:110*/2;
                /*SL:111*/break;
            }
            case 180: {
                final int a2 = /*EL:113*/Type.getType(((FieldInsnNode)v2).desc).getSize();
                /*SL:114*/break;
            }
            default: {
                /*SL:116*/v4 = 1;
                break;
            }
        }
        /*SL:118*/return new SourceValue(v4, v2);
    }
    
    public SourceValue binaryOperation(final AbstractInsnNode a3, final SourceValue v1, final SourceValue v2) {
        final int v3;
        /*SL:125*/switch (a3.getOpcode()) {
            case 47:
            case 49:
            case 97:
            case 99:
            case 101:
            case 103:
            case 105:
            case 107:
            case 109:
            case 111:
            case 113:
            case 115:
            case 121:
            case 123:
            case 125:
            case 127:
            case 129:
            case 131: {
                final int a4 = /*EL:144*/2;
                /*SL:145*/break;
            }
            default: {
                /*SL:147*/v3 = 1;
                break;
            }
        }
        /*SL:149*/return new SourceValue(v3, a3);
    }
    
    public SourceValue ternaryOperation(final AbstractInsnNode a1, final SourceValue a2, final SourceValue a3, final SourceValue a4) {
        /*SL:156*/return new SourceValue(1, a1);
    }
    
    public SourceValue naryOperation(final AbstractInsnNode v2, final List<? extends SourceValue> v3) {
        final int v4 = /*EL:163*/v2.getOpcode();
        final int v5;
        /*SL:164*/if (v4 == 197) {
            final int a1 = /*EL:165*/1;
        }
        else {
            final String a2 = /*EL:167*/(v4 == 186) ? ((InvokeDynamicInsnNode)v2).desc : ((MethodInsnNode)v2).desc;
            /*SL:169*/v5 = Type.getReturnType(a2).getSize();
        }
        /*SL:171*/return new SourceValue(v5, v2);
    }
    
    public void returnOperation(final AbstractInsnNode a1, final SourceValue a2, final SourceValue a3) {
    }
    
    public SourceValue merge(final SourceValue v2, final SourceValue v3) {
        /*SL:181*/if (v2.insns instanceof SmallSet && v3.insns instanceof SmallSet) {
            final Set<AbstractInsnNode> a1 = /*EL:182*/((SmallSet)v2.insns).union((SmallSet)v3.insns);
            /*SL:184*/if (a1 == v2.insns && v2.size == v3.size) {
                /*SL:185*/return v2;
            }
            /*SL:187*/return new SourceValue(Math.min(v2.size, v3.size), a1);
        }
        else {
            /*SL:190*/if (v2.size != v3.size || !v2.insns.containsAll(v3.insns)) {
                final HashSet<AbstractInsnNode> a2 = /*EL:191*/new HashSet<AbstractInsnNode>();
                /*SL:192*/a2.addAll((Collection<?>)v2.insns);
                /*SL:193*/a2.addAll((Collection<?>)v3.insns);
                /*SL:194*/return new SourceValue(Math.min(v2.size, v3.size), a2);
            }
            /*SL:196*/return v2;
        }
    }
}
