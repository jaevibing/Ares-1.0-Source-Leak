package javassist.bytecode;

class CodeAnalyzer implements Opcode
{
    private ConstPool constPool;
    private CodeAttribute codeAttr;
    
    public CodeAnalyzer(final CodeAttribute a1) {
        this.codeAttr = a1;
        this.constPool = a1.getConstPool();
    }
    
    public int computeMaxStack() throws BadBytecode {
        final CodeIterator iterator = /*EL:39*/this.codeAttr.iterator();
        final int codeLength = /*EL:40*/iterator.getCodeLength();
        final int[] array = /*EL:41*/new int[codeLength];
        /*SL:42*/this.constPool = this.codeAttr.getConstPool();
        /*SL:43*/this.initStack(array, this.codeAttr);
        boolean v0;
        /*SL:52*/do {
            v0 = false;
            for (int v = 0; v < codeLength; ++v) {
                if (array[v] < 0) {
                    v0 = true;
                    this.visitBytecode(iterator, array, v);
                }
            }
        } while (v0);
        int v = /*EL:54*/1;
        /*SL:55*/for (int v2 = 0; v2 < codeLength; ++v2) {
            /*SL:56*/if (array[v2] > v) {
                /*SL:57*/v = array[v2];
            }
        }
        /*SL:59*/return v - 1;
    }
    
    private void initStack(final int[] v2, final CodeAttribute v3) {
        /*SL:63*/v2[0] = -1;
        final ExceptionTable v4 = /*EL:64*/v3.getExceptionTable();
        /*SL:65*/if (v4 != null) {
            int a2;
            int a2;
            /*SL:67*/for (a2 = v4.size(), a2 = 0; a2 < a2; ++a2) {
                /*SL:68*/v2[v4.handlerPc(a2)] = -2;
            }
        }
    }
    
    private void visitBytecode(final CodeIterator a3, final int[] v1, int v2) throws BadBytecode {
        final int v3 = /*EL:75*/v1.length;
        /*SL:76*/a3.move(v2);
        int v4 = /*EL:77*/-v1[v2];
        final int[] v5 = /*EL:78*/{ /*EL:79*/-1 };
        /*SL:80*/while (a3.hasNext()) {
            /*SL:81*/v2 = a3.next();
            /*SL:82*/v1[v2] = v4;
            final int a4 = /*EL:83*/a3.byteAt(v2);
            /*SL:84*/v4 = this.visitInst(a4, a3, v2, v4);
            /*SL:85*/if (v4 < 1) {
                /*SL:86*/throw new BadBytecode("stack underflow at " + v2);
            }
            /*SL:88*/if (this.processBranch(a4, a3, v2, v3, v1, v4, v5)) {
                /*SL:89*/break;
            }
            /*SL:91*/if (isEnd(a4)) {
                /*SL:92*/break;
            }
            /*SL:94*/if (a4 != 168 && a4 != 201) {
                continue;
            }
            /*SL:95*/--v4;
        }
    }
    
