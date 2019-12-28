package javassist.bytecode.stackmap;

import javassist.bytecode.Descriptor;
import javassist.bytecode.Opcode;
import javassist.bytecode.ByteArray;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.ClassPool;

public abstract class Tracer implements TypeTag
{
    protected ClassPool classPool;
    protected ConstPool cpool;
    protected String returnType;
    protected int stackTop;
    protected TypeData[] stackTypes;
    protected TypeData[] localsTypes;
    
    public Tracer(final ClassPool a1, final ConstPool a2, final int a3, final int a4, final String a5) {
        this.classPool = a1;
        this.cpool = a2;
        this.returnType = a5;
        this.stackTop = 0;
        this.stackTypes = TypeData.make(a3);
        this.localsTypes = TypeData.make(a4);
    }
    
    public Tracer(final Tracer a1) {
        this.classPool = a1.classPool;
        this.cpool = a1.cpool;
        this.returnType = a1.returnType;
        this.stackTop = a1.stackTop;
        this.stackTypes = TypeData.make(a1.stackTypes.length);
        this.localsTypes = TypeData.make(a1.localsTypes.length);
    }
    
    protected int doOpcode(final int v2, final byte[] v3) throws BadBytecode {
        try {
            final int a1 = /*EL:71*/v3[v2] & 0xFF;
            /*SL:72*/if (a1 < 96) {
                /*SL:73*/if (a1 < 54) {
                    /*SL:74*/return this.doOpcode0_53(v2, v3, a1);
                }
                /*SL:76*/return this.doOpcode54_95(v2, v3, a1);
            }
            else {
                /*SL:78*/if (a1 < 148) {
                    /*SL:79*/return this.doOpcode96_147(v2, v3, a1);
                }
                /*SL:81*/return this.doOpcode148_201(v2, v3, a1);
            }
        }
        catch (ArrayIndexOutOfBoundsException a2) {
            /*SL:84*/throw new BadBytecode("inconsistent stack height " + a2.getMessage(), a2);
        }
    }
    
    protected void visitBranch(final int a1, final byte[] a2, final int a3) throws BadBytecode {
    }
    
    protected void visitGoto(final int a1, final byte[] a2, final int a3) throws BadBytecode {
    }
    
    protected void visitReturn(final int a1, final byte[] a2) throws BadBytecode {
    }
    
    protected void visitThrow(final int a1, final byte[] a2) throws BadBytecode {
    }
    
    protected void visitTableSwitch(final int a1, final byte[] a2, final int a3, final int a4, final int a5) throws BadBytecode {
    }
    
    protected void visitLookupSwitch(final int a1, final byte[] a2, final int a3, final int a4, final int a5) throws BadBytecode {
    }
    
    protected void visitJSR(final int a1, final byte[] a2) throws BadBytecode {
    }
    
    protected void visitRET(final int a1, final byte[] a2) throws BadBytecode {
    }
    
