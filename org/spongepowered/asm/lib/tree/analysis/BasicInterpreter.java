package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.MultiANewArrayInsnNode;
import java.util.List;
import org.spongepowered.asm.lib.tree.IntInsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.Opcodes;

public class BasicInterpreter extends Interpreter<BasicValue> implements Opcodes
{
    public BasicInterpreter() {
        super(327680);
    }
    
    protected BasicInterpreter(final int a1) {
        super(a1);
    }
    
    public BasicValue newValue(final Type a1) {
        /*SL:65*/if (a1 == null) {
            /*SL:66*/return BasicValue.UNINITIALIZED_VALUE;
        }
        /*SL:68*/switch (a1.getSort()) {
            case 0: {
                /*SL:70*/return null;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                /*SL:76*/return BasicValue.INT_VALUE;
            }
            case 6: {
                /*SL:78*/return BasicValue.FLOAT_VALUE;
            }
            case 7: {
                /*SL:80*/return BasicValue.LONG_VALUE;
            }
            case 8: {
                /*SL:82*/return BasicValue.DOUBLE_VALUE;
            }
            case 9:
            case 10: {
                /*SL:85*/return BasicValue.REFERENCE_VALUE;
            }
            default: {
                /*SL:87*/throw new Error("Internal error");
            }
        }
    }
    
    public BasicValue newOperation(final AbstractInsnNode v0) throws AnalyzerException {
        /*SL:94*/switch (v0.getOpcode()) {
            case 1: {
                /*SL:96*/return this.newValue(Type.getObjectType("null"));
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                /*SL:104*/return BasicValue.INT_VALUE;
            }
            case 9:
            case 10: {
                /*SL:107*/return BasicValue.LONG_VALUE;
            }
            case 11:
            case 12:
            case 13: {
                /*SL:111*/return BasicValue.FLOAT_VALUE;
            }
            case 14:
            case 15: {
                /*SL:114*/return BasicValue.DOUBLE_VALUE;
            }
            case 16:
            case 17: {
                /*SL:117*/return BasicValue.INT_VALUE;
            }
            case 18: {
                final Object v = /*EL:119*/((LdcInsnNode)v0).cst;
                /*SL:120*/if (v instanceof Integer) {
                    /*SL:121*/return BasicValue.INT_VALUE;
                }
                /*SL:122*/if (v instanceof Float) {
                    /*SL:123*/return BasicValue.FLOAT_VALUE;
                }
                /*SL:124*/if (v instanceof Long) {
                    /*SL:125*/return BasicValue.LONG_VALUE;
                }
                /*SL:126*/if (v instanceof Double) {
                    /*SL:127*/return BasicValue.DOUBLE_VALUE;
                }
                /*SL:128*/if (v instanceof String) {
                    /*SL:129*/return this.newValue(Type.getObjectType("java/lang/String"));
                }
                /*SL:130*/if (v instanceof Type) {
                    final int a1 = /*EL:131*/((Type)v).getSort();
                    /*SL:132*/if (a1 == 10 || a1 == 9) {
                        /*SL:133*/return this.newValue(Type.getObjectType("java/lang/Class"));
                    }
                    /*SL:134*/if (a1 == 11) {
                        /*SL:135*/return this.newValue(/*EL:136*/Type.getObjectType("java/lang/invoke/MethodType"));
                    }
                    /*SL:138*/throw new IllegalArgumentException("Illegal LDC constant " + v);
                }
                else {
                    /*SL:141*/if (v instanceof Handle) {
                        /*SL:142*/return this.newValue(/*EL:143*/Type.getObjectType("java/lang/invoke/MethodHandle"));
                    }
                    /*SL:145*/throw new IllegalArgumentException("Illegal LDC constant " + v);
                }
                break;
            }
            case 168: {
                /*SL:149*/return BasicValue.RETURNADDRESS_VALUE;
            }
            case 178: {
                /*SL:151*/return this.newValue(Type.getType(((FieldInsnNode)v0).desc));
            }
            case 187: {
                /*SL:153*/return this.newValue(Type.getObjectType(((TypeInsnNode)v0).desc));
            }
            default: {
                /*SL:155*/throw new Error("Internal error.");
            }
        }
    }
    
    public BasicValue copyOperation(final AbstractInsnNode a1, final BasicValue a2) throws AnalyzerException {
        /*SL:162*/return a2;
    }
    
