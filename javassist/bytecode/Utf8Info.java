package javassist.bytecode;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

class Utf8Info extends ConstInfo
{
    static final int tag = 1;
    String string;
    
    public Utf8Info(final String a1, final int a2) {
        super(a2);
        this.string = a1;
    }
    
    public Utf8Info(final DataInputStream a1, final int a2) throws IOException {
        super(a2);
        this.string = a1.readUTF();
    }
    
    @Override
    public int hashCode() {
        /*SL:1811*/return this.string.hashCode();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1815*/return a1 instanceof Utf8Info && ((Utf8Info)a1).string.equals(this.string);
    }
    
    @Override
    public int getTag() {
        /*SL:1818*/return 1;
    }
    
    @Override
    public int copy(final ConstPool a1, final ConstPool a2, final Map a3) {
        /*SL:1821*/return a2.addUtf8Info(this.string);
    }
    
    @Override
    public void write(final DataOutputStream a1) throws IOException {
        /*SL:1825*/a1.writeByte(1);
        /*SL:1826*/a1.writeUTF(this.string);
    }
    
    @Override
    public void print(final PrintWriter a1) {
        /*SL:1830*/a1.print("UTF8 \"");
        /*SL:1831*/a1.print(this.string);
        /*SL:1832*/a1.println("\"");
    }
}
