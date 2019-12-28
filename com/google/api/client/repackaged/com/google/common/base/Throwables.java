package com.google.api.client.repackaged.com.google.common.base;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractList;
import java.util.Arrays;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.google.api.client.repackaged.com.google.common.annotations.Beta;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.VisibleForTesting;
import com.google.api.client.repackaged.com.google.common.annotations.GwtIncompatible;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Throwables
{
    @GwtIncompatible
    private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
    @GwtIncompatible
    @VisibleForTesting
    static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
    @Nullable
    @GwtIncompatible
    private static final Object jla;
    @Nullable
    @GwtIncompatible
    private static final Method getStackTraceElementMethod;
    @Nullable
    @GwtIncompatible
    private static final Method getStackTraceDepthMethod;
    
    @GwtIncompatible
    public static <X extends Throwable> void throwIfInstanceOf(final Throwable a1, final Class<X> a2) throws X, Throwable {
        /*SL:73*/Preconditions.<Throwable>checkNotNull(a1);
        /*SL:74*/if (a2.isInstance(a1)) {
            /*SL:75*/throw a2.cast(a1);
        }
    }
    
    @Deprecated
    @GwtIncompatible
    public static <X extends Throwable> void propagateIfInstanceOf(@Nullable final Throwable a1, final Class<X> a2) throws X, Throwable {
        /*SL:102*/if (a1 != null) {
            /*SL:103*/Throwables.<Throwable>throwIfInstanceOf(a1, (Class<Throwable>)a2);
        }
    }
    
    public static void throwIfUnchecked(final Throwable a1) {
        /*SL:127*/Preconditions.<Throwable>checkNotNull(a1);
        /*SL:128*/if (a1 instanceof RuntimeException) {
            /*SL:129*/throw (RuntimeException)a1;
        }
        /*SL:131*/if (a1 instanceof Error) {
            /*SL:132*/throw (Error)a1;
        }
    }
    
    @Deprecated
    @GwtIncompatible
    public static void propagateIfPossible(@Nullable final Throwable a1) {
        /*SL:157*/if (a1 != null) {
            throwIfUnchecked(/*EL:158*/a1);
        }
    }
    
    @GwtIncompatible
    public static <X extends Throwable> void propagateIfPossible(@Nullable final Throwable a1, final Class<X> a2) throws X, Throwable {
        /*SL:183*/Throwables.<Throwable>propagateIfInstanceOf(a1, (Class<Throwable>)a2);
        propagateIfPossible(/*EL:184*/a1);
    }
    
    @GwtIncompatible
    public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@Nullable final Throwable a1, final Class<X1> a2, final Class<X2> a3) throws X1, X2, Throwable {
        /*SL:202*/Preconditions.<Class<X2>>checkNotNull(a3);
        /*SL:203*/Throwables.<X1>propagateIfInstanceOf(a1, a2);
        /*SL:204*/Throwables.<X2>propagateIfPossible(a1, a3);
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static RuntimeException propagate(final Throwable a1) {
        throwIfUnchecked(/*EL:239*/a1);
        /*SL:240*/throw new RuntimeException(a1);
    }
    
    public static Throwable getRootCause(Throwable a1) {
        Throwable v1;
        /*SL:253*/while ((v1 = a1.getCause()) != null) {
            /*SL:254*/a1 = v1;
        }
        /*SL:256*/return a1;
    }
    
    @Beta
    public static List<Throwable> getCausalChain(Throwable a1) {
        /*SL:276*/Preconditions.<Throwable>checkNotNull(a1);
        final List<Throwable> v1 = /*EL:277*/new ArrayList<Throwable>(4);
        /*SL:278*/while (a1 != null) {
            /*SL:279*/v1.add(a1);
            /*SL:280*/a1 = a1.getCause();
        }
        /*SL:282*/return Collections.<Throwable>unmodifiableList((List<? extends Throwable>)v1);
    }
    
    @GwtIncompatible
    public static String getStackTraceAsString(final Throwable a1) {
        final StringWriter v1 = /*EL:293*/new StringWriter();
        /*SL:294*/a1.printStackTrace(new PrintWriter(v1));
        /*SL:295*/return v1.toString();
    }
    
    @Beta
    @GwtIncompatible
    public static List<StackTraceElement> lazyStackTrace(final Throwable a1) {
        /*SL:329*/return lazyStackTraceIsLazy() ? jlaStackTrace(a1) : Collections.<StackTraceElement>unmodifiableList((List<? extends StackTraceElement>)Arrays.<? extends T>asList((T[])a1.getStackTrace()));
    }
    
    @Beta
    @GwtIncompatible
    public static boolean lazyStackTraceIsLazy() {
        /*SL:343*/return Throwables.getStackTraceElementMethod != null & Throwables.getStackTraceDepthMethod != null;
    }
    
    @GwtIncompatible
    private static List<StackTraceElement> jlaStackTrace(final Throwable a1) {
        /*SL:348*/Preconditions.<Throwable>checkNotNull(a1);
        /*SL:355*/return new AbstractList<StackTraceElement>() {
            @Override
            public StackTraceElement get(final int a1) {
                /*SL:358*/return (StackTraceElement)invokeAccessibleNonThrowingMethod(Throwables.getStackTraceElementMethod, Throwables.jla, new Object[] { a1, a1 });
            }
            
            @Override
            public int size() {
                /*SL:364*/return (int)invokeAccessibleNonThrowingMethod(Throwables.getStackTraceDepthMethod, Throwables.jla, new Object[] { a1 });
            }
        };
    }
    
    @GwtIncompatible
    private static Object invokeAccessibleNonThrowingMethod(final Method a3, final Object v1, final Object... v2) {
        try {
            /*SL:373*/return a3.invoke(v1, v2);
        }
        catch (IllegalAccessException a4) {
            /*SL:375*/throw new RuntimeException(a4);
        }
        catch (InvocationTargetException a5) {
            throw propagate(/*EL:377*/a5.getCause());
        }
    }
    
    @Nullable
    @GwtIncompatible
    private static Object getJLA() {
        try {
            final Class<?> v1 = /*EL:423*/Class.forName("sun.misc.SharedSecrets", false, null);
            final Method v2 = /*EL:424*/v1.getMethod("getJavaLangAccess", (Class<?>[])new Class[0]);
            /*SL:425*/return v2.invoke(null, new Object[0]);
        }
        catch (ThreadDeath v3) {
            /*SL:427*/throw v3;
        }
        catch (Throwable v4) {
            /*SL:433*/return null;
        }
    }
    
    @Nullable
    @GwtIncompatible
    private static Method getGetMethod() {
        /*SL:444*/return getJlaMethod("getStackTraceElement", Throwable.class, Integer.TYPE);
    }
    
    @Nullable
    @GwtIncompatible
    private static Method getSizeMethod() {
        /*SL:454*/return getJlaMethod("getStackTraceDepth", Throwable.class);
    }
    
    @Nullable
    @GwtIncompatible
    private static Method getJlaMethod(final String v1, final Class<?>... v2) throws ThreadDeath {
        try {
            /*SL:461*/return Class.forName("sun.misc.JavaLangAccess", false, null).getMethod(v1, v2);
        }
        catch (ThreadDeath a1) {
            /*SL:463*/throw a1;
        }
        catch (Throwable a2) {
            /*SL:469*/return null;
        }
    }
    
    static {
        jla = getJLA();
        getStackTraceElementMethod = ((Throwables.jla == null) ? null : getGetMethod());
        getStackTraceDepthMethod = ((Throwables.jla == null) ? null : getSizeMethod());
    }
}
