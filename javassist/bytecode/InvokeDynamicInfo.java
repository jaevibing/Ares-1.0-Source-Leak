package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class InvokeDynamicInfo extends ConstInfo
{
    static final int tag = 18;
    int bootstrap;
    int nameAndType;
    
    public InvokeDynamicInfo(final int a1, final int a2, final int a3) {
        super(a3);
        this.bootstrap = a1;
        this.nameAndType = a2;
    }
    
    public InvokeDynamicInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.bootstrap = a1.readUnsignedShort();
        this.nameAndType = a1.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        /*SL:1968*/return this.bootstrap << 16 ^ this.nameAndType;
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:1971*/if (v2 instanceof InvokeDynamicInfo) {
            final InvokeDynamicInfo a1 = /*EL:1972*/(InvokeDynamicInfo)v2;
            /*SL:1973*/return a1.bootstrap == this.bootstrap && a1.nameAndType == this.nameAndType;
        }
        /*SL:1976*/return false;
    }
    
    @Override
    public int getTag() {
        /*SL:1979*/return 18;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1982*/return a2.addInvokeDynamicInfo(this.bootstrap, a1.getItem(this.nameAndType).copy(/*EL:1983*/a1, a2, a3));
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1987*/a1.writeByte(18);
        /*SL:1988*/a1.writeShort(this.bootstrap);
        /*SL:1989*/a1.writeShort(this.nameAndType);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1993*/a1.print("InvokeDynamic #");
        /*SL:1994*/a1.print(this.bootstrap);
        /*SL:1995*/a1.print(", name&type #");
        /*SL:1996*/a1.println(this.nameAndType);
    }
}