    private int doOpcode0_53(final int v2, final byte[] v3, final int v4) throws BadBytecode {
        final TypeData[] v5 = /*EL:134*/this.stackTypes;
        /*SL:135*/switch (v4) {
            case 0: {
                /*SL:137*/break;
            }
            case 1: {
                /*SL:139*/v5[this.stackTop++] = new TypeData.NullType();
                /*SL:140*/break;
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                /*SL:148*/v5[this.stackTop++] = Tracer.INTEGER;
                /*SL:149*/break;
            }
            case 9:
            case 10: {
                /*SL:152*/v5[this.stackTop++] = Tracer.LONG;
                /*SL:153*/v5[this.stackTop++] = Tracer.TOP;
                /*SL:154*/break;
            }
            case 11:
            case 12:
            case 13: {
                /*SL:158*/v5[this.stackTop++] = Tracer.FLOAT;
                /*SL:159*/break;
            }
            case 14:
            case 15: {
                /*SL:162*/v5[this.stackTop++] = Tracer.DOUBLE;
                /*SL:163*/v5[this.stackTop++] = Tracer.TOP;
                /*SL:164*/break;
            }
            case 16:
            case 17: {
                /*SL:167*/v5[this.stackTop++] = Tracer.INTEGER;
                /*SL:168*/return (v4 == 17) ? 3 : 2;
            }
            case 18: {
                /*SL:170*/this.doLDC(v3[v2 + 1] & 0xFF);
                /*SL:171*/return 2;
            }
            case 19:
            case 20: {
                /*SL:174*/this.doLDC(ByteArray.readU16bit(v3, v2 + 1));
                /*SL:175*/return 3;
            }
            case 21: {
                /*SL:177*/return this.doXLOAD(Tracer.INTEGER, v3, v2);
            }
            case 22: {
                /*SL:179*/return this.doXLOAD(Tracer.LONG, v3, v2);
            }
            case 23: {
                /*SL:181*/return this.doXLOAD(Tracer.FLOAT, v3, v2);
            }
            case 24: {
                /*SL:183*/return this.doXLOAD(Tracer.DOUBLE, v3, v2);
            }
            case 25: {
                /*SL:185*/return this.doALOAD(v3[v2 + 1] & 0xFF);
            }
            case 26:
            case 27:
            case 28:
            case 29: {
                /*SL:190*/v5[this.stackTop++] = Tracer.INTEGER;
                /*SL:191*/break;
            }
            case 30:
            case 31:
            case 32:
            case 33: {
                /*SL:196*/v5[this.stackTop++] = Tracer.LONG;
                /*SL:197*/v5[this.stackTop++] = Tracer.TOP;
                /*SL:198*/break;
            }
            case 34:
            case 35:
            case 36:
            case 37: {
                /*SL:203*/v5[this.stackTop++] = Tracer.FLOAT;
                /*SL:204*/break;
            }
            case 38:
            case 39:
            case 40:
            case 41: {
                /*SL:209*/v5[this.stackTop++] = Tracer.DOUBLE;
                /*SL:210*/v5[this.stackTop++] = Tracer.TOP;
                /*SL:211*/break;
            }
            case 42:
            case 43:
            case 44:
            case 45: {
                final int a1 = /*EL:216*/v4 - 42;
                /*SL:217*/v5[this.stackTop++] = this.localsTypes[a1];
                /*SL:218*/break;
            }
            case 46: {
                final TypeData[] array = /*EL:220*/v5;
                final int stackTop = this.stackTop - 1;
                this.stackTop = stackTop;
                array[stackTop - 1] = Tracer.INTEGER;
                /*SL:221*/break;
            }
            case 47: {
                /*SL:223*/v5[this.stackTop - 2] = Tracer.LONG;
                /*SL:224*/v5[this.stackTop - 1] = Tracer.TOP;
                /*SL:225*/break;
            }
            case 48: {
                final TypeData[] array2 = /*EL:227*/v5;
                final int stackTop2 = this.stackTop - 1;
                this.stackTop = stackTop2;
                array2[stackTop2 - 1] = Tracer.FLOAT;
                /*SL:228*/break;
            }
            case 49: {
                /*SL:230*/v5[this.stackTop - 2] = Tracer.DOUBLE;
                /*SL:231*/v5[this.stackTop - 1] = Tracer.TOP;
                /*SL:232*/break;
            }
            case 50: {
                final int stackTop3 = /*EL:234*/this.stackTop - 1;
                this.stackTop = stackTop3;
                final int a2 = stackTop3 - 1;
                final TypeData a3 = /*EL:235*/v5[a2];
                /*SL:236*/v5[a2] = TypeData.ArrayElement.make(a3);
                /*SL:237*/break;
            }
            case 51:
            case 52:
            case 53: {
                final TypeData[] array3 = /*EL:241*/v5;
                final int stackTop4 = this.stackTop - 1;
                this.stackTop = stackTop4;
                array3[stackTop4 - 1] = Tracer.INTEGER;
                /*SL:242*/break;
            }
            default: {
                /*SL:244*/throw new RuntimeException("fatal");
            }
        }
        /*SL:247*/return 1;
    }
    
    private void doLDC(final int a1) {
        final TypeData[] v1 = /*EL:251*/this.stackTypes;
        final int v2 = /*EL:252*/this.cpool.getTag(a1);
        /*SL:253*/if (v2 == 8) {
            /*SL:254*/v1[this.stackTop++] = new TypeData.ClassName("java.lang.String");
        }
        else/*SL:255*/ if (v2 == 3) {
            /*SL:256*/v1[this.stackTop++] = Tracer.INTEGER;
        }
        else/*SL:257*/ if (v2 == 4) {
            /*SL:258*/v1[this.stackTop++] = Tracer.FLOAT;
        }
        else/*SL:259*/ if (v2 == 5) {
            /*SL:260*/v1[this.stackTop++] = Tracer.LONG;
            /*SL:261*/v1[this.stackTop++] = Tracer.TOP;
        }
        else/*SL:263*/ if (v2 == 6) {
            /*SL:264*/v1[this.stackTop++] = Tracer.DOUBLE;
            /*SL:265*/v1[this.stackTop++] = Tracer.TOP;
        }
        else {
            /*SL:267*/if (v2 != 7) {
                /*SL:270*/throw new RuntimeException("bad LDC: " + v2);
            }
            v1[this.stackTop++] = new TypeData.ClassName("java.lang.Class");
        }
    }
    
    private int doXLOAD(final TypeData a1, final byte[] a2, final int a3) {
        final int v1 = /*EL:274*/a2[a3 + 1] & 0xFF;
        /*SL:275*/return this.doXLOAD(v1, a1);
    }
    
    private int doXLOAD(final int a1, final TypeData a2) {
        /*SL:279*/this.stackTypes[this.stackTop++] = a2;
        /*SL:280*/if (a2.is2WordType()) {
            /*SL:281*/this.stackTypes[this.stackTop++] = Tracer.TOP;
        }
        /*SL:283*/return 2;
    }
    
    private int doALOAD(final int a1) {
        /*SL:287*/this.stackTypes[this.stackTop++] = this.localsTypes[a1];
        /*SL:288*/return 2;
    }
    
