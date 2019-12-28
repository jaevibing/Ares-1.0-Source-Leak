package javassist.bytecode;

import javassist.CtPrimitiveType;
import javassist.CtClass;

public class Bytecode extends ByteVector implements Cloneable, Opcode
{
    public static final CtClass THIS;
    ConstPool constPool;
    int maxStack;
    int maxLocals;
    ExceptionTable tryblocks;
    private int stackDepth;
    
    public Bytecode(final ConstPool a1, final int a2, final int a3) {
        this.constPool = a1;
        this.maxStack = a2;
        this.maxLocals = a3;
        this.tryblocks = new ExceptionTable(a1);
        this.stackDepth = 0;
    }
    
    public Bytecode(final ConstPool a1) {
        this(a1, 0, 0);
    }
    
    @Override
    public Object clone() {
        try {
            final Bytecode v1 = /*EL:169*/(Bytecode)super.clone();
            /*SL:170*/v1.tryblocks = (ExceptionTable)this.tryblocks.clone();
            /*SL:171*/return v1;
        }
        catch (CloneNotSupportedException v2) {
            /*SL:174*/throw new RuntimeException(v2);
        }
    }
    
    public ConstPool getConstPool() {
        /*SL:181*/return this.constPool;
    }
    
    public ExceptionTable getExceptionTable() {
        /*SL:186*/return this.tryblocks;
    }
    
    public CodeAttribute toCodeAttribute() {
        /*SL:193*/return new CodeAttribute(this.constPool, this.maxStack, this.maxLocals, this.get(), this.tryblocks);
    }
    
    public int length() {
        /*SL:200*/return this.getSize();
    }
    
    public byte[] get() {
        /*SL:207*/return this.copy();
    }
    
    public int getMaxStack() {
        /*SL:213*/return this.maxStack;
    }
    
    public void setMaxStack(final int a1) {
        /*SL:230*/this.maxStack = a1;
    }
    
    public int getMaxLocals() {
        /*SL:236*/return this.maxLocals;
    }
    
    public void setMaxLocals(final int a1) {
        /*SL:242*/this.maxLocals = a1;
    }
    
    public void setMaxLocals(final boolean v-3, final CtClass[] v-2, int v-1) {
        /*SL:260*/if (!v-3) {
            /*SL:261*/++v-1;
        }
        /*SL:263*/if (v-2 != null) {
            CtClass a3 = CtClass.doubleType;
            final CtClass v1 = CtClass.longType;
            /*SL:267*/for (int v2 = v-2.length, a2 = 0; a2 < v2; ++a2) {
                /*SL:268*/a3 = v-2[a2];
                /*SL:269*/if (a3 == a3 || a3 == v1) {
                    /*SL:270*/v-1 += 2;
                }
                else {
                    /*SL:272*/++v-1;
                }
            }
        }
        /*SL:276*/this.maxLocals = v-1;
    }
    
    public void incMaxLocals(final int a1) {
        /*SL:283*/this.maxLocals += a1;
    }
    
    public void addExceptionHandler(final int a1, final int a2, final int a3, final CtClass a4) {
        /*SL:291*/this.addExceptionHandler(a1, a2, a3, this.constPool.addClassInfo(a4));
    }
    
    public void addExceptionHandler(final int a1, final int a2, final int a3, final String a4) {
        /*SL:302*/this.addExceptionHandler(a1, a2, a3, this.constPool.addClassInfo(a4));
    }
    
    public void addExceptionHandler(final int a1, final int a2, final int a3, final int a4) {
        /*SL:311*/this.tryblocks.add(a1, a2, a3, a4);
    }
    
    public int currentPc() {
        /*SL:319*/return this.getSize();
    }
    
    @Override
    public int read(final int a1) {
        /*SL:329*/return super.read(a1);
    }
    
    public int read16bit(final int a1) {
        final int v1 = /*EL:337*/this.read(a1);
        final int v2 = /*EL:338*/this.read(a1 + 1);
        /*SL:339*/return (v1 << 8) + (v2 & 0xFF);
    }
    
    public int read32bit(final int a1) {
        final int v1 = /*EL:347*/this.read16bit(a1);
        final int v2 = /*EL:348*/this.read16bit(a1 + 2);
        /*SL:349*/return (v1 << 16) + (v2 & 0xFFFF);
    }
    
