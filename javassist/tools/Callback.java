package javassist.tools;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import java.util.UUID;
import java.util.HashMap;

public abstract class Callback
{
    public static HashMap callbacks;
    private final String sourceCode;
    
    public Callback(final String a1) {
        final String v1 = UUID.randomUUID().toString();
        Callback.callbacks.put(v1, this);
        this.sourceCode = "((javassist.tools.Callback) javassist.tools.Callback.callbacks.get(\"" + v1 + "\")).result(new Object[]{" + a1 + "});";
    }
    
    public abstract void result(final Object[] p0);
    
    @Override
    public String toString() {
        /*SL:77*/return this.sourceCode();
    }
    
    public String sourceCode() {
        /*SL:81*/return this.sourceCode;
    }
    
    public static void insertBefore(final CtBehavior a1, final Callback a2) throws CannotCompileException {
        /*SL:94*/a1.insertBefore(a2.toString());
    }
    
    public static void insertAfter(final CtBehavior a1, final Callback a2) throws CannotCompileException {
        /*SL:110*/a1.insertAfter(a2.toString(), false);
    }
    
    public static void insertAfter(final CtBehavior a1, final Callback a2, final boolean a3) throws CannotCompileException {
        /*SL:131*/a1.insertAfter(a2.toString(), a3);
    }
    
    public static int insertAt(final CtBehavior a1, final Callback a2, final int a3) throws CannotCompileException {
        /*SL:150*/return a1.insertAt(a3, a2.toString());
    }
    
    static {
        Callback.callbacks = new HashMap();
    }
}
