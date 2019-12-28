package javassist.util.proxy;

import java.io.ObjectStreamClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ProxyObjectInputStream extends ObjectInputStream
{
    private ClassLoader loader;
    
    public ProxyObjectInputStream(final InputStream a1) throws IOException {
        super(a1);
        this.loader = Thread.currentThread().getContextClassLoader();
        if (this.loader == null) {
            this.loader = ClassLoader.getSystemClassLoader();
        }
    }
    
    public void setClassLoader(ClassLoader a1) {
        /*SL:59*/if (a1 != null) {
            /*SL:60*/this.loader = a1;
        }
        else {
            /*SL:62*/a1 = ClassLoader.getSystemClassLoader();
        }
    }
    
    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        final boolean boolean1 = /*EL:67*/this.readBoolean();
        /*SL:68*/if (boolean1) {
            String s = /*EL:69*/(String)this.readObject();
            final Class loadClass = /*EL:70*/this.loader.loadClass(s);
            int n = /*EL:71*/this.readInt();
            final Class[] v0 = /*EL:72*/new Class[n];
            /*SL:73*/for (int v = 0; v < n; ++v) {
                /*SL:74*/s = (String)this.readObject();
                /*SL:75*/v0[v] = this.loader.loadClass(s);
            }
            /*SL:77*/n = this.readInt();
            final byte[] v2 = /*EL:78*/new byte[n];
            /*SL:79*/this.read(v2);
            final ProxyFactory v3 = /*EL:80*/new ProxyFactory();
            /*SL:83*/v3.setUseCache(true);
            /*SL:84*/v3.setUseWriteReplace(false);
            /*SL:85*/v3.setSuperclass(loadClass);
            /*SL:86*/v3.setInterfaces(v0);
            final Class v4 = /*EL:87*/v3.createClass(v2);
            /*SL:88*/return ObjectStreamClass.lookup(v4);
        }
        /*SL:90*/return super.readClassDescriptor();
    }
}