    private int doOpcode54_95(final int v-2, final byte[] v-1, final int v0) throws BadBytecode {
        /*SL:292*/switch (v0) {
            case 54: {
                /*SL:294*/return this.doXSTORE(v-2, v-1, Tracer.INTEGER);
            }
            case 55: {
                /*SL:296*/return this.doXSTORE(v-2, v-1, Tracer.LONG);
            }
            case 56: {
                /*SL:298*/return this.doXSTORE(v-2, v-1, Tracer.FLOAT);
            }
            case 57: {
                /*SL:300*/return this.doXSTORE(v-2, v-1, Tracer.DOUBLE);
            }
            case 58: {
                /*SL:302*/return this.doASTORE(v-1[v-2 + 1] & 0xFF);
            }
            case 59:
            case 60:
            case 61:
            case 62: {
                final int a1 = /*EL:307*/v0 - 59;
                /*SL:308*/this.localsTypes[a1] = Tracer.INTEGER;
                /*SL:309*/--this.stackTop;
                /*SL:310*/break;
            }
            case 63:
            case 64:
            case 65:
            case 66: {
                final int a2 = /*EL:315*/v0 - 63;
                /*SL:316*/this.localsTypes[a2] = Tracer.LONG;
                /*SL:317*/this.localsTypes[a2 + 1] = Tracer.TOP;
                /*SL:318*/this.stackTop -= 2;
                /*SL:319*/break;
            }
            case 67:
            case 68:
            case 69:
            case 70: {
                final int a3 = /*EL:324*/v0 - 67;
                /*SL:325*/this.localsTypes[a3] = Tracer.FLOAT;
                /*SL:326*/--this.stackTop;
                /*SL:327*/break;
            }
            case 71:
            case 72:
            case 73:
            case 74: {
                final int v = /*EL:332*/v0 - 71;
                /*SL:333*/this.localsTypes[v] = Tracer.DOUBLE;
                /*SL:334*/this.localsTypes[v + 1] = Tracer.TOP;
                /*SL:335*/this.stackTop -= 2;
                /*SL:336*/break;
            }
            case 75:
            case 76:
            case 77:
            case 78: {
                final int v = /*EL:341*/v0 - 75;
                /*SL:342*/this.doASTORE(v);
                /*SL:343*/break;
            }
            case 79:
            case 80:
            case 81:
            case 82: {
                /*SL:348*/this.stackTop -= ((v0 == 80 || v0 == 82) ? 4 : 3);
                /*SL:349*/break;
            }
            case 83: {
                /*SL:351*/TypeData.aastore(this.stackTypes[this.stackTop - 3], this.stackTypes[this.stackTop - 1], this.classPool);
                /*SL:354*/this.stackTop -= 3;
                /*SL:355*/break;
            }
            case 84:
            case 85:
            case 86: {
                /*SL:359*/this.stackTop -= 3;
                /*SL:360*/break;
            }
            case 87: {
                /*SL:362*/--this.stackTop;
                /*SL:363*/break;
            }
            case 88: {
                /*SL:365*/this.stackTop -= 2;
                /*SL:366*/break;
            }
            case 89: {
                final int v = /*EL:368*/this.stackTop;
                /*SL:369*/this.stackTypes[v] = this.stackTypes[v - 1];
                /*SL:370*/this.stackTop = v + 1;
                /*SL:371*/break;
            }
            case 90:
            case 91: {
                final int v = /*EL:374*/v0 - 90 + 2;
                /*SL:375*/this.doDUP_XX(1, v);
                final int v2 = /*EL:376*/this.stackTop;
                /*SL:377*/this.stackTypes[v2 - v] = this.stackTypes[v2];
                /*SL:378*/this.stackTop = v2 + 1;
                /*SL:379*/break;
            }
            case 92: {
                /*SL:381*/this.doDUP_XX(2, 2);
                /*SL:382*/this.stackTop += 2;
                /*SL:383*/break;
            }
            case 93:
            case 94: {
                final int v = /*EL:386*/v0 - 93 + 3;
                /*SL:387*/this.doDUP_XX(2, v);
                final int v2 = /*EL:388*/this.stackTop;
                /*SL:389*/this.stackTypes[v2 - v] = this.stackTypes[v2];
                /*SL:390*/this.stackTypes[v2 - v + 1] = this.stackTypes[v2 + 1];
                /*SL:391*/this.stackTop = v2 + 2;
                /*SL:392*/break;
            }
            case 95: {
                final int v = /*EL:394*/this.stackTop - 1;
                final TypeData v3 = /*EL:395*/this.stackTypes[v];
                /*SL:396*/this.stackTypes[v] = this.stackTypes[v - 1];
                /*SL:397*/this.stackTypes[v - 1] = v3;
                /*SL:398*/break;
            }
            default: {
                /*SL:400*/throw new RuntimeException("fatal");
            }
        }
        /*SL:403*/return 1;
    }
    