    @Override
    public void write(final int a1, final int a2) {
        /*SL:359*/super.write(a1, a2);
    }
    
    public void write16bit(final int a1, final int a2) {
        /*SL:367*/this.write(a1, a2 >> 8);
        /*SL:368*/this.write(a1 + 1, a2);
    }
    
    public void write32bit(final int a1, final int a2) {
        /*SL:376*/this.write16bit(a1, a2 >> 16);
        /*SL:377*/this.write16bit(a1 + 2, a2);
    }
    
    @Override
    public void add(final int a1) {
        /*SL:384*/super.add(a1);
    }
    
    public void add32bit(final int a1) {
        /*SL:391*/this.add(a1 >> 24, a1 >> 16, a1 >> 8, a1);
    }
    
    @Override
    public void addGap(final int a1) {
        /*SL:400*/super.addGap(a1);
    }
    
    public void addOpcode(final int a1) {
        /*SL:415*/this.add(a1);
        /*SL:416*/this.growStack(Bytecode.STACK_GROW[a1]);
    }
    
    public void growStack(final int a1) {
        /*SL:427*/this.setStackDepth(this.stackDepth + a1);
    }
    
    public int getStackDepth() {
        /*SL:433*/return this.stackDepth;
    }
    
    public void setStackDepth(final int a1) {
        /*SL:443*/this.stackDepth = a1;
        /*SL:444*/if (this.stackDepth > this.maxStack) {
            /*SL:445*/this.maxStack = this.stackDepth;
        }
    }
    
    public void addIndex(final int a1) {
        /*SL:453*/this.add(a1 >> 8, a1);
    }
    
    public void addAload(final int a1) {
        /*SL:462*/if (a1 < 4) {
            /*SL:463*/this.addOpcode(42 + a1);
        }
        else/*SL:464*/ if (a1 < 256) {
            /*SL:465*/this.addOpcode(25);
            /*SL:466*/this.add(a1);
        }
        else {
            /*SL:469*/this.addOpcode(196);
            /*SL:470*/this.addOpcode(25);
            /*SL:471*/this.addIndex(a1);
        }
    }
    
    public void addAstore(final int a1) {
        /*SL:481*/if (a1 < 4) {
            /*SL:482*/this.addOpcode(75 + a1);
        }
        else/*SL:483*/ if (a1 < 256) {
            /*SL:484*/this.addOpcode(58);
            /*SL:485*/this.add(a1);
        }
        else {
            /*SL:488*/this.addOpcode(196);
            /*SL:489*/this.addOpcode(58);
            /*SL:490*/this.addIndex(a1);
        }
    }
    
    public void addIconst(final int a1) {
        /*SL:500*/if (a1 < 6 && -2 < a1) {
            /*SL:501*/this.addOpcode(3 + a1);
        }
        else/*SL:502*/ if (a1 <= 127 && -128 <= a1) {
            /*SL:503*/this.addOpcode(16);
            /*SL:504*/this.add(a1);
        }
        else/*SL:506*/ if (a1 <= 32767 && -32768 <= a1) {
            /*SL:507*/this.addOpcode(17);
            /*SL:508*/this.add(a1 >> 8);
            /*SL:509*/this.add(a1);
        }
        else {
            /*SL:512*/this.addLdc(this.constPool.addIntegerInfo(a1));
        }
    }
    
    public void addConstZero(final CtClass a1) {
        /*SL:522*/if (a1.isPrimitive()) {
            /*SL:523*/if (a1 == CtClass.longType) {
                /*SL:524*/this.addOpcode(9);
            }
            else/*SL:525*/ if (a1 == CtClass.floatType) {
                /*SL:526*/this.addOpcode(11);
            }
            else/*SL:527*/ if (a1 == CtClass.doubleType) {
                /*SL:528*/this.addOpcode(14);
            }
            else {
                /*SL:529*/if (a1 == CtClass.voidType) {
                    /*SL:530*/throw new RuntimeException("void type?");
                }
                /*SL:532*/this.addOpcode(3);
            }
        }
        else {
            /*SL:535*/this.addOpcode(1);
        }
    }
    
