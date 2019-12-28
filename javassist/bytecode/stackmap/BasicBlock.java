package javassist.bytecode.stackmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.BadBytecode;

public class BasicBlock
{
    protected int position;
    protected int length;
    protected int incoming;
    protected BasicBlock[] exit;
    protected boolean stop;
    protected Catch toCatch;
    
    protected BasicBlock(final int a1) {
        this.position = a1;
        this.length = 0;
        this.incoming = 0;
    }
    
    public static BasicBlock find(final BasicBlock[] v1, final int v2) throws BadBytecode {
        /*SL:49*/for (int a2 = 0; a2 < v1.length; ++a2) {
            /*SL:50*/a2 = v1[a2].position;
            /*SL:51*/if (a2 <= v2 && v2 < a2 + v1[a2].length) {
                /*SL:52*/return v1[a2];
            }
        }
        /*SL:55*/throw new BadBytecode("no basic block at " + v2);
    }
    
    @Override
    public String toString() {
        final StringBuffer v1 = /*EL:70*/new StringBuffer();
        final String v2 = /*EL:71*/this.getClass().getName();
        final int v3 = /*EL:72*/v2.lastIndexOf(46);
        /*SL:73*/v1.append((v3 < 0) ? v2 : v2.substring(v3 + 1));
        /*SL:74*/v1.append("[");
        /*SL:75*/this.toString2(v1);
        /*SL:76*/v1.append("]");
        /*SL:77*/return v1.toString();
    }
    
    protected void toString2(final StringBuffer v2) {
        /*SL:81*/v2.append("pos=").append(this.position).append(", len=").append(this.length).append(/*EL:82*/", in=").append(this.incoming).append(", exit{");
        /*SL:84*/if (this.exit != null) {
            /*SL:85*/for (int a1 = 0; a1 < this.exit.length; ++a1) {
                /*SL:86*/v2.append(this.exit[a1].position).append(",");
            }
        }
        /*SL:89*/v2.append("}, {");
        /*SL:91*/for (Catch v3 = this.toCatch; v3 != null; /*SL:94*/v3 = v3.next) {
            v2.append("(").append(v3.body.position).append(", ").append(v3.typeIndex).append("), ");
        }
        /*SL:97*/v2.append("}");
    }
    
    static class JsrBytecode extends BadBytecode
    {
        JsrBytecode() {
            super("JSR");
        }
    }
    
    public static class Catch
    {
        public Catch next;
        public BasicBlock body;
        public int typeIndex;
        
        Catch(final BasicBlock a1, final int a2, final Catch a3) {
            this.body = a1;
            this.typeIndex = a2;
            this.next = a3;
        }
    }
    
    static class Mark implements Comparable
    {
        int position;
        BasicBlock block;
        BasicBlock[] jump;
        boolean alwaysJmp;
        int size;
        Catch catcher;
        
        Mark(final int a1) {
            this.position = a1;
            this.block = null;
            this.jump = null;
            this.alwaysJmp = false;
            this.size = 0;
            this.catcher = null;
        }
        
        @Override
        public int compareTo(final Object v2) {
            /*SL:122*/if (v2 instanceof Mark) {
                final int a1 = /*EL:123*/((Mark)v2).position;
                /*SL:124*/return this.position - a1;
            }
            /*SL:127*/return -1;
        }
        
        void setJump(final BasicBlock[] a1, final int a2, final boolean a3) {
            /*SL:131*/this.jump = a1;
            /*SL:132*/this.size = a2;
            /*SL:133*/this.alwaysJmp = a3;
        }
    }
    
    public static class Maker
    {
        protected BasicBlock makeBlock(final int a1) {
            /*SL:142*/return new BasicBlock(a1);
        }
        
        protected BasicBlock[] makeArray(final int a1) {
            /*SL:146*/return new BasicBlock[a1];
        }
        
        private BasicBlock[] makeArray(final BasicBlock a1) {
            final BasicBlock[] v1 = /*EL:150*/this.makeArray(1);
            /*SL:151*/v1[0] = a1;
            /*SL:152*/return v1;
        }
        
