package javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class EnclosingMethodAttribute extends AttributeInfo
{
    public static final String tag = "EnclosingMethod";
    
    EnclosingMethodAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    public EnclosingMethodAttribute(final ConstPool a1, final String a2, final String a3, final String a4) {
        super(a1, "EnclosingMethod");
        final int v1 = a1.addClassInfo(a2);
        final int v2 = a1.addNameAndTypeInfo(a3, a4);
        final byte[] v3 = { (byte)(v1 >>> 8), (byte)v1, (byte)(v2 >>> 8), (byte)v2 };
        this.set(v3);
    }
    
    public EnclosingMethodAttribute(final ConstPool a1, final String a2) {
        super(a1, "EnclosingMethod");
        final int v1 = a1.addClassInfo(a2);
        final int v2 = 0;
        final byte[] v3 = { (byte)(v1 >>> 8), (byte)v1, (byte)(v2 >>> 8), (byte)v2 };
        this.set(v3);
    }
    
    public int classIndex() {
        /*SL:84*/return ByteArray.readU16bit(this.get(), 0);
    }
    
    public int methodIndex() {
        /*SL:91*/return ByteArray.readU16bit(this.get(), 2);
    }
    
    public String className() {
        /*SL:98*/return this.getConstPool().getClassInfo(this.classIndex());
    }
    
    public String methodName() {
        final ConstPool constPool = /*EL:107*/this.getConstPool();
        final int v0 = /*EL:108*/this.methodIndex();
        /*SL:109*/if (v0 == 0) {
            /*SL:110*/return "<clinit>";
        }
        final int v = /*EL:112*/constPool.getNameAndTypeName(v0);
        /*SL:113*/return constPool.getUtf8Info(v);
    }
    
    public String methodDescriptor() {
        final ConstPool v1 = /*EL:121*/this.getConstPool();
        final int v2 = /*EL:122*/this.methodIndex();
        final int v3 = /*EL:123*/v1.getNameAndTypeDescriptor(v2);
        /*SL:124*/return v1.getUtf8Info(v3);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool a1, final Map a2) {
        /*SL:136*/if (this.methodIndex() == 0) {
            /*SL:137*/return new EnclosingMethodAttribute(a1, this.className());
        }
        /*SL:140*/return new EnclosingMethodAttribute(a1, this.className(), this.methodName(), this.methodDescriptor());
    }
}
