package javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ClassFileWriter
{
    private ByteStream output;
    private ConstPoolWriter constPool;
    private FieldWriter fields;
    private MethodWriter methods;
    int thisClass;
    int superClass;
    
    public ClassFileWriter(final int a1, final int a2) {
        (this.output = new ByteStream(512)).writeInt(-889275714);
        this.output.writeShort(a2);
        this.output.writeShort(a1);
        this.constPool = new ConstPoolWriter(this.output);
        this.fields = new FieldWriter(this.constPool);
        this.methods = new MethodWriter(this.constPool);
    }
    
    public ConstPoolWriter getConstPool() {
        /*SL:103*/return this.constPool;
    }
    
    public FieldWriter getFieldWriter() {
        /*SL:108*/return this.fields;
    }
    
    public MethodWriter getMethodWriter() {
        /*SL:113*/return this.methods;
    }
    
    public byte[] end(final int a4, final int a5, final int v1, final int[] v2, final AttributeWriter v3) {
        /*SL:130*/this.constPool.end();
        /*SL:131*/this.output.writeShort(a4);
        /*SL:132*/this.output.writeShort(a5);
        /*SL:133*/this.output.writeShort(v1);
        /*SL:134*/if (v2 == null) {
            /*SL:135*/this.output.writeShort(0);
        }
        else {
            final int a6 = /*EL:137*/v2.length;
            /*SL:138*/this.output.writeShort(a6);
            /*SL:139*/for (int a7 = 0; a7 < a6; ++a7) {
                /*SL:140*/this.output.writeShort(v2[a7]);
            }
        }
        /*SL:143*/this.output.enlarge(this.fields.dataSize() + this.methods.dataSize() + 6);
        try {
            /*SL:145*/this.output.writeShort(this.fields.size());
            /*SL:146*/this.fields.write(this.output);
            /*SL:148*/this.output.writeShort(this.methods.numOfMethods());
            /*SL:149*/this.methods.write(this.output);
        }
        catch (IOException ex) {}
        writeAttribute(/*EL:153*/this.output, v3, 0);
        /*SL:154*/return this.output.toByteArray();
    }
    
    public void end(final DataOutputStream a4, final int a5, final int a6, final int v1, final int[] v2, final AttributeWriter v3) throws IOException {
        /*SL:176*/this.constPool.end();
        /*SL:177*/this.output.writeTo(a4);
        /*SL:178*/a4.writeShort(a5);
        /*SL:179*/a4.writeShort(a6);
        /*SL:180*/a4.writeShort(v1);
        /*SL:181*/if (v2 == null) {
            /*SL:182*/a4.writeShort(0);
        }
        else {
            final int a7 = /*EL:184*/v2.length;
            /*SL:185*/a4.writeShort(a7);
            /*SL:186*/for (int a8 = 0; a8 < a7; ++a8) {
                /*SL:187*/a4.writeShort(v2[a8]);
            }
        }
        /*SL:190*/a4.writeShort(this.fields.size());
        /*SL:191*/this.fields.write(a4);
        /*SL:193*/a4.writeShort(this.methods.numOfMethods());
        /*SL:194*/this.methods.write(a4);
        /*SL:195*/if (v3 == null) {
            /*SL:196*/a4.writeShort(0);
        }
        else {
            /*SL:198*/a4.writeShort(v3.size());
            /*SL:199*/v3.write(a4);
        }
    }
    
    static void writeAttribute(final ByteStream a1, final AttributeWriter a2, final int a3) {
        /*SL:237*/if (a2 == null) {
            /*SL:238*/a1.writeShort(a3);
            /*SL:239*/return;
        }
        /*SL:242*/a1.writeShort(a2.size() + a3);
        final DataOutputStream v1 = /*EL:243*/new DataOutputStream(a1);
        try {
            /*SL:245*/a2.write(v1);
            /*SL:246*/v1.flush();
        }
        catch (IOException ex) {}
    }
    
    public static final class FieldWriter
    {
        protected ByteStream output;
        protected ConstPoolWriter constPool;
        private int fieldCount;
        
        FieldWriter(final ConstPoolWriter a1) {
            this.output = new ByteStream(128);
            this.constPool = a1;
            this.fieldCount = 0;
        }
        
        public void add(final int a1, final String a2, final String a3, final AttributeWriter a4) {
            final int v1 = /*EL:275*/this.constPool.addUtf8Info(a2);
            final int v2 = /*EL:276*/this.constPool.addUtf8Info(a3);
            /*SL:277*/this.add(a1, v1, v2, a4);
        }
        
        public void add(final int a1, final int a2, final int a3, final AttributeWriter a4) {
            /*SL:290*/++this.fieldCount;
            /*SL:291*/this.output.writeShort(a1);
            /*SL:292*/this.output.writeShort(a2);
            /*SL:293*/this.output.writeShort(a3);
            /*SL:294*/ClassFileWriter.writeAttribute(this.output, a4, 0);
        }
        
        int size() {
            /*SL:297*/return this.fieldCount;
        }
        
        int dataSize() {
            /*SL:299*/return this.output.size();
        }
        
        void write(final OutputStream a1) throws IOException {
            /*SL:305*/this.output.writeTo(a1);
        }
    }
    
    public static final class MethodWriter
    {
        protected ByteStream output;
        protected ConstPoolWriter constPool;
        private int methodCount;
        protected int codeIndex;
        protected int throwsIndex;
        protected int stackIndex;
        private int startPos;
        private boolean isAbstract;
        private int catchPos;
        private int catchCount;
        
        MethodWriter(final ConstPoolWriter a1) {
            /*SL:306*/this.output = new ByteStream(256);
            this.constPool = a1;
            this.methodCount = 0;
            this.codeIndex = 0;
            this.throwsIndex = 0;
            this.stackIndex = 0;
        }
        
        public void begin(final int a3, final String a4, final String a5, final String[] v1, final AttributeWriter v2) {
            final int v3 = /*EL:347*/this.constPool.addUtf8Info(a4);
            final int v4 = /*EL:348*/this.constPool.addUtf8Info(a5);
            final int[] v5;
            /*SL:350*/if (v1 == null) {
                final int[] a6 = /*EL:351*/null;
            }
            else {
                /*SL:353*/v5 = this.constPool.addClassInfo(v1);
            }
            /*SL:355*/this.begin(a3, v3, v4, v5, v2);
        }
        
        public void begin(final int a1, final int a2, final int a3, final int[] a4, final AttributeWriter a5) {
            /*SL:369*/++this.methodCount;
            /*SL:370*/this.output.writeShort(a1);
            /*SL:371*/this.output.writeShort(a2);
            /*SL:372*/this.output.writeShort(a3);
            /*SL:373*/this.isAbstract = ((a1 & 0x400) != 0x0);
            int v1 = /*EL:375*/this.isAbstract ? 0 : 1;
            /*SL:376*/if (a4 != null) {
                /*SL:377*/++v1;
            }
            /*SL:379*/ClassFileWriter.writeAttribute(this.output, a5, v1);
            /*SL:381*/if (a4 != null) {
                /*SL:382*/this.writeThrows(a4);
            }
            /*SL:384*/if (!this.isAbstract) {
                /*SL:385*/if (this.codeIndex == 0) {
                    /*SL:386*/this.codeIndex = this.constPool.addUtf8Info("Code");
                }
                /*SL:388*/this.startPos = this.output.getPos();
                /*SL:389*/this.output.writeShort(this.codeIndex);
                /*SL:390*/this.output.writeBlank(12);
            }
            /*SL:393*/this.catchPos = -1;
            /*SL:394*/this.catchCount = 0;
        }
        
        private void writeThrows(final int[] v2) {
            /*SL:398*/if (this.throwsIndex == 0) {
                /*SL:399*/this.throwsIndex = this.constPool.addUtf8Info("Exceptions");
            }
            /*SL:401*/this.output.writeShort(this.throwsIndex);
            /*SL:402*/this.output.writeInt(v2.length * 2 + 2);
            /*SL:403*/this.output.writeShort(v2.length);
            /*SL:404*/for (int a1 = 0; a1 < v2.length; ++a1) {
                /*SL:405*/this.output.writeShort(v2[a1]);
            }
        }
        
        public void add(final int a1) {
            /*SL:414*/this.output.write(a1);
        }
        
        public void add16(final int a1) {
            /*SL:421*/this.output.writeShort(a1);
        }
        
        public void add32(final int a1) {
            /*SL:428*/this.output.writeInt(a1);
        }
        
        public void addInvoke(final int a1, final String a2, final String a3, final String a4) {
            final int v1 = /*EL:438*/this.constPool.addClassInfo(a2);
            final int v2 = /*EL:439*/this.constPool.addNameAndTypeInfo(a3, a4);
            final int v3 = /*EL:440*/this.constPool.addMethodrefInfo(v1, v2);
            /*SL:441*/this.add(a1);
            /*SL:442*/this.add16(v3);
        }
        
        public void codeEnd(final int a1, final int a2) {
            /*SL:449*/if (!this.isAbstract) {
                /*SL:450*/this.output.writeShort(this.startPos + 6, a1);
                /*SL:451*/this.output.writeShort(this.startPos + 8, a2);
                /*SL:452*/this.output.writeInt(this.startPos + 10, this.output.getPos() - this.startPos - 14);
                /*SL:453*/this.catchPos = this.output.getPos();
                /*SL:454*/this.catchCount = 0;
                /*SL:455*/this.output.writeShort(0);
            }
        }
        
        public void addCatch(final int a1, final int a2, final int a3, final int a4) {
            /*SL:467*/++this.catchCount;
            /*SL:468*/this.output.writeShort(a1);
            /*SL:469*/this.output.writeShort(a2);
            /*SL:470*/this.output.writeShort(a3);
            /*SL:471*/this.output.writeShort(a4);
        }
        
        public void end(final StackMapTable.Writer v1, final AttributeWriter v2) {
            /*SL:483*/if (this.isAbstract) {
                /*SL:484*/return;
            }
            /*SL:487*/this.output.writeShort(this.catchPos, this.catchCount);
            final int v3 = /*EL:489*/(v1 != null) ? 1 : 0;
            /*SL:490*/ClassFileWriter.writeAttribute(this.output, v2, v3);
            /*SL:492*/if (v1 != null) {
                /*SL:493*/if (this.stackIndex == 0) {
                    /*SL:494*/this.stackIndex = this.constPool.addUtf8Info("StackMapTable");
                }
                /*SL:496*/this.output.writeShort(this.stackIndex);
                final byte[] a1 = /*EL:497*/v1.toByteArray();
                /*SL:498*/this.output.writeInt(a1.length);
                /*SL:499*/this.output.write(a1);
            }
            /*SL:503*/this.output.writeInt(this.startPos + 2, this.output.getPos() - this.startPos - 6);
        }
        
        public int size() {
            /*SL:512*/return this.output.getPos() - this.startPos - 14;
        }
        
        int numOfMethods() {
            /*SL:514*/return this.methodCount;
        }
        
        int dataSize() {
            /*SL:516*/return this.output.size();
        }
        
        void write(final OutputStream a1) throws IOException {
            /*SL:522*/this.output.writeTo(a1);
        }
    }
    
    public static final class ConstPoolWriter
    {
        ByteStream output;
        protected int startPos;
        protected int num;
        
        ConstPoolWriter(final ByteStream a1) {
            this.output = a1;
            /*SL:523*/this.startPos = a1.getPos();
            this.num = 1;
            this.output.writeShort(1);
        }
        
        public int[] addClassInfo(final String[] v2) {
            final int v3 = /*EL:547*/v2.length;
            final int[] v4 = /*EL:548*/new int[v3];
            /*SL:549*/for (int a1 = 0; a1 < v3; ++a1) {
                /*SL:550*/v4[a1] = this.addClassInfo(v2[a1]);
            }
            /*SL:552*/return v4;
        }
        
        public int addClassInfo(final String a1) {
            final int v1 = /*EL:566*/this.addUtf8Info(a1);
            /*SL:567*/this.output.write(7);
            /*SL:568*/this.output.writeShort(v1);
            /*SL:569*/return this.num++;
        }
        
        public int addClassInfo(final int a1) {
            /*SL:579*/this.output.write(7);
            /*SL:580*/this.output.writeShort(a1);
            /*SL:581*/return this.num++;
        }
        
        public int addNameAndTypeInfo(final String a1, final String a2) {
            /*SL:592*/return this.addNameAndTypeInfo(this.addUtf8Info(a1), this.addUtf8Info(a2));
        }
        
        public int addNameAndTypeInfo(final int a1, final int a2) {
            /*SL:603*/this.output.write(12);
            /*SL:604*/this.output.writeShort(a1);
            /*SL:605*/this.output.writeShort(a2);
            /*SL:606*/return this.num++;
        }
        
        public int addFieldrefInfo(final int a1, final int a2) {
            /*SL:617*/this.output.write(9);
            /*SL:618*/this.output.writeShort(a1);
            /*SL:619*/this.output.writeShort(a2);
            /*SL:620*/return this.num++;
        }
        
        public int addMethodrefInfo(final int a1, final int a2) {
            /*SL:631*/this.output.write(10);
            /*SL:632*/this.output.writeShort(a1);
            /*SL:633*/this.output.writeShort(a2);
            /*SL:634*/return this.num++;
        }
        
        public int addInterfaceMethodrefInfo(final int a1, final int a2) {
            /*SL:647*/this.output.write(11);
            /*SL:648*/this.output.writeShort(a1);
            /*SL:649*/this.output.writeShort(a2);
            /*SL:650*/return this.num++;
        }
        
        public int addMethodHandleInfo(final int a1, final int a2) {
            /*SL:665*/this.output.write(15);
            /*SL:666*/this.output.write(a1);
            /*SL:667*/this.output.writeShort(a2);
            /*SL:668*/return this.num++;
        }
        
        public int addMethodTypeInfo(final int a1) {
            /*SL:681*/this.output.write(16);
            /*SL:682*/this.output.writeShort(a1);
            /*SL:683*/return this.num++;
        }
        
        public int addInvokeDynamicInfo(final int a1, final int a2) {
            /*SL:698*/this.output.write(18);
            /*SL:699*/this.output.writeShort(a1);
            /*SL:700*/this.output.writeShort(a2);
            /*SL:701*/return this.num++;
        }
        
        public int addStringInfo(final String a1) {
            final int v1 = /*EL:714*/this.addUtf8Info(a1);
            /*SL:715*/this.output.write(8);
            /*SL:716*/this.output.writeShort(v1);
            /*SL:717*/return this.num++;
        }
        
        public int addIntegerInfo(final int a1) {
            /*SL:727*/this.output.write(3);
            /*SL:728*/this.output.writeInt(a1);
            /*SL:729*/return this.num++;
        }
        
        public int addFloatInfo(final float a1) {
            /*SL:739*/this.output.write(4);
            /*SL:740*/this.output.writeFloat(a1);
            /*SL:741*/return this.num++;
        }
        
        public int addLongInfo(final long a1) {
            /*SL:751*/this.output.write(5);
            /*SL:752*/this.output.writeLong(a1);
            final int v1 = /*EL:753*/this.num;
            /*SL:754*/this.num += 2;
            /*SL:755*/return v1;
        }
        
        public int addDoubleInfo(final double a1) {
            /*SL:765*/this.output.write(6);
            /*SL:766*/this.output.writeDouble(a1);
            final int v1 = /*EL:767*/this.num;
            /*SL:768*/this.num += 2;
            /*SL:769*/return v1;
        }
        
        public int addUtf8Info(final String a1) {
            /*SL:779*/this.output.write(1);
            /*SL:780*/this.output.writeUTF(a1);
            /*SL:781*/return this.num++;
        }
        
        void end() {
            /*SL:788*/this.output.writeShort(this.startPos, this.num);
        }
    }
    
    public interface AttributeWriter
    {
        int size();
        
        void write(DataOutputStream p0) throws IOException;
    }
}
