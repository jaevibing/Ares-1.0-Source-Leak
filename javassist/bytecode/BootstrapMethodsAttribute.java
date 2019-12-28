package javassist.bytecode;

import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class BootstrapMethodsAttribute extends AttributeInfo
{
    public static final String tag = "BootstrapMethods";
    
    BootstrapMethodsAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    public BootstrapMethodsAttribute(final ConstPool v-5, final BootstrapMethod[] v-4) {
        super(v-5, "BootstrapMethods");
        int n = 2;
        for (int a1 = 0; a1 < v-4.length; ++a1) {
            n += 4 + v-4[a1].arguments.length * 2;
        }
        final byte[] a3 = new byte[n];
        ByteArray.write16bit(v-4.length, a3, 0);
        int n2 = 2;
        for (int v0 = 0; v0 < v-4.length; ++v0) {
            ByteArray.write16bit(v-4[v0].methodRef, a3, n2);
            ByteArray.write16bit(v-4[v0].arguments.length, a3, n2 + 2);
            final int[] v = v-4[v0].arguments;
            n2 += 4;
            for (int a2 = 0; a2 < v.length; ++a2) {
                ByteArray.write16bit(v[a2], a3, n2);
                n2 += 2;
            }
        }
        this.set(a3);
    }
    
    public BootstrapMethod[] getMethods() {
        final byte[] value = /*EL:83*/this.get();
        final int u16bit = /*EL:84*/ByteArray.readU16bit(value, 0);
        final BootstrapMethod[] array = /*EL:85*/new BootstrapMethod[u16bit];
        int n = /*EL:86*/2;
        /*SL:87*/for (int i = 0; i < u16bit; ++i) {
            final int u16bit2 = /*EL:88*/ByteArray.readU16bit(value, n);
            final int u16bit3 = /*EL:89*/ByteArray.readU16bit(value, n + 2);
            final int[] v0 = /*EL:90*/new int[u16bit3];
            /*SL:91*/n += 4;
            /*SL:92*/for (int v = 0; v < u16bit3; ++v) {
                /*SL:93*/v0[v] = ByteArray.readU16bit(value, n);
                /*SL:94*/n += 2;
            }
            /*SL:97*/array[i] = new BootstrapMethod(u16bit2, v0);
        }
        /*SL:100*/return array;
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v-3, final Map v-2) {
        final BootstrapMethod[] methods = /*EL:112*/this.getMethods();
        final ConstPool v0 = /*EL:113*/this.getConstPool();
        /*SL:114*/for (int v = 0; v < methods.length; ++v) {
            BootstrapMethod a2 = /*EL:115*/methods[v];
            /*SL:116*/a2.methodRef = v0.copy(a2.methodRef, v-3, v-2);
            /*SL:117*/for (a2 = 0; a2 < a2.arguments.length; ++a2) {
                /*SL:118*/a2.arguments[a2] = v0.copy(a2.arguments[a2], v-3, v-2);
            }
        }
        /*SL:121*/return new BootstrapMethodsAttribute(v-3, methods);
    }
    
    public static class BootstrapMethod
    {
        public int methodRef;
        public int[] arguments;
        
        public BootstrapMethod(final int a1, final int[] a2) {
            this.methodRef = a1;
            this.arguments = a2;
        }
    }
}
