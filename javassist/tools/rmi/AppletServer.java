package javassist.tools.rmi;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import javassist.tools.web.BadHttpRequest;
import java.io.OutputStream;
import java.io.InputStream;
import javassist.Translator;
import javassist.ClassPool;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.Hashtable;
import javassist.tools.web.Webserver;

public class AppletServer extends Webserver
{
    private StubGenerator stubGen;
    private Hashtable exportedNames;
    private Vector exportedObjects;
    private static final byte[] okHeader;
    
    public AppletServer(final String a1) throws IOException, NotFoundException, CannotCompileException {
        this(Integer.parseInt(a1));
    }
    
    public AppletServer(final int a1) throws IOException, NotFoundException, CannotCompileException {
        this(ClassPool.getDefault(), new StubGenerator(), a1);
    }
    
    public AppletServer(final int a1, final ClassPool a2) throws IOException, NotFoundException, CannotCompileException {
        this(new ClassPool(a2), new StubGenerator(), a1);
    }
    
    private AppletServer(final ClassPool a1, final StubGenerator a2, final int a3) throws IOException, NotFoundException, CannotCompileException {
        super(a3);
        this.exportedNames = new Hashtable();
        this.exportedObjects = new Vector();
        this.addTranslator(a1, this.stubGen = a2);
    }
    
    @Override
    public void run() {
        /*SL:94*/super.run();
    }
    
    public synchronized int exportObject(final String v1, final Object v2) throws CannotCompileException {
        final Class v3 = /*EL:112*/v2.getClass();
        final ExportedObject v4 = /*EL:113*/new ExportedObject();
        /*SL:114*/v4.object = v2;
        /*SL:115*/v4.methods = v3.getMethods();
        /*SL:116*/this.exportedObjects.addElement(v4);
        /*SL:117*/v4.identifier = this.exportedObjects.size() - 1;
        /*SL:118*/if (v1 != null) {
            /*SL:119*/this.exportedNames.put(v1, v4);
        }
        try {
            /*SL:122*/this.stubGen.makeProxyClass(v3);
        }
        catch (NotFoundException a1) {
            /*SL:125*/throw new CannotCompileException(a1);
        }
        /*SL:128*/return v4.identifier;
    }
    
    @Override
    public void doReply(final InputStream a1, final OutputStream a2, final String a3) throws IOException, BadHttpRequest {
        /*SL:137*/if (a3.startsWith("POST /rmi ")) {
            /*SL:138*/this.processRMI(a1, a2);
        }
        else/*SL:139*/ if (a3.startsWith("POST /lookup ")) {
            /*SL:140*/this.lookupName(a3, a1, a2);
        }
        else {
            /*SL:142*/super.doReply(a1, a2, a3);
        }
    }
    
    private void processRMI(final InputStream v-6, final OutputStream v-5) throws IOException {
        final ObjectInputStream v-7 = /*EL:148*/new ObjectInputStream(v-6);
        final int int1 = /*EL:150*/v-7.readInt();
        final int int2 = /*EL:151*/v-7.readInt();
        Exception ex = /*EL:152*/null;
        Object v0 = /*EL:153*/null;
        try {
            final ExportedObject a1 = /*EL:155*/this.exportedObjects.elementAt(int1);
            final Object[] a2 = /*EL:157*/this.readParameters(v-7);
            /*SL:158*/v0 = this.convertRvalue(a1.methods[int2].invoke(a1.object, a2));
        }
        catch (Exception v) {
            /*SL:162*/ex = v;
            /*SL:163*/this.logging2(v.toString());
        }
        /*SL:166*/v-5.write(AppletServer.okHeader);
        final ObjectOutputStream v2 = /*EL:167*/new ObjectOutputStream(v-5);
        /*SL:168*/if (ex != null) {
            /*SL:169*/v2.writeBoolean(false);
            /*SL:170*/v2.writeUTF(ex.toString());
        }
        else {
            try {
                /*SL:174*/v2.writeBoolean(true);
                /*SL:175*/v2.writeObject(v0);
            }
            catch (NotSerializableException v3) {
                /*SL:178*/this.logging2(v3.toString());
            }
            catch (InvalidClassException v4) {
                /*SL:181*/this.logging2(v4.toString());
            }
        }
        /*SL:184*/v2.flush();
        /*SL:185*/v2.close();
        /*SL:186*/v-7.close();
    }
    
    private Object[] readParameters(final ObjectInputStream v-5) throws IOException, ClassNotFoundException {
        final int int1 = /*EL:192*/v-5.readInt();
        final Object[] array = /*EL:193*/new Object[int1];
        /*SL:194*/for (int i = 0; i < int1; ++i) {
            Object o = /*EL:195*/v-5.readObject();
            /*SL:196*/if (o instanceof RemoteRef) {
                final RemoteRef a1 = /*EL:197*/(RemoteRef)o;
                final ExportedObject v1 = /*EL:198*/this.exportedObjects.elementAt(a1.oid);
                /*SL:200*/o = v1.object;
            }
            /*SL:203*/array[i] = o;
        }
        /*SL:206*/return array;
    }
    
    private Object convertRvalue(final Object a1) throws CannotCompileException {
        /*SL:212*/if (a1 == null) {
            /*SL:213*/return null;
        }
        final String v1 = /*EL:215*/a1.getClass().getName();
        /*SL:216*/if (this.stubGen.isProxyClass(v1)) {
            /*SL:217*/return new RemoteRef(this.exportObject(null, a1), v1);
        }
        /*SL:219*/return a1;
    }
    
    private void lookupName(final String a1, final InputStream a2, final OutputStream a3) throws IOException {
        final ObjectInputStream v1 = /*EL:225*/new ObjectInputStream(a2);
        final String v2 = /*EL:226*/DataInputStream.readUTF(v1);
        final ExportedObject v3 = /*EL:227*/this.exportedNames.get(v2);
        /*SL:228*/a3.write(AppletServer.okHeader);
        final ObjectOutputStream v4 = /*EL:229*/new ObjectOutputStream(a3);
        /*SL:230*/if (v3 == null) {
            /*SL:231*/this.logging2(v2 + "not found.");
            /*SL:232*/v4.writeInt(-1);
            /*SL:233*/v4.writeUTF("error");
        }
        else {
            /*SL:236*/this.logging2(v2);
            /*SL:237*/v4.writeInt(v3.identifier);
            /*SL:238*/v4.writeUTF(v3.object.getClass().getName());
        }
        /*SL:241*/v4.flush();
        /*SL:242*/v4.close();
        /*SL:243*/v1.close();
    }
    
    static {
        okHeader = "HTTP/1.0 200 OK\r\n\r\n".getBytes();
    }
}
