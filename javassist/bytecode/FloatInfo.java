package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class FloatInfo extends ConstInfo
{
    static final int tag = 4;
    float value;
    
    public FloatInfo(final float a1, final int a2) {
        super(a2);
        this.value = a1;
    }
    
    public FloatInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.value = a1.readFloat();
    }
    
    @Override
    public int hashCode() {
        /*SL:1696*/return Float.floatToIntBits(this.value);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1699*/return a1 instanceof FloatInfo && ((FloatInfo)a1).value == this.value;
    }
    
    @Override
    public int getTag() {
        /*SL:1702*/return 4;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1705*/return a2.addFloatInfo(this.value);
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1709*/a1.writeByte(4);
        /*SL:1710*/a1.writeFloat(this.value);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1714*/a1.print("Float ");
        /*SL:1715*/a1.println(this.value);
    }
}
