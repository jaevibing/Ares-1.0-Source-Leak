package javassist.bytecode;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Map;
import java.util.HashMap;

abstract class ConstInfo
{
    int index;
    
    public ConstInfo(final int a1) {
        this.index = a1;
    }
    
    public abstract int getTag();
    
    public String getClassName(final ConstPool a1) {
        /*SL:1289*/return null;
    }
    
    public void renameClass(final ConstPool a1, final String a2, final String a3, final HashMap a4) {
    }
    
    public void renameClass(final ConstPool a1, final Map a2, final HashMap a3) {
    }
    
    public abstract int copy(final ConstPool p0, final ConstPool p1, final Map p2);
    
    public abstract void write(final DataOutputStream p0) throws IOException;
    
    public abstract void print(final PrintWriter p0);
    
    @Override
    public String toString() {
        final ByteArrayOutputStream v1 = /*EL:1299*/new ByteArrayOutputStream();
        final PrintWriter v2 = /*EL:1300*/new PrintWriter(v1);
        /*SL:1301*/this.print(v2);
        /*SL:1302*/return v1.toString();
    }
}