    public void addIload(final int a1) {
        /*SL:544*/if (a1 < 4) {
            /*SL:545*/this.addOpcode(26 + a1);
        }
        else/*SL:546*/ if (a1 < 256) {
            /*SL:547*/this.addOpcode(21);
            /*SL:548*/this.add(a1);
        }
        else {
            /*SL:551*/this.addOpcode(196);
            /*SL:552*/this.addOpcode(21);
            /*SL:553*/this.addIndex(a1);
        }
    }
    
    public void addIstore(final int a1) {
        /*SL:563*/if (a1 < 4) {
            /*SL:564*/this.addOpcode(59 + a1);
        }
        else/*SL:565*/ if (a1 < 256) {
            /*SL:566*/this.addOpcode(54);
            /*SL:567*/this.add(a1);
        }
        else {
            /*SL:570*/this.addOpcode(196);
            /*SL:571*/this.addOpcode(54);
            /*SL:572*/this.addIndex(a1);
        }
    }
    
    public void addLconst(final long a1) {
        /*SL:582*/if (a1 == 0L || a1 == 1L) {
            /*SL:583*/this.addOpcode(9 + (int)a1);
        }
        else {
            /*SL:585*/this.addLdc2w(a1);
        }
    }
    
    public void addLload(final int a1) {
        /*SL:594*/if (a1 < 4) {
            /*SL:595*/this.addOpcode(30 + a1);
        }
        else/*SL:596*/ if (a1 < 256) {
            /*SL:597*/this.addOpcode(22);
            /*SL:598*/this.add(a1);
        }
        else {
            /*SL:601*/this.addOpcode(196);
            /*SL:602*/this.addOpcode(22);
            /*SL:603*/this.addIndex(a1);
        }
    }
    
    public void addLstore(final int a1) {
        /*SL:613*/if (a1 < 4) {
            /*SL:614*/this.addOpcode(63 + a1);
        }
        else/*SL:615*/ if (a1 < 256) {
            /*SL:616*/this.addOpcode(55);
            /*SL:617*/this.add(a1);
        }
        else {
            /*SL:620*/this.addOpcode(196);
            /*SL:621*/this.addOpcode(55);
            /*SL:622*/this.addIndex(a1);
        }
    }
    
    public void addDconst(final double a1) {
        /*SL:632*/if (a1 == 0.0 || a1 == 1.0) {
            /*SL:633*/this.addOpcode(14 + (int)a1);
        }
        else {
            /*SL:635*/this.addLdc2w(a1);
        }
    }
    
    public void addDload(final int a1) {
        /*SL:644*/if (a1 < 4) {
            /*SL:645*/this.addOpcode(38 + a1);
        }
        else/*SL:646*/ if (a1 < 256) {
            /*SL:647*/this.addOpcode(24);
            /*SL:648*/this.add(a1);
        }
        else {
            /*SL:651*/this.addOpcode(196);
            /*SL:652*/this.addOpcode(24);
            /*SL:653*/this.addIndex(a1);
        }
    }
    
    public void addDstore(final int a1) {
        /*SL:663*/if (a1 < 4) {
            /*SL:664*/this.addOpcode(71 + a1);
        }
        else/*SL:665*/ if (a1 < 256) {
            /*SL:666*/this.addOpcode(57);
            /*SL:667*/this.add(a1);
        }
        else {
            /*SL:670*/this.addOpcode(196);
            /*SL:671*/this.addOpcode(57);
            /*SL:672*/this.addIndex(a1);
        }
    }
    
    public void addFconst(final float a1) {
        /*SL:682*/if (a1 == 0.0f || a1 == 1.0f || a1 == 2.0f) {
            /*SL:683*/this.addOpcode(11 + (int)a1);
        }
        else {
            /*SL:685*/this.addLdc(this.constPool.addFloatInfo(a1));
        }
    }
    
    public void addFload(final int a1) {
        /*SL:694*/if (a1 < 4) {
            /*SL:695*/this.addOpcode(34 + a1);
        }
        else/*SL:696*/ if (a1 < 256) {
            /*SL:697*/this.addOpcode(23);
            /*SL:698*/this.add(a1);
        }
        else {
            /*SL:701*/this.addOpcode(196);
            /*SL:702*/this.addOpcode(23);
            /*SL:703*/this.addIndex(a1);
        }
    }
    