    public BasicValue unaryOperation(final AbstractInsnNode v2, final BasicValue v3) throws AnalyzerException {
        /*SL:168*/switch (v2.getOpcode()) {
            case 116:
            case 132:
            case 136:
            case 139:
            case 142:
            case 145:
            case 146:
            case 147: {
                /*SL:177*/return BasicValue.INT_VALUE;
            }
            case 118:
            case 134:
            case 137:
            case 144: {
                /*SL:182*/return BasicValue.FLOAT_VALUE;
            }
            case 117:
            case 133:
            case 140:
            case 143: {
                /*SL:187*/return BasicValue.LONG_VALUE;
            }
            case 119:
            case 135:
            case 138:
            case 141: {
                /*SL:192*/return BasicValue.DOUBLE_VALUE;
            }
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 179: {
                /*SL:207*/return null;
            }
            case 180: {
                /*SL:209*/return this.newValue(Type.getType(((FieldInsnNode)v2).desc));
            }
            case 188: {
                /*SL:211*/switch (((IntInsnNode)v2).operand) {
                    case 4: {
                        /*SL:213*/return this.newValue(Type.getType("[Z"));
                    }
                    case 5: {
                        /*SL:215*/return this.newValue(Type.getType("[C"));
                    }
                    case 8: {
                        /*SL:217*/return this.newValue(Type.getType("[B"));
                    }
                    case 9: {
                        /*SL:219*/return this.newValue(Type.getType("[S"));
                    }
                    case 10: {
                        /*SL:221*/return this.newValue(Type.getType("[I"));
                    }
                    case 6: {
                        /*SL:223*/return this.newValue(Type.getType("[F"));
                    }
                    case 7: {
                        /*SL:225*/return this.newValue(Type.getType("[D"));
                    }
                    case 11: {
                        /*SL:227*/return this.newValue(Type.getType("[J"));
                    }
                    default: {
                        /*SL:229*/throw new AnalyzerException(v2, "Invalid array type");
                    }
                }
                break;
            }
            case 189: {
                final String a1 = /*EL:232*/((TypeInsnNode)v2).desc;
                /*SL:233*/return this.newValue(Type.getType("[" + Type.getObjectType(a1)));
            }
            case 190: {
                /*SL:235*/return BasicValue.INT_VALUE;
            }
            case 191: {
                /*SL:237*/return null;
            }
            case 192: {
                final String a2 = /*EL:239*/((TypeInsnNode)v2).desc;
                /*SL:240*/return this.newValue(Type.getObjectType(a2));
            }
            case 193: {
                /*SL:242*/return BasicValue.INT_VALUE;
            }
            case 194:
            case 195:
            case 198:
            case 199: {
                /*SL:247*/return null;
            }
            default: {
                /*SL:249*/throw new Error("Internal error.");
            }
        }
    }
    
    public BasicValue binaryOperation(final AbstractInsnNode a1, final BasicValue a2, final BasicValue a3) throws AnalyzerException {
        /*SL:257*/switch (a1.getOpcode()) {
            case 46:
            case 51:
            case 52:
            case 53:
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
            case 130: {
                /*SL:273*/return BasicValue.INT_VALUE;
            }
            case 48:
            case 98:
            case 102:
            case 106:
            case 110:
            case 114: {
                /*SL:280*/return BasicValue.FLOAT_VALUE;
            }
            case 47:
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 121:
            case 123:
            case 125:
            case 127:
            case 129:
            case 131: {
                /*SL:293*/return BasicValue.LONG_VALUE;
            }
            case 49:
            case 99:
            case 103:
            case 107:
            case 111:
            case 115: {
                /*SL:300*/return BasicValue.DOUBLE_VALUE;
            }
            case 50: {
                /*SL:302*/return BasicValue.REFERENCE_VALUE;
            }
            case 148:
            case 149:
            case 150:
            case 151:
            case 152: {
                /*SL:308*/return BasicValue.INT_VALUE;
            }
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 181: {
                /*SL:318*/return null;
            }
            default: {
                /*SL:320*/throw new Error("Internal error.");
            }
        }
    }
    
    public BasicValue ternaryOperation(final AbstractInsnNode a1, final BasicValue a2, final BasicValue a3, final BasicValue a4) throws AnalyzerException {
        /*SL:328*/return null;
    }
    
    public BasicValue naryOperation(final AbstractInsnNode a1, final List<? extends BasicValue> a2) throws AnalyzerException {
        final int v1 = /*EL:334*/a1.getOpcode();
        /*SL:335*/if (v1 == 197) {
            /*SL:336*/return this.newValue(Type.getType(((MultiANewArrayInsnNode)a1).desc));
        }
        /*SL:337*/if (v1 == 186) {
            /*SL:338*/return this.newValue(/*EL:339*/Type.getReturnType(((InvokeDynamicInsnNode)a1).desc));
        }
        /*SL:341*/return this.newValue(Type.getReturnType(((MethodInsnNode)a1).desc));
    }
    
    public void returnOperation(final AbstractInsnNode a1, final BasicValue a2, final BasicValue a3) throws AnalyzerException {
    }
    
    public BasicValue merge(final BasicValue a1, final BasicValue a2) {
        /*SL:353*/if (!a1.equals(a2)) {
            /*SL:354*/return BasicValue.UNINITIALIZED_VALUE;
        }
        /*SL:356*/return a1;
    }
}