        private BasicBlock[] makeArray(final BasicBlock a1, final BasicBlock a2) {
            final BasicBlock[] v1 = /*EL:156*/this.makeArray(2);
            /*SL:157*/v1[0] = a1;
            /*SL:158*/v1[1] = a2;
            /*SL:159*/return v1;
        }
        
        public BasicBlock[] make(final MethodInfo a1) throws BadBytecode {
            final CodeAttribute v1 = /*EL:163*/a1.getCodeAttribute();
            /*SL:164*/if (v1 == null) {
                /*SL:165*/return null;
            }
            final CodeIterator v2 = /*EL:167*/v1.iterator();
            /*SL:168*/return this.make(v2, 0, v2.getCodeLength(), v1.getExceptionTable());
        }
        
        public BasicBlock[] make(final CodeIterator a1, final int a2, final int a3, final ExceptionTable a4) throws BadBytecode {
            final HashMap v1 = /*EL:175*/this.makeMarks(a1, a2, a3, a4);
            final BasicBlock[] v2 = /*EL:176*/this.makeBlocks(v1);
            /*SL:177*/this.addCatchers(v2, a4);
            /*SL:178*/return v2;
        }
        
        private Mark makeMark(final HashMap a1, final int a2) {
            /*SL:184*/return this.makeMark0(a1, a2, true, true);
        }
        
        private Mark makeMark(final HashMap a1, final int a2, final BasicBlock[] a3, final int a4, final boolean a5) {
            final Mark v1 = /*EL:192*/this.makeMark0(a1, a2, false, false);
            /*SL:193*/v1.setJump(a3, a4, a5);
            /*SL:194*/return v1;
        }
        
        private Mark makeMark0(final HashMap a1, final int a2, final boolean a3, final boolean a4) {
            final Integer v1 = /*EL:199*/new Integer(a2);
            Mark v2 = /*EL:200*/a1.get(v1);
            /*SL:201*/if (v2 == null) {
                /*SL:202*/v2 = new Mark(a2);
                /*SL:203*/a1.put(v1, v2);
            }
            /*SL:206*/if (a3) {
                /*SL:207*/if (v2.block == null) {
                    /*SL:208*/v2.block = this.makeBlock(a2);
                }
                /*SL:210*/if (a4) {
                    final BasicBlock block = /*EL:211*/v2.block;
                    ++block.incoming;
                }
            }
            /*SL:214*/return v2;
        }
        