    public void addFstore(final int a1) {
        /*SL:713*/if (a1 < 4) {
            /*SL:714*/this.addOpcode(67 + a1);
        }
        else/*SL:715*/ if (a1 < 256) {
            /*SL:716*/this.addOpcode(56);
            /*SL:717*/this.add(a1);
        }
        else {
            /*SL:720*/this.addOpcode(196);
            /*SL:721*/this.addOpcode(56);
            /*SL:722*/this.addIndex(a1);
        }
    }
    
    public int addLoad(final int a1, final CtClass a2) {
        /*SL:735*/if (a2.isPrimitive()) {
            /*SL:736*/if (a2 == CtClass.booleanType || a2 == CtClass.charType || a2 == CtClass.byteType || a2 == CtClass.shortType || a2 == CtClass.intType) {
                /*SL:739*/this.addIload(a1);
            }
            else {
                /*SL:740*/if (a2 == CtClass.longType) {
                    /*SL:741*/this.addLload(a1);
                    /*SL:742*/return 2;
                }
                /*SL:744*/if (a2 == CtClass.floatType) {
                    /*SL:745*/this.addFload(a1);
                }
                else {
                    /*SL:746*/if (a2 == CtClass.doubleType) {
                        /*SL:747*/this.addDload(a1);
                        /*SL:748*/return 2;
                    }
                    /*SL:751*/throw new RuntimeException("void type?");
                }
            }
        }
        else {
            /*SL:754*/this.addAload(a1);
        }
        /*SL:756*/return 1;
    }
    
    public int addStore(final int a1, final CtClass a2) {
        /*SL:768*/if (a2.isPrimitive()) {
            /*SL:769*/if (a2 == CtClass.booleanType || a2 == CtClass.charType || a2 == CtClass.byteType || a2 == CtClass.shortType || a2 == CtClass.intType) {
                /*SL:772*/this.addIstore(a1);
            }
            else {
                /*SL:773*/if (a2 == CtClass.longType) {
                    /*SL:774*/this.addLstore(a1);
                    /*SL:775*/return 2;
                }
                /*SL:777*/if (a2 == CtClass.floatType) {
                    /*SL:778*/this.addFstore(a1);
                }
                else {
                    /*SL:779*/if (a2 == CtClass.doubleType) {
                        /*SL:780*/this.addDstore(a1);
                        /*SL:781*/return 2;
                    }
                    /*SL:784*/throw new RuntimeException("void type?");
                }
            }
        }
        else {
            /*SL:787*/this.addAstore(a1);
        }
        /*SL:789*/return 1;
    }
    
    public int addLoadParameters(final CtClass[] v2, final int v3) {
        int v4 = /*EL:800*/0;
        /*SL:801*/if (v2 != null) {
            int a2;
            int a2;
            /*SL:803*/for (a2 = v2.length, a2 = 0; a2 < a2; ++a2) {
                /*SL:804*/v4 += this.addLoad(v4 + v3, v2[a2]);
            }
        }
        /*SL:807*/return v4;
    }
    
    public void addCheckcast(final CtClass a1) {
        /*SL:816*/this.addOpcode(192);
        /*SL:817*/this.addIndex(this.constPool.addClassInfo(a1));
    }
    
    public void addCheckcast(final String a1) {
        /*SL:826*/this.addOpcode(192);
        /*SL:827*/this.addIndex(this.constPool.addClassInfo(a1));
    }
    
    public void addInstanceof(final String a1) {
        /*SL:836*/this.addOpcode(193);
        /*SL:837*/this.addIndex(this.constPool.addClassInfo(a1));
    }
    
    public void addGetfield(final CtClass a1, final String a2, final String a3) {
        /*SL:850*/this.add(180);
        final int v1 = /*EL:851*/this.constPool.addClassInfo(a1);
        /*SL:852*/this.addIndex(this.constPool.addFieldrefInfo(v1, a2, a3));
        /*SL:853*/this.growStack(Descriptor.dataSize(a3) - 1);
    }
    
    public void addGetfield(final String a1, final String a2, final String a3) {
        /*SL:866*/this.add(180);
        final int v1 = /*EL:867*/this.constPool.addClassInfo(a1);
        /*SL:868*/this.addIndex(this.constPool.addFieldrefInfo(v1, a2, a3));
        /*SL:869*/this.growStack(Descriptor.dataSize(a3) - 1);
    }
    
