package javassist.bytecode.annotation;

import javassist.bytecode.MethodInfo;
import javassist.bytecode.ClassFile;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationDefaultAttribute;
import java.lang.reflect.Proxy;
import javassist.ClassPool;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

public class AnnotationImpl implements InvocationHandler
{
    private static final String JDK_ANNOTATION_CLASS_NAME = "java.lang.annotation.Annotation";
    private static Method JDK_ANNOTATION_TYPE_METHOD;
    private Annotation annotation;
    private ClassPool pool;
    private ClassLoader classLoader;
    private transient Class annotationType;
    private transient int cachedHashCode;
    
    public static Object make(final ClassLoader a1, final Class a2, final ClassPool a3, final Annotation a4) {
        final AnnotationImpl v1 = /*EL:71*/new AnnotationImpl(a4, a3, a1);
        /*SL:72*/return Proxy.newProxyInstance(a1, new Class[] { a2 }, v1);
    }
    
    private AnnotationImpl(final Annotation a1, final ClassPool a2, final ClassLoader a3) {
        this.cachedHashCode = Integer.MIN_VALUE;
        this.annotation = a1;
        this.pool = a2;
        this.classLoader = a3;
    }
    
    public String getTypeName() {
        /*SL:87*/return this.annotation.getTypeName();
    }
    
    private Class getAnnotationType() {
        /*SL:97*/if (this.annotationType == null) {
            final String typeName = /*EL:98*/this.annotation.getTypeName();
            try {
                /*SL:100*/this.annotationType = this.classLoader.loadClass(typeName);
            }
            catch (ClassNotFoundException v2) {
                final NoClassDefFoundError v1 = /*EL:103*/new NoClassDefFoundError("Error loading annotation class: " + typeName);
                /*SL:104*/v1.setStackTrace(v2.getStackTrace());
                /*SL:105*/throw v1;
            }
        }
        /*SL:108*/return this.annotationType;
    }
    
    public Annotation getAnnotation() {
        /*SL:117*/return this.annotation;
    }
    
    @Override
    public Object invoke(final Object a3, final Method v1, final Object[] v2) throws Throwable {
        final String v3 = /*EL:130*/v1.getName();
        /*SL:131*/if (Object.class == v1.getDeclaringClass()) {
            /*SL:132*/if ("equals".equals(v3)) {
                final Object a4 = /*EL:133*/v2[0];
                /*SL:134*/return new Boolean(this.checkEquals(a4));
            }
            /*SL:136*/if ("toString".equals(v3)) {
                /*SL:137*/return this.annotation.toString();
            }
            /*SL:138*/if ("hashCode".equals(v3)) {
                /*SL:139*/return new Integer(this.hashCode());
            }
        }
        else/*SL:141*/ if ("annotationType".equals(v3) && v1.getParameterTypes().length == /*EL:142*/0) {
            /*SL:143*/return this.getAnnotationType();
        }
        final MemberValue v4 = /*EL:145*/this.annotation.getMemberValue(v3);
        /*SL:146*/if (v4 == null) {
            /*SL:147*/return this.getDefault(v3, v1);
        }
        /*SL:149*/return v4.getValue(this.classLoader, this.pool, v1);
    }
    
    private Object getDefault(final String v-2, final Method v-1) throws ClassNotFoundException, RuntimeException {
        final String v0 = /*EL:155*/this.annotation.getTypeName();
        /*SL:156*/if (this.pool != null) {
            try {
                final CtClass v = /*EL:158*/this.pool.get(v0);
                final ClassFile v2 = /*EL:159*/v.getClassFile2();
                final MethodInfo v3 = /*EL:160*/v2.getMethod(v-2);
                /*SL:161*/if (v3 != null) {
                    AnnotationDefaultAttribute a2 = /*EL:162*/(AnnotationDefaultAttribute)v3.getAttribute("AnnotationDefault");
                    /*SL:165*/if (a2 != null) {
                        /*SL:166*/a2 = a2.getDefaultValue();
                        /*SL:167*/return a2.getValue(this.classLoader, this.pool, v-1);
                    }
                }
            }
            catch (NotFoundException v4) {
                /*SL:172*/throw new RuntimeException("cannot find a class file: " + v0);
            }
        }
        /*SL:177*/throw new RuntimeException("no default value: " + v0 + "." + v-2 + "()");
    }
    
