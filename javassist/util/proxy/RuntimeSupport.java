package javassist.util.proxy;

import java.io.Serializable;
import java.io.InvalidClassException;
import java.lang.reflect.Method;

public class RuntimeSupport
{
    public static MethodHandler default_interceptor;
    
    public static void find2Methods(final Class a1, final String a2, final String a3, final int a4, final String a5, final Method[] a6) {
        /*SL:54*/a6[a4 + 1] = ((a3 == null) ? null : findMethod(a1, a3, a5));
        /*SL:55*/a6[a4] = findSuperClassMethod(a1, a2, a5);
    }
    
    public static void find2Methods(final Object a1, final String a2, final String a3, final int a4, final String a5, final Method[] a6) {
        /*SL:73*/a6[a4 + 1] = ((a3 == null) ? null : findMethod(a1, a3, a5));
        /*SL:74*/a6[a4] = findSuperMethod(a1, a2, a5);
    }
    
    public static Method findMethod(final Object a1, final String a2, final String a3) {
        final Method v1 = findMethod2(/*EL:87*/a1.getClass(), a2, a3);
        /*SL:88*/if (v1 == null) {
            error(/*EL:89*/a1.getClass(), a2, a3);
        }
        /*SL:91*/return v1;
    }
    
    public static Method findMethod(final Class a1, final String a2, final String a3) {
        final Method v1 = findMethod2(/*EL:101*/a1, a2, a3);
        /*SL:102*/if (v1 == null) {
            error(/*EL:103*/a1, a2, a3);
        }
        /*SL:105*/return v1;
    }
    
    public static Method findSuperMethod(final Object a1, final String a2, final String a3) {
        final Class v1 = /*EL:116*/a1.getClass();
        /*SL:117*/return findSuperClassMethod(v1, a2, a3);
    }
    
    public static Method findSuperClassMethod(final Class a1, final String a2, final String a3) {
        Method v1 = findSuperMethod2(/*EL:127*/a1.getSuperclass(), a2, a3);
        /*SL:128*/if (v1 == null) {
            /*SL:129*/v1 = searchInterfaces(a1, a2, a3);
        }
        /*SL:131*/if (v1 == null) {
            error(/*EL:132*/a1, a2, a3);
        }
        /*SL:134*/return v1;
    }
    
    private static void error(final Class a1, final String a2, final String a3) {
        /*SL:138*/throw new RuntimeException("not found " + a2 + ":" + a3 + " in " + a1.getName());
    }
    
    private static Method findSuperMethod2(final Class a1, final String a2, final String a3) {
        Method v1 = findMethod2(/*EL:143*/a1, a2, a3);
        /*SL:144*/if (v1 != null) {
            /*SL:145*/return v1;
        }
        final Class v2 = /*EL:147*/a1.getSuperclass();
        /*SL:148*/if (v2 != null) {
            /*SL:149*/v1 = findSuperMethod2(v2, a2, a3);
            /*SL:150*/if (v1 != null) {
                /*SL:151*/return v1;
            }
        }
        /*SL:154*/return searchInterfaces(a1, a2, a3);
    }
    
    private static Method searchInterfaces(final Class a2, final String a3, final String v1) {
        Method v2 = /*EL:158*/null;
        final Class[] v3 = /*EL:159*/a2.getInterfaces();
        /*SL:160*/for (int a4 = 0; a4 < v3.length; ++a4) {
            /*SL:161*/v2 = findSuperMethod2(v3[a4], a3, v1);
            /*SL:162*/if (v2 != null) {
                /*SL:163*/return v2;
            }
        }
        /*SL:166*/return v2;
    }
    
    private static Method findMethod2(final Class a2, final String a3, final String v1) {
        final Method[] v2 = /*EL:170*/SecurityActions.getDeclaredMethods(a2);
        /*SL:172*/for (int v3 = v2.length, a4 = 0; a4 < v3; ++a4) {
            /*SL:173*/if (v2[a4].getName().equals(a3) && makeDescriptor(v2[a4]).equals(/*EL:174*/v1)) {
                /*SL:175*/return v2[a4];
            }
        }
        /*SL:177*/return null;
    }
    
