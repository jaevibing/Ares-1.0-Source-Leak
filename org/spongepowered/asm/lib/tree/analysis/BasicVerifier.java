package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.List;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;

public class BasicVerifier extends BasicInterpreter
{
    public BasicVerifier() {
        super(327680);
    }
    
    protected BasicVerifier(final int a1) {
        super(a1);
    }
    
    public BasicValue copyOperation(final AbstractInsnNode v-1, final BasicValue v0) throws AnalyzerException {
        Value v = null;
        /*SL:61*/switch (v-1.getOpcode()) {
            case 21:
            case 54: {
                final Value a1 = BasicValue.INT_VALUE;
                /*SL:65*/break;
            }
            case 23:
            case 56: {
                final Value a2 = BasicValue.FLOAT_VALUE;
                /*SL:69*/break;
            }
            case 22:
            case 55: {
                /*SL:72*/v = BasicValue.LONG_VALUE;
                /*SL:73*/break;
            }
            case 24:
            case 57: {
                /*SL:76*/v = BasicValue.DOUBLE_VALUE;
                /*SL:77*/break;
            }
            case 25: {
                /*SL:79*/if (!v0.isReference()) {
                    /*SL:80*/throw new AnalyzerException(v-1, null, "an object reference", v0);
                }
                /*SL:83*/return v0;
            }
            case 58: {
                /*SL:85*/if (!v0.isReference() && !BasicValue.RETURNADDRESS_VALUE.equals(v0)) {
                    /*SL:87*/throw new AnalyzerException(v-1, null, "an object reference or a return address", v0);
                }
                /*SL:90*/return v0;
            }
            default: {
                /*SL:92*/return v0;
            }
        }
        /*SL:94*/if (!v.equals(v0)) {
            /*SL:95*/throw new AnalyzerException(v-1, null, v, v0);
        }
        /*SL:97*/return v0;
    }
    
    public BasicValue unaryOperation(final AbstractInsnNode v-1, final BasicValue v0) throws AnalyzerException {
        BasicValue v = null;
        /*SL:104*/switch (v-1.getOpcode()) {
            case 116:
            case 132:
            case 133:
            case 134:
            case 135:
            case 145:
            case 146:
            case 147:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 188:
            case 189: {
                final BasicValue a1 = BasicValue.INT_VALUE;
                /*SL:125*/break;
            }
            case 118:
            case 139:
            case 140:
            case 141:
            case 174: {
                final BasicValue a2 = BasicValue.FLOAT_VALUE;
                /*SL:132*/break;
            }
            case 117:
            case 136:
            case 137:
            case 138:
            case 173: {
                /*SL:138*/v = BasicValue.LONG_VALUE;
                /*SL:139*/break;
            }
            case 119:
            case 142:
            case 143:
            case 144:
            case 175: {
                /*SL:145*/v = BasicValue.DOUBLE_VALUE;
                /*SL:146*/break;
            }
            case 180: {
                /*SL:148*/v = this.newValue(/*EL:149*/Type.getObjectType(((FieldInsnNode)v-1).owner));
                /*SL:150*/break;
            }
            case 192: {
                /*SL:152*/if (!v0.isReference()) {
                    /*SL:153*/throw new AnalyzerException(v-1, null, "an object reference", v0);
                }
                /*SL:156*/return super.unaryOperation(v-1, v0);
            }
            case 190: {
                /*SL:158*/if (!this.isArrayValue(v0)) {
                    /*SL:159*/throw new AnalyzerException(v-1, null, "an array reference", v0);
                }
                /*SL:162*/return super.unaryOperation(v-1, v0);
            }
            case 176:
            case 191:
            case 193:
            case 194:
            case 195:
            case 198:
            case 199: {
                /*SL:170*/if (!v0.isReference()) {
                    /*SL:171*/throw new AnalyzerException(v-1, null, "an object reference", v0);
                }
                /*SL:174*/return super.unaryOperation(v-1, v0);
            }
            case 179: {
                /*SL:176*/v = this.newValue(Type.getType(((FieldInsnNode)v-1).desc));
                /*SL:177*/break;
            }
            default: {
                /*SL:179*/throw new Error("Internal error.");
            }
        }
        /*SL:181*/if (!this.isSubTypeOf(v0, v)) {
            /*SL:182*/throw new AnalyzerException(v-1, null, v, v0);
        }
        /*SL:184*/return super.unaryOperation(v-1, v0);
    }
    
