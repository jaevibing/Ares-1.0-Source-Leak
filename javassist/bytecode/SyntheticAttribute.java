package javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class SyntheticAttribute extends AttributeInfo
{
    public static final String tag = "Synthetic";
    
    SyntheticAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    public SyntheticAttribute(final ConstPool a1) {
        super(a1, "Synthetic", new byte[0]);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool a1, final Map a2) {
        /*SL:54*/return new SyntheticAttribute(a1);
    }
}
