package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import javassist.CannotCompileException;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class StackMap extends AttributeInfo
{
    public static final String tag = "StackMap";
    public static final int TOP = 0;
    public static final int INTEGER = 1;
    public static final int FLOAT = 2;
    public static final int DOUBLE = 3;
    public static final int LONG = 4;
    public static final int NULL = 5;
    public static final int THIS = 6;
    public static final int OBJECT = 7;
    public static final int UNINIT = 8;
    
    StackMap(final ConstPool a1, final byte[] a2) {
        super(a1, "StackMap", a2);
    }
    
    StackMap(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    public int numOfEntries() {
        /*SL:68*/return ByteArray.readU16bit(this.info, 0);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool a1, final Map a2) {
        final Copier v1 = /*EL:120*/new Copier(this, a1, a2);
        /*SL:121*/v1.visit();
        /*SL:122*/return v1.getStackMap();
    }
    
    public void insertLocal(final int a1, final int a2, final int a3) throws BadBytecode {
        final byte[] v1 = /*EL:291*/new InsertLocal(this, a1, a2, a3).doit();
        /*SL:292*/this.set(v1);
    }
    
    void shiftPc(final int a1, final int a2, final boolean a3) throws BadBytecode {
        /*SL:379*/new Shifter(this, a1, a2, a3).visit();
    }
    
    void shiftForSwitch(final int a1, final int a2) throws BadBytecode {
        /*SL:410*/new SwitchShifter(this, a1, a2).visit();
    }
    
    public void removeNew(final int a1) throws CannotCompileException {
        final byte[] v1 = /*EL:442*/new NewRemover(this, a1).doit();
        /*SL:443*/this.set(v1);
    }
    
    public void print(final PrintWriter a1) {
        /*SL:505*/new Printer(this, a1).print();
    }
    
    public static class Walker
    {
        byte[] info;
        
        public Walker(final StackMap a1) {
            this.info = a1.get();
        }
        
        public void visit() {
            final int u16bit = /*EL:142*/ByteArray.readU16bit(this.info, 0);
            int n = /*EL:143*/2;
            /*SL:144*/for (int v0 = 0; v0 < u16bit; ++v0) {
                final int v = /*EL:145*/ByteArray.readU16bit(this.info, n);
                final int v2 = /*EL:146*/ByteArray.readU16bit(this.info, n + 2);
                /*SL:147*/n = this.locals(n + 4, v, v2);
                final int v3 = /*EL:148*/ByteArray.readU16bit(this.info, n);
                /*SL:149*/n = this.stack(n + 2, v, v3);
            }
        }
        
        public int locals(final int a1, final int a2, final int a3) {
            /*SL:158*/return this.typeInfoArray(a1, a2, a3, true);
        }
        
        public int stack(final int a1, final int a2, final int a3) {
            /*SL:166*/return this.typeInfoArray(a1, a2, a3, false);
        }
        
        public int typeInfoArray(int a3, final int a4, final int v1, final boolean v2) {
            /*SL:178*/for (int a5 = 0; a5 < v1; ++a5) {
                /*SL:179*/a3 = this.typeInfoArray2(a5, a3);
            }
            /*SL:181*/return a3;
        }
        
        int typeInfoArray2(final int v2, int v3) {
            final byte v4 = /*EL:185*/this.info[v3];
            /*SL:186*/if (v4 == 7) {
                final int a1 = /*EL:187*/ByteArray.readU16bit(this.info, v3 + 1);
                /*SL:188*/this.objectVariable(v3, a1);
                /*SL:189*/v3 += 3;
            }
            else/*SL:191*/ if (v4 == 8) {
                final int a2 = /*EL:192*/ByteArray.readU16bit(this.info, v3 + 1);
                /*SL:193*/this.uninitialized(v3, a2);
                /*SL:194*/v3 += 3;
            }
            else {
                /*SL:197*/this.typeInfo(v3, v4);
                /*SL:198*/++v3;
            }
            /*SL:201*/return v3;
        }
        
        public void typeInfo(final int a1, final byte a2) {
        }
        
        public void objectVariable(final int a1, final int a2) {
        }
        
        public void uninitialized(final int a1, final int a2) {
        }
    }
    
    static class Copier extends Walker
    {
        byte[] dest;
        ConstPool srcCp;
        ConstPool destCp;
        Map classnames;
        
        Copier(final StackMap a1, final ConstPool a2, final Map a3) {
            /*SL:221*/super(a1);
            this.srcCp = a1.getConstPool();
            this.dest = new byte[this.info.length];
            this.destCp = a2;
            this.classnames = a3;
        }
        
        @Override
        public void visit() {
            final int v1 = /*EL:238*/ByteArray.readU16bit(this.info, 0);
            /*SL:239*/ByteArray.write16bit(v1, this.dest, 0);
            /*SL:240*/super.visit();
        }
        
        @Override
        public int locals(final int a1, final int a2, final int a3) {
            /*SL:244*/ByteArray.write16bit(a2, this.dest, a1 - 4);
            /*SL:245*/return super.locals(a1, a2, a3);
        }
        
        @Override
        public int typeInfoArray(final int a1, final int a2, final int a3, final boolean a4) {
            /*SL:249*/ByteArray.write16bit(a3, this.dest, a1 - 2);
            /*SL:250*/return super.typeInfoArray(a1, a2, a3, a4);
        }
        
        @Override
        public void typeInfo(final int a1, final byte a2) {
            /*SL:254*/this.dest[a1] = a2;
        }
        
        @Override
        public void objectVariable(final int a1, final int a2) {
            /*SL:258*/this.dest[a1] = 7;
            final int v1 = /*EL:259*/this.srcCp.copy(a2, this.destCp, this.classnames);
            /*SL:260*/ByteArray.write16bit(v1, this.dest, a1 + 1);
        }
        
        @Override
        public void uninitialized(final int a1, final int a2) {
            /*SL:264*/this.dest[a1] = 8;
            /*SL:265*/ByteArray.write16bit(a2, this.dest, a1 + 1);
        }
        
        public StackMap getStackMap() {
            /*SL:269*/return new StackMap(this.destCp, this.dest);
        }
    }
    
    static class SimpleCopy extends Walker
    {
        Writer writer;
        
        SimpleCopy(final StackMap a1) {
            super(a1);
            this.writer = new Writer();
        }
        
        byte[] doit() {
            /*SL:304*/this.visit();
            /*SL:305*/return this.writer.toByteArray();
        }
        
        @Override
        public void visit() {
            final int v1 = /*EL:309*/ByteArray.readU16bit(this.info, 0);
            /*SL:310*/this.writer.write16bit(v1);
            /*SL:311*/super.visit();
        }
        
        @Override
        public int locals(final int a1, final int a2, final int a3) {
            /*SL:315*/this.writer.write16bit(a2);
            /*SL:316*/return super.locals(a1, a2, a3);
        }
        
        @Override
        public int typeInfoArray(final int a1, final int a2, final int a3, final boolean a4) {
            /*SL:320*/this.writer.write16bit(a3);
            /*SL:321*/return super.typeInfoArray(a1, a2, a3, a4);
        }
        
        @Override
        public void typeInfo(final int a1, final byte a2) {
            /*SL:325*/this.writer.writeVerifyTypeInfo(a2, 0);
        }
        
        @Override
        public void objectVariable(final int a1, final int a2) {
            /*SL:329*/this.writer.writeVerifyTypeInfo(7, a2);
        }
        
        @Override
        public void uninitialized(final int a1, final int a2) {
            /*SL:333*/this.writer.writeVerifyTypeInfo(8, a2);
        }
    }
    
    static class InsertLocal extends SimpleCopy
    {
        private int varIndex;
        private int varTag;
        private int varData;
        
        InsertLocal(final StackMap a1, final int a2, final int a3, final int a4) {
            super(a1);
            this.varIndex = a2;
            /*SL:334*/this.varTag = a3;
            this.varData = a4;
        }
        
        @Override
        public int typeInfoArray(int a3, final int a4, final int v1, final boolean v2) {
            /*SL:349*/if (!v2 || v1 < this.varIndex) {
                /*SL:350*/return super.typeInfoArray(a3, a4, v1, v2);
            }
            /*SL:352*/this.writer.write16bit(v1 + 1);
            /*SL:353*/for (int a5 = 0; a5 < v1; ++a5) {
                /*SL:354*/if (a5 == this.varIndex) {
                    /*SL:355*/this.writeVarTypeInfo();
                }
                /*SL:357*/a3 = this.typeInfoArray2(a5, a3);
            }
            /*SL:360*/if (v1 == this.varIndex) {
                /*SL:361*/this.writeVarTypeInfo();
            }
            /*SL:363*/return a3;
        }
        
        private void writeVarTypeInfo() {
            /*SL:367*/if (this.varTag == 7) {
                /*SL:368*/this.writer.writeVerifyTypeInfo(7, this.varData);
            }
            else/*SL:369*/ if (this.varTag == 8) {
                /*SL:370*/this.writer.writeVerifyTypeInfo(8, this.varData);
            }
            else {
                /*SL:372*/this.writer.writeVerifyTypeInfo(this.varTag, 0);
            }
        }
    }
    
    static class Shifter extends Walker
    {
        private int where;
        private int gap;
        private boolean exclusive;
        
        public Shifter(final StackMap a1, final int a2, final int a3, final boolean a4) {
            super(a1);
            this.where = a2;
            this.gap = a3;
            this.exclusive = a4;
        }
        
        @Override
        public int locals(final int a1, final int a2, final int a3) {
            /*SL:394*/if (this.exclusive) {
                if (this.where > a2) {
                    return /*EL:397*/super.locals(a1, a2, a3);
                }
            }
            else if (this.where >= a2) {
                return super.locals(a1, a2, a3);
            }
            ByteArray.write16bit(a2 + this.gap, this.info, a1 - 4);
            return super.locals(a1, a2, a3);
        }
        
        @Override
        public void uninitialized(final int a1, final int a2) {
            /*SL:401*/if (this.where <= a2) {
                /*SL:402*/ByteArray.write16bit(a2 + this.gap, this.info, a1 + 1);
            }
        }
    }
    
    static class SwitchShifter extends Walker
    {
        private int where;
        private int gap;
        
        public SwitchShifter(final StackMap a1, final int a2, final int a3) {
            super(a1);
            this.where = a2;
            this.gap = a3;
        }
        
        @Override
        public int locals(final int a1, final int a2, final int a3) {
            /*SL:423*/if (this.where == a1 + a2) {
                /*SL:424*/ByteArray.write16bit(a2 - this.gap, this.info, a1 - 4);
            }
            else/*SL:425*/ if (this.where == a1) {
                /*SL:426*/ByteArray.write16bit(a2 + this.gap, this.info, a1 - 4);
            }
            /*SL:428*/return super.locals(a1, a2, a3);
        }
    }
    
    static class NewRemover extends SimpleCopy
    {
        int posOfNew;
        
        NewRemover(final StackMap a1, final int a2) {
            super(a1);
            this.posOfNew = a2;
        }
        
        @Override
        public int stack(final int a1, final int a2, final int a3) {
            /*SL:455*/return this.stackTypeInfoArray(a1, a2, a3);
        }
        
        private int stackTypeInfoArray(int v-6, final int v-5, final int v-4) {
            int n = /*EL:459*/v-6;
            int n2 = /*EL:460*/0;
            /*SL:461*/for (int a3 = 0; a3 < v-4; ++a3) {
                final byte a2 = /*EL:462*/this.info[n];
                /*SL:463*/if (a2 == 7) {
                    /*SL:464*/n += 3;
                }
                else/*SL:465*/ if (a2 == 8) {
                    /*SL:466*/a3 = ByteArray.readU16bit(this.info, n + 1);
                    /*SL:467*/if (a3 == this.posOfNew) {
                        /*SL:468*/++n2;
                    }
                    /*SL:470*/n += 3;
                }
                else {
                    /*SL:473*/++n;
                }
            }
            /*SL:476*/this.writer.write16bit(v-4 - n2);
            /*SL:477*/for (int i = 0; i < v-4; ++i) {
                final byte v0 = /*EL:478*/this.info[v-6];
                /*SL:479*/if (v0 == 7) {
                    final int v = /*EL:480*/ByteArray.readU16bit(this.info, v-6 + 1);
                    /*SL:481*/this.objectVariable(v-6, v);
                    /*SL:482*/v-6 += 3;
                }
                else/*SL:484*/ if (v0 == 8) {
                    final int v = /*EL:485*/ByteArray.readU16bit(this.info, v-6 + 1);
                    /*SL:486*/if (v != this.posOfNew) {
                        /*SL:487*/this.uninitialized(v-6, v);
                    }
                    /*SL:489*/v-6 += 3;
                }
                else {
                    /*SL:492*/this.typeInfo(v-6, v0);
                    /*SL:493*/++v-6;
                }
            }
            /*SL:497*/return v-6;
        }
    }
    
    static class Printer extends Walker
    {
        private PrintWriter writer;
        
        public Printer(final StackMap a1, final PrintWriter a2) {
            super(a1);
            this.writer = a2;
        }
        
        public void print() {
            final int v1 = /*EL:517*/ByteArray.readU16bit(this.info, 0);
            /*SL:518*/this.writer.println(v1 + " entries");
            /*SL:519*/this.visit();
        }
        
        @Override
        public int locals(final int a1, final int a2, final int a3) {
            /*SL:523*/this.writer.println("  * offset " + a2);
            /*SL:524*/return super.locals(a1, a2, a3);
        }
    }
    
    public static class Writer
    {
        private ByteArrayOutputStream output;
        
        public Writer() {
            this.output = new ByteArrayOutputStream();
        }
        
        public byte[] toByteArray() {
            /*SL:547*/return this.output.toByteArray();
        }
        
        public StackMap toStackMap(final ConstPool a1) {
            /*SL:554*/return new StackMap(a1, this.output.toByteArray());
        }
        
        public void writeVerifyTypeInfo(final int a1, final int a2) {
            /*SL:563*/this.output.write(a1);
            /*SL:564*/if (a1 == 7 || a1 == 8) {
                /*SL:565*/this.write16bit(a2);
            }
        }
        
        public void write16bit(final int a1) {
            /*SL:572*/this.output.write(a1 >>> 8 & 0xFF);
            /*SL:573*/this.output.write(a1 & 0xFF);
        }
    }
}
