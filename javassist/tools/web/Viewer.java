package javassist.tools.web;

import java.io.InputStream;
import java.net.URLConnection;
import java.io.IOException;
import java.net.URL;
import java.lang.reflect.InvocationTargetException;

public class Viewer extends ClassLoader
{
    private String server;
    private int port;
    
    public static void main(final String[] v-1) throws Throwable {
        /*SL:59*/if (v-1.length >= 3) {
            final Viewer a1 = /*EL:60*/new Viewer(v-1[0], Integer.parseInt(v-1[1]));
            final String[] v1 = /*EL:61*/new String[v-1.length - 3];
            /*SL:62*/System.arraycopy(v-1, 3, v1, 0, v-1.length - 3);
            /*SL:63*/a1.run(v-1[2], v1);
        }
        else {
            System.err.println(/*EL:66*/"Usage: java javassist.tools.web.Viewer <host> <port> class [args ...]");
        }
    }
    
    public Viewer(final String a1, final int a2) {
        this.server = a1;
        this.port = a2;
    }
    
    public String getServer() {
        /*SL:84*/return this.server;
    }
    
    public int getPort() {
        /*SL:89*/return this.port;
    }
    
    public void run(final String v1, final String[] v2) throws Throwable {
        final Class v3 = /*EL:100*/this.loadClass(v1);
        try {
            /*SL:102*/v3.getDeclaredMethod("main", String[].class).invoke(null, v2);
        }
        catch (InvocationTargetException a1) {
            /*SL:106*/throw a1.getTargetException();
        }
    }
    
    @Override
    protected synchronized Class loadClass(final String a1, final boolean a2) throws ClassNotFoundException {
        Class v1 = /*EL:116*/this.findLoadedClass(a1);
        /*SL:117*/if (v1 == null) {
            /*SL:118*/v1 = this.findClass(a1);
        }
        /*SL:120*/if (v1 == null) {
            /*SL:121*/throw new ClassNotFoundException(a1);
        }
        /*SL:123*/if (a2) {
            /*SL:124*/this.resolveClass(v1);
        }
        /*SL:126*/return v1;
    }
    
    @Override
    protected Class findClass(final String v2) throws ClassNotFoundException {
        Class v3 = /*EL:140*/null;
        /*SL:141*/if (v2.startsWith("java.") || v2.startsWith("javax.") || v2.equals("javassist.tools.web.Viewer")) {
            /*SL:143*/v3 = this.findSystemClass(v2);
        }
        /*SL:145*/if (v3 == null) {
            try {
                final byte[] a1 = /*EL:147*/this.fetchClass(v2);
                /*SL:148*/if (a1 != null) {
                    /*SL:149*/v3 = this.defineClass(v2, a1, 0, a1.length);
                }
            }
            catch (Exception ex) {}
        }
        /*SL:154*/return v3;
    }
    
    protected byte[] fetchClass(final String v-6) throws Exception {
        final URL url = /*EL:164*/new URL("http", this.server, this.port, "/" + v-6.replace('.', '/') + /*EL:165*/".class");
        final URLConnection openConnection = /*EL:166*/url.openConnection();
        /*SL:167*/openConnection.connect();
        final int contentLength = /*EL:168*/openConnection.getContentLength();
        final InputStream inputStream = /*EL:169*/openConnection.getInputStream();
        final byte[] array;
        /*SL:170*/if (contentLength <= 0) {
            final byte[] a1 = /*EL:171*/this.readStream(inputStream);
        }
        else {
            /*SL:173*/array = new byte[contentLength];
            int v0 = /*EL:174*/0;
            /*SL:183*/do {
                final int v = inputStream.read(array, v0, contentLength - v0);
                if (v < 0) {
                    inputStream.close();
                    throw new IOException("the stream was closed: " + v-6);
                }
                v0 += v;
            } while (v0 < contentLength);
        }
        /*SL:186*/inputStream.close();
        /*SL:187*/return array;
    }
    
    private byte[] readStream(final InputStream v2) throws IOException {
        byte[] v3 = /*EL:191*/new byte[4096];
        int v4 = /*EL:192*/0;
        int v5 = /*EL:193*/0;
        /*SL:203*/do {
            v4 += v5;
            if (v3.length - v4 <= 0) {
                final byte[] a1 = new byte[v3.length * 2];
                System.arraycopy(v3, 0, a1, 0, v4);
                v3 = a1;
            }
            v5 = v2.read(v3, v4, v3.length - v4);
        } while (v5 >= 0);
        final byte[] v6 = /*EL:205*/new byte[v4];
        /*SL:206*/System.arraycopy(v3, 0, v6, 0, v4);
        /*SL:207*/return v6;
    }
}
