package javassist.compiler;

import java.util.HashMap;

public final class KeywordTable extends HashMap
{
    public int lookup(final String a1) {
        final Object v1 = /*EL:23*/this.get(a1);
        /*SL:24*/if (v1 == null) {
            /*SL:25*/return -1;
        }
        /*SL:27*/return (int)v1;
    }
    
    public void append(final String a1, final int a2) {
        /*SL:31*/this.put(a1, new Integer(a2));
    }
}
