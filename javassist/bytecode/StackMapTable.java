package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import javassist.CannotCompileException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class StackMapTable extends AttributeInfo
{
    public static final String tag = "StackMapTable";
    public static final int TOP = 0;
    public static final int INTEGER = 1;
    public static final int FLOAT = 2;
    public static final int DOUBLE = 3;
    public static final int LONG = 4;
    public static final int NULL = 5;
    public static final int THIS = 6;
    public static final int OBJECT = 7;
    public static final int UNINIT = 8;
    
    StackMapTable(final ConstPool a1, final byte[] a2) {
        super(a1, "StackMapTable", a2);
    }
    
    StackMapTable(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v1, final Map v2) throws RuntimeCopyException {
        try {
            /*SL:70*/return new StackMapTable(v1, new Copier(this.constPool, this.info, v1, v2).doit());
        }
        catch (BadBytecode a1) {
            /*SL:73*/throw new RuntimeCopyException("bad bytecode. fatal?");
        }
    }
    
    @Override
    void write(final DataOutputStream a1) throws IOException {
        /*SL:91*/super.write(a1);
    }
    
    public void insertLocal(final int a1, final int a2, final int a3) throws BadBytecode {
        final byte[] v1 = /*EL:461*/new InsertLocal(this.get(), a1, a2, a3).doit();
        /*SL:462*/this.set(v1);
    }
    
    public static int typeTagOf(final char a1) {
        /*SL:475*/switch (a1) {
            case 'D': {
                /*SL:477*/return 3;
            }
            case 'F': {
                /*SL:479*/return 2;
            }
            case 'J': {
                /*SL:481*/return 4;
            }
            case 'L':
            case '[': {
                /*SL:484*/return 7;
            }
            default: {
                /*SL:487*/return 1;
            }
        }
    }
    
    public void println(final PrintWriter a1) {
        /*SL:697*/Printer.print(this, a1);
    }
    
    public void println(final PrintStream a1) {
        /*SL:706*/Printer.print(this, new PrintWriter(a1, true));
    }
    
    void shiftPc(final int a1, final int a2, final boolean a3) throws BadBytecode {
        /*SL:807*/new OffsetShifter(this, a1, a2).parse();
        /*SL:808*/new Shifter(this, a1, a2, a3).doit();
    }
    
    void shiftForSwitch(final int a1, final int a2) throws BadBytecode {
        /*SL:926*/new SwitchShifter(this, a1, a2).doit();
    }
    
    public void removeNew(final int v0) throws CannotCompileException {
        try {
            final byte[] a1 = /*EL:1000*/new NewRemover(this.get(), v0).doit();
            /*SL:1001*/this.set(a1);
        }
        catch (BadBytecode v) {
            /*SL:1004*/throw new CannotCompileException("bad stack map table", v);
        }
    }
    
    public static class RuntimeCopyException extends RuntimeException
    {
        public RuntimeCopyException(final String a1) {
            super(a1);
        }
    }
    
    public static class Walker
    {
        byte[] info;
        int numOfEntries;
        
        public Walker(final StackMapTable a1) {
            this(a1.get());
        }
        
        public Walker(final byte[] a1) {
            this.info = a1;
            this.numOfEntries = ByteArray.readU16bit(a1, 0);
        }
        
        public final int size() {
            /*SL:172*/return this.numOfEntries;
        }
        
        public void parse() throws BadBytecode {
            final int numOfEntries = /*EL:178*/this.numOfEntries;
            int v0 = /*EL:179*/2;
            /*SL:180*/for (int v = 0; v < numOfEntries; ++v) {
                /*SL:181*/v0 = this.stackMapFrames(v0, v);
            }
        }
        
        int stackMapFrames(int v2, final int v3) throws BadBytecode {
            final int v4 = /*EL:194*/this.info[v2] & 0xFF;
            /*SL:195*/if (v4 < 64) {
                /*SL:196*/this.sameFrame(v2, v4);
                /*SL:197*/++v2;
            }
            else/*SL:199*/ if (v4 < 128) {
                /*SL:200*/v2 = this.sameLocals(v2, v4);
            }
            else {
                /*SL:201*/if (v4 < 247) {
                    /*SL:202*/throw new BadBytecode("bad frame_type in StackMapTable");
                }
                /*SL:203*/if (v4 == 247) {
                    /*SL:204*/v2 = this.sameLocals(v2, v4);
                }
                else/*SL:205*/ if (v4 < 251) {
                    final int a1 = /*EL:206*/ByteArray.readU16bit(this.info, v2 + 1);
                    /*SL:207*/this.chopFrame(v2, a1, 251 - v4);
                    /*SL:208*/v2 += 3;
                }
                else/*SL:210*/ if (v4 == 251) {
                    final int a2 = /*EL:211*/ByteArray.readU16bit(this.info, v2 + 1);
                    /*SL:212*/this.sameFrame(v2, a2);
                    /*SL:213*/v2 += 3;
                }
                else/*SL:215*/ if (v4 < 255) {
                    /*SL:216*/v2 = this.appendFrame(v2, v4);
                }
                else {
                    /*SL:218*/v2 = this.fullFrame(v2);
                }
            }
            /*SL:220*/return v2;
        }
        
        public void sameFrame(final int a1, final int a2) throws BadBytecode {
        }
        
        private int sameLocals(int v1, final int v2) throws BadBytecode {
            final int v3 = /*EL:234*/v1;
            final int v4;
            /*SL:236*/if (v2 < 128) {
                final int a1 = /*EL:237*/v2 - 64;
            }
            else {
                /*SL:239*/v4 = ByteArray.readU16bit(this.info, v1 + 1);
                /*SL:240*/v1 += 2;
            }
            final int v5 = /*EL:243*/this.info[v1 + 1] & 0xFF;
            int v6 = /*EL:244*/0;
            /*SL:245*/if (v5 == 7 || v5 == 8) {
                /*SL:246*/v6 = ByteArray.readU16bit(this.info, v1 + 2);
                /*SL:247*/this.objectOrUninitialized(v5, v6, v1 + 2);
                /*SL:248*/v1 += 2;
            }
            /*SL:251*/this.sameLocals(v3, v4, v5, v6);
            /*SL:252*/return v1 + 2;
        }
        
        public void sameLocals(final int a1, final int a2, final int a3, final int a4) throws BadBytecode {
        }
        
        public void chopFrame(final int a1, final int a2, final int a3) throws BadBytecode {
        }
        
        private int appendFrame(final int v2, final int v3) throws BadBytecode {
            final int v4 = /*EL:280*/v3 - 251;
            final int v5 = /*EL:281*/ByteArray.readU16bit(this.info, v2 + 1);
            final int[] v6 = /*EL:282*/new int[v4];
            final int[] v7 = /*EL:283*/new int[v4];
            int v8 = /*EL:284*/v2 + 3;
            /*SL:285*/for (int a2 = 0; a2 < v4; ++a2) {
                /*SL:286*/a2 = (this.info[v8] & 0xFF);
                /*SL:287*/v6[a2] = a2;
                /*SL:288*/if (a2 == 7 || a2 == 8) {
                    /*SL:290*/this.objectOrUninitialized(a2, v7[a2] = ByteArray.readU16bit(this.info, v8 + 1), v8 + 1);
                    /*SL:291*/v8 += 3;
                }
                else {
                    /*SL:294*/v7[a2] = 0;
                    /*SL:295*/++v8;
                }
            }
            /*SL:299*/this.appendFrame(v2, v5, v6, v7);
            /*SL:300*/return v8;
        }
        
        public void appendFrame(final int a1, final int a2, final int[] a3, final int[] a4) throws BadBytecode {
        }
        
        private int fullFrame(final int a1) throws BadBytecode {
            final int v1 = /*EL:316*/ByteArray.readU16bit(this.info, a1 + 1);
            final int v2 = /*EL:317*/ByteArray.readU16bit(this.info, a1 + 3);
            final int[] v3 = /*EL:318*/new int[v2];
            final int[] v4 = /*EL:319*/new int[v2];
            int v5 = /*EL:320*/this.verifyTypeInfo(a1 + 5, v2, v3, v4);
            final int v6 = /*EL:321*/ByteArray.readU16bit(this.info, v5);
            final int[] v7 = /*EL:322*/new int[v6];
            final int[] v8 = /*EL:323*/new int[v6];
            /*SL:324*/v5 = this.verifyTypeInfo(v5 + 2, v6, v7, v8);
            /*SL:325*/this.fullFrame(a1, v1, v3, v4, v7, v8);
            /*SL:326*/return v5;
        }
        
        public void fullFrame(final int a1, final int a2, final int[] a3, final int[] a4, final int[] a5, final int[] a6) throws BadBytecode {
        }
        
        private int verifyTypeInfo(int a4, final int v1, final int[] v2, final int[] v3) {
            /*SL:346*/for (int a5 = 0; a5 < v1; ++a5) {
                final int a6 = /*EL:347*/this.info[a4++] & 0xFF;
                /*SL:348*/v2[a5] = a6;
                /*SL:349*/if (a6 == 7 || a6 == 8) {
                    /*SL:351*/this.objectOrUninitialized(a6, v3[a5] = ByteArray.readU16bit(this.info, a4), a4);
                    /*SL:352*/a4 += 2;
                }
            }
            /*SL:356*/return a4;
        }
        
        public void objectOrUninitialized(final int a1, final int a2, final int a3) {
        }
    }
    
    static class SimpleCopy extends Walker
    {
        private Writer writer;
        
        public SimpleCopy(final byte[] a1) {
            /*SL:367*/super(a1);
            this.writer = new Writer(a1.length);
        }
        
        public byte[] doit() throws BadBytecode {
            /*SL:379*/this.parse();
            /*SL:380*/return this.writer.toByteArray();
        }
        
        @Override
        public void sameFrame(final int a1, final int a2) {
            /*SL:384*/this.writer.sameFrame(a2);
        }
        
        @Override
        public void sameLocals(final int a1, final int a2, final int a3, final int a4) {
            /*SL:388*/this.writer.sameLocals(a2, a3, this.copyData(a3, a4));
        }
        
        @Override
        public void chopFrame(final int a1, final int a2, final int a3) {
            /*SL:392*/this.writer.chopFrame(a2, a3);
        }
        
        @Override
        public void appendFrame(final int a1, final int a2, final int[] a3, final int[] a4) {
            /*SL:396*/this.writer.appendFrame(a2, a3, this.copyData(a3, a4));
        }
        
        @Override
        public void fullFrame(final int a1, final int a2, final int[] a3, final int[] a4, final int[] a5, final int[] a6) {
            /*SL:401*/this.writer.fullFrame(a2, a3, this.copyData(a3, a4), a5, this.copyData(a5, a6));
        }
        
        protected int copyData(final int a1, final int a2) {
            /*SL:406*/return a2;
        }
        
        protected int[] copyData(final int[] a1, final int[] a2) {
            /*SL:410*/return a2;
        }
    }
    
    static class Copier extends SimpleCopy
    {
        private ConstPool srcPool;
        private ConstPool destPool;
        private Map classnames;
        
        public Copier(final ConstPool a1, final byte[] a2, final ConstPool a3, final Map a4) {
            super(a2);
            this.srcPool = a1;
            this.destPool = a3;
            this.classnames = a4;
        }
        
        @Override
        protected int copyData(final int a1, final int a2) {
            /*SL:426*/if (a1 == 7) {
                /*SL:427*/return this.srcPool.copy(a2, this.destPool, this.classnames);
            }
            /*SL:429*/return a2;
        }
        
        @Override
        protected int[] copyData(final int[] v1, final int[] v2) {
            final int[] v3 = /*EL:433*/new int[v2.length];
            /*SL:434*/for (int a1 = 0; a1 < v2.length; ++a1) {
                /*SL:435*/if (v1[a1] == 7) {
                    /*SL:436*/v3[a1] = this.srcPool.copy(v2[a1], this.destPool, this.classnames);
                }
                else {
                    /*SL:438*/v3[a1] = v2[a1];
                }
            }
            /*SL:440*/return v3;
        }
    }
    
    static class InsertLocal extends SimpleCopy
    {
        private int varIndex;
        private int varTag;
        private int varData;
        
        public InsertLocal(final byte[] a1, final int a2, final int a3, final int a4) {
            super(a1);
            this.varIndex = a2;
            this.varTag = a3;
            this.varData = a4;
        }
        
        @Override
        public void fullFrame(final int a3, final int a4, final int[] a5, final int[] a6, final int[] v1, final int[] v2) {
            final int v3 = /*EL:509*/a5.length;
            /*SL:510*/if (v3 < this.varIndex) {
                /*SL:511*/super.fullFrame(a3, a4, a5, a6, v1, v2);
                /*SL:512*/return;
            }
            final int v4 = /*EL:515*/(this.varTag == 4 || this.varTag == 3) ? 2 : 1;
            final int[] v5 = /*EL:516*/new int[v3 + v4];
            final int[] v6 = /*EL:517*/new int[v3 + v4];
            final int v7 = /*EL:518*/this.varIndex;
            int v8 = /*EL:519*/0;
            /*SL:520*/for (int a7 = 0; a7 < v3; ++a7) {
                /*SL:521*/if (v8 == v7) {
                    /*SL:522*/v8 += v4;
                }
                /*SL:524*/v5[v8] = a5[a7];
                /*SL:525*/v6[v8++] = a6[a7];
            }
            /*SL:528*/v5[v7] = this.varTag;
            /*SL:529*/v6[v7] = this.varData;
            /*SL:530*/if (v4 > 1) {
                /*SL:532*/v6[v7 + 1] = (v5[v7 + 1] = 0);
            }
            /*SL:535*/super.fullFrame(a3, a4, v5, v6, v1, v2);
        }
    }
    
    public static class Writer
    {
        ByteArrayOutputStream output;
        int numOfEntries;
        
        public Writer(final int a1) {
            this.output = new ByteArrayOutputStream(a1);
            this.numOfEntries = 0;
            this.output.write(0);
            this.output.write(0);
        }
        
        public byte[] toByteArray() {
            final byte[] v1 = /*EL:561*/this.output.toByteArray();
            /*SL:562*/ByteArray.write16bit(this.numOfEntries, v1, 0);
            /*SL:563*/return v1;
        }
        
        public StackMapTable toStackMapTable(final ConstPool a1) {
            /*SL:574*/return new StackMapTable(a1, this.toByteArray());
        }
        
        public void sameFrame(final int a1) {
            /*SL:581*/++this.numOfEntries;
            /*SL:582*/if (a1 < 64) {
                /*SL:583*/this.output.write(a1);
            }
            else {
                /*SL:585*/this.output.write(251);
                /*SL:586*/this.write16(a1);
            }
        }
        
        public void sameLocals(final int a1, final int a2, final int a3) {
            /*SL:602*/++this.numOfEntries;
            /*SL:603*/if (a1 < 64) {
                /*SL:604*/this.output.write(a1 + 64);
            }
            else {
                /*SL:606*/this.output.write(247);
                /*SL:607*/this.write16(a1);
            }
            /*SL:610*/this.writeTypeInfo(a2, a3);
        }
        
        public void chopFrame(final int a1, final int a2) {
            /*SL:619*/++this.numOfEntries;
            /*SL:620*/this.output.write(251 - a2);
            /*SL:621*/this.write16(a1);
        }
        
        public void appendFrame(final int a3, final int[] v1, final int[] v2) {
            /*SL:638*/++this.numOfEntries;
            final int v3 = /*EL:639*/v1.length;
            /*SL:640*/this.output.write(v3 + 251);
            /*SL:641*/this.write16(a3);
            /*SL:642*/for (int a4 = 0; a4 < v3; ++a4) {
                /*SL:643*/this.writeTypeInfo(v1[a4], v2[a4]);
            }
        }
        
        public void fullFrame(final int a4, final int[] a5, final int[] v1, final int[] v2, final int[] v3) {
            /*SL:667*/++this.numOfEntries;
            /*SL:668*/this.output.write(255);
            /*SL:669*/this.write16(a4);
            int v4 = /*EL:670*/a5.length;
            /*SL:671*/this.write16(v4);
            /*SL:672*/for (int a6 = 0; a6 < v4; ++a6) {
                /*SL:673*/this.writeTypeInfo(a5[a6], v1[a6]);
            }
            /*SL:675*/v4 = v2.length;
            /*SL:676*/this.write16(v4);
            /*SL:677*/for (int a7 = 0; a7 < v4; ++a7) {
                /*SL:678*/this.writeTypeInfo(v2[a7], v3[a7]);
            }
        }
        
        private void writeTypeInfo(final int a1, final int a2) {
            /*SL:682*/this.output.write(a1);
            /*SL:683*/if (a1 == 7 || a1 == 8) {
                /*SL:684*/this.write16(a2);
            }
        }
        
        private void write16(final int a1) {
            /*SL:688*/this.output.write(a1 >>> 8 & 0xFF);
            /*SL:689*/this.output.write(a1 & 0xFF);
        }
    }
    
    static class Printer extends Walker
    {
        private PrintWriter writer;
        private int offset;
        
        public static void print(final StackMapTable a2, final PrintWriter v1) {
            try {
                /*SL:718*/new Printer(a2.get(), v1).parse();
            }
            catch (BadBytecode a3) {
                /*SL:721*/v1.println(a3.getMessage());
            }
        }
        
        Printer(final byte[] a1, final PrintWriter a2) {
            super(a1);
            this.writer = a2;
            this.offset = -1;
        }
        
        @Override
        public void sameFrame(final int a1, final int a2) {
            /*SL:732*/this.offset += a2 + 1;
            /*SL:733*/this.writer.println(this.offset + " same frame: " + a2);
        }
        
        @Override
        public void sameLocals(final int a1, final int a2, final int a3, final int a4) {
            /*SL:737*/this.offset += a2 + 1;
            /*SL:738*/this.writer.println(this.offset + " same locals: " + a2);
            /*SL:739*/this.printTypeInfo(a3, a4);
        }
        
        @Override
        public void chopFrame(final int a1, final int a2, final int a3) {
            /*SL:743*/this.offset += a2 + 1;
            /*SL:744*/this.writer.println(this.offset + " chop frame: " + a2 + ",    " + a3 + " last locals");
        }
        
        @Override
        public void appendFrame(final int a3, final int a4, final int[] v1, final int[] v2) {
            /*SL:748*/this.offset += a4 + 1;
            /*SL:749*/this.writer.println(this.offset + " append frame: " + a4);
            /*SL:750*/for (int a5 = 0; a5 < v1.length; ++a5) {
                /*SL:751*/this.printTypeInfo(v1[a5], v2[a5]);
            }
        }
        
        @Override
        public void fullFrame(final int a4, final int a5, final int[] a6, final int[] v1, final int[] v2, final int[] v3) {
            /*SL:756*/this.offset += a5 + 1;
            /*SL:757*/this.writer.println(this.offset + " full frame: " + a5);
            /*SL:758*/this.writer.println("[locals]");
            /*SL:759*/for (int a7 = 0; a7 < a6.length; ++a7) {
                /*SL:760*/this.printTypeInfo(a6[a7], v1[a7]);
            }
            /*SL:762*/this.writer.println("[stack]");
            /*SL:763*/for (int a8 = 0; a8 < v2.length; ++a8) {
                /*SL:764*/this.printTypeInfo(v2[a8], v3[a8]);
            }
        }
        
        private void printTypeInfo(final int a1, final int a2) {
            String v1 = /*EL:768*/null;
            /*SL:769*/switch (a1) {
                case 0: {
                    /*SL:771*/v1 = "top";
                    /*SL:772*/break;
                }
                case 1: {
                    /*SL:774*/v1 = "integer";
                    /*SL:775*/break;
                }
                case 2: {
                    /*SL:777*/v1 = "float";
                    /*SL:778*/break;
                }
                case 3: {
                    /*SL:780*/v1 = "double";
                    /*SL:781*/break;
                }
                case 4: {
                    /*SL:783*/v1 = "long";
                    /*SL:784*/break;
                }
                case 5: {
                    /*SL:786*/v1 = "null";
                    /*SL:787*/break;
                }
                case 6: {
                    /*SL:789*/v1 = "this";
                    /*SL:790*/break;
                }
                case 7: {
                    /*SL:792*/v1 = "object (cpool_index " + a2 + ")";
                    /*SL:793*/break;
                }
                case 8: {
                    /*SL:795*/v1 = "uninitialized (offset " + a2 + ")";
                    break;
                }
            }
            /*SL:799*/this.writer.print("    ");
            /*SL:800*/this.writer.println(v1);
        }
    }
    
    static class OffsetShifter extends Walker
    {
        int where;
        int gap;
        
        public OffsetShifter(final StackMapTable a1, final int a2, final int a3) {
            super(a1);
            this.where = a2;
            this.gap = a3;
        }
        
        @Override
        public void objectOrUninitialized(final int a1, final int a2, final int a3) {
            /*SL:821*/if (a1 == 8 && /*EL:822*/this.where <= a2) {
                /*SL:823*/ByteArray.write16bit(a2 + this.gap, this.info, a3);
            }
        }
    }
    
    static class Shifter extends Walker
    {
        private StackMapTable stackMap;
        int where;
        int gap;
        int position;
        byte[] updatedInfo;
        boolean exclusive;
        
        public Shifter(final StackMapTable a1, final int a2, final int a3, final boolean a4) {
            super(a1);
            this.stackMap = a1;
            this.where = a2;
            this.gap = a3;
            this.position = 0;
            this.updatedInfo = null;
            /*SL:824*/this.exclusive = a4;
        }
        
        public void doit() throws BadBytecode {
            /*SL:845*/this.parse();
            /*SL:846*/if (this.updatedInfo != null) {
                /*SL:847*/this.stackMap.set(this.updatedInfo);
            }
        }
        
        @Override
        public void sameFrame(final int a1, final int a2) {
            /*SL:851*/this.update(a1, a2, 0, 251);
        }
        
        @Override
        public void sameLocals(final int a1, final int a2, final int a3, final int a4) {
            /*SL:855*/this.update(a1, a2, 64, 247);
        }
        
        void update(final int v1, final int v2, final int v3, final int v4) {
            final int v5 = /*EL:859*/this.position;
            /*SL:860*/this.position = v5 + v2 + ((v5 != 0) ? 1 : 0);
            final boolean v6;
            /*SL:862*/if (this.exclusive) {
                final boolean a1 = /*EL:863*/v5 < this.where && this.where <= this.position;
            }
            else {
                /*SL:865*/v6 = (v5 <= this.where && this.where < this.position);
            }
            /*SL:867*/if (v6) {
                final int a2 = /*EL:868*/v2 + this.gap;
                /*SL:869*/this.position += this.gap;
                /*SL:870*/if (a2 < 64) {
                    /*SL:871*/this.info[v1] = (byte)(a2 + v3);
                }
                else/*SL:872*/ if (v2 < 64) {
                    final byte[] a3 = insertGap(/*EL:873*/this.info, v1, 2);
                    /*SL:874*/a3[v1] = (byte)v4;
                    /*SL:875*/ByteArray.write16bit(a2, a3, v1 + 1);
                    /*SL:876*/this.updatedInfo = a3;
                }
                else {
                    /*SL:879*/ByteArray.write16bit(a2, this.info, v1 + 1);
                }
            }
        }
        
        static byte[] insertGap(final byte[] a2, final int a3, final int v1) {
            final int v2 = /*EL:884*/a2.length;
            final byte[] v3 = /*EL:885*/new byte[v2 + v1];
            /*SL:886*/for (int a4 = 0; a4 < v2; ++a4) {
                /*SL:887*/v3[a4 + ((a4 < a3) ? 0 : v1)] = a2[a4];
            }
            /*SL:889*/return v3;
        }
        
        @Override
        public void chopFrame(final int a1, final int a2, final int a3) {
            /*SL:893*/this.update(a1, a2);
        }
        
        @Override
        public void appendFrame(final int a1, final int a2, final int[] a3, final int[] a4) {
            /*SL:897*/this.update(a1, a2);
        }
        
        @Override
        public void fullFrame(final int a1, final int a2, final int[] a3, final int[] a4, final int[] a5, final int[] a6) {
            /*SL:902*/this.update(a1, a2);
        }
        
        void update(final int v2, final int v3) {
            final int v4 = /*EL:906*/this.position;
            /*SL:907*/this.position = v4 + v3 + ((v4 != 0) ? 1 : 0);
            final boolean v5;
            /*SL:909*/if (this.exclusive) {
                final boolean a1 = /*EL:910*/v4 < this.where && this.where <= this.position;
            }
            else {
                /*SL:912*/v5 = (v4 <= this.where && this.where < this.position);
            }
            /*SL:914*/if (v5) {
                final int a2 = /*EL:915*/v3 + this.gap;
                /*SL:916*/ByteArray.write16bit(a2, this.info, v2 + 1);
                /*SL:917*/this.position += this.gap;
            }
        }
    }
    
    static class SwitchShifter extends Shifter
    {
        SwitchShifter(final StackMapTable a1, final int a2, final int a3) {
            super(a1, a2, a3, false);
        }
        
        @Override
        void update(final int a4, final int v1, final int v2, final int v3) {
            final int v4 = /*EL:935*/this.position;
            /*SL:936*/this.position = v4 + v1 + ((v4 != 0) ? 1 : 0);
            int v5;
            /*SL:938*/if (this.where == this.position) {
                /*SL:939*/v5 = v1 - this.gap;
            }
            else {
                /*SL:940*/if (this.where != v4) {
                    /*SL:943*/return;
                }
                v5 = v1 + this.gap;
            }
            /*SL:945*/if (v1 < 64) {
                /*SL:946*/if (v5 < 64) {
                    /*SL:947*/this.info[a4] = (byte)(v5 + v2);
                }
                else {
                    final byte[] a5 = /*EL:949*/Shifter.insertGap(this.info, a4, 2);
                    /*SL:950*/a5[a4] = (byte)v3;
                    /*SL:951*/ByteArray.write16bit(v5, a5, a4 + 1);
                    /*SL:952*/this.updatedInfo = a5;
                }
            }
            else/*SL:955*/ if (v5 < 64) {
                final byte[] a6 = deleteGap(/*EL:956*/this.info, a4, 2);
                /*SL:957*/a6[a4] = (byte)(v5 + v2);
                /*SL:958*/this.updatedInfo = a6;
            }
            else {
                /*SL:961*/ByteArray.write16bit(v5, this.info, a4 + 1);
            }
        }
        
        static byte[] deleteGap(final byte[] a2, int a3, final int v1) {
            /*SL:965*/a3 += v1;
            final int v2 = /*EL:966*/a2.length;
            final byte[] v3 = /*EL:967*/new byte[v2 - v1];
            /*SL:968*/for (int a4 = 0; a4 < v2; ++a4) {
                /*SL:969*/v3[a4 - ((a4 < a3) ? 0 : v1)] = a2[a4];
            }
            /*SL:971*/return v3;
        }
        
        @Override
        void update(final int a1, final int a2) {
            final int v1 = /*EL:975*/this.position;
            /*SL:976*/this.position = v1 + a2 + ((v1 != 0) ? 1 : 0);
            int v2;
            /*SL:978*/if (this.where == this.position) {
                /*SL:979*/v2 = a2 - this.gap;
            }
            else {
                /*SL:980*/if (this.where != v1) {
                    /*SL:983*/return;
                }
                v2 = a2 + this.gap;
            }
            /*SL:985*/ByteArray.write16bit(v2, this.info, a1 + 1);
        }
    }
    
    static class NewRemover extends SimpleCopy
    {
        int posOfNew;
        
        public NewRemover(final byte[] a1, final int a2) {
            super(a1);
            this.posOfNew = a2;
        }
        
        @Override
        public void sameLocals(final int a1, final int a2, final int a3, final int a4) {
            /*SL:1017*/if (a3 == 8 && a4 == this.posOfNew) {
                /*SL:1018*/super.sameFrame(a1, a2);
            }
            else {
                /*SL:1020*/super.sameLocals(a1, a2, a3, a4);
            }
        }
        
        @Override
        public void fullFrame(final int v1, final int v2, final int[] v3, final int[] v4, int[] v5, int[] v6) {
            /*SL:1026*/for (int v7 = v5.length - 1, a5 = 0; a5 < v7; ++a5) {
                /*SL:1027*/if (v5[a5] == 8 && v6[a5] == this.posOfNew && v5[a5 + 1] == 8 && v6[a5 + 1] == this.posOfNew) {
                    final int[] a2 = /*EL:1030*/new int[++v7 - 2];
                    final int[] a3 = /*EL:1031*/new int[v7 - 2];
                    int a4 = /*EL:1032*/0;
                    /*SL:1033*/for (a5 = 0; a5 < v7; ++a5) {
                        /*SL:1034*/if (a5 == a5) {
                            /*SL:1035*/++a5;
                        }
                        else {
                            /*SL:1037*/a2[a4] = v5[a5];
                            /*SL:1038*/a3[a4++] = v6[a5];
                        }
                    }
                    /*SL:1041*/v5 = a2;
                    /*SL:1042*/v6 = a3;
                    /*SL:1043*/break;
                }
            }
            /*SL:1046*/super.fullFrame(v1, v2, v3, v4, v5, v6);
        }
    }
}
