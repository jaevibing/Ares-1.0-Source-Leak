package javassist.bytecode.annotation;

import javassist.bytecode.ByteArray;
import java.io.IOException;
import javassist.bytecode.ConstPool;
import java.io.OutputStream;

public class AnnotationsWriter
{
    protected OutputStream output;
    private ConstPool pool;
    
    public AnnotationsWriter(final OutputStream a1, final ConstPool a2) {
        this.output = a1;
        this.pool = a2;
    }
    
    public ConstPool getConstPool() {
        /*SL:79*/return this.pool;
    }
    
    public void close() throws IOException {
        /*SL:87*/this.output.close();
    }
    
    public void numParameters(final int a1) throws IOException {
        /*SL:97*/this.output.write(a1);
    }
    
    public void numAnnotations(final int a1) throws IOException {
        /*SL:107*/this.write16bit(a1);
    }
    
    public void annotation(final String a1, final int a2) throws IOException {
        /*SL:122*/this.annotation(this.pool.addUtf8Info(a1), a2);
    }
    
    public void annotation(final int a1, final int a2) throws IOException {
        /*SL:137*/this.write16bit(a1);
        /*SL:138*/this.write16bit(a2);
    }
    
    public void memberValuePair(final String a1) throws IOException {
        /*SL:151*/this.memberValuePair(this.pool.addUtf8Info(a1));
    }
    
    public void memberValuePair(final int a1) throws IOException {
        /*SL:165*/this.write16bit(a1);
    }
    
    public void constValueIndex(final boolean a1) throws IOException {
        /*SL:175*/this.constValueIndex(90, this.pool.addIntegerInfo((int)(a1 ? 1 : 0)));
    }
    
    public void constValueIndex(final byte a1) throws IOException {
        /*SL:185*/this.constValueIndex(66, this.pool.addIntegerInfo(a1));
    }
    
    public void constValueIndex(final char a1) throws IOException {
        /*SL:195*/this.constValueIndex(67, this.pool.addIntegerInfo(a1));
    }
    
    public void constValueIndex(final short a1) throws IOException {
        /*SL:205*/this.constValueIndex(83, this.pool.addIntegerInfo(a1));
    }
    
    public void constValueIndex(final int a1) throws IOException {
        /*SL:215*/this.constValueIndex(73, this.pool.addIntegerInfo(a1));
    }
    
    public void constValueIndex(final long a1) throws IOException {
        /*SL:225*/this.constValueIndex(74, this.pool.addLongInfo(a1));
    }
    
    public void constValueIndex(final float a1) throws IOException {
        /*SL:235*/this.constValueIndex(70, this.pool.addFloatInfo(a1));
    }
    
    public void constValueIndex(final double a1) throws IOException {
        /*SL:245*/this.constValueIndex(68, this.pool.addDoubleInfo(a1));
    }
    
    public void constValueIndex(final String a1) throws IOException {
        /*SL:255*/this.constValueIndex(115, this.pool.addUtf8Info(a1));
    }
    
    public void constValueIndex(final int a1, final int a2) throws IOException {
        /*SL:269*/this.output.write(a1);
        /*SL:270*/this.write16bit(a2);
    }
    
    public void enumConstValue(final String a1, final String a2) throws IOException {
        /*SL:283*/this.enumConstValue(this.pool.addUtf8Info(a1), this.pool.addUtf8Info(a2));
    }
    
    public void enumConstValue(final int a1, final int a2) throws IOException {
        /*SL:299*/this.output.write(101);
        /*SL:300*/this.write16bit(a1);
        /*SL:301*/this.write16bit(a2);
    }
    
    public void classInfoIndex(final String a1) throws IOException {
        /*SL:311*/this.classInfoIndex(this.pool.addUtf8Info(a1));
    }
    
    public void classInfoIndex(final int a1) throws IOException {
        /*SL:321*/this.output.write(99);
        /*SL:322*/this.write16bit(a1);
    }
    
    public void annotationValue() throws IOException {
        /*SL:331*/this.output.write(64);
    }
    
    public void arrayValue(final int a1) throws IOException {
        /*SL:345*/this.output.write(91);
        /*SL:346*/this.write16bit(a1);
    }
    
    protected void write16bit(final int a1) throws IOException {
        final byte[] v1 = /*EL:350*/new byte[2];
        /*SL:351*/ByteArray.write16bit(a1, v1, 0);
        /*SL:352*/this.output.write(v1);
    }
}