    private int doXSTORE(final int a1, final byte[] a2, final TypeData a3) {
        final int v1 = /*EL:407*/a2[a1 + 1] & 0xFF;
        /*SL:408*/return this.doXSTORE(v1, a3);
    }
    
    private int doXSTORE(final int a1, final TypeData a2) {
        /*SL:412*/--this.stackTop;
        /*SL:413*/this.localsTypes[a1] = a2;
        /*SL:414*/if (a2.is2WordType()) {
            /*SL:415*/--this.stackTop;
            /*SL:416*/this.localsTypes[a1 + 1] = Tracer.TOP;
        }
        /*SL:419*/return 2;
    }
    
    private int doASTORE(final int a1) {
        /*SL:423*/--this.stackTop;
        /*SL:425*/this.localsTypes[a1] = this.stackTypes[this.stackTop];
        /*SL:426*/return 2;
    }
    
    private void doDUP_XX(final int a1, final int a2) {
        final TypeData[] v1 = /*EL:430*/this.stackTypes;
        /*SL:433*/for (int v2 = this.stackTop - 1, v3 = v2 - a2; v2 > v3; /*SL:435*/--v2) {
            v1[v2 + a1] = v1[v2];
        }
    }
    
    private int doOpcode96_147(final int a1, final byte[] a2, final int a3) {
        /*SL:440*/if (a3 <= 131) {
            /*SL:441*/this.stackTop += Opcode.STACK_GROW[a3];
            /*SL:442*/return 1;
        }
        /*SL:445*/switch (a3) {
            case 132: {
                /*SL:448*/return 3;
            }
            case 133: {
                /*SL:450*/this.stackTypes[this.stackTop - 1] = Tracer.LONG;
                /*SL:451*/this.stackTypes[this.stackTop] = Tracer.TOP;
                /*SL:452*/++this.stackTop;
                /*SL:453*/break;
            }
            case 134: {
                /*SL:455*/this.stackTypes[this.stackTop - 1] = Tracer.FLOAT;
                /*SL:456*/break;
            }
            case 135: {
                /*SL:458*/this.stackTypes[this.stackTop - 1] = Tracer.DOUBLE;
                /*SL:459*/this.stackTypes[this.stackTop] = Tracer.TOP;
                /*SL:460*/++this.stackTop;
                /*SL:461*/break;
            }
            case 136: {
                final TypeData[] stackTypes = /*EL:463*/this.stackTypes;
                final int stackTop = this.stackTop - 1;
                this.stackTop = stackTop;
                stackTypes[stackTop - 1] = Tracer.INTEGER;
                /*SL:464*/break;
            }
            case 137: {
                final TypeData[] stackTypes2 = /*EL:466*/this.stackTypes;
                final int stackTop2 = this.stackTop - 1;
                this.stackTop = stackTop2;
                stackTypes2[stackTop2 - 1] = Tracer.FLOAT;
                /*SL:467*/break;
            }
            case 138: {
                /*SL:469*/this.stackTypes[this.stackTop - 2] = Tracer.DOUBLE;
                /*SL:470*/break;
            }
            case 139: {
                /*SL:472*/this.stackTypes[this.stackTop - 1] = Tracer.INTEGER;
                /*SL:473*/break;
            }
            case 140: {
                /*SL:475*/this.stackTypes[this.stackTop - 1] = Tracer.LONG;
                /*SL:476*/this.stackTypes[this.stackTop] = Tracer.TOP;
                /*SL:477*/++this.stackTop;
                /*SL:478*/break;
            }
            case 141: {
                /*SL:480*/this.stackTypes[this.stackTop - 1] = Tracer.DOUBLE;
                /*SL:481*/this.stackTypes[this.stackTop] = Tracer.TOP;
                /*SL:482*/++this.stackTop;
                /*SL:483*/break;
            }
            case 142: {
                final TypeData[] stackTypes3 = /*EL:485*/this.stackTypes;
                final int stackTop3 = this.stackTop - 1;
                this.stackTop = stackTop3;
                stackTypes3[stackTop3 - 1] = Tracer.INTEGER;
                /*SL:486*/break;
            }
            case 143: {
                /*SL:488*/this.stackTypes[this.stackTop - 2] = Tracer.LONG;
                /*SL:489*/break;
            }
            case 144: {
                final TypeData[] stackTypes4 = /*EL:491*/this.stackTypes;
                final int stackTop4 = this.stackTop - 1;
                this.stackTop = stackTop4;
                stackTypes4[stackTop4 - 1] = Tracer.FLOAT;
                /*SL:492*/break;
            }
            case 145:
            case 146:
            case 147: {
                /*SL:496*/break;
            }
            default: {
                /*SL:498*/throw new RuntimeException("fatal");
            }
        }
        /*SL:501*/return 1;
    }
    
