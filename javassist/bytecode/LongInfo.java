package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class LongInfo extends ConstInfo
{
    static final int tag = 5;
    long value;
    
    public LongInfo(final long a1, final int a2) {
        super(a2);
        this.value = a1;
    }
    
    public LongInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.value = a1.readLong();
    }
    
    @Override
    public int hashCode() {
        /*SL:1733*/return (int)(this.value ^ this.value >>> 32);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1736*/return a1 instanceof LongInfo && ((LongInfo)a1).value == this.value;
    }
    
    @Override
    public int getTag() {
        /*SL:1739*/return 5;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1742*/return a2.addLongInfo(this.value);
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1746*/a1.writeByte(5);
        /*SL:1747*/a1.writeLong(this.value);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1751*/a1.print("Long ");
        /*SL:1752*/a1.println(this.value);
    }
}
