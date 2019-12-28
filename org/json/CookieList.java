package org.json;

import java.util.Iterator;

public class CookieList
{
    public static JSONObject toJSONObject(final String v1) throws JSONException {
        final JSONObject v2 = /*EL:48*/new JSONObject();
        final JSONTokener v3 = /*EL:49*/new JSONTokener(v1);
        /*SL:50*/while (v3.more()) {
            final String a1 = /*EL:51*/Cookie.unescape(v3.nextTo('='));
            /*SL:52*/v3.next('=');
            /*SL:53*/v2.put(a1, Cookie.unescape(v3.nextTo(';')));
            /*SL:54*/v3.next();
        }
        /*SL:56*/return v2;
    }
    
    public static String toString(final JSONObject v-3) throws JSONException {
        boolean b = /*EL:69*/false;
        final StringBuilder sb = /*EL:70*/new StringBuilder();
        /*SL:72*/for (final String v1 : v-3.keySet()) {
            final Object a1 = /*EL:73*/v-3.opt(v1);
            /*SL:74*/if (!JSONObject.NULL.equals(a1)) {
                /*SL:75*/if (b) {
                    /*SL:76*/sb.append(';');
                }
                /*SL:78*/sb.append(Cookie.escape(v1));
                /*SL:79*/sb.append("=");
                /*SL:80*/sb.append(Cookie.escape(a1.toString()));
                /*SL:81*/b = true;
            }
        }
        /*SL:84*/return sb.toString();
    }
}
