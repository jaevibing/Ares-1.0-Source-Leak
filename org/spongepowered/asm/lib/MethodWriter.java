package org.spongepowered.asm.lib;

class MethodWriter extends MethodVisitor
{
    static final int ACC_CONSTRUCTOR = 524288;
    static final int SAME_FRAME = 0;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
    static final int RESERVED = 128;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
    static final int CHOP_FRAME = 248;
    static final int SAME_FRAME_EXTENDED = 251;
    static final int APPEND_FRAME = 252;
    static final int FULL_FRAME = 255;
    static final int FRAMES = 0;
    static final int INSERTED_FRAMES = 1;
    static final int MAXS = 2;
    static final int NOTHING = 3;
    final ClassWriter cw;
    private int access;
    private final int name;
    private final int desc;
    private final String descriptor;
    String signature;
    int classReaderOffset;
    int classReaderLength;
    int exceptionCount;
    int[] exceptions;
    private ByteVector annd;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private AnnotationWriter tanns;
    private AnnotationWriter itanns;
    private AnnotationWriter[] panns;
    private AnnotationWriter[] ipanns;
    private int synthetics;
    private Attribute attrs;
    private ByteVector code;
    private int maxStack;
    private int maxLocals;
    private int currentLocals;
    private int frameCount;
    private ByteVector stackMap;
    private int previousFrameOffset;
    private int[] previousFrame;
    private int[] frame;
    private int handlerCount;
    private Handler firstHandler;
    private Handler lastHandler;
    private int methodParametersCount;
    private ByteVector methodParameters;
    private int localVarCount;
    private ByteVector localVar;
    private int localVarTypeCount;
    private ByteVector localVarType;
    private int lineNumberCount;
    private ByteVector lineNumber;
    private int lastCodeOffset;
    private AnnotationWriter ctanns;
    private AnnotationWriter ictanns;
    private Attribute cattrs;
    private int subroutines;
    private final int compute;
    private Label labels;
    private Label previousBlock;
    private Label currentBlock;
    private int stackSize;
    private int maxStackSize;
    
    MethodWriter(final ClassWriter a4, final int a5, final String a6, final String a7, final String v1, final String[] v2, final int v3) {
        super(327680);
        this.code = new ByteVector();
        if (a4.firstMethod == null) {
            a4.firstMethod = this;
        }
        else {
            a4.lastMethod.mv = this;
        }
        a4.lastMethod = this;
        this.cw = a4;
        this.access = a5;
        if ("<init>".equals(a6)) {
            this.access |= 0x80000;
        }
        this.name = a4.newUTF8(a6);
        this.desc = a4.newUTF8(a7);
        this.descriptor = a7;
        this.signature = v1;
        if (v2 != null && v2.length > 0) {
            this.exceptionCount = v2.length;
            this.exceptions = new int[this.exceptionCount];
            for (int a8 = 0; a8 < this.exceptionCount; ++a8) {
                this.exceptions[a8] = a4.newClass(v2[a8]);
            }
        }
        if ((this.compute = v3) != 3) {
            int a9 = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
            if ((a5 & 0x8) != 0x0) {
                --a9;
            }
            this.maxLocals = a9;
            this.currentLocals = a9;
            this.labels = new Label();
            final Label labels = this.labels;
            labels.status |= 0x8;
            this.visitLabel(this.labels);
        }
    }
    