    private int doOpcode148_201(final int v-5, final byte[] v-4, final int v-3) throws BadBytecode {
        /*SL:505*/switch (v-3) {
            case 148: {
                /*SL:507*/this.stackTypes[this.stackTop - 4] = Tracer.INTEGER;
                /*SL:508*/this.stackTop -= 3;
                /*SL:509*/break;
            }
            case 149:
            case 150: {
                final TypeData[] stackTypes = /*EL:512*/this.stackTypes;
                final int stackTop = this.stackTop - 1;
                this.stackTop = stackTop;
                stackTypes[stackTop - 1] = Tracer.INTEGER;
                /*SL:513*/break;
            }
            case 151:
            case 152: {
                /*SL:516*/this.stackTypes[this.stackTop - 4] = Tracer.INTEGER;
                /*SL:517*/this.stackTop -= 3;
                /*SL:518*/break;
            }
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158: {
                /*SL:525*/--this.stackTop;
                /*SL:526*/this.visitBranch(v-5, v-4, ByteArray.readS16bit(v-4, v-5 + 1));
                /*SL:527*/return 3;
            }
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166: {
                /*SL:536*/this.stackTop -= 2;
                /*SL:537*/this.visitBranch(v-5, v-4, ByteArray.readS16bit(v-4, v-5 + 1));
                /*SL:538*/return 3;
            }
            case 167: {
                /*SL:540*/this.visitGoto(v-5, v-4, ByteArray.readS16bit(v-4, v-5 + 1));
                /*SL:541*/return 3;
            }
            case 168: {
                /*SL:543*/this.visitJSR(v-5, v-4);
                /*SL:544*/return 3;
            }
            case 169: {
                /*SL:546*/this.visitRET(v-5, v-4);
                /*SL:547*/return 2;
            }
            case 170: {
                /*SL:549*/--this.stackTop;
                final int a1 = /*EL:550*/(v-5 & 0xFFFFFFFC) + 8;
                final int a2 = /*EL:551*/ByteArray.read32bit(v-4, a1);
                final int a3 = /*EL:552*/ByteArray.read32bit(v-4, a1 + 4);
                final int v1 = /*EL:553*/a3 - a2 + 1;
                /*SL:554*/this.visitTableSwitch(v-5, v-4, v1, a1 + 8, ByteArray.read32bit(v-4, a1 - 4));
                /*SL:555*/return v1 * 4 + 16 - (v-5 & 0x3);
            }
            case 171: {
                /*SL:557*/--this.stackTop;
                final int n = /*EL:558*/(v-5 & 0xFFFFFFFC) + 8;
                final int read32bit = /*EL:559*/ByteArray.read32bit(v-4, n);
                /*SL:560*/this.visitLookupSwitch(v-5, v-4, read32bit, n + 4, ByteArray.read32bit(v-4, n - 4));
                /*SL:561*/return read32bit * 8 + 12 - (v-5 & 0x3);
            }
            case 172: {
                /*SL:563*/--this.stackTop;
                /*SL:564*/this.visitReturn(v-5, v-4);
                /*SL:565*/break;
            }
            case 173: {
                /*SL:567*/this.stackTop -= 2;
                /*SL:568*/this.visitReturn(v-5, v-4);
                /*SL:569*/break;
            }
            case 174: {
                /*SL:571*/--this.stackTop;
                /*SL:572*/this.visitReturn(v-5, v-4);
                /*SL:573*/break;
            }
            case 175: {
                /*SL:575*/this.stackTop -= 2;
                /*SL:576*/this.visitReturn(v-5, v-4);
                /*SL:577*/break;
            }
            case 176: {
                final TypeData[] stackTypes2 = /*EL:579*/this.stackTypes;
                final int stackTop2 = this.stackTop - 1;
                this.stackTop = stackTop2;
                stackTypes2[stackTop2].setType(this.returnType, this.classPool);
                /*SL:580*/this.visitReturn(v-5, v-4);
                /*SL:581*/break;
            }
            case 177: {
                /*SL:583*/this.visitReturn(v-5, v-4);
                /*SL:584*/break;
            }
            case 178: {
                /*SL:586*/return this.doGetField(v-5, v-4, false);
            }
            case 179: {
                /*SL:588*/return this.doPutField(v-5, v-4, false);
            }
            case 180: {
                /*SL:590*/return this.doGetField(v-5, v-4, true);
            }
            case 181: {
                /*SL:592*/return this.doPutField(v-5, v-4, true);
            }
            case 182:
            case 183: {
                /*SL:595*/return this.doInvokeMethod(v-5, v-4, true);
            }
            case 184: {
                /*SL:597*/return this.doInvokeMethod(v-5, v-4, false);
            }
            case 185: {
                /*SL:599*/return this.doInvokeIntfMethod(v-5, v-4);
            }
            case 186: {
                /*SL:601*/return this.doInvokeDynamic(v-5, v-4);
            }
            case 187: {
                final int n = /*EL:603*/ByteArray.readU16bit(v-4, v-5 + 1);
                /*SL:605*/this.stackTypes[this.stackTop++] = new TypeData.UninitData(v-5, this.cpool.getClassInfo(n));
                /*SL:606*/return 3;
            }
            case 188: {
                /*SL:608*/return this.doNEWARRAY(v-5, v-4);
            }
            case 189: {
                final int n = /*EL:610*/ByteArray.readU16bit(v-4, v-5 + 1);
                String s = /*EL:611*/this.cpool.getClassInfo(n).replace('.', '/');
                /*SL:612*/if (s.charAt(0) == '[') {
                    /*SL:613*/s = "[" + s;
                }
                else {
                    /*SL:615*/s = "[L" + s + ";";
                }
                /*SL:617*/this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(s);
                /*SL:619*/return 3;
            }
            case 190: {
                /*SL:621*/this.stackTypes[this.stackTop - 1].setType("[Ljava.lang.Object;", this.classPool);
                /*SL:622*/this.stackTypes[this.stackTop - 1] = Tracer.INTEGER;
                /*SL:623*/break;
            }
            case 191: {
                final TypeData[] stackTypes3 = /*EL:625*/this.stackTypes;
                final int stackTop3 = this.stackTop - 1;
                this.stackTop = stackTop3;
                stackTypes3[stackTop3].setType("java.lang.Throwable", this.classPool);
                /*SL:626*/this.visitThrow(v-5, v-4);
                /*SL:627*/break;
            }
            case 192: {
                final int n = /*EL:630*/ByteArray.readU16bit(v-4, v-5 + 1);
                String s = /*EL:631*/this.cpool.getClassInfo(n);
                /*SL:632*/if (s.charAt(0) == '[') {
                    /*SL:633*/s = s.replace('.', '/');
                }
                /*SL:635*/this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(s);
                /*SL:636*/return 3;
            }
            case 193: {
                /*SL:639*/this.stackTypes[this.stackTop - 1] = Tracer.INTEGER;
                /*SL:640*/return 3;
            }
            case 194:
            case 195: {
                /*SL:643*/--this.stackTop;
                /*SL:645*/break;
            }
            case 196: {
                /*SL:647*/return this.doWIDE(v-5, v-4);
            }
            case 197: {
                /*SL:649*/return this.doMultiANewArray(v-5, v-4);
            }
            case 198:
            case 199: {
                /*SL:652*/--this.stackTop;
                /*SL:653*/this.visitBranch(v-5, v-4, ByteArray.readS16bit(v-4, v-5 + 1));
                /*SL:654*/return 3;
            }
            case 200: {
                /*SL:656*/this.visitGoto(v-5, v-4, ByteArray.read32bit(v-4, v-5 + 1));
                /*SL:657*/return 5;
            }
            case 201: {
                /*SL:659*/this.visitJSR(v-5, v-4);
                /*SL:660*/return 5;
            }
        }
        /*SL:662*/return 1;
    }
    
