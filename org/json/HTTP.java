package org.json;

import java.util.Iterator;
import java.util.Locale;

public class HTTP
{
    public static final String CRLF = "\r\n";
    
    public static JSONObject toJSONObject(final String v1) throws JSONException {
        final JSONObject v2 = /*EL:72*/new JSONObject();
        final HTTPTokener v3 = /*EL:73*/new HTTPTokener(v1);
        final String v4 = /*EL:76*/v3.nextToken();
        /*SL:77*/if (v4.toUpperCase(Locale.ROOT).startsWith("HTTP")) {
            /*SL:81*/v2.put("HTTP-Version", v4);
            /*SL:82*/v2.put("Status-Code", v3.nextToken());
            /*SL:83*/v2.put("Reason-Phrase", v3.nextTo('\0'));
            /*SL:84*/v3.next();
        }
        else {
            /*SL:90*/v2.put("Method", v4);
            /*SL:91*/v2.put("Request-URI", v3.nextToken());
            /*SL:92*/v2.put("HTTP-Version", v3.nextToken());
        }
        /*SL:97*/while (v3.more()) {
            final String a1 = /*EL:98*/v3.nextTo(':');
            /*SL:99*/v3.next(':');
            /*SL:100*/v2.put(a1, v3.nextTo('\0'));
            /*SL:101*/v3.next();
        }
        /*SL:103*/return v2;
    }
    
    public static String toString(final JSONObject v-2) throws JSONException {
        final StringBuilder sb = /*EL:128*/new StringBuilder();
        /*SL:129*/if (v-2.has("Status-Code") && v-2.has("Reason-Phrase")) {
            /*SL:130*/sb.append(v-2.getString("HTTP-Version"));
            /*SL:131*/sb.append(' ');
            /*SL:132*/sb.append(v-2.getString("Status-Code"));
            /*SL:133*/sb.append(' ');
            /*SL:134*/sb.append(v-2.getString("Reason-Phrase"));
        }
        else {
            /*SL:135*/if (!v-2.has("Method") || !v-2.has("Request-URI")) {
                /*SL:144*/throw new JSONException("Not enough material for an HTTP header.");
            }
            sb.append(v-2.getString("Method"));
            sb.append(' ');
            sb.append('\"');
            sb.append(v-2.getString("Request-URI"));
            sb.append('\"');
            sb.append(' ');
            sb.append(v-2.getString("HTTP-Version"));
        }
        /*SL:146*/sb.append("\r\n");
        /*SL:148*/for (final String v1 : v-2.keySet()) {
            final String a1 = /*EL:149*/v-2.optString(v1);
            /*SL:152*/if (!"HTTP-Version".equals(v1) && !"Status-Code".equals(v1) && !"Reason-Phrase".equals(v1) && !"Method".equals(v1) && !"Request-URI".equals(v1) && !JSONObject.NULL.equals(a1)) {
                /*SL:153*/sb.append(v1);
                /*SL:154*/sb.append(": ");
                /*SL:155*/sb.append(v-2.optString(v1));
                /*SL:156*/sb.append("\r\n");
            }
        }
        /*SL:159*/sb.append("\r\n");
        /*SL:160*/return sb.toString();
    }
}
