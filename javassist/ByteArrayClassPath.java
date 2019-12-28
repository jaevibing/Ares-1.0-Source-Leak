package javassist;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayClassPath implements ClassPath
{
    protected String classname;
    protected byte[] classfile;
    
    public ByteArrayClassPath(final String a1, final byte[] a2) {
        this.classname = a1;
        this.classfile = a2;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String toString() {
        /*SL:71*/return "byte[]:" + this.classname;
    }
    
    @Override
    public InputStream openClassfile(final String a1) {
        /*SL:78*/if (this.classname.equals(a1)) {
            /*SL:79*/return new ByteArrayInputStream(this.classfile);
        }
        /*SL:81*/return null;
    }
    
    @Override
    public URL find(final String v2) {
        /*SL:88*/if (this.classname.equals(v2)) {
            final String a1 = /*EL:89*/v2.replace('.', '/') + ".class";
            try {
                /*SL:92*/return new URL("file:/ByteArrayClassPath/" + a1);
            }
            catch (MalformedURLException ex) {}
        }
        /*SL:97*/return null;
    }
}