    public void addGetstatic(final CtClass a1, final String a2, final String a3) {
        /*SL:882*/this.add(178);
        final int v1 = /*EL:883*/this.constPool.addClassInfo(a1);
        /*SL:884*/this.addIndex(this.constPool.addFieldrefInfo(v1, a2, a3));
        /*SL:885*/this.growStack(Descriptor.dataSize(a3));
    }
    
    public void addGetstatic(final String a1, final String a2, final String a3) {
        /*SL:898*/this.add(178);
        final int v1 = /*EL:899*/this.constPool.addClassInfo(a1);
        /*SL:900*/this.addIndex(this.constPool.addFieldrefInfo(v1, a2, a3));
        /*SL:901*/this.growStack(Descriptor.dataSize(a3));
    }
    
    public void addInvokespecial(final CtClass a1, final String a2, final CtClass a3, final CtClass[] a4) {
        final String v1 = /*EL:914*/Descriptor.ofMethod(a3, a4);
        /*SL:915*/this.addInvokespecial(a1, a2, v1);
    }
    
    public void addInvokespecial(final CtClass a1, final String a2, final String a3) {
        final boolean v1 = /*EL:929*/a1 != null && a1.isInterface();
        /*SL:930*/this.addInvokespecial(v1, this.constPool.addClassInfo(a1), /*EL:931*/a2, a3);
    }
    
    public void addInvokespecial(final String a1, final String a2, final String a3) {
        /*SL:946*/this.addInvokespecial(false, this.constPool.addClassInfo(a1), a2, a3);
    }
    
    public void addInvokespecial(final int a1, final String a2, final String a3) {
        /*SL:962*/this.addInvokespecial(false, a1, a2, a3);
    }
    
    public void addInvokespecial(final boolean a3, final int a4, final String v1, final String v2) {
        final int v3;
        /*SL:980*/if (a3) {
            final int a5 = /*EL:981*/this.constPool.addInterfaceMethodrefInfo(a4, v1, v2);
        }
        else {
            /*SL:983*/v3 = this.constPool.addMethodrefInfo(a4, v1, v2);
        }
        /*SL:985*/this.addInvokespecial(v3, v2);
    }
    
    public void addInvokespecial(final int a1, final String a2) {
        /*SL:999*/this.add(183);
        /*SL:1000*/this.addIndex(a1);
        /*SL:1001*/this.growStack(Descriptor.dataSize(a2) - 1);
    }
    
    public void addInvokestatic(final CtClass a1, final String a2, final CtClass a3, final CtClass[] a4) {
        final String v1 = /*EL:1014*/Descriptor.ofMethod(a3, a4);
        /*SL:1015*/this.addInvokestatic(a1, a2, v1);
    }
    
    public void addInvokestatic(final CtClass a3, final String v1, final String v2) {
        final boolean v3;
        /*SL:1029*/if (a3 == Bytecode.THIS) {
            final boolean a4 = /*EL:1030*/false;
        }
        else {
            /*SL:1032*/v3 = a3.isInterface();
        }
        /*SL:1034*/this.addInvokestatic(this.constPool.addClassInfo(a3), v1, v2, v3);
    }
    
    public void addInvokestatic(final String a1, final String a2, final String a3) {
        /*SL:1048*/this.addInvokestatic(this.constPool.addClassInfo(a1), a2, a3);
    }
    
    public void addInvokestatic(final int a1, final String a2, final String a3) {
        /*SL:1062*/this.addInvokestatic(a1, a2, a3, false);
    }
    
    private void addInvokestatic(final int a3, final String a4, final String v1, final boolean v2) {
        /*SL:1067*/this.add(184);
        final int v3;
        /*SL:1069*/if (v2) {
            final int a5 = /*EL:1070*/this.constPool.addInterfaceMethodrefInfo(a3, a4, v1);
        }
        else {
            /*SL:1072*/v3 = this.constPool.addMethodrefInfo(a3, a4, v1);
        }
        /*SL:1074*/this.addIndex(v3);
        /*SL:1075*/this.growStack(Descriptor.dataSize(v1));
    }
    
