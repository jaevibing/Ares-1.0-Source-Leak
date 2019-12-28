package javassist.util.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Method;

class SecurityActions
{
    static Method[] getDeclaredMethods(final Class a1) {
        /*SL:29*/if (System.getSecurityManager() == null) {
            /*SL:30*/return a1.getDeclaredMethods();
        }
        /*SL:33*/return AccessController.<Method[]>doPrivileged((PrivilegedAction<Method[]>)new PrivilegedAction() {
            @Override
            public Object run() {
                /*SL:35*/return a1.getDeclaredMethods();
            }
        });
    }
    
    static Constructor[] getDeclaredConstructors(final Class a1) {
        /*SL:42*/if (System.getSecurityManager() == null) {
            /*SL:43*/return a1.getDeclaredConstructors();
        }
        /*SL:46*/return AccessController.<Constructor[]>doPrivileged((PrivilegedAction<Constructor[]>)new PrivilegedAction() {
            @Override
            public Object run() {
                /*SL:48*/return a1.getDeclaredConstructors();
            }
        });
    }
    
    static Method getDeclaredMethod(final Class a2, final String a3, final Class[] v1) throws NoSuchMethodException {
        /*SL:56*/if (System.getSecurityManager() == null) {
            /*SL:57*/return a2.getDeclaredMethod(a3, (Class[])v1);
        }
        try {
            /*SL:61*/return AccessController.<Method>doPrivileged((PrivilegedExceptionAction<Method>)new PrivilegedExceptionAction() {
                @Override
                public Object run() throws Exception {
                    /*SL:63*/return a2.getDeclaredMethod(a3, (Class[])v1);
                }
            });
        }
        catch (PrivilegedActionException a4) {
            /*SL:68*/if (a4.getCause() instanceof NoSuchMethodException) {
                /*SL:69*/throw (NoSuchMethodException)a4.getCause();
            }
            /*SL:71*/throw new RuntimeException(a4.getCause());
        }
    }
    
    static Constructor getDeclaredConstructor(final Class a2, final Class[] v1) throws NoSuchMethodException {
        /*SL:80*/if (System.getSecurityManager() == null) {
            /*SL:81*/return a2.getDeclaredConstructor((Class[])v1);
        }
        try {
            /*SL:85*/return AccessController.<Constructor>doPrivileged((PrivilegedExceptionAction<Constructor>)new PrivilegedExceptionAction() {
                @Override
                public Object run() throws Exception {
                    /*SL:87*/return a2.getDeclaredConstructor((Class[])v1);
                }
            });
        }
        catch (PrivilegedActionException a3) {
            /*SL:92*/if (a3.getCause() instanceof NoSuchMethodException) {
                /*SL:93*/throw (NoSuchMethodException)a3.getCause();
            }
            /*SL:95*/throw new RuntimeException(a3.getCause());
        }
    }
    
    static void setAccessible(final AccessibleObject a1, final boolean a2) {
        /*SL:102*/if (System.getSecurityManager() == null) {
            /*SL:103*/a1.setAccessible(a2);
        }
        else {
            /*SL:105*/AccessController.<Object>doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
                @Override
                public Object run() {
                    /*SL:107*//*EL:108*/a1.setAccessible(a2);
                    return null;
                }
            });
        }
    }
    
    static void set(final Field a2, final Object a3, final Object v1) throws IllegalAccessException {
        /*SL:117*/if (System.getSecurityManager() == null) {
            /*SL:118*/a2.set(a3, v1);
        }
        else {
            try {
                /*SL:121*/AccessController.<Object>doPrivileged((PrivilegedExceptionAction<Object>)new PrivilegedExceptionAction() {
                    @Override
                    public Object run() throws Exception {
                        /*SL:123*//*EL:124*/a2.set(a3, v1);
                        return null;
                    }
                });
            }
            catch (PrivilegedActionException a4) {
                /*SL:129*/if (a4.getCause() instanceof NoSuchMethodException) {
                    /*SL:130*/throw (IllegalAccessException)a4.getCause();
                }
                /*SL:132*/throw new RuntimeException(a4.getCause());
            }
        }
    }
}