    private boolean processBranch(final int v-11, final CodeIterator v-10, final int v-9, final int v-8, final int[] v-7, final int v-6, final int[] v-5) throws BadBytecode {
        /*SL:103*/if ((153 <= v-11 && v-11 <= 166) || v-11 == 198 || v-11 == 199) {
            final int a1 = /*EL:105*/v-9 + v-10.s16bitAt(v-9 + 1);
            /*SL:106*/this.checkTarget(v-9, a1, v-8, v-7, v-6);
        }
        else {
            /*SL:110*/switch (v-11) {
                case 167: {
                    final int a2 = /*EL:112*/v-9 + v-10.s16bitAt(v-9 + 1);
                    /*SL:113*/this.checkTarget(v-9, a2, v-8, v-7, v-6);
                    /*SL:114*/return true;
                }
                case 200: {
                    final int a3 = /*EL:116*/v-9 + v-10.s32bitAt(v-9 + 1);
                    /*SL:117*/this.checkTarget(v-9, a3, v-8, v-7, v-6);
                    /*SL:118*/return true;
                }
                case 168:
                case 201: {
                    final int a5;
                    /*SL:121*/if (v-11 == 168) {
                        final int a4 = /*EL:122*/v-9 + v-10.s16bitAt(v-9 + 1);
                    }
                    else {
                        /*SL:124*/a5 = v-9 + v-10.s32bitAt(v-9 + 1);
                    }
                    /*SL:126*/this.checkTarget(v-9, a5, v-8, v-7, v-6);
                    /*SL:134*/if (v-5[0] < 0) {
                        /*SL:135*/v-5[0] = v-6;
                        /*SL:136*/return false;
                    }
                    /*SL:138*/if (v-6 == v-5[0]) {
                        /*SL:139*/return false;
                    }
                    /*SL:141*/throw new BadBytecode("sorry, cannot compute this data flow due to JSR: " + v-6 + "," + v-5[0]);
                }
                case 169: {
                    /*SL:145*/if (v-5[0] < 0) {
                        /*SL:146*/v-5[0] = v-6 + 1;
                        /*SL:147*/return false;
                    }
                    /*SL:149*/if (v-6 + 1 == v-5[0]) {
                        /*SL:150*/return true;
                    }
                    /*SL:152*/throw new BadBytecode("sorry, cannot compute this data flow due to RET: " + v-6 + "," + v-5[0]);
                }
                case 170:
                case 171: {
                    int a8 = /*EL:157*/(v-9 & 0xFFFFFFFC) + 4;
                    int a9 = /*EL:158*/v-9 + v-10.s32bitAt(a8);
                    /*SL:159*/this.checkTarget(v-9, a9, v-8, v-7, v-6);
                    /*SL:160*/if (v-11 == 171) {
                        final int a6 = /*EL:161*/v-10.s32bitAt(a8 + 4);
                        /*SL:162*/a8 += 12;
                        /*SL:163*/for (int a7 = 0; a7 < a6; ++a7) {
                            /*SL:164*/a9 = v-9 + v-10.s32bitAt(a8);
                            /*SL:165*/this.checkTarget(v-9, a9, v-8, v-7, v-6);
                            /*SL:167*/a8 += 8;
                        }
                    }
                    else {
                        final int s32bit = /*EL:171*/v-10.s32bitAt(a8 + 4);
                        final int s32bit2 = /*EL:172*/v-10.s32bitAt(a8 + 8);
                        final int v0 = /*EL:173*/s32bit2 - s32bit + 1;
                        /*SL:174*/a8 += 12;
                        /*SL:175*/for (int v = 0; v < v0; ++v) {
                            /*SL:176*/a9 = v-9 + v-10.s32bitAt(a8);
                            /*SL:177*/this.checkTarget(v-9, a9, v-8, v-7, v-6);
                            /*SL:179*/a8 += 4;
                        }
                    }
                    /*SL:183*/return true;
                }
            }
        }
        /*SL:187*/return false;
    }
    
    private void checkTarget(final int a1, final int a2, final int a3, final int[] a4, final int a5) throws BadBytecode {
        /*SL:194*/if (a2 < 0 || a3 <= a2) {
            /*SL:195*/throw new BadBytecode("bad branch offset at " + a1);
        }
        final int v1 = /*EL:197*/a4[a2];
        /*SL:198*/if (v1 == 0) {
            /*SL:199*/a4[a2] = -a5;
        }
        else/*SL:200*/ if (v1 != a5 && v1 != -a5) {
            /*SL:201*/throw new BadBytecode("verification error (" + a5 + "," + v1 + ") at " + a1);
        }
    }
    
    private static boolean isEnd(final int a1) {
        /*SL:206*/return (172 <= a1 && a1 <= 177) || a1 == 191;
    }
    
    private int visitInst(int v2, final CodeIterator v3, final int v4, int v5) throws BadBytecode {
        /*SL:216*/switch (v2) {
            case 180: {
                /*SL:218*/v5 += this.getFieldSize(v3, v4) - 1;
                /*SL:219*/return /*EL:260*/v5;
            }
            case 181: {
                v5 -= this.getFieldSize(v3, v4) + 1;
                return v5;
            }
            case 178: {
                v5 += this.getFieldSize(v3, v4);
                return v5;
            }
            case 179: {
                v5 -= this.getFieldSize(v3, v4);
                return v5;
            }
            case 182:
            case 183: {
                final String a1 = this.constPool.getMethodrefType(v3.u16bitAt(v4 + 1));
                v5 += Descriptor.dataSize(a1) - 1;
                return v5;
            }
            case 184: {
                final String a2 = this.constPool.getMethodrefType(v3.u16bitAt(v4 + 1));
                v5 += Descriptor.dataSize(a2);
                return v5;
            }
            case 185: {
                final String a3 = this.constPool.getInterfaceMethodrefType(v3.u16bitAt(v4 + 1));
                v5 += Descriptor.dataSize(a3) - 1;
                return v5;
            }
            case 186: {
                final String a4 = this.constPool.getInvokeDynamicType(v3.u16bitAt(v4 + 1));
                v5 += Descriptor.dataSize(a4);
                return v5;
            }
            case 191: {
                v5 = 1;
                return v5;
            }
            case 197: {
                v5 += 1 - v3.byteAt(v4 + 3);
                return v5;
            }
            case 196: {
                v2 = v3.byteAt(v4 + 1);
                break;
            }
        }
        v5 += CodeAnalyzer.STACK_GROW[v2];
        return v5;
    }
    
    private int getFieldSize(final CodeIterator a1, final int a2) {
        final String v1 = /*EL:264*/this.constPool.getFieldrefType(a1.u16bitAt(a2 + 1));
        /*SL:265*/return Descriptor.dataSize(v1);
    }
}
