package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class StringInfo extends ConstInfo
{
    static final int tag = 8;
    int string;
    
    public StringInfo(final int a1, final int a2) {
        super(a2);
        this.string = a1;
    }
    
    public StringInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.string = a1.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        /*SL:1622*/return this.string;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1625*/return a1 instanceof StringInfo && ((StringInfo)a1).string == this.string;
    }
    
    @Override
    public int getTag() {
        /*SL:1628*/return 8;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1631*/return a2.addStringInfo(a1.getUtf8Info(this.string));
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1635*/a1.writeByte(8);
        /*SL:1636*/a1.writeShort(this.string);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1640*/a1.print("String #");
        /*SL:1641*/a1.println(this.string);
    }
}