    private int doWIDE(final int v2, final byte[] v3) throws BadBytecode {
        final int v4 = /*EL:666*/v3[v2 + 1] & 0xFF;
        /*SL:667*/switch (v4) {
            case 21: {
                /*SL:669*/this.doWIDE_XLOAD(v2, v3, Tracer.INTEGER);
                /*SL:670*/break;
            }
            case 22: {
                /*SL:672*/this.doWIDE_XLOAD(v2, v3, Tracer.LONG);
                /*SL:673*/break;
            }
            case 23: {
                /*SL:675*/this.doWIDE_XLOAD(v2, v3, Tracer.FLOAT);
                /*SL:676*/break;
            }
            case 24: {
                /*SL:678*/this.doWIDE_XLOAD(v2, v3, Tracer.DOUBLE);
                /*SL:679*/break;
            }
            case 25: {
                final int a1 = /*EL:681*/ByteArray.readU16bit(v3, v2 + 2);
                /*SL:682*/this.doALOAD(a1);
                /*SL:683*/break;
            }
            case 54: {
                /*SL:685*/this.doWIDE_STORE(v2, v3, Tracer.INTEGER);
                /*SL:686*/break;
            }
            case 55: {
                /*SL:688*/this.doWIDE_STORE(v2, v3, Tracer.LONG);
                /*SL:689*/break;
            }
            case 56: {
                /*SL:691*/this.doWIDE_STORE(v2, v3, Tracer.FLOAT);
                /*SL:692*/break;
            }
            case 57: {
                /*SL:694*/this.doWIDE_STORE(v2, v3, Tracer.DOUBLE);
                /*SL:695*/break;
            }
            case 58: {
                final int a2 = /*EL:697*/ByteArray.readU16bit(v3, v2 + 2);
                /*SL:698*/this.doASTORE(a2);
                /*SL:699*/break;
            }
            case 132: {
                /*SL:702*/return 6;
            }
            case 169: {
                /*SL:704*/this.visitRET(v2, v3);
                /*SL:705*/break;
            }
            default: {
                /*SL:707*/throw new RuntimeException("bad WIDE instruction: " + v4);
            }
        }
        /*SL:710*/return 4;
    }
    
    private void doWIDE_XLOAD(final int a1, final byte[] a2, final TypeData a3) {
        final int v1 = /*EL:714*/ByteArray.readU16bit(a2, a1 + 2);
        /*SL:715*/this.doXLOAD(v1, a3);
    }
    