        private HashMap makeMarks(final CodeIterator v-8, final int v-7, final int v-6, final ExceptionTable v-5) throws BadBytecode {
            /*SL:221*/v-8.begin();
            /*SL:222*/v-8.move(v-7);
            final HashMap hashMap = /*EL:223*/new HashMap();
            /*SL:224*/while (v-8.hasNext()) {
                final int a5 = /*EL:225*/v-8.next();
                /*SL:226*/if (a5 >= v-6) {
                    /*SL:227*/break;
                }
                final int byte1 = /*EL:229*/v-8.byteAt(a5);
                /*SL:230*/if ((153 <= byte1 && byte1 <= 166) || byte1 == 198 || byte1 == 199) {
                    final Mark a1 = /*EL:232*/this.makeMark(hashMap, a5 + v-8.s16bitAt(a5 + 1));
                    final Mark a2 = /*EL:233*/this.makeMark(hashMap, a5 + 3);
                    /*SL:234*/this.makeMark(hashMap, a5, this.makeArray(a1.block, a2.block), 3, false);
                }
                else/*SL:236*/ if (167 <= byte1 && byte1 <= 171) {
                    /*SL:237*/switch (byte1) {
                        case 167: {
                            /*SL:239*/this.makeGoto(hashMap, a5, a5 + v-8.s16bitAt(a5 + 1), 3);
                            /*SL:240*/continue;
                        }
                        case 168: {
                            /*SL:242*/this.makeJsr(hashMap, a5, a5 + v-8.s16bitAt(a5 + 1), 3);
                            /*SL:243*/continue;
                        }
                        case 169: {
                            /*SL:245*/this.makeMark(hashMap, a5, null, 2, true);
                            /*SL:246*/continue;
                        }
                        case 170: {
                            final int a3 = /*EL:248*/(a5 & 0xFFFFFFFC) + 4;
                            final int a4 = /*EL:249*/v-8.s32bitAt(a3 + 4);
                            final int v1 = /*EL:250*/v-8.s32bitAt(a3 + 8);
                            final int v2 = /*EL:251*/v1 - a4 + 1;
                            final BasicBlock[] v3 = /*EL:252*/this.makeArray(v2 + 1);
                            /*SL:253*/v3[0] = this.makeMark(hashMap, a5 + v-8.s32bitAt(a3)).block;
                            int v4 = /*EL:254*/a3 + 12;
                            final int v5 = /*EL:255*/v4 + v2 * 4;
                            int v6 = /*EL:256*/1;
                            /*SL:257*/while (v4 < v5) {
                                /*SL:258*/v3[v6++] = this.makeMark(hashMap, a5 + v-8.s32bitAt(v4)).block;
                                /*SL:259*/v4 += 4;
                            }
                            /*SL:261*/this.makeMark(hashMap, a5, v3, v5 - a5, true);
                            /*SL:262*/continue;
                        }
                        case 171: {
                            final int a6 = /*EL:264*/(a5 & 0xFFFFFFFC) + 4;
                            final int v7 = /*EL:265*/v-8.s32bitAt(a6 + 4);
                            final BasicBlock[] v8 = /*EL:266*/this.makeArray(v7 + 1);
                            /*SL:267*/v8[0] = this.makeMark(hashMap, a5 + v-8.s32bitAt(a6)).block;
                            int v2 = /*EL:268*/a6 + 8 + 4;
                            final int v9 = /*EL:269*/v2 + v7 * 8 - 4;
                            int v4 = /*EL:270*/1;
                            /*SL:271*/while (v2 < v9) {
                                /*SL:272*/v8[v4++] = this.makeMark(hashMap, a5 + v-8.s32bitAt(v2)).block;
                                /*SL:273*/v2 += 8;
                            }
                            /*SL:275*/this.makeMark(hashMap, a5, v8, v9 - a5, true);
                            continue;
                        }
                    }
                }
                else/*SL:278*/ if ((172 <= byte1 && byte1 <= 177) || byte1 == 191) {
                    /*SL:279*/this.makeMark(hashMap, a5, null, 1, true);
                }
                else/*SL:280*/ if (byte1 == 200) {
                    /*SL:281*/this.makeGoto(hashMap, a5, a5 + v-8.s32bitAt(a5 + 1), 5);
                }
                else/*SL:282*/ if (byte1 == 201) {
                    /*SL:283*/this.makeJsr(hashMap, a5, a5 + v-8.s32bitAt(a5 + 1), 5);
                }
                else {
                    /*SL:284*/if (byte1 != 196 || v-8.byteAt(a5 + 1) != 169) {
                        continue;
                    }
                    /*SL:285*/this.makeMark(hashMap, a5, null, 4, true);
                }
            }
            /*SL:288*/if (v-5 != null) {
                int a5 = /*EL:289*/v-5.size();
                /*SL:290*/while (--a5 >= 0) {
                    /*SL:291*/this.makeMark0(hashMap, v-5.startPc(a5), true, false);
                    /*SL:292*/this.makeMark(hashMap, v-5.handlerPc(a5));
                }
            }
            /*SL:296*/return hashMap;
        }
        
        private void makeGoto(final HashMap a1, final int a2, final int a3, final int a4) {
            final Mark v1 = /*EL:300*/this.makeMark(a1, a3);
            final BasicBlock[] v2 = /*EL:301*/this.makeArray(v1.block);
            /*SL:302*/this.makeMark(a1, a2, v2, a4, true);
        }
        
