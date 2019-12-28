package com.google.api.client.repackaged.com.google.common.base;

import java.util.BitSet;
import com.google.api.client.repackaged.com.google.common.annotations.VisibleForTesting;
import com.google.api.client.repackaged.com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
final class SmallCharMatcher extends NamedFastMatcher
{
    static final int MAX_SIZE = 1023;
    private final char[] table;
    private final boolean containsZero;
    private final long filter;
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final double DESIRED_LOAD_FACTOR = 0.5;
    
    private SmallCharMatcher(final char[] a1, final long a2, final boolean a3, final String a4) {
        super(a4);
        this.table = a1;
        this.filter = a2;
        this.containsZero = a3;
    }
    
    static int smear(final int a1) {
        /*SL:54*/return 461845907 * Integer.rotateLeft(a1 * -862048943, 15);
    }
    
    private boolean checkFilter(final int a1) {
        /*SL:58*/return 0x1L == (0x1L & this.filter >> a1);
    }
    
    @VisibleForTesting
    static int chooseTableSize(final int a1) {
        /*SL:74*/if (a1 == 1) {
            /*SL:75*/return 2;
        }
        int v1;
        /*SL:80*/for (v1 = Integer.highestOneBit(a1 - 1) << 1; v1 * 0.5 < a1; /*SL:81*/v1 <<= 1) {}
        /*SL:83*/return v1;
    }
    
    static CharMatcher from(final BitSet v1, final String v2) {
        long v3 = /*EL:88*/0L;
        final int v4 = /*EL:89*/v1.cardinality();
        final boolean v5 = /*EL:90*/v1.get(0);
        final char[] v6 = /*EL:92*/new char[chooseTableSize(v4)];
        final int v7 = /*EL:93*/v6.length - 1;
        /*SL:94*/for (int a2 = v1.nextSetBit(0); a2 != -1; a2 = v1.nextSetBit(a2 + 1)) {
            /*SL:96*/v3 |= 1L << a2;
            /*SL:100*/for (a2 = (smear(a2) & v7); v6[a2] != '\0'; /*SL:105*/a2 = (a2 + 1 & v7)) {}
            v6[a2] = (char)a2;
        }
        /*SL:108*/return new SmallCharMatcher(v6, v3, v5, v2);
    }
    
    @Override
    public boolean matches(final char a1) {
        /*SL:113*/if (a1 == '\0') {
            /*SL:114*/return this.containsZero;
        }
        /*SL:116*/if (!this.checkFilter(a1)) {
            /*SL:117*/return false;
        }
        final int v1 = /*EL:119*/this.table.length - 1;
        int v3;
        final int v2 = /*EL:121*/v3 = (smear(a1) & v1);
        /*SL:123*/while (this.table[v3] != '\0') {
            /*SL:125*/if (this.table[v3] == a1) {
                /*SL:126*/return true;
            }
            /*SL:128*/v3 = (v3 + 1 & v1);
            /*SL:131*/if (v3 == v2) {
                /*SL:132*/return false;
            }
        }
        return false;
    }
    
    @Override
    void setBits(final BitSet v0) {
        /*SL:137*/if (this.containsZero) {
            /*SL:138*/v0.set(0);
        }
        /*SL:140*/for (final char a1 : this.table) {
            /*SL:141*/if (a1 != '\0') {
                /*SL:142*/v0.set(a1);
            }
        }
    }
}
