package javassist.util.proxy;

import java.io.ObjectStreamClass;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

public class ProxyObjectOutputStream extends ObjectOutputStream
{
    public ProxyObjectOutputStream(final OutputStream a1) throws IOException {
        super(a1);
    }
    
    @Override
    protected void writeClassDescriptor(final ObjectStreamClass v-5) throws IOException {
        final Class forClass = /*EL:48*/v-5.forClass();
        /*SL:49*/if (ProxyFactory.isProxyClass(forClass)) {
            /*SL:50*/this.writeBoolean(true);
            final Class superclass = /*EL:51*/forClass.getSuperclass();
            final Class[] interfaces = /*EL:52*/forClass.getInterfaces();
            final byte[] filterSignature = /*EL:53*/ProxyFactory.getFilterSignature(forClass);
            String v0 = /*EL:54*/superclass.getName();
            /*SL:55*/this.writeObject(v0);
            /*SL:57*/this.writeInt(interfaces.length - 1);
            /*SL:58*/for (int v = 0; v < interfaces.length; ++v) {
                final Class a1 = /*EL:59*/interfaces[v];
                /*SL:60*/if (a1 != ProxyObject.class && a1 != Proxy.class) {
                    /*SL:61*/v0 = interfaces[v].getName();
                    /*SL:62*/this.writeObject(v0);
                }
            }
            /*SL:65*/this.writeInt(filterSignature.length);
            /*SL:66*/this.write(filterSignature);
        }
        else {
            /*SL:68*/this.writeBoolean(false);
            /*SL:69*/super.writeClassDescriptor(v-5);
        }
    }
}
