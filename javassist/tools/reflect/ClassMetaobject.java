package javassist.tools.reflect;

import java.util.Arrays;
import java.lang.reflect.InvocationTargetException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.io.Serializable;

public class ClassMetaobject implements Serializable
{
    static final String methodPrefix = "_m_";
    static final int methodPrefixLen = 3;
    private Class javaClass;
    private Constructor[] constructors;
    private Method[] methods;
    public static boolean useContextClassLoader;
    
    public ClassMetaobject(final String[] v2) {
        try {
            this.javaClass = this.getClassObject(v2[0]);
        }
        catch (ClassNotFoundException a1) {
            throw new RuntimeException("not found: " + v2[0] + ", useContextClassLoader: " + Boolean.toString(ClassMetaobject.useContextClassLoader), a1);
        }
        this.constructors = this.javaClass.getConstructors();
        this.methods = null;
    }
    
    private void writeObject(final ObjectOutputStream a1) throws IOException {
        /*SL:87*/a1.writeUTF(this.javaClass.getName());
    }
    
    private void readObject(final ObjectInputStream a1) throws IOException, ClassNotFoundException {
        /*SL:93*/this.javaClass = this.getClassObject(a1.readUTF());
        /*SL:94*/this.constructors = this.javaClass.getConstructors();
        /*SL:95*/this.methods = null;
    }
    
    private Class getClassObject(final String a1) throws ClassNotFoundException {
        /*SL:99*/if (ClassMetaobject.useContextClassLoader) {
            /*SL:101*/return Thread.currentThread().getContextClassLoader().loadClass(a1);
        }
        /*SL:103*/return Class.forName(a1);
    }
    
    public final Class getJavaClass() {
        /*SL:110*/return this.javaClass;
    }
    
    public final String getName() {
        /*SL:117*/return this.javaClass.getName();
    }
    
    public final boolean isInstance(final Object a1) {
        /*SL:124*/return this.javaClass.isInstance(a1);
    }
    
    public final Object newInstance(final Object[] v-2) throws CannotCreateException {
        /*SL:136*/for (int length = this.constructors.length, v0 = 0; v0 < length; ++v0) {
            try {
                /*SL:138*/return this.constructors[v0].newInstance(v-2);
            }
            catch (IllegalArgumentException ex) {}
            catch (InstantiationException a1) {
                /*SL:144*/throw new CannotCreateException(a1);
            }
            catch (IllegalAccessException v) {
                /*SL:147*/throw new CannotCreateException(v);
            }
            catch (InvocationTargetException v2) {
                /*SL:150*/throw new CannotCreateException(v2);
            }
        }
        /*SL:154*/throw new CannotCreateException("no constructor matches");
    }
    
    public Object trapFieldRead(final String v-1) {
        final Class v0 = /*EL:165*/this.getJavaClass();
        try {
            /*SL:167*/return v0.getField(v-1).get(null);
        }
        catch (NoSuchFieldException a1) {
            /*SL:170*/throw new RuntimeException(a1.toString());
        }
        catch (IllegalAccessException v) {
            /*SL:173*/throw new RuntimeException(v.toString());
        }
    }
    
    public void trapFieldWrite(final String v2, final Object v3) {
        final Class v4 = /*EL:185*/this.getJavaClass();
        try {
            /*SL:187*/v4.getField(v2).set(null, v3);
        }
        catch (NoSuchFieldException a1) {
            /*SL:190*/throw new RuntimeException(a1.toString());
        }
        catch (IllegalAccessException a2) {
            /*SL:193*/throw new RuntimeException(a2.toString());
        }
    }
    
