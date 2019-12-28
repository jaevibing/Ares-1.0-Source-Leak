package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.DataInputStream;

class NameAndTypeInfo extends ConstInfo
{
    static final int tag = 12;
    int memberName;
    int typeDescriptor;
    
    public NameAndTypeInfo(final int a1, final int a2, final int a3) {
        super(a3);
        this.memberName = a1;
        this.typeDescriptor = a2;
    }
    
    public NameAndTypeInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.memberName = a1.readUnsignedShort();
        this.typeDescriptor = a1.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        /*SL:1435*/return this.memberName << 16 ^ this.typeDescriptor;
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:1438*/if (v2 instanceof NameAndTypeInfo) {
            final NameAndTypeInfo a1 = /*EL:1439*/(NameAndTypeInfo)v2;
            /*SL:1440*/return a1.memberName == this.memberName && a1.typeDescriptor == this.typeDescriptor;
        }
        /*SL:1443*/return false;
    }
    
    @Override
    public int getTag() {
        /*SL:1446*/return 12;
    }
    
    @Override
    public void renameClass(final ConstPool a1, final String a2, final String a3, final HashMap a4) {
        final String v1 = /*EL:1449*/a1.getUtf8Info(this.typeDescriptor);
        final String v2 = /*EL:1450*/Descriptor.rename(v1, a2, a3);
        /*SL:1451*/if (v1 != v2) {
            /*SL:1452*/if (a4 == null) {
                /*SL:1453*/this.typeDescriptor = a1.addUtf8Info(v2);
            }
            else {
                /*SL:1455*/a4.remove(this);
                /*SL:1456*/this.typeDescriptor = a1.addUtf8Info(v2);
                /*SL:1457*/a4.put(this, this);
            }
        }
    }
    
    @Override
    public void renameClass(final ConstPool a1, final Map a2, final HashMap a3) {
        final String v1 = /*EL:1462*/a1.getUtf8Info(this.typeDescriptor);
        final String v2 = /*EL:1463*/Descriptor.rename(v1, a2);
        /*SL:1464*/if (v1 != v2) {
            /*SL:1465*/if (a3 == null) {
                /*SL:1466*/this.typeDescriptor = a1.addUtf8Info(v2);
            }
            else {
                /*SL:1468*/a3.remove(this);
                /*SL:1469*/this.typeDescriptor = a1.addUtf8Info(v2);
                /*SL:1470*/a3.put(this, this);
            }
        }
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        final String v1 = /*EL:1475*/a1.getUtf8Info(this.memberName);
        String v2 = /*EL:1476*/a1.getUtf8Info(this.typeDescriptor);
        /*SL:1477*/v2 = Descriptor.rename(v2, a3);
        /*SL:1478*/return a2.addNameAndTypeInfo(a2.addUtf8Info(v1), a2.addUtf8Info(v2));
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1483*/a1.writeByte(12);
        /*SL:1484*/a1.writeShort(this.memberName);
        /*SL:1485*/a1.writeShort(this.typeDescriptor);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1489*/a1.print("NameAndType #");
        /*SL:1490*/a1.print(this.memberName);
        /*SL:1491*/a1.print(", type #");
        /*SL:1492*/a1.println(this.typeDescriptor);
    }
}
