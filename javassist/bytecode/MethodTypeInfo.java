package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.DataInputStream;

class MethodTypeInfo extends ConstInfo
{
    static final int tag = 16;
    int descriptor;
    
    public MethodTypeInfo(final int a1, final int a2) {
        super(a2);
        this.descriptor = a1;
    }
    
    public MethodTypeInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.descriptor = a1.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        /*SL:1898*/return this.descriptor;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1901*/return a1 instanceof MethodTypeInfo && /*EL:1902*/((MethodTypeInfo)a1).descriptor == this.descriptor;
    }
    
    @Override
    public int getTag() {
        /*SL:1907*/return 16;
    }
    
    @Override
    public void renameClass(final ConstPool a1, final String a2, final String a3, final HashMap a4) {
        final String v1 = /*EL:1910*/a1.getUtf8Info(this.descriptor);
        final String v2 = /*EL:1911*/Descriptor.rename(v1, a2, a3);
        /*SL:1912*/if (v1 != v2) {
            /*SL:1913*/if (a4 == null) {
                /*SL:1914*/this.descriptor = a1.addUtf8Info(v2);
            }
            else {
                /*SL:1916*/a4.remove(this);
                /*SL:1917*/this.descriptor = a1.addUtf8Info(v2);
                /*SL:1918*/a4.put(this, this);
            }
        }
    }
    
    @Override
    public void renameClass(final ConstPool a1, final Map a2, final HashMap a3) {
        final String v1 = /*EL:1923*/a1.getUtf8Info(this.descriptor);
        final String v2 = /*EL:1924*/Descriptor.rename(v1, a2);
        /*SL:1925*/if (v1 != v2) {
            /*SL:1926*/if (a3 == null) {
                /*SL:1927*/this.descriptor = a1.addUtf8Info(v2);
            }
            else {
                /*SL:1929*/a3.remove(this);
                /*SL:1930*/this.descriptor = a1.addUtf8Info(v2);
                /*SL:1931*/a3.put(this, this);
            }
        }
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        String v1 = /*EL:1936*/a1.getUtf8Info(this.descriptor);
        /*SL:1937*/v1 = Descriptor.rename(v1, a3);
        /*SL:1938*/return a2.addMethodTypeInfo(a2.addUtf8Info(v1));
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1942*/a1.writeByte(16);
        /*SL:1943*/a1.writeShort(this.descriptor);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1947*/a1.print("MethodType #");
        /*SL:1948*/a1.println(this.descriptor);
    }
}