    public void visitParameter(final String a1, final int a2) {
        /*SL:506*/if (this.methodParameters == null) {
            /*SL:507*/this.methodParameters = new ByteVector();
        }
        /*SL:509*/++this.methodParametersCount;
        /*SL:510*/this.methodParameters.putShort((a1 == null) ? 0 : this.cw.newUTF8(a1)).putShort(a2);
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        /*SL:519*/this.annd = new ByteVector();
        /*SL:520*/return new AnnotationWriter(this.cw, false, this.annd, null, 0);
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final ByteVector v1 = /*EL:529*/new ByteVector();
        /*SL:531*/v1.putShort(this.cw.newUTF8(a1)).putShort(0);
        final AnnotationWriter v2 = /*EL:532*/new AnnotationWriter(this.cw, true, v1, v1, 2);
        /*SL:533*/if (a2) {
            /*SL:534*/v2.next = this.anns;
            /*SL:535*/this.anns = v2;
        }
        else {
            /*SL:537*/v2.next = this.ianns;
            /*SL:538*/this.ianns = v2;
        }
        /*SL:540*/return v2;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final ByteVector v1 = /*EL:549*/new ByteVector();
        /*SL:551*/AnnotationWriter.putTarget(a1, a2, v1);
        /*SL:553*/v1.putShort(this.cw.newUTF8(a3)).putShort(0);
        final AnnotationWriter v2 = /*EL:554*/new AnnotationWriter(this.cw, true, v1, v1, v1.length - 2);
        /*SL:556*/if (a4) {
            /*SL:557*/v2.next = this.tanns;
            /*SL:558*/this.tanns = v2;
        }
        else {
            /*SL:560*/v2.next = this.itanns;
            /*SL:561*/this.itanns = v2;
        }
        /*SL:563*/return v2;
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int a1, final String a2, final boolean a3) {
        final ByteVector v1 = /*EL:572*/new ByteVector();
        /*SL:573*/if ("Ljava/lang/Synthetic;".equals(a2)) {
            /*SL:576*/this.synthetics = Math.max(this.synthetics, a1 + 1);
            /*SL:577*/return new AnnotationWriter(this.cw, false, v1, null, 0);
        }
        /*SL:580*/v1.putShort(this.cw.newUTF8(a2)).putShort(0);
        final AnnotationWriter v2 = /*EL:581*/new AnnotationWriter(this.cw, true, v1, v1, 2);
        /*SL:582*/if (a3) {
            /*SL:583*/if (this.panns == null) {
                /*SL:584*/this.panns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            /*SL:586*/v2.next = this.panns[a1];
            /*SL:587*/this.panns[a1] = v2;
        }
        else {
            /*SL:589*/if (this.ipanns == null) {
                /*SL:590*/this.ipanns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            /*SL:592*/v2.next = this.ipanns[a1];
            /*SL:593*/this.ipanns[a1] = v2;
        }
        /*SL:595*/return v2;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:600*/if (a1.isCodeAttribute()) {
            /*SL:601*/a1.next = this.cattrs;
            /*SL:602*/this.cattrs = a1;
        }
        else {
            /*SL:604*/a1.next = this.attrs;
            /*SL:605*/this.attrs = a1;
        }
    }
    
    public void visitCode() {
    }
    
    public void visitFrame(final int v-5, final int v-4, final Object[] v-3, final int v-2, final Object[] v-1) {
        /*SL:616*/if (this.compute == 0) {
            /*SL:617*/return;
        }
        /*SL:620*/if (this.compute == 1) {
            /*SL:621*/if (this.currentBlock.frame == null) {
                /*SL:625*/this.currentBlock.frame = new CurrentFrame();
                /*SL:626*/this.currentBlock.frame.owner = this.currentBlock;
                /*SL:627*/this.currentBlock.frame.initInputFrame(this.cw, this.access, /*EL:628*/Type.getArgumentTypes(this.descriptor), v-4);
                /*SL:629*/this.visitImplicitFirstFrame();
            }
            else {
                /*SL:631*/if (v-5 == -1) {
                    /*SL:632*/this.currentBlock.frame.set(this.cw, v-4, v-3, v-2, v-1);
                }
                /*SL:640*/this.visitFrame(this.currentBlock.frame);
            }
        }
        else/*SL:642*/ if (v-5 == -1) {
            /*SL:643*/if (this.previousFrame == null) {
                /*SL:644*/this.visitImplicitFirstFrame();
            }
            /*SL:646*/this.currentLocals = v-4;
            int a3 = /*EL:647*/this.startFrame(this.code.length, v-4, v-2);
            /*SL:648*/for (int a2 = 0; a2 < v-4; ++a2) {
                /*SL:649*/if (v-3[a2] instanceof String) {
                    /*SL:651*/this.frame[a3++] = (0x1700000 | this.cw.addType((String)v-3[a2]));
                }
                else/*SL:652*/ if (v-3[a2] instanceof Integer) {
                    /*SL:653*/this.frame[a3++] = (int)v-3[a2];
                }
                else {
                    /*SL:656*/this.frame[a3++] = (0x1800000 | this.cw.addUninitializedType("", ((Label)v-3[a2]).position));
                }
            }
            /*SL:660*/for (a3 = 0; a3 < v-2; ++a3) {
                /*SL:661*/if (v-1[a3] instanceof String) {
                    /*SL:663*/this.frame[a3++] = (0x1700000 | this.cw.addType((String)v-1[a3]));
                }
                else/*SL:664*/ if (v-1[a3] instanceof Integer) {
                    /*SL:665*/this.frame[a3++] = (int)v-1[a3];
                }
                else {
                    /*SL:668*/this.frame[a3++] = (0x1800000 | this.cw.addUninitializedType("", ((Label)v-1[a3]).position));
                }
            }
            /*SL:672*/this.endFrame();
        }
        else {
            final int v0;
            /*SL:675*/if (this.stackMap == null) {
                /*SL:676*/this.stackMap = new ByteVector();
                final int a4 = /*EL:677*/this.code.length;
            }
            else {
                /*SL:679*/v0 = this.code.length - this.previousFrameOffset - 1;
                /*SL:680*/if (v0 < 0) {
                    /*SL:681*/if (v-5 == 3) {
                        /*SL:682*/return;
                    }
                    /*SL:684*/throw new IllegalStateException();
                }
            }
            /*SL:689*/switch (v-5) {
                case 0: {
                    /*SL:691*/this.currentLocals = v-4;
                    /*SL:692*/this.stackMap.putByte(255).putShort(v0).putShort(v-4);
                    /*SL:693*/for (int a5 = 0; a5 < v-4; ++a5) {
                        /*SL:694*/this.writeFrameType(v-3[a5]);
                    }
                    /*SL:696*/this.stackMap.putShort(v-2);
                    /*SL:697*/for (int v = 0; v < v-2; ++v) {
                        /*SL:698*/this.writeFrameType(v-1[v]);
                    }
                    /*SL:700*/break;
                }
                case 1: {
                    /*SL:702*/this.currentLocals += v-4;
                    /*SL:703*/this.stackMap.putByte(251 + v-4).putShort(v0);
                    /*SL:704*/for (int v = 0; v < v-4; ++v) {
                        /*SL:705*/this.writeFrameType(v-3[v]);
                    }
                    /*SL:707*/break;
                }
                case 2: {
                    /*SL:709*/this.currentLocals -= v-4;
                    /*SL:710*/this.stackMap.putByte(251 - v-4).putShort(v0);
                    /*SL:711*/break;
                }
                case 3: {
                    /*SL:713*/if (v0 < 64) {
                        /*SL:714*/this.stackMap.putByte(v0);
                        break;
                    }
                    /*SL:716*/this.stackMap.putByte(251).putShort(v0);
                    /*SL:718*/break;
                }
                case 4: {
                    /*SL:720*/if (v0 < 64) {
                        /*SL:721*/this.stackMap.putByte(64 + v0);
                    }
                    else {
                        /*SL:723*/this.stackMap.putByte(247).putShort(v0);
                    }
                    /*SL:726*/this.writeFrameType(v-1[0]);
                    break;
                }
            }
            /*SL:730*/this.previousFrameOffset = this.code.length;
            /*SL:731*/++this.frameCount;
        }
        /*SL:734*/this.maxStack = Math.max(this.maxStack, v-2);
        /*SL:735*/this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
    }
    
    public void visitInsn(final int v2) {
        /*SL:740*/this.lastCodeOffset = this.code.length;
        /*SL:742*/this.code.putByte(v2);
        /*SL:745*/if (this.currentBlock != null) {
            /*SL:746*/if (this.compute == 0 || this.compute == 1) {
                /*SL:747*/this.currentBlock.frame.execute(v2, 0, null, null);
            }
            else {
                final int a1 = /*EL:750*/this.stackSize + Frame.SIZE[v2];
                /*SL:751*/if (a1 > this.maxStackSize) {
                    /*SL:752*/this.maxStackSize = a1;
                }
                /*SL:754*/this.stackSize = a1;
            }
            /*SL:757*/if ((v2 >= 172 && v2 <= 177) || v2 == 191) {
                /*SL:759*/this.noSuccessor();
            }
        }
    }
    
    public void visitIntInsn(final int v1, final int v2) {
        /*SL:766*/this.lastCodeOffset = this.code.length;
        /*SL:768*/if (this.currentBlock != null) {
            /*SL:769*/if (this.compute == 0 || this.compute == 1) {
                /*SL:770*/this.currentBlock.frame.execute(v1, v2, null, null);
            }
            else/*SL:771*/ if (v1 != 188) {
                final int a1 = /*EL:774*/this.stackSize + 1;
                /*SL:775*/if (a1 > this.maxStackSize) {
                    /*SL:776*/this.maxStackSize = a1;
                }
                /*SL:778*/this.stackSize = a1;
            }
        }
        /*SL:782*/if (v1 == 17) {
            /*SL:783*/this.code.put12(v1, v2);
        }
        else {
            /*SL:785*/this.code.put11(v1, v2);
        }
    }
    
    public void visitVarInsn(final int v-1, final int v0) {
        /*SL:791*/this.lastCodeOffset = this.code.length;
        /*SL:793*/if (this.currentBlock != null) {
            /*SL:794*/if (this.compute == 0 || this.compute == 1) {
                /*SL:795*/this.currentBlock.frame.execute(v-1, v0, null, null);
            }
            else/*SL:798*/ if (v-1 == 169) {
                final Label currentBlock = /*EL:800*/this.currentBlock;
                currentBlock.status |= 0x100;
                /*SL:803*/this.currentBlock.inputStackTop = this.stackSize;
                /*SL:804*/this.noSuccessor();
            }
            else {
                final int a1 = /*EL:806*/this.stackSize + Frame.SIZE[v-1];
                /*SL:807*/if (a1 > this.maxStackSize) {
                    /*SL:808*/this.maxStackSize = a1;
                }
                /*SL:810*/this.stackSize = a1;
            }
        }
        /*SL:814*/if (this.compute != 3) {
            final int v;
            /*SL:817*/if (v-1 == 22 || v-1 == 24 || v-1 == 55 || v-1 == 57) {
                final int a2 = /*EL:819*/v0 + 2;
            }
            else {
                /*SL:821*/v = v0 + 1;
            }
            /*SL:823*/if (v > this.maxLocals) {
                /*SL:824*/this.maxLocals = v;
            }
        }
        /*SL:828*/if (v0 < 4 && v-1 != 169) {
            int v;
            /*SL:830*/if (v-1 < 54) {
                /*SL:832*/v = 26 + (v-1 - 21 << 2) + v0;
            }
            else {
                /*SL:835*/v = 59 + (v-1 - 54 << 2) + v0;
            }
            /*SL:837*/this.code.putByte(v);
        }
        else/*SL:838*/ if (v0 >= 256) {
            /*SL:839*/this.code.putByte(196).put12(v-1, v0);
        }
        else {
            /*SL:841*/this.code.put11(v-1, v0);
        }
        /*SL:843*/if (v-1 >= 54 && this.compute == 0 && this.handlerCount > 0) {
            /*SL:844*/this.visitLabel(new Label());
        }
    }
    
    public void visitTypeInsn(final int v1, final String v2) {
        /*SL:850*/this.lastCodeOffset = this.code.length;
        final Item v3 = /*EL:851*/this.cw.newClassItem(v2);
        /*SL:853*/if (this.currentBlock != null) {
            /*SL:854*/if (this.compute == 0 || this.compute == 1) {
                /*SL:855*/this.currentBlock.frame.execute(v1, this.code.length, this.cw, v3);
            }
            else/*SL:856*/ if (v1 == 187) {
                final int a1 = /*EL:859*/this.stackSize + 1;
                /*SL:860*/if (a1 > this.maxStackSize) {
                    /*SL:861*/this.maxStackSize = a1;
                }
                /*SL:863*/this.stackSize = a1;
            }
        }
        /*SL:867*/this.code.put12(v1, v3.index);
    }
    
    public void visitFieldInsn(final int v-4, final String v-3, final String v-2, final String v-1) {
        /*SL:873*/this.lastCodeOffset = this.code.length;
        final Item v0 = /*EL:874*/this.cw.newFieldItem(v-3, v-2, v-1);
        /*SL:876*/if (this.currentBlock != null) {
            /*SL:877*/if (this.compute == 0 || this.compute == 1) {
                /*SL:878*/this.currentBlock.frame.execute(v-4, 0, this.cw, v0);
            }
            else {
                final char v = /*EL:882*/v-1.charAt(0);
                final int a4;
                /*SL:883*/switch (v-4) {
                    case 178: {
                        final int a1 = /*EL:885*/this.stackSize + ((v == 'D' || v == 'J') ? 2 : 1);
                        /*SL:886*/break;
                    }
                    case 179: {
                        final int a2 = /*EL:888*/this.stackSize + ((v == 'D' || v == 'J') ? -2 : -1);
                        /*SL:889*/break;
                    }
                    case 180: {
                        final int a3 = /*EL:891*/this.stackSize + ((v == 'D' || v == 'J') ? 1 : 0);
                        /*SL:892*/break;
                    }
                    default: {
                        /*SL:895*/a4 = this.stackSize + ((v == 'D' || v == 'J') ? -3 : -2);
                        break;
                    }
                }
                /*SL:899*/if (a4 > this.maxStackSize) {
                    /*SL:900*/this.maxStackSize = a4;
                }
                /*SL:902*/this.stackSize = a4;
            }
        }
        /*SL:906*/this.code.put12(v-4, v0.index);
    }
    
    public void visitMethodInsn(final int a4, final String a5, final String v1, final String v2, final boolean v3) {
        /*SL:912*/this.lastCodeOffset = this.code.length;
        final Item v4 = /*EL:913*/this.cw.newMethodItem(a5, v1, v2, v3);
        int v5 = /*EL:914*/v4.intVal;
        /*SL:916*/if (this.currentBlock != null) {
            /*SL:917*/if (this.compute == 0 || this.compute == 1) {
                /*SL:918*/this.currentBlock.frame.execute(a4, 0, this.cw, v4);
            }
            else {
                /*SL:928*/if (v5 == 0) {
                    /*SL:931*/v5 = Type.getArgumentsAndReturnSizes(v2);
                    /*SL:934*/v4.intVal = v5;
                }
                final int a7;
                /*SL:937*/if (a4 == 184) {
                    final int a6 = /*EL:938*/this.stackSize - (v5 >> 2) + (v5 & 0x3) + 1;
                }
                else {
                    /*SL:940*/a7 = this.stackSize - (v5 >> 2) + (v5 & 0x3);
                }
                /*SL:943*/if (a7 > this.maxStackSize) {
                    /*SL:944*/this.maxStackSize = a7;
                }
                /*SL:946*/this.stackSize = a7;
            }
        }
        /*SL:950*/if (a4 == 185) {
            /*SL:951*/if (v5 == 0) {
                /*SL:952*/v5 = Type.getArgumentsAndReturnSizes(v2);
                /*SL:953*/v4.intVal = v5;
            }
            /*SL:955*/this.code.put12(185, v4.index).put11(v5 >> 2, 0);
        }
        else {
            /*SL:957*/this.code.put12(a4, v4.index);
        }
    }
    
    public void visitInvokeDynamicInsn(final String a3, final String a4, final Handle v1, final Object... v2) {
        /*SL:964*/this.lastCodeOffset = this.code.length;
        final Item v3 = /*EL:965*/this.cw.newInvokeDynamicItem(a3, a4, v1, v2);
        int v4 = /*EL:966*/v3.intVal;
        /*SL:968*/if (this.currentBlock != null) {
            /*SL:969*/if (this.compute == 0 || this.compute == 1) {
                /*SL:970*/this.currentBlock.frame.execute(186, 0, this.cw, v3);
            }
            else {
                /*SL:980*/if (v4 == 0) {
                    /*SL:983*/v4 = Type.getArgumentsAndReturnSizes(a4);
                    /*SL:986*/v3.intVal = v4;
                }
                final int a5 = /*EL:988*/this.stackSize - (v4 >> 2) + (v4 & 0x3) + 1;
                /*SL:991*/if (a5 > this.maxStackSize) {
                    /*SL:992*/this.maxStackSize = a5;
                }
                /*SL:994*/this.stackSize = a5;
            }
        }
        /*SL:998*/this.code.put12(186, v3.index);
        /*SL:999*/this.code.putShort(0);
    }
    
    public void visitJumpInsn(int a1, final Label a2) {
        final boolean v1 = /*EL:1004*/a1 >= 200;
        /*SL:1005*/a1 = (v1 ? (a1 - 33) : a1);
        /*SL:1006*/this.lastCodeOffset = this.code.length;
        Label v2 = /*EL:1007*/null;
        /*SL:1009*/if (this.currentBlock != null) {
            /*SL:1010*/if (this.compute == 0) {
                /*SL:1011*/this.currentBlock.frame.execute(a1, 0, null, null);
                final Label first = /*EL:1013*/a2.getFirst();
                first.status |= 0x10;
                /*SL:1015*/this.addSuccessor(0, a2);
                /*SL:1016*/if (a1 != 167) {
                    /*SL:1018*/v2 = new Label();
                }
            }
            else/*SL:1020*/ if (this.compute == 1) {
                /*SL:1021*/this.currentBlock.frame.execute(a1, 0, null, null);
            }
            else/*SL:1023*/ if (a1 == 168) {
                /*SL:1024*/if ((a2.status & 0x200) == 0x0) {
                    /*SL:1025*/a2.status |= 0x200;
                    /*SL:1026*/++this.subroutines;
                }
                final Label currentBlock = /*EL:1028*/this.currentBlock;
                currentBlock.status |= 0x80;
                /*SL:1029*/this.addSuccessor(this.stackSize + 1, a2);
                /*SL:1031*/v2 = new Label();
            }
            else {
                /*SL:1043*/this.addSuccessor(this.stackSize += Frame.SIZE[a1], a2);
            }
        }
        /*SL:1048*/if ((a2.status & 0x2) != 0x0 && a2.position - this.code.length < -32768) {
            /*SL:1057*/if (a1 == 167) {
                /*SL:1058*/this.code.putByte(200);
            }
            else/*SL:1059*/ if (a1 == 168) {
                /*SL:1060*/this.code.putByte(201);
            }
            else {
                /*SL:1064*/if (v2 != null) {
                    final Label label = /*EL:1065*/v2;
                    label.status |= 0x10;
                }
                /*SL:1067*/this.code.putByte((a1 <= 166) ? ((a1 + 1 ^ 0x1) - 1) : (a1 ^ 0x1));
                /*SL:1069*/this.code.putShort(8);
                /*SL:1070*/this.code.putByte(200);
            }
            /*SL:1072*/a2.put(this, this.code, this.code.length - 1, true);
        }
        else/*SL:1073*/ if (v1) {
            /*SL:1079*/this.code.putByte(a1 + 33);
            /*SL:1080*/a2.put(this, this.code, this.code.length - 1, true);
        }
        else {
            /*SL:1088*/this.code.putByte(a1);
            /*SL:1089*/a2.put(this, this.code, this.code.length - 1, false);
        }
        /*SL:1091*/if (this.currentBlock != null) {
            /*SL:1092*/if (v2 != null) {
                /*SL:1097*/this.visitLabel(v2);
            }
            /*SL:1099*/if (a1 == 167) {
                /*SL:1100*/this.noSuccessor();
            }
        }
    }
    
    public void visitLabel(final Label a1) {
        final ClassWriter cw = /*EL:1108*/this.cw;
        cw.hasAsmInsns |= a1.resolve(this, this.code.length, this.code.data);
        /*SL:1110*/if ((a1.status & 0x1) != 0x0) {
            /*SL:1111*/return;
        }
        /*SL:1113*/if (this.compute == 0) {
            /*SL:1114*/if (this.currentBlock != null) {
                /*SL:1115*/if (a1.position == this.currentBlock.position) {
                    final Label currentBlock = /*EL:1117*/this.currentBlock;
                    currentBlock.status |= (a1.status & 0x10);
                    /*SL:1118*/a1.frame = this.currentBlock.frame;
                    /*SL:1119*/return;
                }
                /*SL:1122*/this.addSuccessor(0, a1);
            }
            /*SL:1125*/this.currentBlock = a1;
            /*SL:1126*/if (a1.frame == null) {
                /*SL:1127*/a1.frame = new Frame();
                /*SL:1128*/a1.frame.owner = a1;
            }
            /*SL:1131*/if (this.previousBlock != null) {
                /*SL:1132*/if (a1.position == this.previousBlock.position) {
                    final Label previousBlock = /*EL:1133*/this.previousBlock;
                    previousBlock.status |= (a1.status & 0x10);
                    /*SL:1134*/a1.frame = this.previousBlock.frame;
                    /*SL:1135*/this.currentBlock = this.previousBlock;
                    /*SL:1136*/return;
                }
                /*SL:1138*/this.previousBlock.successor = a1;
            }
            /*SL:1140*/this.previousBlock = a1;
        }
        else/*SL:1141*/ if (this.compute == 1) {
            /*SL:1142*/if (this.currentBlock == null) {
                /*SL:1147*/this.currentBlock = a1;
            }
            else {
                /*SL:1151*/this.currentBlock.frame.owner = a1;
            }
        }
        else/*SL:1153*/ if (this.compute == 2) {
            /*SL:1154*/if (this.currentBlock != null) {
                /*SL:1156*/this.currentBlock.outputStackMax = this.maxStackSize;
                /*SL:1157*/this.addSuccessor(this.stackSize, a1);
            }
            /*SL:1160*/this.currentBlock = a1;
            /*SL:1162*/this.stackSize = 0;
            /*SL:1163*/this.maxStackSize = 0;
            /*SL:1165*/if (this.previousBlock != null) {
                /*SL:1166*/this.previousBlock.successor = a1;
            }
            /*SL:1168*/this.previousBlock = a1;
        }
    }
    
    public void visitLdcInsn(final Object v-1) {
        /*SL:1174*/this.lastCodeOffset = this.code.length;
        final Item v0 = /*EL:1175*/this.cw.newConstItem(v-1);
        /*SL:1177*/if (this.currentBlock != null) {
            /*SL:1178*/if (this.compute == 0 || this.compute == 1) {
                /*SL:1179*/this.currentBlock.frame.execute(18, 0, this.cw, v0);
            }
            else {
                final int v;
                /*SL:1183*/if (v0.type == 5 || v0.type == 6) {
                    final int a1 = /*EL:1184*/this.stackSize + 2;
                }
                else {
                    /*SL:1186*/v = this.stackSize + 1;
                }
                /*SL:1189*/if (v > this.maxStackSize) {
                    /*SL:1190*/this.maxStackSize = v;
                }
                /*SL:1192*/this.stackSize = v;
            }
        }
        int v = /*EL:1196*/v0.index;
        /*SL:1197*/if (v0.type == 5 || v0.type == 6) {
            /*SL:1198*/this.code.put12(20, v);
        }
        else/*SL:1199*/ if (v >= 256) {
            /*SL:1200*/this.code.put12(19, v);
        }
        else {
            /*SL:1202*/this.code.put11(18, v);
        }
    }
    
    public void visitIincInsn(final int v1, final int v2) {
        /*SL:1208*/this.lastCodeOffset = this.code.length;
        /*SL:1209*/if (this.currentBlock != null && /*EL:1210*/(this.compute == 0 || this.compute == 1)) {
            /*SL:1211*/this.currentBlock.frame.execute(132, v1, null, null);
        }
        /*SL:1214*/if (this.compute != 3) {
            final int a1 = /*EL:1216*/v1 + 1;
            /*SL:1217*/if (a1 > this.maxLocals) {
                /*SL:1218*/this.maxLocals = a1;
            }
        }
        /*SL:1222*/if (v1 > 255 || v2 > 127 || v2 < -128) {
            /*SL:1223*/this.code.putByte(196).put12(132, v1).putShort(v2);
        }
        else {
            /*SL:1226*/this.code.putByte(132).put11(v1, v2);
        }
    }
    
    public void visitTableSwitchInsn(final int a3, final int a4, final Label v1, final Label... v2) {
        /*SL:1233*/this.lastCodeOffset = this.code.length;
        final int v3 = /*EL:1235*/this.code.length;
        /*SL:1236*/this.code.putByte(170);
        /*SL:1237*/this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        /*SL:1238*/v1.put(this, this.code, v3, true);
        /*SL:1239*/this.code.putInt(a3).putInt(a4);
        /*SL:1240*/for (int a5 = 0; a5 < v2.length; ++a5) {
            /*SL:1241*/v2[a5].put(this, this.code, v3, true);
        }
        /*SL:1244*/this.visitSwitchInsn(v1, v2);
    }
    
    public void visitLookupSwitchInsn(final Label a3, final int[] v1, final Label[] v2) {
        /*SL:1250*/this.lastCodeOffset = this.code.length;
        final int v3 = /*EL:1252*/this.code.length;
        /*SL:1253*/this.code.putByte(171);
        /*SL:1254*/this.code.putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        /*SL:1255*/a3.put(this, this.code, v3, true);
        /*SL:1256*/this.code.putInt(v2.length);
        /*SL:1257*/for (int a4 = 0; a4 < v2.length; ++a4) {
            /*SL:1258*/this.code.putInt(v1[a4]);
            /*SL:1259*/v2[a4].put(this, this.code, v3, true);
        }
        /*SL:1262*/this.visitSwitchInsn(a3, v2);
    }
    
    private void visitSwitchInsn(final Label v2, final Label[] v3) {
        /*SL:1267*/if (this.currentBlock != null) {
            /*SL:1268*/if (this.compute == 0) {
                /*SL:1269*/this.currentBlock.frame.execute(171, 0, null, null);
                /*SL:1271*/this.addSuccessor(0, v2);
                final Label first = /*EL:1272*/v2.getFirst();
                first.status |= 0x10;
                /*SL:1273*/for (int a1 = 0; a1 < v3.length; ++a1) {
                    /*SL:1274*/this.addSuccessor(0, v3[a1]);
                    final Label first2 = /*EL:1275*/v3[a1].getFirst();
                    first2.status |= 0x10;
                }
            }
            else {
                /*SL:1281*/this.addSuccessor(--this.stackSize, v2);
                /*SL:1282*/for (int a2 = 0; a2 < v3.length; ++a2) {
                    /*SL:1283*/this.addSuccessor(this.stackSize, v3[a2]);
                }
            }
            /*SL:1287*/this.noSuccessor();
        }
    }
    
    public void visitMultiANewArrayInsn(final String a1, final int a2) {
        /*SL:1293*/this.lastCodeOffset = this.code.length;
        final Item v1 = /*EL:1294*/this.cw.newClassItem(a1);
        /*SL:1296*/if (this.currentBlock != null) {
            /*SL:1297*/if (this.compute == 0 || this.compute == 1) {
                /*SL:1298*/this.currentBlock.frame.execute(197, a2, this.cw, v1);
            }
            else {
                /*SL:1302*/this.stackSize += 1 - a2;
            }
        }
        /*SL:1306*/this.code.put12(197, v1.index).putByte(a2);
    }
    
    public AnnotationVisitor visitInsnAnnotation(int a1, final TypePath a2, final String a3, final boolean a4) {
        final ByteVector v1 = /*EL:1315*/new ByteVector();
        /*SL:1317*/a1 = ((a1 & 0xFF0000FF) | this.lastCodeOffset << 8);
        /*SL:1318*/AnnotationWriter.putTarget(a1, a2, v1);
        /*SL:1320*/v1.putShort(this.cw.newUTF8(a3)).putShort(0);
        final AnnotationWriter v2 = /*EL:1321*/new AnnotationWriter(this.cw, true, v1, v1, v1.length - 2);
        /*SL:1323*/if (a4) {
            /*SL:1324*/v2.next = this.ctanns;
            /*SL:1325*/this.ctanns = v2;
        }
        else {
            /*SL:1327*/v2.next = this.ictanns;
            /*SL:1328*/this.ictanns = v2;
        }
        /*SL:1330*/return v2;
    }
    
    public void visitTryCatchBlock(final Label a1, final Label a2, final Label a3, final String a4) {
        /*SL:1336*/++this.handlerCount;
        final Handler v1 = /*EL:1337*/new Handler();
        /*SL:1338*/v1.start = a1;
        /*SL:1339*/v1.end = a2;
        /*SL:1340*/v1.handler = a3;
        /*SL:1341*/v1.desc = a4;
        /*SL:1342*/v1.type = ((a4 != null) ? this.cw.newClass(a4) : 0);
        /*SL:1343*/if (this.lastHandler == null) {
            /*SL:1344*/this.firstHandler = v1;
        }
        else {
            /*SL:1346*/this.lastHandler.next = v1;
        }
        /*SL:1348*/this.lastHandler = v1;
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final ByteVector v1 = /*EL:1357*/new ByteVector();
        /*SL:1359*/AnnotationWriter.putTarget(a1, a2, v1);
        /*SL:1361*/v1.putShort(this.cw.newUTF8(a3)).putShort(0);
        final AnnotationWriter v2 = /*EL:1362*/new AnnotationWriter(this.cw, true, v1, v1, v1.length - 2);
        /*SL:1364*/if (a4) {
            /*SL:1365*/v2.next = this.ctanns;
            /*SL:1366*/this.ctanns = v2;
        }
        else {
            /*SL:1368*/v2.next = this.ictanns;
            /*SL:1369*/this.ictanns = v2;
        }
        /*SL:1371*/return v2;
    }
    
    public void visitLocalVariable(final String a4, final String a5, final String a6, final Label v1, final Label v2, final int v3) {
        /*SL:1378*/if (a6 != null) {
            /*SL:1379*/if (this.localVarType == null) {
                /*SL:1380*/this.localVarType = new ByteVector();
            }
            /*SL:1382*/++this.localVarTypeCount;
            /*SL:1383*/this.localVarType.putShort(v1.position).putShort(v2.position - v1.position).putShort(/*EL:1384*/this.cw.newUTF8(a4)).putShort(/*EL:1385*/this.cw.newUTF8(a6)).putShort(v3);
        }
        /*SL:1388*/if (this.localVar == null) {
            /*SL:1389*/this.localVar = new ByteVector();
        }
        /*SL:1391*/++this.localVarCount;
        /*SL:1392*/this.localVar.putShort(v1.position).putShort(v2.position - v1.position).putShort(/*EL:1393*/this.cw.newUTF8(a4)).putShort(/*EL:1394*/this.cw.newUTF8(a5)).putShort(v3);
        /*SL:1396*/if (this.compute != 3) {
            final char a7 = /*EL:1398*/a5.charAt(0);
            final int a8 = /*EL:1399*/v3 + ((a7 == 'J' || a7 == 'D') ? 2 : 1);
            /*SL:1400*/if (a8 > this.maxLocals) {
                /*SL:1401*/this.maxLocals = a8;
            }
        }
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int a4, final TypePath a5, final Label[] a6, final Label[] a7, final int[] v1, final String v2, final boolean v3) {
        final ByteVector v4 = /*EL:1413*/new ByteVector();
        /*SL:1415*/v4.putByte(a4 >>> 24).putShort(a6.length);
        /*SL:1416*/for (int a8 = 0; a8 < a6.length; ++a8) {
            /*SL:1417*/v4.putShort(a6[a8].position).putShort(a7[a8].position - a6[a8].position).putShort(/*EL:1418*/v1[a8]);
        }
        /*SL:1421*/if (a5 == null) {
            /*SL:1422*/v4.putByte(0);
        }
        else {
            final int a9 = /*EL:1424*/a5.b[a5.offset] * 2 + 1;
            /*SL:1425*/v4.putByteArray(a5.b, a5.offset, a9);
        }
        /*SL:1428*/v4.putShort(this.cw.newUTF8(v2)).putShort(0);
        final AnnotationWriter v5 = /*EL:1429*/new AnnotationWriter(this.cw, true, v4, v4, v4.length - 2);
        /*SL:1431*/if (v3) {
            /*SL:1432*/v5.next = this.ctanns;
            /*SL:1433*/this.ctanns = v5;
        }
        else {
            /*SL:1435*/v5.next = this.ictanns;
            /*SL:1436*/this.ictanns = v5;
        }
        /*SL:1438*/return v5;
    }
    
    public void visitLineNumber(final int a1, final Label a2) {
        /*SL:1443*/if (this.lineNumber == null) {
            /*SL:1444*/this.lineNumber = new ByteVector();
        }
        /*SL:1446*/++this.lineNumberCount;
        /*SL:1447*/this.lineNumber.putShort(a2.position);
        /*SL:1448*/this.lineNumber.putShort(a1);
    }
    
    public void visitMaxs(final int v-3, final int v-2) {
        /*SL:1453*/if (this.compute == 0) {
            /*SL:1456*/for (Handler handler = this.firstHandler; handler != null; /*SL:1478*/handler = handler.next) {
                Label a2 = handler.start.getFirst();
                final Label v1 = handler.handler.getFirst();
                final Label v2 = handler.end.getFirst();
                final String v3 = (handler.desc == null) ? "java/lang/Throwable" : handler.desc;
                final int v4 = 0x1700000 | this.cw.addType(v3);
                final Label label = v1;
                label.status |= 0x10;
                while (a2 != v2) {
                    a2 = new Edge();
                    a2.info = v4;
                    a2.successor = v1;
                    a2.next = a2.successors;
                    a2.successors = a2;
                    a2 = a2.successor;
                }
            }
            Frame v5 = /*EL:1482*/this.labels.frame;
            /*SL:1483*/v5.initInputFrame(this.cw, this.access, Type.getArgumentTypes(this.descriptor), this.maxLocals);
            /*SL:1485*/this.visitFrame(v5);
            int v6 = /*EL:1493*/0;
            Label v2 = /*EL:1494*/this.labels;
            /*SL:1495*/while (v2 != null) {
                final Label v7 = /*EL:1497*/v2;
                /*SL:1498*/v2 = v2.next;
                /*SL:1499*/v7.next = null;
                /*SL:1500*/v5 = v7.frame;
                /*SL:1502*/if ((v7.status & 0x10) != 0x0) {
                    final Label label2 = /*EL:1503*/v7;
                    label2.status |= 0x20;
                }
                final Label label3 = /*EL:1506*/v7;
                label3.status |= 0x40;
                final int v4 = /*EL:1508*/v5.inputStack.length + v7.outputStackMax;
                /*SL:1509*/if (v4 > v6) {
                    /*SL:1510*/v6 = v4;
                }
                /*SL:1514*/for (Edge v8 = v7.successors; v8 != null; /*SL:1523*/v8 = v8.next) {
                    final Label v9 = v8.successor.getFirst();
                    final boolean v10 = v5.merge(this.cw, v9.frame, v8.info);
                    if (v10 && v9.next == null) {
                        v9.next = v2;
                        v2 = v9;
                    }
                }
            }
            /*SL:1529*/for (Label v7 = this.labels; v7 != null; /*SL:1557*/v7 = v7.successor) {
                v5 = v7.frame;
                if ((v7.status & 0x20) != 0x0) {
                    this.visitFrame(v5);
                }
                if ((v7.status & 0x40) == 0x0) {
                    final Label v11 = v7.successor;
                    final int v12 = v7.position;
                    final int v13 = ((v11 == null) ? this.code.length : v11.position) - 1;
                    if (v13 >= v12) {
                        v6 = Math.max(v6, 1);
                        for (int v14 = v12; v14 < v13; ++v14) {
                            this.code.data[v14] = 0;
                        }
                        this.code.data[v13] = -65;
                        int v14 = this.startFrame(v12, 0, 1);
                        this.frame[v14] = (0x1700000 | this.cw.addType("java/lang/Throwable"));
                        this.endFrame();
                        this.firstHandler = Handler.remove(this.firstHandler, v7, v11);
                    }
                }
            }
            Handler handler = /*EL:1560*/this.firstHandler;
            /*SL:1561*/this.handlerCount = 0;
            /*SL:1562*/while (handler != null) {
                /*SL:1563*/++this.handlerCount;
                /*SL:1564*/handler = handler.next;
            }
            /*SL:1567*/this.maxStack = v6;
        }
        else/*SL:1568*/ if (this.compute == 2) {
            /*SL:1571*/for (Handler handler = this.firstHandler; handler != null; /*SL:1595*/handler = handler.next) {
                Label v15 = handler.start;
                final Label v1 = handler.handler;
                for (Label v2 = handler.end; v15 != v2; v15 = v15.successor) {
                    final Edge v16 = new Edge();
                    v16.info = Integer.MAX_VALUE;
                    v16.successor = v1;
                    if ((v15.status & 0x80) == 0x0) {
                        v16.next = v15.successors;
                        v15.successors = v16;
                    }
                    else {
                        v16.next = v15.successors.next.next;
                        v15.successors.next.next = v16;
                    }
                }
            }
            /*SL:1598*/if (this.subroutines > 0) {
                int v17 = /*EL:1605*/0;
                /*SL:1606*/this.labels.visitSubroutine(null, 1L, this.subroutines);
                /*SL:1609*/for (Label v1 = this.labels; v1 != null; /*SL:1621*/v1 = v1.successor) {
                    if ((v1.status & 0x80) != 0x0) {
                        final Label v2 = v1.successors.next.successor;
                        if ((v2.status & 0x400) == 0x0) {
                            ++v17;
                            v2.visitSubroutine(null, v17 / 32L << 32 | 1L << v17 % 32, this.subroutines);
                        }
                    }
                }
                /*SL:1625*/for (Label v1 = this.labels; v1 != null; /*SL:1636*/v1 = v1.successor) {
                    if ((v1.status & 0x80) != 0x0) {
                        for (Label v2 = this.labels; v2 != null; v2 = v2.successor) {
                            final Label label4 = v2;
                            label4.status &= 0xFFFFF7FF;
                        }
                        final Label v7 = v1.successors.next.successor;
                        v7.visitSubroutine(v1, 0L, this.subroutines);
                    }
                }
            }
            int v17 = /*EL:1650*/0;
            Label v1 = /*EL:1651*/this.labels;
            /*SL:1652*/while (v1 != null) {
                Label v2 = /*EL:1654*/v1;
                /*SL:1655*/v1 = v1.next;
                final int v18 = /*EL:1657*/v2.inputStackTop;
                final int v4 = /*EL:1658*/v18 + v2.outputStackMax;
                /*SL:1660*/if (v4 > v17) {
                    /*SL:1661*/v17 = v4;
                }
                Edge v8 = /*EL:1664*/v2.successors;
                /*SL:1665*/if ((v2.status & 0x80) != 0x0) {
                    /*SL:1667*/v8 = v8.next;
                }
                /*SL:1669*/while (v8 != null) {
                    /*SL:1670*/v2 = v8.successor;
                    /*SL:1672*/if ((v2.status & 0x8) == 0x0) {
                        /*SL:1674*/v2.inputStackTop = ((v8.info == Integer.MAX_VALUE) ? 1 : (v18 + v8.info));
                        final Label label5 = /*EL:1677*/v2;
                        label5.status |= 0x8;
                        /*SL:1678*/v2.next = v1;
                        /*SL:1679*/v1 = v2;
                    }
                    /*SL:1681*/v8 = v8.next;
                }
            }
            /*SL:1684*/this.maxStack = Math.max(v-3, v17);
        }
        else {
            /*SL:1686*/this.maxStack = v-3;
            /*SL:1687*/this.maxLocals = v-2;
        }
    }
    
    public void visitEnd() {
    }
    
    private void addSuccessor(final int a1, final Label a2) {
        final Edge v1 = /*EL:1709*/new Edge();
        /*SL:1710*/v1.info = a1;
        /*SL:1711*/v1.successor = a2;
        /*SL:1713*/v1.next = this.currentBlock.successors;
        /*SL:1714*/this.currentBlock.successors = v1;
    }
    
    private void noSuccessor() {
        /*SL:1722*/if (this.compute == 0) {
            final Label v1 = /*EL:1723*/new Label();
            /*SL:1724*/v1.frame = new Frame();
            /*SL:1725*/(v1.frame.owner = v1).resolve(/*EL:1726*/this, this.code.length, this.code.data);
            /*SL:1727*/this.previousBlock.successor = v1;
            /*SL:1728*/this.previousBlock = v1;
        }
        else {
            /*SL:1730*/this.currentBlock.outputStackMax = this.maxStackSize;
        }
        /*SL:1732*/if (this.compute != 1) {
            /*SL:1733*/this.currentBlock = null;
        }
    }
    
    private void visitFrame(final Frame v-6) {
        int n = /*EL:1749*/0;
        int i = /*EL:1750*/0;
        int a2 = /*EL:1751*/0;
        final int[] inputLocals = /*EL:1752*/v-6.inputLocals;
        final int[] inputStack = /*EL:1753*/v-6.inputStack;
        /*SL:1756*/for (int v0 = 0; v0 < inputLocals.length; ++v0) {
            final int a1 = /*EL:1757*/inputLocals[v0];
            /*SL:1758*/if (a1 == 16777216) {
                /*SL:1759*/++n;
            }
            else {
                /*SL:1761*/i += n + 1;
                /*SL:1762*/n = 0;
            }
            /*SL:1764*/if (a1 == 16777220 || a1 == 16777219) {
                /*SL:1765*/++v0;
            }
        }
        /*SL:1770*/for (int v0 = 0; v0 < inputStack.length; ++v0) {
            final int v = /*EL:1771*/inputStack[v0];
            /*SL:1772*/++a2;
            /*SL:1773*/if (v == 16777220 || v == 16777219) {
                /*SL:1774*/++v0;
            }
        }
        int v2 = /*EL:1778*/this.startFrame(v-6.owner.position, i, a2);
        int v0 = /*EL:1779*/0;
        while (i > 0) {
            final int v = /*EL:1780*/inputLocals[v0];
            /*SL:1781*/this.frame[v2++] = v;
            /*SL:1782*/if (v == 16777220 || v == 16777219) {
                /*SL:1783*/++v0;
            }
            ++v0;
            --i;
        }
        /*SL:1786*/for (v0 = 0; v0 < inputStack.length; ++v0) {
            final int v = /*EL:1787*/inputStack[v0];
            /*SL:1788*/this.frame[v2++] = v;
            /*SL:1789*/if (v == 16777220 || v == 16777219) {
                /*SL:1790*/++v0;
            }
        }
        /*SL:1793*/this.endFrame();
    }
    
    private void visitImplicitFirstFrame() {
        int startFrame = /*EL:1801*/this.startFrame(0, this.descriptor.length() + 1, 0);
        /*SL:1802*/if ((this.access & 0x8) == 0x0) {
            /*SL:1803*/if ((this.access & 0x80000) == 0x0) {
                /*SL:1804*/this.frame[startFrame++] = (0x1700000 | this.cw.addType(this.cw.thisName));
            }
            else {
                /*SL:1806*/this.frame[startFrame++] = 6;
            }
        }
        int v0 = /*EL:1809*/1;
        while (true) {
            final int v = /*EL:1811*/v0;
            /*SL:1812*/switch (this.descriptor.charAt(v0++)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z': {
                    /*SL:1818*/this.frame[startFrame++] = 1;
                    /*SL:1819*/continue;
                }
                case 'F': {
                    /*SL:1821*/this.frame[startFrame++] = 2;
                    /*SL:1822*/continue;
                }
                case 'J': {
                    /*SL:1824*/this.frame[startFrame++] = 4;
                    /*SL:1825*/continue;
                }
                case 'D': {
                    /*SL:1827*/this.frame[startFrame++] = 3;
                    /*SL:1828*/continue;
                }
                case '[': {
                    /*SL:1830*/while (this.descriptor.charAt(v0) == '[') {
                        /*SL:1831*/++v0;
                    }
                    /*SL:1833*/if (this.descriptor.charAt(v0) == 'L') {
                        /*SL:1834*/++v0;
                        /*SL:1835*/while (this.descriptor.charAt(v0) != ';') {
                            /*SL:1836*/++v0;
                        }
                    }
                    /*SL:1840*/this.frame[startFrame++] = (0x1700000 | this.cw.addType(this.descriptor.substring(v, ++v0)));
                    /*SL:1841*/continue;
                }
                case 'L': {
                    /*SL:1843*/while (this.descriptor.charAt(v0) != ';') {
                        /*SL:1844*/++v0;
                    }
                    /*SL:1847*/this.frame[startFrame++] = (0x1700000 | this.cw.addType(this.descriptor.substring(v + 1, v0++)));
                    /*SL:1848*/continue;
                }
                default: {
                    /*SL:1853*/this.frame[1] = startFrame - 3;
                    /*SL:1854*/this.endFrame();
                }
            }
        }
    }
    
    private int startFrame(final int a1, final int a2, final int a3) {
        final int v1 = /*EL:1869*/3 + a2 + a3;
        /*SL:1870*/if (this.frame == null || this.frame.length < v1) {
            /*SL:1871*/this.frame = new int[v1];
        }
        /*SL:1873*/this.frame[0] = a1;
        /*SL:1874*/this.frame[1] = a2;
        /*SL:1875*/this.frame[2] = a3;
        /*SL:1876*/return 3;
    }
    
    private void endFrame() {
        /*SL:1884*/if (this.previousFrame != null) {
            /*SL:1885*/if (this.stackMap == null) {
                /*SL:1886*/this.stackMap = new ByteVector();
            }
            /*SL:1888*/this.writeFrame();
            /*SL:1889*/++this.frameCount;
        }
        /*SL:1891*/this.previousFrame = this.frame;
        /*SL:1892*/this.frame = null;
    }
    
    private void writeFrame() {
        final int n = /*EL:1900*/this.frame[1];
        final int n2 = /*EL:1901*/this.frame[2];
        /*SL:1902*/if ((this.cw.version & 0xFFFF) < 50) {
            /*SL:1903*/this.stackMap.putShort(this.frame[0]).putShort(n);
            /*SL:1904*/this.writeFrameTypes(3, 3 + n);
            /*SL:1905*/this.stackMap.putShort(n2);
            /*SL:1906*/this.writeFrameTypes(3 + n, 3 + n + n2);
            /*SL:1907*/return;
        }
        int n3 = /*EL:1909*/this.previousFrame[1];
        int n4 = /*EL:1910*/255;
        int v0 = /*EL:1911*/0;
        int v;
        /*SL:1913*/if (this.frameCount == 0) {
            /*SL:1914*/v = this.frame[0];
        }
        else {
            /*SL:1916*/v = this.frame[0] - this.previousFrame[0] - 1;
        }
        /*SL:1918*/if (n2 == 0) {
            /*SL:1919*/v0 = n - n3;
            /*SL:1920*/switch (v0) {
                case -3:
                case -2:
                case -1: {
                    /*SL:1924*/n4 = 248;
                    /*SL:1925*/n3 = n;
                    /*SL:1926*/break;
                }
                case 0: {
                    /*SL:1928*/n4 = ((v < 64) ? 0 : 251);
                    /*SL:1929*/break;
                }
                case 1:
                case 2:
                case 3: {
                    /*SL:1933*/n4 = 252;
                    break;
                }
            }
        }
        else/*SL:1936*/ if (n == n3 && n2 == 1) {
            /*SL:1937*/n4 = ((v < 63) ? 64 : 247);
        }
        /*SL:1940*/if (n4 != 255) {
            int v2 = /*EL:1942*/3;
            /*SL:1943*/for (int v3 = 0; v3 < n3; ++v3) {
                /*SL:1944*/if (this.frame[v2] != this.previousFrame[v2]) {
                    /*SL:1945*/n4 = 255;
                    /*SL:1946*/break;
                }
                /*SL:1948*/++v2;
            }
        }
        /*SL:1951*/switch (n4) {
            case 0: {
                /*SL:1953*/this.stackMap.putByte(v);
                /*SL:1954*/break;
            }
            case 64: {
                /*SL:1956*/this.stackMap.putByte(64 + v);
                /*SL:1957*/this.writeFrameTypes(3 + n, 4 + n);
                /*SL:1958*/break;
            }
            case 247: {
                /*SL:1960*/this.stackMap.putByte(247).putShort(v);
                /*SL:1962*/this.writeFrameTypes(3 + n, 4 + n);
                /*SL:1963*/break;
            }
            case 251: {
                /*SL:1965*/this.stackMap.putByte(251).putShort(v);
                /*SL:1966*/break;
            }
            case 248: {
                /*SL:1968*/this.stackMap.putByte(251 + v0).putShort(v);
                /*SL:1969*/break;
            }
            case 252: {
                /*SL:1971*/this.stackMap.putByte(251 + v0).putShort(v);
                /*SL:1972*/this.writeFrameTypes(3 + n3, 3 + n);
                /*SL:1973*/break;
            }
            default: {
                /*SL:1976*/this.stackMap.putByte(255).putShort(v).putShort(n);
                /*SL:1977*/this.writeFrameTypes(3, 3 + n);
                /*SL:1978*/this.stackMap.putShort(n2);
                /*SL:1979*/this.writeFrameTypes(3 + n, 3 + n + n2);
                break;
            }
        }
    }
    
    private void writeFrameTypes(final int v-2, final int v-1) {
        /*SL:1995*/for (int v0 = v-2; v0 < v-1; ++v0) {
            final int v = /*EL:1996*/this.frame[v0];
            int v2 = /*EL:1997*/v & 0xF0000000;
            /*SL:1998*/if (v2 == 0) {
                final int a1 = /*EL:1999*/v & 0xFFFFF;
                /*SL:2000*/switch (v & 0xFF00000) {
                    case 24117248: {
                        /*SL:2002*/this.stackMap.putByte(7).putShort(this.cw.newClass(this.cw.typeTable[a1].strVal1));
                        /*SL:2004*/break;
                    }
                    case 25165824: {
                        /*SL:2006*/this.stackMap.putByte(8).putShort(this.cw.typeTable[a1].intVal);
                        /*SL:2007*/break;
                    }
                    default: {
                        /*SL:2009*/this.stackMap.putByte(a1);
                        break;
                    }
                }
            }
            else {
                final StringBuilder a2 = /*EL:2012*/new StringBuilder();
                /*SL:2013*/v2 >>= 28;
                /*SL:2014*/while (v2-- > 0) {
                    /*SL:2015*/a2.append('[');
                }
                /*SL:2017*/if ((v & 0xFF00000) == 0x1700000) {
                    /*SL:2018*/a2.append('L');
                    /*SL:2019*/a2.append(this.cw.typeTable[v & 0xFFFFF].strVal1);
                    /*SL:2020*/a2.append(';');
                }
                else {
                    /*SL:2022*/switch (v & 0xF) {
                        case 1: {
                            /*SL:2024*/a2.append('I');
                            /*SL:2025*/break;
                        }
                        case 2: {
                            /*SL:2027*/a2.append('F');
                            /*SL:2028*/break;
                        }
                        case 3: {
                            /*SL:2030*/a2.append('D');
                            /*SL:2031*/break;
                        }
                        case 9: {
                            /*SL:2033*/a2.append('Z');
                            /*SL:2034*/break;
                        }
                        case 10: {
                            /*SL:2036*/a2.append('B');
                            /*SL:2037*/break;
                        }
                        case 11: {
                            /*SL:2039*/a2.append('C');
                            /*SL:2040*/break;
                        }
                        case 12: {
                            /*SL:2042*/a2.append('S');
                            /*SL:2043*/break;
                        }
                        default: {
                            /*SL:2045*/a2.append('J');
                            break;
                        }
                    }
                }
                /*SL:2048*/this.stackMap.putByte(7).putShort(this.cw.newClass(a2.toString()));
            }
        }
    }
    
    private void writeFrameType(final Object a1) {
        /*SL:2054*/if (a1 instanceof String) {
            /*SL:2055*/this.stackMap.putByte(7).putShort(this.cw.newClass((String)a1));
        }
        else/*SL:2056*/ if (a1 instanceof Integer) {
            /*SL:2057*/this.stackMap.putByte((int)a1);
        }
        else {
            /*SL:2059*/this.stackMap.putByte(8).putShort(((Label)a1).position);
        }
    }
    
    final int getSize() {
        /*SL:2073*/if (this.classReaderOffset != 0) {
            /*SL:2074*/return 6 + this.classReaderLength;
        }
        int v0 = /*EL:2076*/8;
        /*SL:2077*/if (this.code.length > 0) {
            /*SL:2078*/if (this.code.length > 65535) {
                /*SL:2079*/throw new RuntimeException("Method code too large!");
            }
            /*SL:2081*/this.cw.newUTF8("Code");
            /*SL:2082*/v0 += 18 + this.code.length + 8 * this.handlerCount;
            /*SL:2083*/if (this.localVar != null) {
                /*SL:2084*/this.cw.newUTF8("LocalVariableTable");
                /*SL:2085*/v0 += 8 + this.localVar.length;
            }
            /*SL:2087*/if (this.localVarType != null) {
                /*SL:2088*/this.cw.newUTF8("LocalVariableTypeTable");
                /*SL:2089*/v0 += 8 + this.localVarType.length;
            }
            /*SL:2091*/if (this.lineNumber != null) {
                /*SL:2092*/this.cw.newUTF8("LineNumberTable");
                /*SL:2093*/v0 += 8 + this.lineNumber.length;
            }
            /*SL:2095*/if (this.stackMap != null) {
                final boolean v = /*EL:2096*/(this.cw.version & 0xFFFF) >= 50;
                /*SL:2097*/this.cw.newUTF8(v ? "StackMapTable" : "StackMap");
                /*SL:2098*/v0 += 8 + this.stackMap.length;
            }
            /*SL:2100*/if (this.ctanns != null) {
                /*SL:2101*/this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
                /*SL:2102*/v0 += 8 + this.ctanns.getSize();
            }
            /*SL:2104*/if (this.ictanns != null) {
                /*SL:2105*/this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
                /*SL:2106*/v0 += 8 + this.ictanns.getSize();
            }
            /*SL:2108*/if (this.cattrs != null) {
                /*SL:2109*/v0 += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
        }
        /*SL:2113*/if (this.exceptionCount > 0) {
            /*SL:2114*/this.cw.newUTF8("Exceptions");
            /*SL:2115*/v0 += 8 + 2 * this.exceptionCount;
        }
        /*SL:2117*/if ((this.access & 0x1000) != 0x0 && /*EL:2118*/((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:2120*/this.cw.newUTF8("Synthetic");
            /*SL:2121*/v0 += 6;
        }
        /*SL:2124*/if ((this.access & 0x20000) != 0x0) {
            /*SL:2125*/this.cw.newUTF8("Deprecated");
            /*SL:2126*/v0 += 6;
        }
        /*SL:2128*/if (this.signature != null) {
            /*SL:2129*/this.cw.newUTF8("Signature");
            /*SL:2130*/this.cw.newUTF8(this.signature);
            /*SL:2131*/v0 += 8;
        }
        /*SL:2133*/if (this.methodParameters != null) {
            /*SL:2134*/this.cw.newUTF8("MethodParameters");
            /*SL:2135*/v0 += 7 + this.methodParameters.length;
        }
        /*SL:2137*/if (this.annd != null) {
            /*SL:2138*/this.cw.newUTF8("AnnotationDefault");
            /*SL:2139*/v0 += 6 + this.annd.length;
        }
        /*SL:2141*/if (this.anns != null) {
            /*SL:2142*/this.cw.newUTF8("RuntimeVisibleAnnotations");
            /*SL:2143*/v0 += 8 + this.anns.getSize();
        }
        /*SL:2145*/if (this.ianns != null) {
            /*SL:2146*/this.cw.newUTF8("RuntimeInvisibleAnnotations");
            /*SL:2147*/v0 += 8 + this.ianns.getSize();
        }
        /*SL:2149*/if (this.tanns != null) {
            /*SL:2150*/this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
            /*SL:2151*/v0 += 8 + this.tanns.getSize();
        }
        /*SL:2153*/if (this.itanns != null) {
            /*SL:2154*/this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
            /*SL:2155*/v0 += 8 + this.itanns.getSize();
        }
        /*SL:2157*/if (this.panns != null) {
            /*SL:2158*/this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
            /*SL:2159*/v0 += 7 + 2 * (this.panns.length - this.synthetics);
            /*SL:2160*/for (int v2 = this.panns.length - 1; v2 >= this.synthetics; --v2) {
                /*SL:2161*/v0 += ((this.panns[v2] == null) ? 0 : this.panns[v2].getSize());
            }
        }
        /*SL:2164*/if (this.ipanns != null) {
            /*SL:2165*/this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
            /*SL:2166*/v0 += 7 + 2 * (this.ipanns.length - this.synthetics);
            /*SL:2167*/for (int v2 = this.ipanns.length - 1; v2 >= this.synthetics; --v2) {
                /*SL:2168*/v0 += ((this.ipanns[v2] == null) ? 0 : this.ipanns[v2].getSize());
            }
        }
        /*SL:2171*/if (this.attrs != null) {
            /*SL:2172*/v0 += this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        /*SL:2174*/return v0;
    }
    
    final void put(final ByteVector v-4) {
        final int n = /*EL:2185*/64;
        final int n2 = /*EL:2186*/0xE0000 | (this.access & 0x40000) / 64;
        /*SL:2189*/v-4.putShort(this.access & ~n2).putShort(this.name).putShort(this.desc);
        /*SL:2190*/if (this.classReaderOffset != 0) {
            /*SL:2191*/v-4.putByteArray(this.cw.cr.b, this.classReaderOffset, this.classReaderLength);
            /*SL:2192*/return;
        }
        int n3 = /*EL:2194*/0;
        /*SL:2195*/if (this.code.length > 0) {
            /*SL:2196*/++n3;
        }
        /*SL:2198*/if (this.exceptionCount > 0) {
            /*SL:2199*/++n3;
        }
        /*SL:2201*/if ((this.access & 0x1000) != 0x0 && /*EL:2202*/((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:2204*/++n3;
        }
        /*SL:2207*/if ((this.access & 0x20000) != 0x0) {
            /*SL:2208*/++n3;
        }
        /*SL:2210*/if (this.signature != null) {
            /*SL:2211*/++n3;
        }
        /*SL:2213*/if (this.methodParameters != null) {
            /*SL:2214*/++n3;
        }
        /*SL:2216*/if (this.annd != null) {
            /*SL:2217*/++n3;
        }
        /*SL:2219*/if (this.anns != null) {
            /*SL:2220*/++n3;
        }
        /*SL:2222*/if (this.ianns != null) {
            /*SL:2223*/++n3;
        }
        /*SL:2225*/if (this.tanns != null) {
            /*SL:2226*/++n3;
        }
        /*SL:2228*/if (this.itanns != null) {
            /*SL:2229*/++n3;
        }
        /*SL:2231*/if (this.panns != null) {
            /*SL:2232*/++n3;
        }
        /*SL:2234*/if (this.ipanns != null) {
            /*SL:2235*/++n3;
        }
        /*SL:2237*/if (this.attrs != null) {
            /*SL:2238*/n3 += this.attrs.getCount();
        }
        /*SL:2240*/v-4.putShort(n3);
        /*SL:2241*/if (this.code.length > 0) {
            int v0 = /*EL:2242*/12 + this.code.length + 8 * this.handlerCount;
            /*SL:2243*/if (this.localVar != null) {
                /*SL:2244*/v0 += 8 + this.localVar.length;
            }
            /*SL:2246*/if (this.localVarType != null) {
                /*SL:2247*/v0 += 8 + this.localVarType.length;
            }
            /*SL:2249*/if (this.lineNumber != null) {
                /*SL:2250*/v0 += 8 + this.lineNumber.length;
            }
            /*SL:2252*/if (this.stackMap != null) {
                /*SL:2253*/v0 += 8 + this.stackMap.length;
            }
            /*SL:2255*/if (this.ctanns != null) {
                /*SL:2256*/v0 += 8 + this.ctanns.getSize();
            }
            /*SL:2258*/if (this.ictanns != null) {
                /*SL:2259*/v0 += 8 + this.ictanns.getSize();
            }
            /*SL:2261*/if (this.cattrs != null) {
                /*SL:2262*/v0 += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
            /*SL:2265*/v-4.putShort(this.cw.newUTF8("Code")).putInt(v0);
            /*SL:2266*/v-4.putShort(this.maxStack).putShort(this.maxLocals);
            /*SL:2267*/v-4.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            /*SL:2268*/v-4.putShort(this.handlerCount);
            /*SL:2269*/if (this.handlerCount > 0) {
                /*SL:2271*/for (Handler a1 = this.firstHandler; a1 != null; /*SL:2274*/a1 = a1.next) {
                    v-4.putShort(a1.start.position).putShort(a1.end.position).putShort(a1.handler.position).putShort(a1.type);
                }
            }
            /*SL:2277*/n3 = 0;
            /*SL:2278*/if (this.localVar != null) {
                /*SL:2279*/++n3;
            }
            /*SL:2281*/if (this.localVarType != null) {
                /*SL:2282*/++n3;
            }
            /*SL:2284*/if (this.lineNumber != null) {
                /*SL:2285*/++n3;
            }
            /*SL:2287*/if (this.stackMap != null) {
                /*SL:2288*/++n3;
            }
            /*SL:2290*/if (this.ctanns != null) {
                /*SL:2291*/++n3;
            }
            /*SL:2293*/if (this.ictanns != null) {
                /*SL:2294*/++n3;
            }
            /*SL:2296*/if (this.cattrs != null) {
                /*SL:2297*/n3 += this.cattrs.getCount();
            }
            /*SL:2299*/v-4.putShort(n3);
            /*SL:2300*/if (this.localVar != null) {
                /*SL:2301*/v-4.putShort(this.cw.newUTF8("LocalVariableTable"));
                /*SL:2302*/v-4.putInt(this.localVar.length + 2).putShort(this.localVarCount);
                /*SL:2303*/v-4.putByteArray(this.localVar.data, 0, this.localVar.length);
            }
            /*SL:2305*/if (this.localVarType != null) {
                /*SL:2306*/v-4.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
                /*SL:2307*/v-4.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
                /*SL:2308*/v-4.putByteArray(this.localVarType.data, 0, this.localVarType.length);
            }
            /*SL:2310*/if (this.lineNumber != null) {
                /*SL:2311*/v-4.putShort(this.cw.newUTF8("LineNumberTable"));
                /*SL:2312*/v-4.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
                /*SL:2313*/v-4.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
            }
            /*SL:2315*/if (this.stackMap != null) {
                final boolean v = /*EL:2316*/(this.cw.version & 0xFFFF) >= 50;
                /*SL:2317*/v-4.putShort(this.cw.newUTF8(v ? "StackMapTable" : "StackMap"));
                /*SL:2318*/v-4.putInt(this.stackMap.length + 2).putShort(this.frameCount);
                /*SL:2319*/v-4.putByteArray(this.stackMap.data, 0, this.stackMap.length);
            }
            /*SL:2321*/if (this.ctanns != null) {
                /*SL:2322*/v-4.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
                /*SL:2323*/this.ctanns.put(v-4);
            }
            /*SL:2325*/if (this.ictanns != null) {
                /*SL:2326*/v-4.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
                /*SL:2327*/this.ictanns.put(v-4);
            }
            /*SL:2329*/if (this.cattrs != null) {
                /*SL:2330*/this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, v-4);
            }
        }
        /*SL:2333*/if (this.exceptionCount > 0) {
            /*SL:2334*/v-4.putShort(this.cw.newUTF8("Exceptions")).putInt(2 * this.exceptionCount + 2);
            /*SL:2336*/v-4.putShort(this.exceptionCount);
            /*SL:2337*/for (int v0 = 0; v0 < this.exceptionCount; ++v0) {
                /*SL:2338*/v-4.putShort(this.exceptions[v0]);
            }
        }
        /*SL:2341*/if ((this.access & 0x1000) != 0x0 && /*EL:2342*/((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:2344*/v-4.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        /*SL:2347*/if ((this.access & 0x20000) != 0x0) {
            /*SL:2348*/v-4.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        /*SL:2350*/if (this.signature != null) {
            /*SL:2351*/v-4.putShort(this.cw.newUTF8("Signature")).putInt(2).putShort(this.cw.newUTF8(this.signature));
        }
        /*SL:2354*/if (this.methodParameters != null) {
            /*SL:2355*/v-4.putShort(this.cw.newUTF8("MethodParameters"));
            /*SL:2356*/v-4.putInt(this.methodParameters.length + 1).putByte(this.methodParametersCount);
            /*SL:2358*/v-4.putByteArray(this.methodParameters.data, 0, this.methodParameters.length);
        }
        /*SL:2360*/if (this.annd != null) {
            /*SL:2361*/v-4.putShort(this.cw.newUTF8("AnnotationDefault"));
            /*SL:2362*/v-4.putInt(this.annd.length);
            /*SL:2363*/v-4.putByteArray(this.annd.data, 0, this.annd.length);
        }
        /*SL:2365*/if (this.anns != null) {
            /*SL:2366*/v-4.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
            /*SL:2367*/this.anns.put(v-4);
        }
        /*SL:2369*/if (this.ianns != null) {
            /*SL:2370*/v-4.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
            /*SL:2371*/this.ianns.put(v-4);
        }
        /*SL:2373*/if (this.tanns != null) {
            /*SL:2374*/v-4.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
            /*SL:2375*/this.tanns.put(v-4);
        }
        /*SL:2377*/if (this.itanns != null) {
            /*SL:2378*/v-4.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
            /*SL:2379*/this.itanns.put(v-4);
        }
        /*SL:2381*/if (this.panns != null) {
            /*SL:2382*/v-4.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
            /*SL:2383*/AnnotationWriter.put(this.panns, this.synthetics, v-4);
        }
        /*SL:2385*/if (this.ipanns != null) {
            /*SL:2386*/v-4.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
            /*SL:2387*/AnnotationWriter.put(this.ipanns, this.synthetics, v-4);
        }
        /*SL:2389*/if (this.attrs != null) {
            /*SL:2390*/this.attrs.put(this.cw, null, 0, -1, -1, v-4);
        }
    }
}
