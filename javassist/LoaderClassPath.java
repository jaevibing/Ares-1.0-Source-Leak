package javassist;

import java.net.URL;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class LoaderClassPath implements ClassPath
{
    private WeakReference clref;
    
    public LoaderClassPath(final ClassLoader a1) {
        this.clref = new WeakReference((T)a1);
    }
    
    @Override
    public String toString() {
        Object v1 = /*EL:53*/null;
        /*SL:54*/if (this.clref != null) {
            /*SL:55*/v1 = this.clref.get();
        }
        /*SL:57*/return (v1 == null) ? "<null>" : v1.toString();
    }
    
    @Override
    public InputStream openClassfile(final String a1) {
        final String v1 = /*EL:66*/a1.replace('.', '/') + ".class";
        final ClassLoader v2 = /*EL:67*/(ClassLoader)this.clref.get();
        /*SL:68*/if (v2 == null) {
            /*SL:69*/return null;
        }
        /*SL:71*/return v2.getResourceAsStream(v1);
    }
    
    @Override
    public URL find(final String a1) {
        final String v1 = /*EL:82*/a1.replace('.', '/') + ".class";
        final ClassLoader v2 = /*EL:83*/(ClassLoader)this.clref.get();
        /*SL:84*/if (v2 == null) {
            /*SL:85*/return null;
        }
        /*SL:87*/return v2.getResource(v1);
    }
    
    @Override
    public void close() {
        /*SL:94*/this.clref = null;
    }
}
