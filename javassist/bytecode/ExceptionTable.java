package javassist.bytecode;

import java.io.DataOutputStream;
import java.util.Map;
import java.util.Collection;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.ArrayList;

public class ExceptionTable implements Cloneable
{
    private ConstPool constPool;
    private ArrayList entries;
    
    public ExceptionTable(final ConstPool a1) {
        this.constPool = a1;
        this.entries = new ArrayList();
    }
    
    ExceptionTable(final ConstPool v-6, final DataInputStream v-5) throws IOException {
        this.constPool = v-6;
        final int unsignedShort = v-5.readUnsignedShort();
        final ArrayList entries = new ArrayList(unsignedShort);
        for (int i = 0; i < unsignedShort; ++i) {
            final int a1 = v-5.readUnsignedShort();
            final int a2 = v-5.readUnsignedShort();
            final int v1 = v-5.readUnsignedShort();
            final int v2 = v-5.readUnsignedShort();
            entries.add(new ExceptionTableEntry(a1, a2, v1, v2));
        }
        this.entries = entries;
    }
    
    public Object clone() throws CloneNotSupportedException {
        final ExceptionTable v1 = /*EL:77*/(ExceptionTable)super.clone();
        /*SL:78*/v1.entries = new ArrayList(this.entries);
        /*SL:79*/return v1;
    }
    
    public int size() {
        /*SL:87*/return this.entries.size();
    }
    
    public int startPc(final int a1) {
        final ExceptionTableEntry v1 = /*EL:96*/this.entries.get(a1);
        /*SL:97*/return v1.startPc;
    }
    
    public void setStartPc(final int a1, final int a2) {
        final ExceptionTableEntry v1 = /*EL:107*/this.entries.get(a1);
        /*SL:108*/v1.startPc = a2;
    }
    
    public int endPc(final int a1) {
        final ExceptionTableEntry v1 = /*EL:117*/this.entries.get(a1);
        /*SL:118*/return v1.endPc;
    }
    
    public void setEndPc(final int a1, final int a2) {
        final ExceptionTableEntry v1 = /*EL:128*/this.entries.get(a1);
        /*SL:129*/v1.endPc = a2;
    }
    
    public int handlerPc(final int a1) {
        final ExceptionTableEntry v1 = /*EL:138*/this.entries.get(a1);
        /*SL:139*/return v1.handlerPc;
    }
    
    public void setHandlerPc(final int a1, final int a2) {
        final ExceptionTableEntry v1 = /*EL:149*/this.entries.get(a1);
        /*SL:150*/v1.handlerPc = a2;
    }
    
    public int catchType(final int a1) {
        final ExceptionTableEntry v1 = /*EL:161*/this.entries.get(a1);
        /*SL:162*/return v1.catchType;
    }
    
    public void setCatchType(final int a1, final int a2) {
        final ExceptionTableEntry v1 = /*EL:172*/this.entries.get(a1);
        /*SL:173*/v1.catchType = a2;
    }
    
    public void add(final int a3, final ExceptionTable v1, final int v2) {
        int v3 = /*EL:184*/v1.size();
        /*SL:185*/while (--v3 >= 0) {
            final ExceptionTableEntry a4 = /*EL:186*/v1.entries.get(v3);
            /*SL:188*/this.add(a3, a4.startPc + v2, a4.endPc + v2, a4.handlerPc + v2, a4.catchType);
        }
    }
    
    public void add(final int a1, final int a2, final int a3, final int a4, final int a5) {
        /*SL:203*/if (a2 < a3) {
            /*SL:204*/this.entries.add(a1, new ExceptionTableEntry(a2, a3, a4, a5));
        }
    }
    
    public void add(final int a1, final int a2, final int a3, final int a4) {
        /*SL:217*/if (a1 < a2) {
            /*SL:218*/this.entries.add(new ExceptionTableEntry(a1, a2, a3, a4));
        }
    }
    
    public void remove(final int a1) {
        /*SL:227*/this.entries.remove(a1);
    }
    
    public ExceptionTable copy(final ConstPool v-4, final Map v-3) {
        final ExceptionTable exceptionTable = /*EL:240*/new ExceptionTable(v-4);
        final ConstPool constPool = /*EL:241*/this.constPool;
        /*SL:243*/for (int v0 = this.size(), v = 0; v < v0; ++v) {
            final ExceptionTableEntry a1 = /*EL:244*/this.entries.get(v);
            final int a2 = /*EL:245*/constPool.copy(a1.catchType, v-4, v-3);
            /*SL:246*/exceptionTable.add(a1.startPc, a1.endPc, a1.handlerPc, a2);
        }
        /*SL:249*/return exceptionTable;
    }
    
    void shiftPc(final int v1, final int v2, final boolean v3) {
        final int v4 = /*EL:253*/this.size();
        /*SL:254*/for (ExceptionTableEntry a2 = (ExceptionTableEntry)0; a2 < v4; ++a2) {
            /*SL:255*/a2 = this.entries.get(a2);
            /*SL:256*/a2.startPc = shiftPc(a2.startPc, v1, v2, v3);
            /*SL:257*/a2.endPc = shiftPc(a2.endPc, v1, v2, v3);
            /*SL:258*/a2.handlerPc = shiftPc(a2.handlerPc, v1, v2, v3);
        }
    }
    
    private static int shiftPc(int a1, final int a2, final int a3, final boolean a4) {
        /*SL:264*/if (a1 > a2 || (a4 && a1 == a2)) {
            /*SL:265*/a1 += a3;
        }
        /*SL:267*/return a1;
    }
    
    void write(final DataOutputStream v-1) throws IOException {
        final int v0 = /*EL:271*/this.size();
        /*SL:272*/v-1.writeShort(v0);
        /*SL:273*/for (int v = 0; v < v0; ++v) {
            final ExceptionTableEntry a1 = /*EL:274*/this.entries.get(v);
            /*SL:275*/v-1.writeShort(a1.startPc);
            /*SL:276*/v-1.writeShort(a1.endPc);
            /*SL:277*/v-1.writeShort(a1.handlerPc);
            /*SL:278*/v-1.writeShort(a1.catchType);
        }
    }
}
