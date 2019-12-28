package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

abstract class MemberrefInfo extends ConstInfo
{
    int classIndex;
    int nameAndTypeIndex;
    
    public MemberrefInfo(final int a1, final int a2, final int a3) {
        super(a3);
        this.classIndex = a1;
        this.nameAndTypeIndex = a2;
    }
    
    public MemberrefInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.classIndex = a1.readUnsignedShort();
        this.nameAndTypeIndex = a1.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        /*SL:1512*/return this.classIndex << 16 ^ this.nameAndTypeIndex;
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:1515*/if (v2 instanceof MemberrefInfo) {
            final MemberrefInfo a1 = /*EL:1516*/(MemberrefInfo)v2;
            /*SL:1518*/return a1.classIndex == this.classIndex && a1.nameAndTypeIndex == this.nameAndTypeIndex && a1.getClass() == this.getClass();
        }
        /*SL:1521*/return false;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        final int v1 = /*EL:1525*/a1.getItem(this.classIndex).copy(a1, a2, a3);
        final int v2 = /*EL:1526*/a1.getItem(this.nameAndTypeIndex).copy(a1, a2, a3);
        /*SL:1527*/return this.copy2(a2, v1, v2);
    }
    
    protected abstract int copy2(final ConstPool p0, final int p1, final int p2);
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1533*/a1.writeByte(this.getTag());
        /*SL:1534*/a1.writeShort(this.classIndex);
        /*SL:1535*/a1.writeShort(this.nameAndTypeIndex);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1539*/a1.print(this.getTagName() + " #");
        /*SL:1540*/a1.print(this.classIndex);
        /*SL:1541*/a1.print(", name&type #");
        /*SL:1542*/a1.println(this.nameAndTypeIndex);
    }
    
    public abstract String getTagName();
}
