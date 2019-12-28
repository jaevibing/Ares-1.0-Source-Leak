package javassist.bytecode.analysis;

import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Opcode;

public class Executor implements Opcode
{
    private final ConstPool constPool;
    private final ClassPool classPool;
    private final Type STRING_TYPE;
    private final Type CLASS_TYPE;
    private final Type THROWABLE_TYPE;
    private int lastPos;
    
    public Executor(final ClassPool v1, final ConstPool v2) {
        this.constPool = v2;
        this.classPool = v1;
        try {
            this.STRING_TYPE = this.getType("java.lang.String");
            this.CLASS_TYPE = this.getType("java.lang.Class");
            this.THROWABLE_TYPE = this.getType("java.lang.Throwable");
        }
        catch (Exception a1) {
            throw new RuntimeException(a1);
        }
    }
    
    public void execute(final MethodInfo v-6, final int v-5, final CodeIterator v-4, final Frame v-3, final Subroutine v-2) throws BadBytecode {
        /*SL:68*/this.lastPos = v-5;
        final int byte1 = /*EL:69*/v-4.byteAt(v-5);
        /*SL:73*/switch (byte1) {
            case 1: {
                /*SL:77*/v-3.push(Type.UNINIT);
                /*SL:78*/break;
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                /*SL:86*/v-3.push(Type.INTEGER);
                /*SL:87*/break;
            }
            case 9:
            case 10: {
                /*SL:90*/v-3.push(Type.LONG);
                /*SL:91*/v-3.push(Type.TOP);
                /*SL:92*/break;
            }
            case 11:
            case 12:
            case 13: {
                /*SL:96*/v-3.push(Type.FLOAT);
                /*SL:97*/break;
            }
            case 14:
            case 15: {
                /*SL:100*/v-3.push(Type.DOUBLE);
                /*SL:101*/v-3.push(Type.TOP);
                /*SL:102*/break;
            }
            case 16:
            case 17: {
                /*SL:105*/v-3.push(Type.INTEGER);
                /*SL:106*/break;
            }
            case 18: {
                /*SL:108*/this.evalLDC(v-4.byteAt(v-5 + 1), v-3);
                /*SL:109*/break;
            }
            case 19:
            case 20: {
                /*SL:112*/this.evalLDC(v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:113*/break;
            }
            case 21: {
                /*SL:115*/this.evalLoad(Type.INTEGER, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:116*/break;
            }
            case 22: {
                /*SL:118*/this.evalLoad(Type.LONG, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:119*/break;
            }
            case 23: {
                /*SL:121*/this.evalLoad(Type.FLOAT, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:122*/break;
            }
            case 24: {
                /*SL:124*/this.evalLoad(Type.DOUBLE, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:125*/break;
            }
            case 25: {
                /*SL:127*/this.evalLoad(Type.OBJECT, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:128*/break;
            }
            case 26:
            case 27:
            case 28:
            case 29: {
                /*SL:133*/this.evalLoad(Type.INTEGER, byte1 - 26, v-3, v-2);
                /*SL:134*/break;
            }
            case 30:
            case 31:
            case 32:
            case 33: {
                /*SL:139*/this.evalLoad(Type.LONG, byte1 - 30, v-3, v-2);
                /*SL:140*/break;
            }
            case 34:
            case 35:
            case 36:
            case 37: {
                /*SL:145*/this.evalLoad(Type.FLOAT, byte1 - 34, v-3, v-2);
                /*SL:146*/break;
            }
            case 38:
            case 39:
            case 40:
            case 41: {
                /*SL:151*/this.evalLoad(Type.DOUBLE, byte1 - 38, v-3, v-2);
                /*SL:152*/break;
            }
            case 42:
            case 43:
            case 44:
            case 45: {
                /*SL:157*/this.evalLoad(Type.OBJECT, byte1 - 42, v-3, v-2);
                /*SL:158*/break;
            }
            case 46: {
                /*SL:160*/this.evalArrayLoad(Type.INTEGER, v-3);
                /*SL:161*/break;
            }
            case 47: {
                /*SL:163*/this.evalArrayLoad(Type.LONG, v-3);
                /*SL:164*/break;
            }
            case 48: {
                /*SL:166*/this.evalArrayLoad(Type.FLOAT, v-3);
                /*SL:167*/break;
            }
            case 49: {
                /*SL:169*/this.evalArrayLoad(Type.DOUBLE, v-3);
                /*SL:170*/break;
            }
            case 50: {
                /*SL:172*/this.evalArrayLoad(Type.OBJECT, v-3);
                /*SL:173*/break;
            }
            case 51:
            case 52:
            case 53: {
                /*SL:177*/this.evalArrayLoad(Type.INTEGER, v-3);
                /*SL:178*/break;
            }
            case 54: {
                /*SL:180*/this.evalStore(Type.INTEGER, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:181*/break;
            }
            case 55: {
                /*SL:183*/this.evalStore(Type.LONG, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:184*/break;
            }
            case 56: {
                /*SL:186*/this.evalStore(Type.FLOAT, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:187*/break;
            }
            case 57: {
                /*SL:189*/this.evalStore(Type.DOUBLE, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:190*/break;
            }
            case 58: {
                /*SL:192*/this.evalStore(Type.OBJECT, v-4.byteAt(v-5 + 1), v-3, v-2);
                /*SL:193*/break;
            }
            case 59:
            case 60:
            case 61:
            case 62: {
                /*SL:198*/this.evalStore(Type.INTEGER, byte1 - 59, v-3, v-2);
                /*SL:199*/break;
            }
            case 63:
            case 64:
            case 65:
            case 66: {
                /*SL:204*/this.evalStore(Type.LONG, byte1 - 63, v-3, v-2);
                /*SL:205*/break;
            }
            case 67:
            case 68:
            case 69:
            case 70: {
                /*SL:210*/this.evalStore(Type.FLOAT, byte1 - 67, v-3, v-2);
                /*SL:211*/break;
            }
            case 71:
            case 72:
            case 73:
            case 74: {
                /*SL:216*/this.evalStore(Type.DOUBLE, byte1 - 71, v-3, v-2);
                /*SL:217*/break;
            }
            case 75:
            case 76:
            case 77:
            case 78: {
                /*SL:222*/this.evalStore(Type.OBJECT, byte1 - 75, v-3, v-2);
                /*SL:223*/break;
            }
            case 79: {
                /*SL:225*/this.evalArrayStore(Type.INTEGER, v-3);
                /*SL:226*/break;
            }
            case 80: {
                /*SL:228*/this.evalArrayStore(Type.LONG, v-3);
                /*SL:229*/break;
            }
            case 81: {
                /*SL:231*/this.evalArrayStore(Type.FLOAT, v-3);
                /*SL:232*/break;
            }
            case 82: {
                /*SL:234*/this.evalArrayStore(Type.DOUBLE, v-3);
                /*SL:235*/break;
            }
            case 83: {
                /*SL:237*/this.evalArrayStore(Type.OBJECT, v-3);
                /*SL:238*/break;
            }
            case 84:
            case 85:
            case 86: {
                /*SL:242*/this.evalArrayStore(Type.INTEGER, v-3);
                /*SL:243*/break;
            }
            case 87: {
                /*SL:245*/if (v-3.pop() == Type.TOP) {
                    /*SL:246*/throw new BadBytecode("POP can not be used with a category 2 value, pos = " + v-5);
                }
                break;
            }
            case 88: {
                /*SL:249*/v-3.pop();
                /*SL:250*/v-3.pop();
                /*SL:251*/break;
            }
            case 89: {
                final Type a1 = /*EL:253*/v-3.peek();
                /*SL:254*/if (a1 == Type.TOP) {
                    /*SL:255*/throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + v-5);
                }
                /*SL:257*/v-3.push(v-3.peek());
                /*SL:258*/break;
            }
            case 90:
            case 91: {
                final Type a2 = /*EL:262*/v-3.peek();
                /*SL:263*/if (a2 == Type.TOP) {
                    /*SL:264*/throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + v-5);
                }
                int a3 = /*EL:265*/v-3.getTopIndex();
                final int a4 = /*EL:266*/a3 - (byte1 - 90) - 1;
                /*SL:267*/v-3.push(a2);
                /*SL:269*/while (a3 > a4) {
                    /*SL:270*/v-3.setStack(a3, v-3.getStack(a3 - 1));
                    /*SL:271*/--a3;
                }
                /*SL:273*/v-3.setStack(a4, a2);
                /*SL:274*/break;
            }
            case 92: {
                /*SL:277*/v-3.push(v-3.getStack(v-3.getTopIndex() - 1));
                /*SL:278*/v-3.push(v-3.getStack(v-3.getTopIndex() - 1));
                /*SL:279*/break;
            }
            case 93:
            case 94: {
                int a5 = /*EL:282*/v-3.getTopIndex();
                final int v1 = /*EL:283*/a5 - (byte1 - 93) - 1;
                final Type v2 = /*EL:284*/v-3.getStack(v-3.getTopIndex() - 1);
                final Type v3 = /*EL:285*/v-3.peek();
                /*SL:286*/v-3.push(v2);
                /*SL:287*/v-3.push(v3);
                /*SL:288*/while (a5 > v1) {
                    /*SL:289*/v-3.setStack(a5, v-3.getStack(a5 - 2));
                    /*SL:290*/--a5;
                }
                /*SL:292*/v-3.setStack(v1, v3);
                /*SL:293*/v-3.setStack(v1 - 1, v2);
                /*SL:294*/break;
            }
            case 95: {
                final Type v4 = /*EL:297*/v-3.pop();
                final Type v5 = /*EL:298*/v-3.pop();
                /*SL:299*/if (v4.getSize() == 2 || v5.getSize() == 2) {
                    /*SL:300*/throw new BadBytecode("Swap can not be used with category 2 values, pos = " + v-5);
                }
                /*SL:301*/v-3.push(v4);
                /*SL:302*/v-3.push(v5);
                /*SL:303*/break;
            }
            case 96: {
                /*SL:308*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:309*/break;
            }
            case 97: {
                /*SL:311*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:312*/break;
            }
            case 98: {
                /*SL:314*/this.evalBinaryMath(Type.FLOAT, v-3);
                /*SL:315*/break;
            }
            case 99: {
                /*SL:317*/this.evalBinaryMath(Type.DOUBLE, v-3);
                /*SL:318*/break;
            }
            case 100: {
                /*SL:320*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:321*/break;
            }
            case 101: {
                /*SL:323*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:324*/break;
            }
            case 102: {
                /*SL:326*/this.evalBinaryMath(Type.FLOAT, v-3);
                /*SL:327*/break;
            }
            case 103: {
                /*SL:329*/this.evalBinaryMath(Type.DOUBLE, v-3);
                /*SL:330*/break;
            }
            case 104: {
                /*SL:332*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:333*/break;
            }
            case 105: {
                /*SL:335*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:336*/break;
            }
            case 106: {
                /*SL:338*/this.evalBinaryMath(Type.FLOAT, v-3);
                /*SL:339*/break;
            }
            case 107: {
                /*SL:341*/this.evalBinaryMath(Type.DOUBLE, v-3);
                /*SL:342*/break;
            }
            case 108: {
                /*SL:344*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:345*/break;
            }
            case 109: {
                /*SL:347*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:348*/break;
            }
            case 110: {
                /*SL:350*/this.evalBinaryMath(Type.FLOAT, v-3);
                /*SL:351*/break;
            }
            case 111: {
                /*SL:353*/this.evalBinaryMath(Type.DOUBLE, v-3);
                /*SL:354*/break;
            }
            case 112: {
                /*SL:356*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:357*/break;
            }
            case 113: {
                /*SL:359*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:360*/break;
            }
            case 114: {
                /*SL:362*/this.evalBinaryMath(Type.FLOAT, v-3);
                /*SL:363*/break;
            }
            case 115: {
                /*SL:365*/this.evalBinaryMath(Type.DOUBLE, v-3);
                /*SL:366*/break;
            }
            case 116: {
                /*SL:370*/this.verifyAssignable(Type.INTEGER, this.simplePeek(v-3));
                /*SL:371*/break;
            }
            case 117: {
                /*SL:373*/this.verifyAssignable(Type.LONG, this.simplePeek(v-3));
                /*SL:374*/break;
            }
            case 118: {
                /*SL:376*/this.verifyAssignable(Type.FLOAT, this.simplePeek(v-3));
                /*SL:377*/break;
            }
            case 119: {
                /*SL:379*/this.verifyAssignable(Type.DOUBLE, this.simplePeek(v-3));
                /*SL:380*/break;
            }
            case 120: {
                /*SL:384*/this.evalShift(Type.INTEGER, v-3);
                /*SL:385*/break;
            }
            case 121: {
                /*SL:387*/this.evalShift(Type.LONG, v-3);
                /*SL:388*/break;
            }
            case 122: {
                /*SL:390*/this.evalShift(Type.INTEGER, v-3);
                /*SL:391*/break;
            }
            case 123: {
                /*SL:393*/this.evalShift(Type.LONG, v-3);
                /*SL:394*/break;
            }
            case 124: {
                /*SL:396*/this.evalShift(Type.INTEGER, v-3);
                /*SL:397*/break;
            }
            case 125: {
                /*SL:399*/this.evalShift(Type.LONG, v-3);
                /*SL:400*/break;
            }
            case 126: {
                /*SL:404*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:405*/break;
            }
            case 127: {
                /*SL:407*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:408*/break;
            }
            case 128: {
                /*SL:410*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:411*/break;
            }
            case 129: {
                /*SL:413*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:414*/break;
            }
            case 130: {
                /*SL:416*/this.evalBinaryMath(Type.INTEGER, v-3);
                /*SL:417*/break;
            }
            case 131: {
                /*SL:419*/this.evalBinaryMath(Type.LONG, v-3);
                /*SL:420*/break;
            }
            case 132: {
                final int v6 = /*EL:423*/v-4.byteAt(v-5 + 1);
                /*SL:424*/this.verifyAssignable(Type.INTEGER, v-3.getLocal(v6));
                /*SL:425*/this.access(v6, Type.INTEGER, v-2);
                /*SL:426*/break;
            }
            case 133: {
                /*SL:431*/this.verifyAssignable(Type.INTEGER, this.simplePop(v-3));
                /*SL:432*/this.simplePush(Type.LONG, v-3);
                /*SL:433*/break;
            }
            case 134: {
                /*SL:435*/this.verifyAssignable(Type.INTEGER, this.simplePop(v-3));
                /*SL:436*/this.simplePush(Type.FLOAT, v-3);
                /*SL:437*/break;
            }
            case 135: {
                /*SL:439*/this.verifyAssignable(Type.INTEGER, this.simplePop(v-3));
                /*SL:440*/this.simplePush(Type.DOUBLE, v-3);
                /*SL:441*/break;
            }
            case 136: {
                /*SL:443*/this.verifyAssignable(Type.LONG, this.simplePop(v-3));
                /*SL:444*/this.simplePush(Type.INTEGER, v-3);
                /*SL:445*/break;
            }
            case 137: {
                /*SL:447*/this.verifyAssignable(Type.LONG, this.simplePop(v-3));
                /*SL:448*/this.simplePush(Type.FLOAT, v-3);
                /*SL:449*/break;
            }
            case 138: {
                /*SL:451*/this.verifyAssignable(Type.LONG, this.simplePop(v-3));
                /*SL:452*/this.simplePush(Type.DOUBLE, v-3);
                /*SL:453*/break;
            }
            case 139: {
                /*SL:455*/this.verifyAssignable(Type.FLOAT, this.simplePop(v-3));
                /*SL:456*/this.simplePush(Type.INTEGER, v-3);
                /*SL:457*/break;
            }
            case 140: {
                /*SL:459*/this.verifyAssignable(Type.FLOAT, this.simplePop(v-3));
                /*SL:460*/this.simplePush(Type.LONG, v-3);
                /*SL:461*/break;
            }
            case 141: {
                /*SL:463*/this.verifyAssignable(Type.FLOAT, this.simplePop(v-3));
                /*SL:464*/this.simplePush(Type.DOUBLE, v-3);
                /*SL:465*/break;
            }
            case 142: {
                /*SL:467*/this.verifyAssignable(Type.DOUBLE, this.simplePop(v-3));
                /*SL:468*/this.simplePush(Type.INTEGER, v-3);
                /*SL:469*/break;
            }
            case 143: {
                /*SL:471*/this.verifyAssignable(Type.DOUBLE, this.simplePop(v-3));
                /*SL:472*/this.simplePush(Type.LONG, v-3);
                /*SL:473*/break;
            }
            case 144: {
                /*SL:475*/this.verifyAssignable(Type.DOUBLE, this.simplePop(v-3));
                /*SL:476*/this.simplePush(Type.FLOAT, v-3);
                /*SL:477*/break;
            }
            case 145:
            case 146:
            case 147: {
                /*SL:481*/this.verifyAssignable(Type.INTEGER, v-3.peek());
                /*SL:482*/break;
            }
            case 148: {
                /*SL:484*/this.verifyAssignable(Type.LONG, this.simplePop(v-3));
                /*SL:485*/this.verifyAssignable(Type.LONG, this.simplePop(v-3));
                /*SL:486*/v-3.push(Type.INTEGER);
                /*SL:487*/break;
            }
            case 149:
            case 150: {
                /*SL:490*/this.verifyAssignable(Type.FLOAT, this.simplePop(v-3));
                /*SL:491*/this.verifyAssignable(Type.FLOAT, this.simplePop(v-3));
                /*SL:492*/v-3.push(Type.INTEGER);
                /*SL:493*/break;
            }
            case 151:
            case 152: {
                /*SL:496*/this.verifyAssignable(Type.DOUBLE, this.simplePop(v-3));
                /*SL:497*/this.verifyAssignable(Type.DOUBLE, this.simplePop(v-3));
                /*SL:498*/v-3.push(Type.INTEGER);
                /*SL:499*/break;
            }
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158: {
                /*SL:508*/this.verifyAssignable(Type.INTEGER, this.simplePop(v-3));
                /*SL:509*/break;
            }
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164: {
                /*SL:516*/this.verifyAssignable(Type.INTEGER, this.simplePop(v-3));
                /*SL:517*/this.verifyAssignable(Type.INTEGER, this.simplePop(v-3));
                /*SL:518*/break;
            }
            case 165:
            case 166: {
                /*SL:521*/this.verifyAssignable(Type.OBJECT, this.simplePop(v-3));
                /*SL:522*/this.verifyAssignable(Type.OBJECT, this.simplePop(v-3));
            }
            case 168: {
                /*SL:527*/v-3.push(Type.RETURN_ADDRESS);
                /*SL:528*/break;
            }
            case 169: {
                /*SL:530*/this.verifyAssignable(Type.RETURN_ADDRESS, v-3.getLocal(v-4.byteAt(v-5 + 1)));
                /*SL:531*/break;
            }
            case 170:
            case 171:
            case 172: {
                /*SL:535*/this.verifyAssignable(Type.INTEGER, this.simplePop(v-3));
                /*SL:536*/break;
            }
            case 173: {
                /*SL:538*/this.verifyAssignable(Type.LONG, this.simplePop(v-3));
                /*SL:539*/break;
            }
            case 174: {
                /*SL:541*/this.verifyAssignable(Type.FLOAT, this.simplePop(v-3));
                /*SL:542*/break;
            }
            case 175: {
                /*SL:544*/this.verifyAssignable(Type.DOUBLE, this.simplePop(v-3));
                /*SL:545*/break;
            }
            case 176: {
                try {
                    final CtClass v7 = /*EL:548*/Descriptor.getReturnType(v-6.getDescriptor(), this.classPool);
                    /*SL:549*/this.verifyAssignable(Type.get(v7), this.simplePop(v-3));
                }
                catch (NotFoundException v8) {
                    /*SL:551*/throw new RuntimeException(v8);
                }
            }
            case 178: {
                /*SL:557*/this.evalGetField(byte1, v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:558*/break;
            }
            case 179: {
                /*SL:560*/this.evalPutField(byte1, v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:561*/break;
            }
            case 180: {
                /*SL:563*/this.evalGetField(byte1, v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:564*/break;
            }
            case 181: {
                /*SL:566*/this.evalPutField(byte1, v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:567*/break;
            }
            case 182:
            case 183:
            case 184: {
                /*SL:571*/this.evalInvokeMethod(byte1, v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:572*/break;
            }
            case 185: {
                /*SL:574*/this.evalInvokeIntfMethod(byte1, v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:575*/break;
            }
            case 186: {
                /*SL:577*/this.evalInvokeDynamic(byte1, v-4.u16bitAt(v-5 + 1), v-3);
                /*SL:578*/break;
            }
            case 187: {
                /*SL:580*/v-3.push(this.resolveClassInfo(this.constPool.getClassInfo(v-4.u16bitAt(v-5 + 1))));
                /*SL:581*/break;
            }
            case 188: {
                /*SL:583*/this.evalNewArray(v-5, v-4, v-3);
                /*SL:584*/break;
            }
            case 189: {
                /*SL:586*/this.evalNewObjectArray(v-5, v-4, v-3);
                /*SL:587*/break;
            }
            case 190: {
                final Type v4 = /*EL:589*/this.simplePop(v-3);
                /*SL:590*/if (!v4.isArray() && v4 != Type.UNINIT) {
                    /*SL:591*/throw new BadBytecode("Array length passed a non-array [pos = " + v-5 + "]: " + v4);
                }
                /*SL:592*/v-3.push(Type.INTEGER);
                /*SL:593*/break;
            }
            case 191: {
                /*SL:596*/this.verifyAssignable(this.THROWABLE_TYPE, this.simplePop(v-3));
                /*SL:597*/break;
            }
            case 192: {
                /*SL:599*/this.verifyAssignable(Type.OBJECT, this.simplePop(v-3));
                /*SL:600*/v-3.push(this.typeFromDesc(this.constPool.getClassInfoByDescriptor(v-4.u16bitAt(v-5 + 1))));
                /*SL:601*/break;
            }
            case 193: {
                /*SL:603*/this.verifyAssignable(Type.OBJECT, this.simplePop(v-3));
                /*SL:604*/v-3.push(Type.INTEGER);
                /*SL:605*/break;
            }
            case 194:
            case 195: {
                /*SL:608*/this.verifyAssignable(Type.OBJECT, this.simplePop(v-3));
                /*SL:609*/break;
            }
            case 196: {
                /*SL:611*/this.evalWide(v-5, v-4, v-3, v-2);
                /*SL:612*/break;
            }
            case 197: {
                /*SL:614*/this.evalNewObjectArray(v-5, v-4, v-3);
                /*SL:615*/break;
            }
            case 198:
            case 199: {
                /*SL:618*/this.verifyAssignable(Type.OBJECT, this.simplePop(v-3));
            }
            case 201: {
                /*SL:623*/v-3.push(Type.RETURN_ADDRESS);
                break;
            }
        }
    }
    
    private Type zeroExtend(final Type a1) {
        /*SL:629*/if (a1 == Type.SHORT || a1 == Type.BYTE || a1 == Type.CHAR || a1 == Type.BOOLEAN) {
            /*SL:630*/return Type.INTEGER;
        }
        /*SL:632*/return a1;
    }
    
    private void evalArrayLoad(final Type a1, final Frame a2) throws BadBytecode {
        final Type v1 = /*EL:636*/a2.pop();
        final Type v2 = /*EL:637*/a2.pop();
        /*SL:641*/if (v2 == Type.UNINIT) {
            /*SL:642*/this.verifyAssignable(Type.INTEGER, v1);
            /*SL:643*/if (a1 == Type.OBJECT) {
                /*SL:644*/this.simplePush(Type.UNINIT, a2);
            }
            else {
                /*SL:646*/this.simplePush(a1, a2);
            }
            /*SL:648*/return;
        }
        Type v3 = /*EL:651*/v2.getComponent();
        /*SL:653*/if (v3 == null) {
            /*SL:654*/throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + v3);
        }
        /*SL:656*/v3 = this.zeroExtend(v3);
        /*SL:658*/this.verifyAssignable(a1, v3);
        /*SL:659*/this.verifyAssignable(Type.INTEGER, v1);
        /*SL:660*/this.simplePush(v3, a2);
    }
    
    private void evalArrayStore(final Type a1, final Frame a2) throws BadBytecode {
        final Type v1 = /*EL:664*/this.simplePop(a2);
        final Type v2 = /*EL:665*/a2.pop();
        final Type v3 = /*EL:666*/a2.pop();
        /*SL:668*/if (v3 == Type.UNINIT) {
            /*SL:669*/this.verifyAssignable(Type.INTEGER, v2);
            /*SL:670*/return;
        }
        Type v4 = /*EL:673*/v3.getComponent();
        /*SL:675*/if (v4 == null) {
            /*SL:676*/throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + v4);
        }
        /*SL:678*/v4 = this.zeroExtend(v4);
        /*SL:680*/this.verifyAssignable(a1, v4);
        /*SL:681*/this.verifyAssignable(Type.INTEGER, v2);
        /*SL:689*/if (a1 == Type.OBJECT) {
            /*SL:690*/this.verifyAssignable(a1, v1);
        }
        else {
            /*SL:692*/this.verifyAssignable(v4, v1);
        }
    }
    
    private void evalBinaryMath(final Type a1, final Frame a2) throws BadBytecode {
        final Type v1 = /*EL:697*/this.simplePop(a2);
        final Type v2 = /*EL:698*/this.simplePop(a2);
        /*SL:700*/this.verifyAssignable(a1, v1);
        /*SL:701*/this.verifyAssignable(a1, v2);
        /*SL:702*/this.simplePush(v2, a2);
    }
    
    private void evalGetField(final int a3, final int v1, final Frame v2) throws BadBytecode {
        final String v3 = /*EL:706*/this.constPool.getFieldrefType(v1);
        final Type v4 = /*EL:707*/this.zeroExtend(this.typeFromDesc(v3));
        /*SL:709*/if (a3 == 180) {
            final Type a4 = /*EL:710*/this.resolveClassInfo(this.constPool.getFieldrefClassName(v1));
            /*SL:711*/this.verifyAssignable(a4, this.simplePop(v2));
        }
        /*SL:714*/this.simplePush(v4, v2);
    }
    
    private void evalInvokeIntfMethod(final int a1, final int a2, final Frame a3) throws BadBytecode {
        final String v1 = /*EL:718*/this.constPool.getInterfaceMethodrefType(a2);
        final Type[] v2 = /*EL:719*/this.paramTypesFromDesc(v1);
        int v3 = /*EL:720*/v2.length;
        /*SL:722*/while (v3 > 0) {
            /*SL:723*/this.verifyAssignable(this.zeroExtend(v2[--v3]), this.simplePop(a3));
        }
        final String v4 = /*EL:725*/this.constPool.getInterfaceMethodrefClassName(a2);
        final Type v5 = /*EL:726*/this.resolveClassInfo(v4);
        /*SL:727*/this.verifyAssignable(v5, this.simplePop(a3));
        final Type v6 = /*EL:729*/this.returnTypeFromDesc(v1);
        /*SL:730*/if (v6 != Type.VOID) {
            /*SL:731*/this.simplePush(this.zeroExtend(v6), a3);
        }
    }
    
    private void evalInvokeMethod(final int a3, final int v1, final Frame v2) throws BadBytecode {
        final String v3 = /*EL:735*/this.constPool.getMethodrefType(v1);
        final Type[] v4 = /*EL:736*/this.paramTypesFromDesc(v3);
        int v5 = /*EL:737*/v4.length;
        /*SL:739*/while (v5 > 0) {
            /*SL:740*/this.verifyAssignable(this.zeroExtend(v4[--v5]), this.simplePop(v2));
        }
        /*SL:742*/if (a3 != 184) {
            final Type a4 = /*EL:743*/this.resolveClassInfo(this.constPool.getMethodrefClassName(v1));
            /*SL:744*/this.verifyAssignable(a4, this.simplePop(v2));
        }
        final Type v6 = /*EL:747*/this.returnTypeFromDesc(v3);
        /*SL:748*/if (v6 != Type.VOID) {
            /*SL:749*/this.simplePush(this.zeroExtend(v6), v2);
        }
    }
    
    private void evalInvokeDynamic(final int a1, final int a2, final Frame a3) throws BadBytecode {
        final String v1 = /*EL:753*/this.constPool.getInvokeDynamicType(a2);
        final Type[] v2 = /*EL:754*/this.paramTypesFromDesc(v1);
        int v3 = /*EL:755*/v2.length;
        /*SL:757*/while (v3 > 0) {
            /*SL:758*/this.verifyAssignable(this.zeroExtend(v2[--v3]), this.simplePop(a3));
        }
        final Type v4 = /*EL:762*/this.returnTypeFromDesc(v1);
        /*SL:763*/if (v4 != Type.VOID) {
            /*SL:764*/this.simplePush(this.zeroExtend(v4), a3);
        }
    }
    
    private void evalLDC(final int v-2, final Frame v-1) throws BadBytecode {
        final int v0 = /*EL:768*/this.constPool.getTag(v-2);
        Type v = null;
        /*SL:770*/switch (v0) {
            case 8: {
                final Type a1 = /*EL:772*/this.STRING_TYPE;
                /*SL:773*/break;
            }
            case 3: {
                final Type a2 = Type.INTEGER;
                /*SL:776*/break;
            }
            case 4: {
                /*SL:778*/v = Type.FLOAT;
                /*SL:779*/break;
            }
            case 5: {
                /*SL:781*/v = Type.LONG;
                /*SL:782*/break;
            }
            case 6: {
                /*SL:784*/v = Type.DOUBLE;
                /*SL:785*/break;
            }
            case 7: {
                /*SL:787*/v = this.CLASS_TYPE;
                /*SL:788*/break;
            }
            default: {
                /*SL:790*/throw new BadBytecode("bad LDC [pos = " + this.lastPos + "]: " + v0);
            }
        }
        /*SL:793*/this.simplePush(v, v-1);
    }
    
    private void evalLoad(final Type a1, final int a2, final Frame a3, final Subroutine a4) throws BadBytecode {
        final Type v1 = /*EL:797*/a3.getLocal(a2);
        /*SL:799*/this.verifyAssignable(a1, v1);
        /*SL:801*/this.simplePush(v1, a3);
        /*SL:802*/this.access(a2, v1, a4);
    }
    
    private void evalNewArray(final int a1, final CodeIterator a2, final Frame a3) throws BadBytecode {
        /*SL:806*/this.verifyAssignable(Type.INTEGER, this.simplePop(a3));
        Type v1 = /*EL:807*/null;
        final int v2 = /*EL:808*/a2.byteAt(a1 + 1);
        /*SL:809*/switch (v2) {
            case 4: {
                /*SL:811*/v1 = this.getType("boolean[]");
                /*SL:812*/break;
            }
            case 5: {
                /*SL:814*/v1 = this.getType("char[]");
                /*SL:815*/break;
            }
            case 8: {
                /*SL:817*/v1 = this.getType("byte[]");
                /*SL:818*/break;
            }
            case 9: {
                /*SL:820*/v1 = this.getType("short[]");
                /*SL:821*/break;
            }
            case 10: {
                /*SL:823*/v1 = this.getType("int[]");
                /*SL:824*/break;
            }
            case 11: {
                /*SL:826*/v1 = this.getType("long[]");
                /*SL:827*/break;
            }
            case 6: {
                /*SL:829*/v1 = this.getType("float[]");
                /*SL:830*/break;
            }
            case 7: {
                /*SL:832*/v1 = this.getType("double[]");
                /*SL:833*/break;
            }
            default: {
                /*SL:835*/throw new BadBytecode("Invalid array type [pos = " + a1 + "]: " + v2);
            }
        }
        /*SL:839*/a3.push(v1);
    }
    
    private void evalNewObjectArray(final int a3, final CodeIterator v1, final Frame v2) throws BadBytecode {
        final Type v3 = /*EL:844*/this.resolveClassInfo(this.constPool.getClassInfo(v1.u16bitAt(a3 + 1)));
        String v4 = /*EL:845*/v3.getCtClass().getName();
        final int v5 = /*EL:846*/v1.byteAt(a3);
        int v6 = 0;
        /*SL:849*/if (v5 == 197) {
            final int a4 = /*EL:850*/v1.byteAt(a3 + 3);
        }
        else {
            /*SL:852*/v4 += "[]";
            /*SL:853*/v6 = 1;
        }
        /*SL:856*/while (v6-- > 0) {
            /*SL:857*/this.verifyAssignable(Type.INTEGER, this.simplePop(v2));
        }
        /*SL:860*/this.simplePush(this.getType(v4), v2);
    }
    
    private void evalPutField(final int a3, final int v1, final Frame v2) throws BadBytecode {
        final String v3 = /*EL:864*/this.constPool.getFieldrefType(v1);
        final Type v4 = /*EL:865*/this.zeroExtend(this.typeFromDesc(v3));
        /*SL:867*/this.verifyAssignable(v4, this.simplePop(v2));
        /*SL:869*/if (a3 == 181) {
            final Type a4 = /*EL:870*/this.resolveClassInfo(this.constPool.getFieldrefClassName(v1));
            /*SL:871*/this.verifyAssignable(a4, this.simplePop(v2));
        }
    }
    
    private void evalShift(final Type a1, final Frame a2) throws BadBytecode {
        final Type v1 = /*EL:876*/this.simplePop(a2);
        final Type v2 = /*EL:877*/this.simplePop(a2);
        /*SL:879*/this.verifyAssignable(Type.INTEGER, v1);
        /*SL:880*/this.verifyAssignable(a1, v2);
        /*SL:881*/this.simplePush(v2, a2);
    }
    
    private void evalStore(final Type a1, final int a2, final Frame a3, final Subroutine a4) throws BadBytecode {
        final Type v1 = /*EL:885*/this.simplePop(a3);
        /*SL:888*/if (a1 != Type.OBJECT || v1 != Type.RETURN_ADDRESS) {
            /*SL:889*/this.verifyAssignable(a1, v1);
        }
        /*SL:890*/this.simpleSetLocal(a2, v1, a3);
        /*SL:891*/this.access(a2, v1, a4);
    }
    
    private void evalWide(final int a1, final CodeIterator a2, final Frame a3, final Subroutine a4) throws BadBytecode {
        final int v1 = /*EL:895*/a2.byteAt(a1 + 1);
        final int v2 = /*EL:896*/a2.u16bitAt(a1 + 2);
        /*SL:897*/switch (v1) {
            case 21: {
                /*SL:899*/this.evalLoad(Type.INTEGER, v2, a3, a4);
                /*SL:900*/break;
            }
            case 22: {
                /*SL:902*/this.evalLoad(Type.LONG, v2, a3, a4);
                /*SL:903*/break;
            }
            case 23: {
                /*SL:905*/this.evalLoad(Type.FLOAT, v2, a3, a4);
                /*SL:906*/break;
            }
            case 24: {
                /*SL:908*/this.evalLoad(Type.DOUBLE, v2, a3, a4);
                /*SL:909*/break;
            }
            case 25: {
                /*SL:911*/this.evalLoad(Type.OBJECT, v2, a3, a4);
                /*SL:912*/break;
            }
            case 54: {
                /*SL:914*/this.evalStore(Type.INTEGER, v2, a3, a4);
                /*SL:915*/break;
            }
            case 55: {
                /*SL:917*/this.evalStore(Type.LONG, v2, a3, a4);
                /*SL:918*/break;
            }
            case 56: {
                /*SL:920*/this.evalStore(Type.FLOAT, v2, a3, a4);
                /*SL:921*/break;
            }
            case 57: {
                /*SL:923*/this.evalStore(Type.DOUBLE, v2, a3, a4);
                /*SL:924*/break;
            }
            case 58: {
                /*SL:926*/this.evalStore(Type.OBJECT, v2, a3, a4);
                /*SL:927*/break;
            }
            case 132: {
                /*SL:929*/this.verifyAssignable(Type.INTEGER, a3.getLocal(v2));
                /*SL:930*/break;
            }
            case 169: {
                /*SL:932*/this.verifyAssignable(Type.RETURN_ADDRESS, a3.getLocal(v2));
                /*SL:933*/break;
            }
            default: {
                /*SL:935*/throw new BadBytecode("Invalid WIDE operand [pos = " + a1 + "]: " + v1);
            }
        }
    }
    
    private Type getType(final String v2) throws BadBytecode {
        try {
            /*SL:942*/return Type.get(this.classPool.get(v2));
        }
        catch (NotFoundException a1) {
            /*SL:944*/throw new BadBytecode("Could not find class [pos = " + this.lastPos + "]: " + v2);
        }
    }
    
    private Type[] paramTypesFromDesc(final String v-2) throws BadBytecode {
        CtClass[] parameterTypes = /*EL:949*/null;
        try {
            /*SL:951*/parameterTypes = Descriptor.getParameterTypes(v-2, this.classPool);
        }
        catch (NotFoundException a1) {
            /*SL:953*/throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + a1.getMessage());
        }
        /*SL:956*/if (parameterTypes == null) {
            /*SL:957*/throw new BadBytecode("Could not obtain parameters for descriptor [pos = " + this.lastPos + "]: " + v-2);
        }
        final Type[] v0 = /*EL:959*/new Type[parameterTypes.length];
        /*SL:960*/for (int v = 0; v < v0.length; ++v) {
            /*SL:961*/v0[v] = Type.get(parameterTypes[v]);
        }
        /*SL:963*/return v0;
    }
    
    private Type returnTypeFromDesc(final String v2) throws BadBytecode {
        CtClass v3 = /*EL:967*/null;
        try {
            /*SL:969*/v3 = Descriptor.getReturnType(v2, this.classPool);
        }
        catch (NotFoundException a1) {
            /*SL:971*/throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + a1.getMessage());
        }
        /*SL:974*/if (v3 == null) {
            /*SL:975*/throw new BadBytecode("Could not obtain return type for descriptor [pos = " + this.lastPos + "]: " + v2);
        }
        /*SL:977*/return Type.get(v3);
    }
    
    private Type simplePeek(final Frame a1) {
        final Type v1 = /*EL:981*/a1.peek();
        /*SL:982*/return (v1 == Type.TOP) ? a1.getStack(a1.getTopIndex() - 1) : v1;
    }
    
    private Type simplePop(final Frame a1) {
        final Type v1 = /*EL:986*/a1.pop();
        /*SL:987*/return (v1 == Type.TOP) ? a1.pop() : v1;
    }
    
    private void simplePush(final Type a1, final Frame a2) {
        /*SL:991*/a2.push(a1);
        /*SL:992*/if (a1.getSize() == 2) {
            /*SL:993*/a2.push(Type.TOP);
        }
    }
    
    private void access(final int a1, final Type a2, final Subroutine a3) {
        /*SL:997*/if (a3 == null) {
            /*SL:998*/return;
        }
        /*SL:999*/a3.access(a1);
        /*SL:1000*/if (a2.getSize() == 2) {
            /*SL:1001*/a3.access(a1 + 1);
        }
    }
    
    private void simpleSetLocal(final int a1, final Type a2, final Frame a3) {
        /*SL:1005*/a3.setLocal(a1, a2);
        /*SL:1006*/if (a2.getSize() == 2) {
            /*SL:1007*/a3.setLocal(a1 + 1, Type.TOP);
        }
    }
    
    private Type resolveClassInfo(final String v2) throws BadBytecode {
        CtClass v3 = /*EL:1011*/null;
        try {
            /*SL:1013*/if (v2.charAt(0) == '[') {
                /*SL:1014*/v3 = Descriptor.toCtClass(v2, this.classPool);
            }
            else {
                /*SL:1016*/v3 = this.classPool.get(v2);
            }
        }
        catch (NotFoundException a1) {
            /*SL:1020*/throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + a1.getMessage());
        }
        /*SL:1023*/if (v3 == null) {
            /*SL:1024*/throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + v2);
        }
        /*SL:1026*/return Type.get(v3);
    }
    
    private Type typeFromDesc(final String v2) throws BadBytecode {
        CtClass v3 = /*EL:1030*/null;
        try {
            /*SL:1032*/v3 = Descriptor.toCtClass(v2, this.classPool);
        }
        catch (NotFoundException a1) {
            /*SL:1034*/throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + a1.getMessage());
        }
        /*SL:1037*/if (v3 == null) {
            /*SL:1038*/throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + v2);
        }
        /*SL:1040*/return Type.get(v3);
    }
    
    private void verifyAssignable(final Type a1, final Type a2) throws BadBytecode {
        /*SL:1044*/if (!a1.isAssignableFrom(a2)) {
            /*SL:1045*/throw new BadBytecode("Expected type: " + a1 + " Got: " + a2 + " [pos = " + this.lastPos + "]");
        }
    }
}
