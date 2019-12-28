package javassist.tools.rmi;

import java.io.IOException;
import java.io.BufferedOutputStream;
import java.lang.reflect.Constructor;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.applet.Applet;
import java.io.Serializable;

public class ObjectImporter implements Serializable
{
    private final byte[] endofline;
    private String servername;
    private String orgServername;
    private int port;
    private int orgPort;
    protected byte[] lookupCommand;
    protected byte[] rmiCommand;
    private static final Class[] proxyConstructorParamTypes;
    
    public ObjectImporter(final Applet a1) {
        this.endofline = new byte[] { 13, 10 };
        this.lookupCommand = "POST /lookup HTTP/1.0".getBytes();
        this.rmiCommand = "POST /rmi HTTP/1.0".getBytes();
        final URL v1 = a1.getCodeBase();
        final String host = v1.getHost();
        this.servername = host;
        this.orgServername = host;
        final int port = v1.getPort();
        this.port = port;
        this.orgPort = port;
    }
    
    public ObjectImporter(final String a1, final int a2) {
        this.endofline = new byte[] { 13, 10 };
        this.lookupCommand = "POST /lookup HTTP/1.0".getBytes();
        this.rmiCommand = "POST /rmi HTTP/1.0".getBytes();
        this.servername = a1;
        this.orgServername = a1;
        this.port = a2;
        this.orgPort = a2;
    }
    
    public Object getObject(final String v2) {
        try {
            /*SL:124*/return this.lookupObject(v2);
        }
        catch (ObjectNotFoundException a1) {
            /*SL:127*/return null;
        }
    }
    
    public void setHttpProxy(final String a1, final int a2) {
        final String v1 = /*EL:136*/"POST http://" + this.orgServername + ":" + this.orgPort;
        String v2 = /*EL:137*/v1 + "/lookup HTTP/1.0";
        /*SL:138*/this.lookupCommand = v2.getBytes();
        /*SL:139*/v2 = v1 + "/rmi HTTP/1.0";
        /*SL:140*/this.rmiCommand = v2.getBytes();
        /*SL:141*/this.servername = a1;
        /*SL:142*/this.port = a2;
    }
    
    public Object lookupObject(final String v-1) throws ObjectNotFoundException {
        try {
            final Socket a1 = /*EL:156*/new Socket(this.servername, this.port);
            final OutputStream v1 = /*EL:157*/a1.getOutputStream();
            /*SL:158*/v1.write(this.lookupCommand);
            /*SL:159*/v1.write(this.endofline);
            /*SL:160*/v1.write(this.endofline);
            final ObjectOutputStream v2 = /*EL:162*/new ObjectOutputStream(v1);
            /*SL:163*/v2.writeUTF(v-1);
            /*SL:164*/v2.flush();
            final InputStream v3 = /*EL:166*/new BufferedInputStream(a1.getInputStream());
            /*SL:167*/this.skipHeader(v3);
            final ObjectInputStream v4 = /*EL:168*/new ObjectInputStream(v3);
            final int v5 = /*EL:169*/v4.readInt();
            final String v6 = /*EL:170*/v4.readUTF();
            /*SL:171*/v4.close();
            /*SL:172*/v2.close();
            /*SL:173*/a1.close();
            /*SL:175*/if (v5 >= 0) {
                /*SL:176*/return this.createProxy(v5, v6);
            }
        }
        catch (Exception v7) {
            /*SL:179*/v7.printStackTrace();
            /*SL:180*/throw new ObjectNotFoundException(v-1, v7);
        }
        /*SL:183*/throw new ObjectNotFoundException(v-1);
    }
    
    private Object createProxy(final int a1, final String a2) throws Exception {
        final Class v1 = /*EL:190*/Class.forName(a2);
        final Constructor v2 = /*EL:191*/v1.getConstructor((Class[])ObjectImporter.proxyConstructorParamTypes);
        /*SL:192*/return v2.newInstance(this, new Integer(a1));
    }
    
    public Object call(final int v-7, final int v-6, final Object[] v-5) throws RemoteException {
        boolean boolean1;
        Object o;
        String utf;
        try {
            Socket a2 = /*EL:225*/new Socket(this.servername, this.port);
            /*SL:227*/a2 = new BufferedOutputStream(a2.getOutputStream());
            /*SL:228*/a2.write(this.rmiCommand);
            /*SL:229*/a2.write(this.endofline);
            /*SL:230*/a2.write(this.endofline);
            final ObjectOutputStream v1 = /*EL:232*/new ObjectOutputStream(a2);
            /*SL:233*/v1.writeInt(v-7);
            /*SL:234*/v1.writeInt(v-6);
            /*SL:235*/this.writeParameters(v1, v-5);
            /*SL:236*/v1.flush();
            final InputStream v2 = /*EL:238*/new BufferedInputStream(a2.getInputStream());
            /*SL:239*/this.skipHeader(v2);
            final ObjectInputStream v3 = /*EL:240*/new ObjectInputStream(v2);
            /*SL:241*/boolean1 = v3.readBoolean();
            /*SL:242*/o = null;
            /*SL:243*/utf = null;
            /*SL:244*/if (boolean1) {
                /*SL:245*/o = v3.readObject();
            }
            else {
                /*SL:247*/utf = v3.readUTF();
            }
            /*SL:249*/v3.close();
            /*SL:250*/v1.close();
            /*SL:251*/a2.close();
            /*SL:253*/if (o instanceof RemoteRef) {
                final RemoteRef a3 = /*EL:254*/(RemoteRef)o;
                /*SL:255*/o = this.createProxy(a3.oid, a3.classname);
            }
        }
        catch (ClassNotFoundException a4) {
            /*SL:259*/throw new RemoteException(a4);
        }
        catch (IOException a5) {
            /*SL:262*/throw new RemoteException(a5);
        }
        catch (Exception a6) {
            /*SL:265*/throw new RemoteException(a6);
        }
        /*SL:268*/if (boolean1) {
            /*SL:269*/return o;
        }
        /*SL:271*/throw new RemoteException(utf);
    }
    
    private void skipHeader(final InputStream v2) throws IOException {
        int v3;
        /*SL:283*/do {
            v3 = 0;
            int a1;
            while ((a1 = v2.read()) >= 0 && a1 != 13) {
                ++v3;
            }
            v2.read();
        } while (v3 > 0);
    }
    
    private void writeParameters(final ObjectOutputStream v2, final Object[] v3) throws IOException {
        final int v4 = /*EL:289*/v3.length;
        /*SL:290*/v2.writeInt(v4);
        /*SL:291*/for (Proxy a2 = (Proxy)0; a2 < v4; ++a2) {
            /*SL:292*/if (v3[a2] instanceof Proxy) {
                /*SL:293*/a2 = (Proxy)v3[a2];
                /*SL:294*/v2.writeObject(new RemoteRef(a2._getObjectId()));
            }
            else {
                /*SL:297*/v2.writeObject(v3[a2]);
            }
        }
    }
    
    static {
        proxyConstructorParamTypes = new Class[] { ObjectImporter.class, Integer.TYPE };
    }
}
