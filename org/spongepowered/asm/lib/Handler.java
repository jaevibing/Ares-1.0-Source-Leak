package org.spongepowered.asm.lib;

class Handler
{
    Label start;
    Label end;
    Label handler;
    String desc;
    int type;
    Handler next;
    
    static Handler remove(Handler a2, final Label a3, final Label v1) {
        /*SL:84*/if (a2 == null) {
            /*SL:85*/return null;
        }
        /*SL:87*/a2.next = remove(a2.next, a3, v1);
        final int v2 = /*EL:89*/a2.start.position;
        final int v3 = /*EL:90*/a2.end.position;
        final int v4 = /*EL:91*/a3.position;
        final int v5 = /*EL:92*/(v1 == null) ? Integer.MAX_VALUE : v1.position;
        /*SL:94*/if (v4 < v3 && v5 > v2) {
            /*SL:95*/if (v4 <= v2) {
                /*SL:96*/if (v5 >= v3) {
                    /*SL:98*/a2 = a2.next;
                }
                else {
                    /*SL:101*/a2.start = v1;
                }
            }
            else/*SL:103*/ if (v5 >= v3) {
                /*SL:105*/a2.end = a3;
            }
            else {
                final Handler a4 = /*EL:108*/new Handler();
                /*SL:109*/a4.start = v1;
                /*SL:110*/a4.end = a2.end;
                /*SL:111*/a4.handler = a2.handler;
                /*SL:112*/a4.desc = a2.desc;
                /*SL:113*/a4.type = a2.type;
                /*SL:114*/a4.next = a2.next;
                /*SL:115*/a2.end = a3;
                /*SL:116*/a2.next = a4;
            }
        }
        /*SL:119*/return a2;
    }
}
