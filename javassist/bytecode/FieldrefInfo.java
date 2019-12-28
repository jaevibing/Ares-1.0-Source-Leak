package javassist.bytecode;

import java.io.IOException;
import java.io.DataInputStream;

class FieldrefInfo extends MemberrefInfo
{
    static final int tag = 9;
    
    public FieldrefInfo(final int a1, final int a2, final int a3) {
        super(a1, a2, a3);
    }
    
    public FieldrefInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a1, a2);
    }
    
    @Override
    public int getTag() {
        /*SL:1559*/return 9;
    }
    
    @Override
    public String getTagName() {
        /*SL:1561*/return "Field";
    }
    
    @Override
    protected int copy2(final ConstPool a1, final int a2, final int a3) {
        /*SL:1564*/return a1.addFieldrefInfo(a2, a3);
    }
}
