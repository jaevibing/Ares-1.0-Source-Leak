package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.DataInputStream;

class ClassInfo extends ConstInfo
{
    static final int tag = 7;
    int name;
    
    public ClassInfo(final int a1, final int a2) {
        super(a2);
        this.name = a1;
    }
    
    public ClassInfo(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.name = a1.readUnsignedShort();
    }
    
    @Override
    public int hashCode() {
        /*SL:1338*/return this.name;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1341*/return a1 instanceof ClassInfo && ((ClassInfo)a1).name == this.name;
    }
    
    @Override
    public int getTag() {
        /*SL:1344*/return 7;
    }
    
    @Override
    public String getClassName(final ConstPool a1) {
        /*SL:1347*/return a1.getUtf8Info(this.name);
    }
    
    @Override
    public void renameClass(final ConstPool a3, final String a4, final String v1, final HashMap v2) {
        final String v3 = /*EL:1351*/a3.getUtf8Info(this.name);
        String v4 = /*EL:1352*/null;
        /*SL:1353*/if (v3.equals(a4)) {
            /*SL:1354*/v4 = v1;
        }
        else/*SL:1355*/ if (v3.charAt(0) == '[') {
            final String a5 = /*EL:1356*/Descriptor.rename(v3, a4, v1);
            /*SL:1357*/if (v3 != a5) {
                /*SL:1358*/v4 = a5;
            }
        }
        /*SL:1361*/if (v4 != null) {
            /*SL:1362*/if (v2 == null) {
                /*SL:1363*/this.name = a3.addUtf8Info(v4);
            }
            else {
                /*SL:1365*/v2.remove(this);
                /*SL:1366*/this.name = a3.addUtf8Info(v4);
                /*SL:1367*/v2.put(this, this);
            }
        }
    }
    
    @Override
    public void renameClass(final ConstPool v1, final Map v2, final HashMap v3) {
        final String v4 = /*EL:1372*/v1.getUtf8Info(this.name);
        String v5 = /*EL:1373*/null;
        /*SL:1374*/if (v4.charAt(0) == '[') {
            final String a1 = /*EL:1375*/Descriptor.rename(v4, v2);
            /*SL:1376*/if (v4 != a1) {
                /*SL:1377*/v5 = a1;
            }
        }
        else {
            final String a2 = /*EL:1380*/v2.get(v4);
            /*SL:1381*/if (a2 != null && !a2.equals(v4)) {
                /*SL:1382*/v5 = a2;
            }
        }
        /*SL:1385*/if (v5 != null) {
            /*SL:1386*/if (v3 == null) {
                /*SL:1387*/this.name = v1.addUtf8Info(v5);
            }
            else {
                /*SL:1389*/v3.remove(this);
                /*SL:1390*/this.name = v1.addUtf8Info(v5);
                /*SL:1391*/v3.put(this, this);
            }
        }
    }
    
    @Override
    public int copy(final ConstPool a3, final ConstPool v1, final Map v2) {
        String v3 = /*EL:1397*/a3.getUtf8Info(this.name);
        /*SL:1398*/if (v2 != null) {
            final String a4 = /*EL:1399*/v2.get(v3);
            /*SL:1400*/if (a4 != null) {
                /*SL:1401*/v3 = a4;
            }
        }
        /*SL:1404*/return v1.addClassInfo(v3);
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1408*/a1.writeByte(7);
        /*SL:1409*/a1.writeShort(this.name);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1413*/a1.print("Class #");
        /*SL:1414*/a1.println(this.name);
    }
}
