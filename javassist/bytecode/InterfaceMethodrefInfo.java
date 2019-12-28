package javassist.bytecode;

import java.io.IOException;
import java.io.DataInputStream;

class InterfaceMethodrefInfo extends MemberrefInfo
{
    static final int tag = 11;
    
    public InterfaceMethodrefInfo(final int a1, final int a2, final int a3) {
        super(a1, a2, a3);
    }
    
    public InterfaceMethodrefInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a1, a2);
    }
    
    @Override
    public int getTag() {
        /*SL:1599*/return 11;
    }
    
    @Override
    public String getTagName() {
        /*SL:1601*/return "Interface";
    }
    
    @Override
    protected int copy2(final ConstPool a1, final int a2, final int a3) {
        /*SL:1604*/return a1.addInterfaceMethodrefInfo(a2, a3);
    }
}
