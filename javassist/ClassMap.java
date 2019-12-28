package javassist;

import javassist.bytecode.Descriptor;
import java.util.HashMap;

public class ClassMap extends HashMap
{
    private ClassMap parent;
    
    public ClassMap() {
        this.parent = null;
    }
    
    ClassMap(final ClassMap a1) {
        this.parent = a1;
    }
    
    public void put(final CtClass a1, final CtClass a2) {
        /*SL:71*/this.put(a1.getName(), a2.getName());
    }
    
    public void put(final String a1, final String a2) {
        /*SL:93*/if (a1 == a2) {
            /*SL:94*/return;
        }
        final String v1 = toJvmName(/*EL:96*/a1);
        final String v2 = /*EL:97*/(String)this.get(v1);
        /*SL:98*/if (v2 == null || !v2.equals(v1)) {
            /*SL:99*/super.put(v1, toJvmName(a2));
        }
    }
    
    public void putIfNone(final String a1, final String a2) {
        /*SL:112*/if (a1 == a2) {
            /*SL:113*/return;
        }
        final String v1 = toJvmName(/*EL:115*/a1);
        final String v2 = /*EL:116*/(String)this.get(v1);
        /*SL:117*/if (v2 == null) {
            /*SL:118*/super.put(v1, toJvmName(a2));
        }
    }
    
    protected final void put0(final Object a1, final Object a2) {
        /*SL:122*/super.put(a1, a2);
    }
    
    @Override
    public Object get(final Object a1) {
        final Object v1 = /*EL:136*/super.get(a1);
        /*SL:137*/if (v1 == null && this.parent != null) {
            /*SL:138*/return this.parent.get(a1);
        }
        /*SL:140*/return v1;
    }
    
    public void fix(final CtClass a1) {
        /*SL:147*/this.fix(a1.getName());
    }
    
    public void fix(final String a1) {
        final String v1 = toJvmName(/*EL:154*/a1);
        /*SL:155*/super.put(v1, v1);
    }
    
    public static String toJvmName(final String a1) {
        /*SL:163*/return Descriptor.toJvmName(a1);
    }
    
    public static String toJavaName(final String a1) {
        /*SL:171*/return Descriptor.toJavaName(a1);
    }
}
