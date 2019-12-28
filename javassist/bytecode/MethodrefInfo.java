package javassist.bytecode;

import java.io.IOException;
import java.io.DataInputStream;

class MethodrefInfo extends MemberrefInfo
{
    static final int tag = 10;
    
    public MethodrefInfo(final int a1, final int a2, final int a3) {
        super(a1, a2, a3);
    }
    
    public MethodrefInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a1, a2);
    }
    
    @Override
    public int getTag() {
        /*SL:1579*/return 10;
    }
    
    @Override
    public String getTagName() {
        /*SL:1581*/return "Method";
    }
    
    @Override
    protected int copy2(final ConstPool a1, final int a2, final int a3) {
        /*SL:1584*/return a1.addMethodrefInfo(a2, a3);
    }
}
