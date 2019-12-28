package javassist.util.proxy;

import java.io.ObjectStreamException;
import java.io.InvalidObjectException;
import java.io.InvalidClassException;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.io.Serializable;

class SerializedProxy implements Serializable
{
    private String superClass;
    private String[] interfaces;
    private byte[] filterSignature;
    private MethodHandler handler;
    
    SerializedProxy(final Class v1, final byte[] v2, final MethodHandler v3) {
        this.filterSignature = v2;
        this.handler = v3;
        this.superClass = v1.getSuperclass().getName();
        final Class[] v4 = v1.getInterfaces();
        final int v5 = v4.length;
        this.interfaces = new String[v5 - 1];
        final String v6 = ProxyObject.class.getName();
        final String v7 = Proxy.class.getName();
        for (String a2 = (String)0; a2 < v5; ++a2) {
            a2 = v4[a2].getName();
            if (!a2.equals(v6) && !a2.equals(v7)) {
                this.interfaces[a2] = a2;
            }
        }
    }
    
    protected Class loadClass(final String v2) throws ClassNotFoundException {
        try {
            /*SL:63*/return AccessController.<Class>doPrivileged((PrivilegedExceptionAction<Class>)new PrivilegedExceptionAction() {
                @Override
                public Object run() throws Exception {
                    final ClassLoader v1 = /*EL:65*/Thread.currentThread().getContextClassLoader();
                    /*SL:66*/return Class.forName(v2, true, v1);
                }
            });
        }
        catch (PrivilegedActionException a1) {
            /*SL:71*/throw new RuntimeException("cannot load the class: " + v2, a1.getException());
        }
    }
    
    Object readResolve() throws ObjectStreamException {
        try {
            final int length = /*EL:77*/this.interfaces.length;
            final Class[] v0 = /*EL:78*/new Class[length];
            /*SL:79*/for (int v = 0; v < length; ++v) {
                /*SL:80*/v0[v] = this.loadClass(this.interfaces[v]);
            }
            final ProxyFactory v2 = /*EL:82*/new ProxyFactory();
            /*SL:83*/v2.setSuperclass(this.loadClass(this.superClass));
            /*SL:84*/v2.setInterfaces(v0);
            final Proxy v3 = /*EL:85*/v2.createClass(this.filterSignature).newInstance();
            /*SL:86*/v3.setHandler(this.handler);
            /*SL:87*/return v3;
        }
        catch (ClassNotFoundException ex) {
            /*SL:90*/throw new InvalidClassException(ex.getMessage());
        }
        catch (InstantiationException ex2) {
            /*SL:93*/throw new InvalidObjectException(ex2.getMessage());
        }
        catch (IllegalAccessException ex3) {
            /*SL:96*/throw new InvalidClassException(ex3.getMessage());
        }
    }
}