    public BasicValue binaryOperation(final AbstractInsnNode v-2, final BasicValue v-1, final BasicValue v0) throws AnalyzerException {
        BasicValue v = null;
        BasicValue v2 = null;
        /*SL:193*/switch (v-2.getOpcode()) {
            case 46: {
                final BasicValue a1 = /*EL:195*/this.newValue(Type.getType("[I"));
                final BasicValue a2 = BasicValue.INT_VALUE;
                /*SL:197*/break;
            }
            case 51: {
                /*SL:199*/if (this.isSubTypeOf(v-1, this.newValue(Type.getType("[Z")))) {
                    final BasicValue a3 = /*EL:200*/this.newValue(Type.getType("[Z"));
                }
                else {
                    /*SL:202*/v = this.newValue(Type.getType("[B"));
                }
                /*SL:204*/v2 = BasicValue.INT_VALUE;
                /*SL:205*/break;
            }
            case 52: {
                /*SL:207*/v = this.newValue(Type.getType("[C"));
                /*SL:208*/v2 = BasicValue.INT_VALUE;
                /*SL:209*/break;
            }
            case 53: {
                /*SL:211*/v = this.newValue(Type.getType("[S"));
                /*SL:212*/v2 = BasicValue.INT_VALUE;
                /*SL:213*/break;
            }
            case 47: {
                /*SL:215*/v = this.newValue(Type.getType("[J"));
                /*SL:216*/v2 = BasicValue.INT_VALUE;
                /*SL:217*/break;
            }
            case 48: {
                /*SL:219*/v = this.newValue(Type.getType("[F"));
                /*SL:220*/v2 = BasicValue.INT_VALUE;
                /*SL:221*/break;
            }
            case 49: {
                /*SL:223*/v = this.newValue(Type.getType("[D"));
                /*SL:224*/v2 = BasicValue.INT_VALUE;
                /*SL:225*/break;
            }
            case 50: {
                /*SL:227*/v = this.newValue(Type.getType("[Ljava/lang/Object;"));
                /*SL:228*/v2 = BasicValue.INT_VALUE;
                /*SL:229*/break;
            }
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case 120:
            case 122:
            case 124:
            case 126:
            case 128:
            case 130:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164: {
                /*SL:247*/v = BasicValue.INT_VALUE;
                /*SL:248*/v2 = BasicValue.INT_VALUE;
                /*SL:249*/break;
            }
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case 149:
            case 150: {
                /*SL:257*/v = BasicValue.FLOAT_VALUE;
                /*SL:258*/v2 = BasicValue.FLOAT_VALUE;
                /*SL:259*/break;
            }
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
            case 148: {
                /*SL:269*/v = BasicValue.LONG_VALUE;
                /*SL:270*/v2 = BasicValue.LONG_VALUE;
                /*SL:271*/break;
            }
            case 121:
            case 123:
            case 125: {
                /*SL:275*/v = BasicValue.LONG_VALUE;
                /*SL:276*/v2 = BasicValue.INT_VALUE;
                /*SL:277*/break;
            }
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
            case 151:
            case 152: {
                /*SL:285*/v = BasicValue.DOUBLE_VALUE;
                /*SL:286*/v2 = BasicValue.DOUBLE_VALUE;
                /*SL:287*/break;
            }
            case 165:
            case 166: {
                /*SL:290*/v = BasicValue.REFERENCE_VALUE;
                /*SL:291*/v2 = BasicValue.REFERENCE_VALUE;
                /*SL:292*/break;
            }
            case 181: {
                final FieldInsnNode v3 = /*EL:294*/(FieldInsnNode)v-2;
                /*SL:295*/v = this.newValue(Type.getObjectType(v3.owner));
                /*SL:296*/v2 = this.newValue(Type.getType(v3.desc));
                /*SL:297*/break;
            }
            default: {
                /*SL:299*/throw new Error("Internal error.");
            }
        }
        /*SL:301*/if (!this.isSubTypeOf(v-1, v)) {
            /*SL:302*/throw new AnalyzerException(v-2, "First argument", v, v-1);
        }
        /*SL:304*/if (!this.isSubTypeOf(v0, v2)) {
            /*SL:305*/throw new AnalyzerException(v-2, "Second argument", v2, v0);
        }
        /*SL:308*/if (v-2.getOpcode() == 50) {
            /*SL:309*/return this.getElementValue(v-1);
        }
        /*SL:311*/return super.binaryOperation(v-2, v-1, v0);
    }
    