    public static String makeDescriptor(final Method a1) {
        final Class[] v1 = /*EL:184*/a1.getParameterTypes();
        /*SL:185*/return makeDescriptor(v1, a1.getReturnType());
    }
    
    public static String makeDescriptor(final Class[] a2, final Class v1) {
        final StringBuffer v2 = /*EL:195*/new StringBuffer();
        /*SL:196*/v2.append('(');
        /*SL:197*/for (int a3 = 0; a3 < a2.length; ++a3) {
            makeDesc(/*EL:198*/v2, a2[a3]);
        }
        /*SL:200*/v2.append(')');
        /*SL:201*/if (v1 != null) {
            makeDesc(/*EL:202*/v2, v1);
        }
        /*SL:204*/return v2.toString();
    }
    
    public static String makeDescriptor(final String a1, final Class a2) {
        final StringBuffer v1 = /*EL:214*/new StringBuffer(a1);
        makeDesc(/*EL:215*/v1, a2);
        /*SL:216*/return v1.toString();
    }
    
    private static void makeDesc(final StringBuffer a1, final Class a2) {
        /*SL:220*/if (a2.isArray()) {
            /*SL:221*/a1.append('[');
            makeDesc(/*EL:222*/a1, a2.getComponentType());
        }
        else/*SL:224*/ if (a2.isPrimitive()) {
            /*SL:225*/if (a2 == Void.TYPE) {
                /*SL:226*/a1.append('V');
            }
            else/*SL:227*/ if (a2 == Integer.TYPE) {
                /*SL:228*/a1.append('I');
            }
            else/*SL:229*/ if (a2 == Byte.TYPE) {
                /*SL:230*/a1.append('B');
            }
            else/*SL:231*/ if (a2 == Long.TYPE) {
                /*SL:232*/a1.append('J');
            }
            else/*SL:233*/ if (a2 == Double.TYPE) {
                /*SL:234*/a1.append('D');
            }
            else/*SL:235*/ if (a2 == Float.TYPE) {
                /*SL:236*/a1.append('F');
            }
            else/*SL:237*/ if (a2 == Character.TYPE) {
                /*SL:238*/a1.append('C');
            }
            else/*SL:239*/ if (a2 == Short.TYPE) {
                /*SL:240*/a1.append('S');
            }
            else {
                /*SL:241*/if (a2 != Boolean.TYPE) {
                    /*SL:244*/throw new RuntimeException("bad type: " + a2.getName());
                }
                a1.append('Z');
            }
        }
        else {
            /*SL:247*/a1.append('L').append(a2.getName().replace('.', '/')).append(';');
        }
    }
    
    public static SerializedProxy makeSerializedProxy(final Object a1) throws InvalidClassException {
        final Class v1 = /*EL:261*/a1.getClass();
        MethodHandler v2 = /*EL:263*/null;
        /*SL:264*/if (a1 instanceof ProxyObject) {
            /*SL:265*/v2 = ((ProxyObject)a1).getHandler();
        }
        else/*SL:266*/ if (a1 instanceof Proxy) {
            /*SL:267*/v2 = ProxyFactory.getHandler((Proxy)a1);
        }
        /*SL:269*/return new SerializedProxy(v1, ProxyFactory.getFilterSignature(v1), v2);
    }
    
    static {
        RuntimeSupport.default_interceptor = new DefaultMethodHandler();
    }
    
    static class DefaultMethodHandler implements MethodHandler, Serializable
    {
        @Override
        public Object invoke(final Object a1, final Method a2, final Method a3, final Object[] a4) throws Exception {
            /*SL:38*/return a3.invoke(a1, a4);
        }
    }
}