    private void doWIDE_STORE(final int a1, final byte[] a2, final TypeData a3) {
        final int v1 = /*EL:719*/ByteArray.readU16bit(a2, a1 + 2);
        /*SL:720*/this.doXSTORE(v1, a3);
    }
    
    private int doPutField(final int a1, final byte[] a2, final boolean a3) throws BadBytecode {
        final int v1 = /*EL:724*/ByteArray.readU16bit(a2, a1 + 1);
        final String v2 = /*EL:725*/this.cpool.getFieldrefType(v1);
        /*SL:726*/this.stackTop -= Descriptor.dataSize(v2);
        final char v3 = /*EL:727*/v2.charAt(0);
        /*SL:728*/if (v3 == 'L') {
            /*SL:729*/this.stackTypes[this.stackTop].setType(getFieldClassName(v2, 0), this.classPool);
        }
        else/*SL:730*/ if (v3 == '[') {
            /*SL:731*/this.stackTypes[this.stackTop].setType(v2, this.classPool);
        }
        /*SL:733*/this.setFieldTarget(a3, v1);
        /*SL:734*/return 3;
    }
    
    private int doGetField(final int a1, final byte[] a2, final boolean a3) throws BadBytecode {
        final int v1 = /*EL:738*/ByteArray.readU16bit(a2, a1 + 1);
        /*SL:739*/this.setFieldTarget(a3, v1);
        final String v2 = /*EL:740*/this.cpool.getFieldrefType(v1);
        /*SL:741*/this.pushMemberType(v2);
        /*SL:742*/return 3;
    }
    
    private void setFieldTarget(final boolean v1, final int v2) throws BadBytecode {
        /*SL:746*/if (v1) {
            final String a1 = /*EL:747*/this.cpool.getFieldrefClassName(v2);
            final TypeData[] stackTypes = /*EL:748*/this.stackTypes;
            final int stackTop = this.stackTop - 1;
            this.stackTop = stackTop;
            stackTypes[stackTop].setType(a1, this.classPool);
        }
    }
    
    private int doNEWARRAY(final int v-2, final byte[] v-1) {
        final int v0 = /*EL:753*/this.stackTop - 1;
        String v = null;
        /*SL:755*/switch (v-1[v-2 + 1] & 0xFF) {
            case 4: {
                final String a1 = /*EL:757*/"[Z";
                /*SL:758*/break;
            }
            case 5: {
                final String a2 = /*EL:760*/"[C";
                /*SL:761*/break;
            }
            case 6: {
                /*SL:763*/v = "[F";
                /*SL:764*/break;
            }
            case 7: {
                /*SL:766*/v = "[D";
                /*SL:767*/break;
            }
            case 8: {
                /*SL:769*/v = "[B";
                /*SL:770*/break;
            }
            case 9: {
                /*SL:772*/v = "[S";
                /*SL:773*/break;
            }
            case 10: {
                /*SL:775*/v = "[I";
                /*SL:776*/break;
            }
            case 11: {
                /*SL:778*/v = "[J";
                /*SL:779*/break;
            }
            default: {
                /*SL:781*/throw new RuntimeException("bad newarray");
            }
        }
        /*SL:784*/this.stackTypes[v0] = new TypeData.ClassName(v);
        /*SL:785*/return 2;
    }
    
    private int doMultiANewArray(final int a1, final byte[] a2) {
        final int v1 = /*EL:789*/ByteArray.readU16bit(a2, a1 + 1);
        final int v2 = /*EL:790*/a2[a1 + 3] & 0xFF;
        /*SL:791*/this.stackTop -= v2 - 1;
        final String v3 = /*EL:793*/this.cpool.getClassInfo(v1).replace('.', '/');
        /*SL:794*/this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(v3);
        /*SL:795*/return 4;
    }
    
    private int doInvokeMethod(final int v1, final byte[] v2, final boolean v3) throws BadBytecode {
        final int v4 = /*EL:799*/ByteArray.readU16bit(v2, v1 + 1);
        final String v5 = /*EL:800*/this.cpool.getMethodrefType(v4);
        /*SL:801*/this.checkParamTypes(v5, 1);
        /*SL:802*/if (v3) {
            final String a1 = /*EL:803*/this.cpool.getMethodrefClassName(v4);
            final TypeData[] stackTypes = /*EL:804*/this.stackTypes;
            final int stackTop = this.stackTop - 1;
            this.stackTop = stackTop;
            final TypeData a2 = stackTypes[stackTop];
            /*SL:805*/if (a2 instanceof TypeData.UninitTypeVar && a2.isUninit()) {
                /*SL:806*/this.constructorCalled(a2, ((TypeData.UninitTypeVar)a2).offset());
            }
            else/*SL:807*/ if (a2 instanceof TypeData.UninitData) {
                /*SL:808*/this.constructorCalled(a2, ((TypeData.UninitData)a2).offset());
            }
            /*SL:810*/a2.setType(a1, this.classPool);
        }
        /*SL:813*/this.pushMemberType(v5);
        /*SL:814*/return 3;
    }
    