    public void addInvokevirtual(final CtClass a1, final String a2, final CtClass a3, final CtClass[] a4) {
        final String v1 = /*EL:1092*/Descriptor.ofMethod(a3, a4);
        /*SL:1093*/this.addInvokevirtual(a1, a2, v1);
    }
    
    public void addInvokevirtual(final CtClass a1, final String a2, final String a3) {
        /*SL:1110*/this.addInvokevirtual(this.constPool.addClassInfo(a1), a2, a3);
    }
    
    public void addInvokevirtual(final String a1, final String a2, final String a3) {
        /*SL:1127*/this.addInvokevirtual(this.constPool.addClassInfo(a1), a2, a3);
    }
    
    public void addInvokevirtual(final int a1, final String a2, final String a3) {
        /*SL:1145*/this.add(182);
        /*SL:1146*/this.addIndex(this.constPool.addMethodrefInfo(a1, a2, a3));
        /*SL:1147*/this.growStack(Descriptor.dataSize(a3) - 1);
    }
    
    public void addInvokeinterface(final CtClass a1, final String a2, final CtClass a3, final CtClass[] a4, final int a5) {
        final String v1 = /*EL:1162*/Descriptor.ofMethod(a3, a4);
        /*SL:1163*/this.addInvokeinterface(a1, a2, v1, a5);
    }
    
    public void addInvokeinterface(final CtClass a1, final String a2, final String a3, final int a4) {
        /*SL:1178*/this.addInvokeinterface(this.constPool.addClassInfo(a1), a2, a3, a4);
    }
    
    public void addInvokeinterface(final String a1, final String a2, final String a3, final int a4) {
        /*SL:1194*/this.addInvokeinterface(this.constPool.addClassInfo(a1), a2, a3, a4);
    }
    
    public void addInvokeinterface(final int a1, final String a2, final String a3, final int a4) {
        /*SL:1211*/this.add(185);
        /*SL:1212*/this.addIndex(this.constPool.addInterfaceMethodrefInfo(a1, a2, a3));
        /*SL:1213*/this.add(a4);
        /*SL:1214*/this.add(0);
        /*SL:1215*/this.growStack(Descriptor.dataSize(a3) - 1);
    }
    
    public void addInvokedynamic(final int a1, final String a2, final String a3) {
        final int v1 = /*EL:1229*/this.constPool.addNameAndTypeInfo(a2, a3);
        final int v2 = /*EL:1230*/this.constPool.addInvokeDynamicInfo(a1, v1);
        /*SL:1231*/this.add(186);
        /*SL:1232*/this.addIndex(v2);
        /*SL:1233*/this.add(0, 0);
        /*SL:1234*/this.growStack(Descriptor.dataSize(a3));
    }
    
    public void addLdc(final String a1) {
        /*SL:1244*/this.addLdc(this.constPool.addStringInfo(a1));
    }
    
    public void addLdc(final int a1) {
        /*SL:1253*/if (a1 > 255) {
            /*SL:1254*/this.addOpcode(19);
            /*SL:1255*/this.addIndex(a1);
        }
        else {
            /*SL:1258*/this.addOpcode(18);
            /*SL:1259*/this.add(a1);
        }
    }
    
    public void addLdc2w(final long a1) {
        /*SL:1267*/this.addOpcode(20);
        /*SL:1268*/this.addIndex(this.constPool.addLongInfo(a1));
    }
    
    public void addLdc2w(final double a1) {
        /*SL:1275*/this.addOpcode(20);
        /*SL:1276*/this.addIndex(this.constPool.addDoubleInfo(a1));
    }
    
    public void addNew(final CtClass a1) {
        /*SL:1285*/this.addOpcode(187);
        /*SL:1286*/this.addIndex(this.constPool.addClassInfo(a1));
    }
    
    public void addNew(final String a1) {
        /*SL:1295*/this.addOpcode(187);
        /*SL:1296*/this.addIndex(this.constPool.addClassInfo(a1));
    }
    
    public void addAnewarray(final String a1) {
        /*SL:1305*/this.addOpcode(189);
        /*SL:1306*/this.addIndex(this.constPool.addClassInfo(a1));
    }
    
