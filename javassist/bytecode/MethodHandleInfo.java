package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class MethodHandleInfo extends ConstInfo
{
    static final int tag = 15;
    int refKind;
    int refIndex;
    
    public MethodHandleInfo(final int a1, final int a2, final int a3) {
        super(a3);
        this.refKind = a1;
        this.refIndex = a2;
    }
    
    public MethodHandleInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.refKind = a1.readUnsignedByte();
        this.refIndex = a1.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        /*SL:1852*/return this.refKind << 16 ^ this.refIndex;
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:1855*/if (v2 instanceof MethodHandleInfo) {
            final MethodHandleInfo a1 = /*EL:1856*/(MethodHandleInfo)v2;
            /*SL:1857*/return a1.refKind == this.refKind && a1.refIndex == this.refIndex;
        }
        /*SL:1860*/return false;
    }
    
    @Override
    public int getTag() {
        /*SL:1863*/return 15;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1866*/return a2.addMethodHandleInfo(this.refKind, a1.getItem(this.refIndex).copy(/*EL:1867*/a1, a2, a3));
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1871*/a1.writeByte(15);
        /*SL:1872*/a1.writeByte(this.refKind);
        /*SL:1873*/a1.writeShort(this.refIndex);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1877*/a1.print("MethodHandle #");
        /*SL:1878*/a1.print(this.refKind);
        /*SL:1879*/a1.print(", index #");
        /*SL:1880*/a1.println(this.refIndex);
    }
}
