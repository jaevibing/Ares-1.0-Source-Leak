package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class IntegerInfo extends ConstInfo
{
    static final int tag = 3;
    int value;
    
    public IntegerInfo(final int a1, final int a2) {
        super(a2);
        this.value = a1;
    }
    
    public IntegerInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.value = a1.readInt();
    }
    
    @Override
    public int hashCode() {
        /*SL:1659*/return this.value;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1662*/return a1 instanceof IntegerInfo && ((IntegerInfo)a1).value == this.value;
    }
    
    @Override
    public int getTag() {
        /*SL:1665*/return 3;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1668*/return a2.addIntegerInfo(this.value);
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1672*/a1.writeByte(3);
        /*SL:1673*/a1.writeInt(this.value);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1677*/a1.print("Integer ");
        /*SL:1678*/a1.println(this.value);
    }
}