    public static Object invoke(final Object v1, final int v2, final Object[] v3) throws Throwable {
        final Method[] v4 = /*EL:206*/v1.getClass().getMethods();
        final int v5 = /*EL:207*/v4.length;
        final String v6 = /*EL:208*/"_m_" + v2;
        /*SL:209*/for (IllegalAccessException a3 = (IllegalAccessException)0; a3 < v5; ++a3) {
            /*SL:210*/if (v4[a3].getName().startsWith(v6)) {
                try {
                    /*SL:212*/return v4[a3].invoke(v1, v3);
                }
                catch (InvocationTargetException a2) {
                    /*SL:214*/throw a2.getTargetException();
                }
                catch (IllegalAccessException a3) {
                    /*SL:216*/throw new CannotInvokeException(a3);
                }
            }
        }
        /*SL:220*/throw new CannotInvokeException("cannot find a method");
    }
    
    public Object trapMethodcall(final int v-1, final Object[] v0) throws Throwable {
        try {
            final Method[] a1 = /*EL:235*/this.getReflectiveMethods();
            /*SL:236*/return a1[v-1].invoke(null, v0);
        }
        catch (InvocationTargetException a2) {
            /*SL:239*/throw a2.getTargetException();
        }
        catch (IllegalAccessException v) {
            /*SL:242*/throw new CannotInvokeException(v);
        }
    }
    
    public final Method[] getReflectiveMethods() {
        /*SL:251*/if (this.methods != null) {
            /*SL:252*/return this.methods;
        }
        final Class javaClass = /*EL:254*/this.getJavaClass();
        final Method[] declaredMethods = /*EL:255*/javaClass.getDeclaredMethods();
        final int length = /*EL:256*/declaredMethods.length;
        final int[] array = /*EL:257*/new int[length];
        int n = /*EL:258*/0;
        /*SL:259*/for (int i = 0; i < length; ++i) {
            final Method method = /*EL:260*/declaredMethods[i];
            final String name = /*EL:261*/method.getName();
            /*SL:262*/if (name.startsWith("_m_")) {
                int n2 = /*EL:263*/0;
                int v0 = /*EL:264*/3;
                while (true) {
                    final char v = /*EL:265*/name.charAt(v0);
                    /*SL:266*/if ('0' > v || v > '9') {
                        break;
                    }
                    /*SL:267*/n2 = n2 * 10 + v - 48;
                    ++v0;
                }
                /*SL:272*/array[i] = ++n2;
                /*SL:273*/if (n2 > n) {
                    /*SL:274*/n = n2;
                }
            }
        }
        /*SL:278*/this.methods = new Method[n];
        /*SL:279*/for (int i = 0; i < length; ++i) {
            /*SL:280*/if (array[i] > 0) {
                /*SL:281*/this.methods[array[i] - 1] = declaredMethods[i];
            }
        }
        /*SL:283*/return this.methods;
    }
    
    public final Method getMethod(final int a1) {
        /*SL:299*/return this.getReflectiveMethods()[a1];
    }
    
    public final String getMethodName(final int v2) {
        final String v3 = /*EL:307*/this.getReflectiveMethods()[v2].getName();
        int v4 = /*EL:308*/3;
        char a1;
        do {
            /*SL:310*/a1 = v3.charAt(v4++);
        } while (/*EL:311*/a1 >= '0' && '9' >= a1);
        /*SL:315*/return v3.substring(v4);
    }
    
    public final Class[] getParameterTypes(final int a1) {
        /*SL:324*/return this.getReflectiveMethods()[a1].getParameterTypes();
    }
    
    public final Class getReturnType(final int a1) {
        /*SL:332*/return this.getReflectiveMethods()[a1].getReturnType();
    }
    
    public final int getMethodIndex(final String v1, final Class[] v2) throws NoSuchMethodException {
        final Method[] v3 = /*EL:356*/this.getReflectiveMethods();
        /*SL:357*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:358*/if (v3[a1] != null) {
                /*SL:362*/if (this.getMethodName(a1).equals(v1) && /*EL:363*/Arrays.equals(v2, v3[a1].getParameterTypes())) {
                    /*SL:364*/return a1;
                }
            }
        }
        /*SL:367*/throw new NoSuchMethodException("Method " + v1 + " not found");
    }
    
    static {
        ClassMetaobject.useContextClassLoader = false;
    }
}
