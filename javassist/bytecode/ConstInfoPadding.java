package javassist.bytecode;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Map;

class ConstInfoPadding extends ConstInfo
{
    public ConstInfoPadding(final int a1) {
        super(a1);
    }
    
    @Override
    public int getTag() {
        /*SL:1311*/return 0;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1314*/return a2.addConstInfoPadding();
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1320*/a1.println("padding");
    }
}