    @Override
    public int hashCode() {
        /*SL:185*/if (this.cachedHashCode == Integer.MIN_VALUE) {
            int cachedHashCode = /*EL:186*/0;
            /*SL:189*/this.getAnnotationType();
            final Method[] declaredMethods = /*EL:191*/this.annotationType.getDeclaredMethods();
            /*SL:192*/for (int i = 0; i < declaredMethods.length; ++i) {
                final String name = /*EL:193*/declaredMethods[i].getName();
                int n = /*EL:194*/0;
                final MemberValue memberValue = /*EL:197*/this.annotation.getMemberValue(name);
                Object v0 = /*EL:198*/null;
                try {
                    /*SL:200*/if (memberValue != null) {
                        /*SL:201*/v0 = memberValue.getValue(this.classLoader, this.pool, declaredMethods[i]);
                    }
                    /*SL:202*/if (v0 == null) {
                        /*SL:203*/v0 = this.getDefault(name, declaredMethods[i]);
                    }
                }
                catch (RuntimeException v) {
                    /*SL:206*/throw v;
                }
                catch (Exception v2) {
                    /*SL:209*/throw new RuntimeException("Error retrieving value " + name + " for annotation " + this.annotation.getTypeName(), v2);
                }
                /*SL:213*/if (v0 != null) {
                    /*SL:214*/if (v0.getClass().isArray()) {
                        /*SL:215*/n = arrayHashCode(v0);
                    }
                    else {
                        /*SL:217*/n = v0.hashCode();
                    }
                }
                /*SL:219*/cachedHashCode += (127 * name.hashCode() ^ n);
            }
            /*SL:222*/this.cachedHashCode = cachedHashCode;
        }
        /*SL:224*/return this.cachedHashCode;
    }
    
    private boolean checkEquals(final Object v0) throws Exception {
        /*SL:235*/if (v0 == null) {
            /*SL:236*/return false;
        }
        /*SL:239*/if (v0 instanceof Proxy) {
            final InvocationHandler v = /*EL:240*/Proxy.getInvocationHandler(v0);
            /*SL:241*/if (v instanceof AnnotationImpl) {
                final AnnotationImpl a1 = /*EL:242*/(AnnotationImpl)v;
                /*SL:243*/return this.annotation.equals(a1.annotation);
            }
        }
        final Class v2 = (Class)AnnotationImpl.JDK_ANNOTATION_TYPE_METHOD.invoke(/*EL:247*/v0, (Object[])null);
        /*SL:248*/if (!this.getAnnotationType().equals(v2)) {
            /*SL:249*/return false;
        }
        final Method[] v3 = /*EL:251*/this.annotationType.getDeclaredMethods();
        /*SL:252*/for (int v4 = 0; v4 < v3.length; ++v4) {
            final String v5 = /*EL:253*/v3[v4].getName();
            final MemberValue v6 = /*EL:256*/this.annotation.getMemberValue(v5);
            Object v7 = /*EL:257*/null;
            Object v8 = /*EL:258*/null;
            try {
                /*SL:260*/if (v6 != null) {
                    /*SL:261*/v7 = v6.getValue(this.classLoader, this.pool, v3[v4]);
                }
                /*SL:262*/if (v7 == null) {
                    /*SL:263*/v7 = this.getDefault(v5, v3[v4]);
                }
                /*SL:264*/v8 = v3[v4].invoke(v0, (Object[])null);
            }
            catch (RuntimeException v9) {
                /*SL:267*/throw v9;
            }
            catch (Exception v10) {
                /*SL:270*/throw new RuntimeException("Error retrieving value " + v5 + " for annotation " + this.annotation.getTypeName(), v10);
            }
            /*SL:273*/if (v7 == null && v8 != null) {
                /*SL:274*/return false;
            }
            /*SL:275*/if (v7 != null && !v7.equals(v8)) {
                /*SL:276*/return false;
            }
        }
        /*SL:279*/return true;
    }
    
    private static int arrayHashCode(final Object v-2) {
        /*SL:291*/if (v-2 == null) {
            /*SL:292*/return 0;
        }
        int n = /*EL:294*/1;
        final Object[] v0 = /*EL:296*/(Object[])v-2;
        /*SL:297*/for (int v = 0; v < v0.length; ++v) {
            int a1 = /*EL:298*/0;
            /*SL:299*/if (v0[v] != null) {
                /*SL:300*/a1 = v0[v].hashCode();
            }
            /*SL:301*/n = 31 * n + a1;
        }
        /*SL:303*/return n;
    }
    
    static {
        AnnotationImpl.JDK_ANNOTATION_TYPE_METHOD = null;
        try {
            final Class v1 = Class.forName("java.lang.annotation.Annotation");
            AnnotationImpl.JDK_ANNOTATION_TYPE_METHOD = v1.getMethod("annotationType", (Class[])null);
        }
        catch (Exception ex) {}
    }
}
