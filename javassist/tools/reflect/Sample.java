package javassist.tools.reflect;

public class Sample
{
    private Metaobject _metaobject;
    private static ClassMetaobject _classobject;
    
    public Object trap(final Object[] a1, final int a2) throws Throwable {
        final Metaobject v1 = /*EL:28*/this._metaobject;
        /*SL:29*/if (v1 == null) {
            /*SL:30*/return ClassMetaobject.invoke(this, a2, a1);
        }
        /*SL:32*/return v1.trapMethodcall(a2, a1);
    }
    
    public static Object trapStatic(final Object[] a1, final int a2) throws Throwable {
        /*SL:38*/return Sample._classobject.trapMethodcall(a2, a1);
    }
    
    public static Object trapRead(final Object[] a1, final String a2) {
        /*SL:42*/if (a1[0] == null) {
            /*SL:43*/return Sample._classobject.trapFieldRead(a2);
        }
        /*SL:45*/return ((Metalevel)a1[0])._getMetaobject().trapFieldRead(a2);
    }
    
    public static Object trapWrite(final Object[] a1, final String a2) {
        final Metalevel v1 = /*EL:49*/(Metalevel)a1[0];
        /*SL:50*/if (v1 == null) {
            Sample._classobject.trapFieldWrite(/*EL:51*/a2, a1[1]);
        }
        else {
            /*SL:53*/v1._getMetaobject().trapFieldWrite(a2, a1[1]);
        }
        /*SL:55*/return null;
    }
}