    private void constructorCalled(final TypeData v2, final int v3) {
        /*SL:823*/v2.constructorCalled(v3);
        /*SL:824*/for (int a1 = 0; a1 < this.stackTop; ++a1) {
            /*SL:825*/this.stackTypes[a1].constructorCalled(v3);
        }
        /*SL:827*/for (int a2 = 0; a2 < this.localsTypes.length; ++a2) {
            /*SL:828*/this.localsTypes[a2].constructorCalled(v3);
        }
    }
    
    private int doInvokeIntfMethod(final int a1, final byte[] a2) throws BadBytecode {
        final int v1 = /*EL:832*/ByteArray.readU16bit(a2, a1 + 1);
        final String v2 = /*EL:833*/this.cpool.getInterfaceMethodrefType(v1);
        /*SL:834*/this.checkParamTypes(v2, 1);
        final String v3 = /*EL:835*/this.cpool.getInterfaceMethodrefClassName(v1);
        final TypeData[] stackTypes = /*EL:836*/this.stackTypes;
        final int stackTop = this.stackTop - 1;
        this.stackTop = stackTop;
        stackTypes[stackTop].setType(v3, this.classPool);
        /*SL:837*/this.pushMemberType(v2);
        /*SL:838*/return 5;
    }
    
    private int doInvokeDynamic(final int a1, final byte[] a2) throws BadBytecode {
        final int v1 = /*EL:842*/ByteArray.readU16bit(a2, a1 + 1);
        final String v2 = /*EL:843*/this.cpool.getInvokeDynamicType(v1);
        /*SL:844*/this.checkParamTypes(v2, 1);
        /*SL:852*/this.pushMemberType(v2);
        /*SL:853*/return 5;
    }
    
    private void pushMemberType(final String a1) {
        int v1 = /*EL:857*/0;
        /*SL:858*/if (a1.charAt(0) == '(') {
            /*SL:859*/v1 = a1.indexOf(41) + 1;
            /*SL:860*/if (v1 < 1) {
                /*SL:861*/throw new IndexOutOfBoundsException("bad descriptor: " + a1);
            }
        }
        final TypeData[] v2 = /*EL:865*/this.stackTypes;
        final int v3 = /*EL:866*/this.stackTop;
        /*SL:867*/switch (a1.charAt(v1)) {
            case '[': {
                /*SL:869*/v2[v3] = new TypeData.ClassName(a1.substring(v1));
                /*SL:870*/break;
            }
            case 'L': {
                /*SL:872*/v2[v3] = new TypeData.ClassName(getFieldClassName(a1, v1));
                /*SL:873*/break;
            }
            case 'J': {
                /*SL:875*/v2[v3] = Tracer.LONG;
                /*SL:876*/v2[v3 + 1] = Tracer.TOP;
                /*SL:877*/this.stackTop += 2;
                /*SL:878*/return;
            }
            case 'F': {
                /*SL:880*/v2[v3] = Tracer.FLOAT;
                /*SL:881*/break;
            }
            case 'D': {
                /*SL:883*/v2[v3] = Tracer.DOUBLE;
                /*SL:884*/v2[v3 + 1] = Tracer.TOP;
                /*SL:885*/this.stackTop += 2;
                /*SL:886*/return;
            }
            case 'V': {
                /*SL:888*/return;
            }
            default: {
                /*SL:890*/v2[v3] = Tracer.INTEGER;
                break;
            }
        }
        /*SL:894*/++this.stackTop;
    }
    
    private static String getFieldClassName(final String a1, final int a2) {
        /*SL:898*/return a1.substring(a2 + 1, a1.length() - 1).replace('/', '.');
    }
    
    private void checkParamTypes(final String a1, final int a2) throws BadBytecode {
        char v1 = /*EL:902*/a1.charAt(a2);
        /*SL:903*/if (v1 == ')') {
            /*SL:904*/return;
        }
        int v2 = /*EL:906*/a2;
        boolean v3 = /*EL:907*/false;
        /*SL:908*/while (v1 == '[') {
            /*SL:909*/v3 = true;
            /*SL:910*/v1 = a1.charAt(++v2);
        }
        /*SL:913*/if (v1 == 'L') {
            /*SL:914*/v2 = a1.indexOf(59, v2) + 1;
            /*SL:915*/if (v2 <= 0) {
                /*SL:916*/throw new IndexOutOfBoundsException("bad descriptor");
            }
        }
        else {
            /*SL:919*/++v2;
        }
        /*SL:921*/this.checkParamTypes(a1, v2);
        /*SL:922*/if (!v3 && (v1 == 'J' || v1 == 'D')) {
            /*SL:923*/this.stackTop -= 2;
        }
        else {
            /*SL:925*/--this.stackTop;
        }
        /*SL:927*/if (v3) {
            /*SL:928*/this.stackTypes[this.stackTop].setType(a1.substring(a2, v2), this.classPool);
        }
        else/*SL:929*/ if (v1 == 'L') {
            /*SL:930*/this.stackTypes[this.stackTop].setType(a1.substring(a2 + 1, v2 - 1).replace('/', '.'), this.classPool);
        }
    }
}