    public void addAnewarray(final CtClass a1, final int a2) {
        /*SL:1316*/this.addIconst(a2);
        /*SL:1317*/this.addOpcode(189);
        /*SL:1318*/this.addIndex(this.constPool.addClassInfo(a1));
    }
    
    public void addNewarray(final int a1, final int a2) {
        /*SL:1328*/this.addIconst(a2);
        /*SL:1329*/this.addOpcode(188);
        /*SL:1330*/this.add(a1);
    }
    
    public int addMultiNewarray(final CtClass v1, final int[] v2) {
        final int v3 = /*EL:1341*/v2.length;
        /*SL:1342*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:1343*/this.addIconst(v2[a1]);
        }
        /*SL:1345*/this.growStack(v3);
        /*SL:1346*/return this.addMultiNewarray(v1, v3);
    }
    
    public int addMultiNewarray(final CtClass a1, final int a2) {
        /*SL:1358*/this.add(197);
        /*SL:1359*/this.addIndex(this.constPool.addClassInfo(a1));
        /*SL:1360*/this.add(a2);
        /*SL:1361*/this.growStack(1 - a2);
        /*SL:1362*/return a2;
    }
    
    public int addMultiNewarray(final String a1, final int a2) {
        /*SL:1373*/this.add(197);
        /*SL:1374*/this.addIndex(this.constPool.addClassInfo(a1));
        /*SL:1375*/this.add(a2);
        /*SL:1376*/this.growStack(1 - a2);
        /*SL:1377*/return a2;
    }
    
    public void addPutfield(final CtClass a1, final String a2, final String a3) {
        /*SL:1388*/this.addPutfield0(a1, null, a2, a3);
    }
    
    public void addPutfield(final String a1, final String a2, final String a3) {
        /*SL:1400*/this.addPutfield0(null, a1, a2, a3);
    }
    
    private void addPutfield0(final CtClass a1, final String a2, final String a3, final String a4) {
        /*SL:1405*/this.add(181);
        final int v1 = /*EL:1407*/(a2 == null) ? this.constPool.addClassInfo(a1) : this.constPool.addClassInfo(a2);
        /*SL:1409*/this.addIndex(this.constPool.addFieldrefInfo(v1, a3, a4));
        /*SL:1410*/this.growStack(-1 - Descriptor.dataSize(a4));
    }
    
    public void addPutstatic(final CtClass a1, final String a2, final String a3) {
        /*SL:1421*/this.addPutstatic0(a1, null, a2, a3);
    }
    
    public void addPutstatic(final String a1, final String a2, final String a3) {
        /*SL:1433*/this.addPutstatic0(null, a1, a2, a3);
    }
    
    private void addPutstatic0(final CtClass a1, final String a2, final String a3, final String a4) {
        /*SL:1438*/this.add(179);
        final int v1 = /*EL:1440*/(a2 == null) ? this.constPool.addClassInfo(a1) : this.constPool.addClassInfo(a2);
        /*SL:1442*/this.addIndex(this.constPool.addFieldrefInfo(v1, a3, a4));
        /*SL:1443*/this.growStack(-Descriptor.dataSize(a4));
    }
    
    public void addReturn(final CtClass v2) {
        /*SL:1452*/if (v2 == null) {
            /*SL:1453*/this.addOpcode(177);
        }
        else/*SL:1454*/ if (v2.isPrimitive()) {
            final CtPrimitiveType a1 = /*EL:1455*/(CtPrimitiveType)v2;
            /*SL:1456*/this.addOpcode(a1.getReturnOp());
        }
        else {
            /*SL:1459*/this.addOpcode(176);
        }
    }
    
    public void addRet(final int a1) {
        /*SL:1468*/if (a1 < 256) {
            /*SL:1469*/this.addOpcode(169);
            /*SL:1470*/this.add(a1);
        }
        else {
            /*SL:1473*/this.addOpcode(196);
            /*SL:1474*/this.addOpcode(169);
            /*SL:1475*/this.addIndex(a1);
        }
    }
    
    public void addPrintln(final String a1) {
        /*SL:1486*/this.addGetstatic("java.lang.System", "err", "Ljava/io/PrintStream;");
        /*SL:1487*/this.addLdc(a1);
        /*SL:1488*/this.addInvokevirtual("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
    }
    
    static {
        THIS = ConstPool.THIS;
    }
}