        protected void makeJsr(final HashMap a1, final int a2, final int a3, final int a4) throws BadBytecode {
            /*SL:316*/throw new JsrBytecode();
        }
        
        private BasicBlock[] makeBlocks(final HashMap v-4) {
            final Mark[] array = /*EL:320*/(Mark[])v-4.values().toArray(/*EL:321*/new Mark[v-4.size()]);
            /*SL:322*/Arrays.sort(array);
            final ArrayList list = /*EL:323*/new ArrayList();
            int i = /*EL:324*/0;
            BasicBlock v0 = null;
            /*SL:326*/if (array.length > 0 && array[0].position == 0 && array[0].block != null) {
                final BasicBlock a1 = getBBlock(/*EL:327*/array[i++]);
            }
            else {
                /*SL:329*/v0 = this.makeBlock(0);
            }
            /*SL:331*/list.add(v0);
            /*SL:332*/while (i < array.length) {
                final Mark v = /*EL:333*/array[i++];
                final BasicBlock v2 = getBBlock(/*EL:334*/v);
                /*SL:335*/if (v2 == null) {
                    /*SL:337*/if (v0.length > 0) {
                        /*SL:339*/v0 = this.makeBlock(v0.position + v0.length);
                        /*SL:340*/list.add(v0);
                    }
                    /*SL:343*/v0.length = v.position + v.size - v0.position;
                    /*SL:344*/v0.exit = v.jump;
                    /*SL:345*/v0.stop = v.alwaysJmp;
                }
                else {
                    /*SL:349*/if (v0.length == 0) {
                        /*SL:350*/v0.length = v.position - v0.position;
                        final BasicBlock basicBlock = /*EL:351*/v2;
                        ++basicBlock.incoming;
                        /*SL:352*/v0.exit = this.makeArray(v2);
                    }
                    else/*SL:356*/ if (v0.position + v0.length < v.position) {
                        /*SL:358*/v0 = this.makeBlock(v0.position + v0.length);
                        /*SL:359*/list.add(v0);
                        /*SL:360*/v0.length = v.position - v0.position;
                        /*SL:363*/v0.stop = true;
                        /*SL:364*/v0.exit = this.makeArray(v2);
                    }
                    /*SL:368*/list.add(v2);
                    /*SL:369*/v0 = v2;
                }
            }
            /*SL:373*/return list.<BasicBlock>toArray(this.makeArray(list.size()));
        }
        
        private static BasicBlock getBBlock(final Mark a1) {
            final BasicBlock v1 = /*EL:377*/a1.block;
            /*SL:378*/if (v1 != null && a1.size > 0) {
                /*SL:379*/v1.exit = a1.jump;
                /*SL:380*/v1.length = a1.size;
                /*SL:381*/v1.stop = a1.alwaysJmp;
            }
            /*SL:384*/return v1;
        }
        
        private void addCatchers(final BasicBlock[] v-6, final ExceptionTable v-5) throws BadBytecode {
            /*SL:390*/if (v-5 == null) {
                /*SL:391*/return;
            }
            int size = /*EL:393*/v-5.size();
            /*SL:394*/while (--size >= 0) {
                final BasicBlock find = /*EL:395*/BasicBlock.find(v-6, v-5.handlerPc(size));
                final int startPc = /*EL:396*/v-5.startPc(size);
                final int endPc = /*EL:397*/v-5.endPc(size);
                final int v0 = /*EL:398*/v-5.catchType(size);
                final BasicBlock basicBlock = /*EL:399*/find;
                --basicBlock.incoming;
                /*SL:400*/for (int v = 0; v < v-6.length; ++v) {
                    final BasicBlock a1 = /*EL:401*/v-6[v];
                    final int a2 = /*EL:402*/a1.position;
                    /*SL:403*/if (startPc <= a2 && a2 < endPc) {
                        /*SL:404*/a1.toCatch = new Catch(find, v0, a1.toCatch);
                        final BasicBlock basicBlock2 = /*EL:405*/find;
                        ++basicBlock2.incoming;
                    }
                }
            }
        }
    }
}
