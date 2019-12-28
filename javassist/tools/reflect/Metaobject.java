package javassist.tools.reflect;

import java.lang.reflect.InvocationTargetException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.io.Serializable;

public class Metaobject implements Serializable
{
    protected ClassMetaobject classmetaobject;
    protected Metalevel baseobject;
    protected Method[] methods;
    
    public Metaobject(final Object a1, final Object[] a2) {
        this.baseobject = (Metalevel)a1;
        this.classmetaobject = this.baseobject._getClass();
        this.methods = this.classmetaobject.getReflectiveMethods();
    }
    
    protected Metaobject() {
        this.baseobject = null;
        this.classmetaobject = null;
        this.methods = null;
    }
    
    private void writeObject(final ObjectOutputStream a1) throws IOException {
        /*SL:79*/a1.writeObject(this.baseobject);
    }
    
    private void readObject(final ObjectInputStream a1) throws IOException, ClassNotFoundException {
        /*SL:85*/this.baseobject = (Metalevel)a1.readObject();
        /*SL:86*/this.classmetaobject = this.baseobject._getClass();
        /*SL:87*/this.methods = this.classmetaobject.getReflectiveMethods();
    }
    
    public final ClassMetaobject getClassMetaobject() {
        /*SL:96*/return this.classmetaobject;
    }
    
    public final Object getObject() {
        /*SL:103*/return this.baseobject;
    }
    
    public final void setObject(final Object a1) {
        /*SL:112*/this.baseobject = (Metalevel)a1;
        /*SL:113*/this.classmetaobject = this.baseobject._getClass();
        /*SL:114*/this.methods = this.classmetaobject.getReflectiveMethods();
        /*SL:117*/this.baseobject._setMetaobject(this);
    }
    
    public final String getMethodName(final int v2) {
        final String v3 = /*EL:125*/this.methods[v2].getName();
        int v4 = /*EL:126*/3;
        char a1;
        do {
            /*SL:128*/a1 = v3.charAt(v4++);
        } while (/*EL:129*/a1 >= '0' && '9' >= a1);
        /*SL:133*/return v3.substring(v4);
    }
    
    public final Class[] getParameterTypes(final int a1) {
        /*SL:142*/return this.methods[a1].getParameterTypes();
    }
    
    public final Class getReturnType(final int a1) {
        /*SL:150*/return this.methods[a1].getReturnType();
    }
    
    public Object trapFieldRead(final String v-1) {
        final Class v0 = /*EL:161*/this.getClassMetaobject().getJavaClass();
        try {
            /*SL:163*/return v0.getField(v-1).get(this.getObject());
        }
        catch (NoSuchFieldException a1) {
            /*SL:166*/throw new RuntimeException(a1.toString());
        }
        catch (IllegalAccessException v) {
            /*SL:169*/throw new RuntimeException(v.toString());
        }
    }
    
    public void trapFieldWrite(final String v2, final Object v3) {
        final Class v4 = /*EL:181*/this.getClassMetaobject().getJavaClass();
        try {
            /*SL:183*/v4.getField(v2).set(this.getObject(), v3);
        }
        catch (NoSuchFieldException a1) {
            /*SL:186*/throw new RuntimeException(a1.toString());
        }
        catch (IllegalAccessException a2) {
            /*SL:189*/throw new RuntimeException(a2.toString());
        }
    }
    
    public Object trapMethodcall(final int v2, final Object[] v3) throws Throwable {
        try {
            /*SL:230*/return this.methods[v2].invoke(this.getObject(), v3);
        }
        catch (InvocationTargetException a1) {
            /*SL:233*/throw a1.getTargetException();
        }
        catch (IllegalAccessException a2) {
            /*SL:236*/throw new CannotInvokeException(a2);
        }
    }
}
