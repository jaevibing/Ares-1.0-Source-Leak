package com.fasterxml.jackson.core.sym;

import java.util.Arrays;

public final class NameN extends Name
{
    private final int q1;
    private final int q2;
    private final int q3;
    private final int q4;
    private final int qlen;
    private final int[] q;
    
    NameN(final String a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int[] a7, final int a8) {
        super(a1, a2);
        this.q1 = a3;
        this.q2 = a4;
        this.q3 = a5;
        this.q4 = a6;
        this.q = a7;
        this.qlen = a8;
    }
    
    public static NameN construct(final String a2, final int a3, final int[] a4, final int v1) {
        /*SL:31*/if (v1 < 4) {
            /*SL:32*/throw new IllegalArgumentException();
        }
        final int v2 = /*EL:34*/a4[0];
        final int v3 = /*EL:35*/a4[1];
        final int v4 = /*EL:36*/a4[2];
        final int v5 = /*EL:37*/a4[3];
        final int v6 = /*EL:39*/v1 - 4;
        final int[] v7;
        /*SL:43*/if (v6 > 0) {
            final int[] a5 = /*EL:44*/Arrays.copyOfRange(a4, 4, v1);
        }
        else {
            /*SL:46*/v7 = null;
        }
        /*SL:48*/return new NameN(a2, a3, v2, v3, v4, v5, v7, v1);
    }
    
    @Override
    public boolean equals(final int a1) {
        /*SL:54*/return false;
    }
    
    @Override
    public boolean equals(final int a1, final int a2) {
        /*SL:58*/return false;
    }
    
    @Override
    public boolean equals(final int a1, final int a2, final int a3) {
        /*SL:62*/return false;
    }
    
    @Override
    public boolean equals(final int[] a1, final int a2) {
        /*SL:66*/if (a2 != this.qlen) {
            return false;
        }
        /*SL:69*/if (a1[0] != this.q1) {
            return false;
        }
        /*SL:70*/if (a1[1] != this.q2) {
            return false;
        }
        /*SL:71*/if (a1[2] != this.q3) {
            return false;
        }
        /*SL:72*/if (a1[3] != this.q4) {
            return false;
        }
        /*SL:74*/switch (a2) {
            default: {
                /*SL:76*/return this._equals2(a1);
            }
            case 8: {
                /*SL:78*/if (a1[7] != this.q[3]) {
                    return false;
                }
            }
            case 7: {
                /*SL:80*/if (a1[6] != this.q[2]) {
                    return false;
                }
            }
            case 6: {
                /*SL:82*/if (a1[5] != this.q[1]) {
                    return false;
                }
            }
            case 5: {
                /*SL:84*/if (a1[4] != this.q[0]) {
                    return false;
                }
                return /*EL:87*/true;
            }
            case 4: {
                return true;
            }
        }
    }
    
    private final boolean _equals2(final int[] v2) {
        /*SL:93*/for (int v3 = this.qlen - 4, a1 = 0; a1 < v3; ++a1) {
            /*SL:94*/if (v2[a1 + 4] != this.q[a1]) {
                /*SL:95*/return false;
            }
        }
        /*SL:98*/return true;
    }
}