    public BasicValue ternaryOperation(final AbstractInsnNode v-4, final BasicValue v-3, final BasicValue v-2, final BasicValue v-1) throws AnalyzerException {
        BasicValue v1 = null;
        BasicValue v2 = null;
        /*SL:321*/switch (v-4.getOpcode()) {
            case 79: {
                final BasicValue a1 = /*EL:323*/this.newValue(Type.getType("[I"));
                final BasicValue a2 = BasicValue.INT_VALUE;
                /*SL:325*/break;
            }
            case 84: {
                /*SL:327*/if (this.isSubTypeOf(v-3, this.newValue(Type.getType("[Z")))) {
                    final BasicValue a3 = /*EL:328*/this.newValue(Type.getType("[Z"));
                }
                else {
                    final BasicValue a4 = /*EL:330*/this.newValue(Type.getType("[B"));
                }
                /*SL:332*/v1 = BasicValue.INT_VALUE;
                /*SL:333*/break;
            }
            case 85: {
                /*SL:335*/v2 = this.newValue(Type.getType("[C"));
                /*SL:336*/v1 = BasicValue.INT_VALUE;
                /*SL:337*/break;
            }
            case 86: {
                /*SL:339*/v2 = this.newValue(Type.getType("[S"));
                /*SL:340*/v1 = BasicValue.INT_VALUE;
                /*SL:341*/break;
            }
            case 80: {
                /*SL:343*/v2 = this.newValue(Type.getType("[J"));
                /*SL:344*/v1 = BasicValue.LONG_VALUE;
                /*SL:345*/break;
            }
            case 81: {
                /*SL:347*/v2 = this.newValue(Type.getType("[F"));
                /*SL:348*/v1 = BasicValue.FLOAT_VALUE;
                /*SL:349*/break;
            }
            case 82: {
                /*SL:351*/v2 = this.newValue(Type.getType("[D"));
                /*SL:352*/v1 = BasicValue.DOUBLE_VALUE;
                /*SL:353*/break;
            }
            case 83: {
                /*SL:355*/v2 = v-3;
                /*SL:356*/v1 = BasicValue.REFERENCE_VALUE;
                /*SL:357*/break;
            }
            default: {
                /*SL:359*/throw new Error("Internal error.");
            }
        }
        /*SL:361*/if (!this.isSubTypeOf(v-3, v2)) {
            /*SL:362*/throw new AnalyzerException(v-4, "First argument", "a " + v2 + " array reference", v-3);
        }
        /*SL:364*/if (!BasicValue.INT_VALUE.equals(v-2)) {
            /*SL:365*/throw new AnalyzerException(v-4, "Second argument", BasicValue.INT_VALUE, v-2);
        }
        /*SL:367*/if (!this.isSubTypeOf(v-1, v1)) {
            /*SL:368*/throw new AnalyzerException(v-4, "Third argument", v1, v-1);
        }
        /*SL:371*/return null;
    }
    
    public BasicValue naryOperation(final AbstractInsnNode v-6, final List<? extends BasicValue> v-5) throws AnalyzerException {
        final int opcode = /*EL:377*/v-6.getOpcode();
        /*SL:378*/if (opcode == 197) {
            /*SL:379*/for (int a1 = 0; a1 < v-5.size(); ++a1) {
                /*SL:380*/if (!BasicValue.INT_VALUE.equals(v-5.get(a1))) {
                    /*SL:382*/throw new AnalyzerException(v-6, null, BasicValue.INT_VALUE, (Value)v-5.get(a1));
                }
            }
        }
        else {
            int i = /*EL:386*/0;
            int n = /*EL:387*/0;
            /*SL:388*/if (opcode != 184 && opcode != 186) {
                final Type a2 = /*EL:389*/Type.getObjectType(((MethodInsnNode)v-6).owner);
                /*SL:390*/if (!this.isSubTypeOf((BasicValue)v-5.get(i++), this.newValue(a2))) {
                    /*SL:392*/throw new AnalyzerException(v-6, "Method owner", this.newValue(a2), (Value)v-5.get(0));
                }
            }
            final String v3 = /*EL:395*/(opcode == 186) ? ((InvokeDynamicInsnNode)v-6).desc : ((MethodInsnNode)v-6).desc;
            final Type[] v0 = /*EL:397*/Type.getArgumentTypes(v3);
            /*SL:398*/while (i < v-5.size()) {
                final BasicValue v = /*EL:399*/this.newValue(v0[n++]);
                final BasicValue v2 = /*EL:400*/(BasicValue)v-5.get(i++);
                /*SL:401*/if (!this.isSubTypeOf(v2, v)) {
                    /*SL:402*/throw new AnalyzerException(v-6, "Argument " + n, v, v2);
                }
            }
        }
        /*SL:407*/return super.naryOperation(v-6, v-5);
    }
    
    public void returnOperation(final AbstractInsnNode a1, final BasicValue a2, final BasicValue a3) throws AnalyzerException {
        /*SL:414*/if (!this.isSubTypeOf(a2, a3)) {
            /*SL:415*/throw new AnalyzerException(a1, "Incompatible return type", a3, a2);
        }
    }
    
    protected boolean isArrayValue(final BasicValue a1) {
        /*SL:421*/return a1.isReference();
    }
    
    protected BasicValue getElementValue(final BasicValue a1) throws AnalyzerException {
        /*SL:426*/return BasicValue.REFERENCE_VALUE;
    }
    
    protected boolean isSubTypeOf(final BasicValue a1, final BasicValue a2) {
        /*SL:431*/return a1.equals(a2);
    }
}
