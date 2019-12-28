package org.json;

public class Cookie
{
    public static String escape(final String v-4) {
        final String trim = /*EL:49*/v-4.trim();
        final int length = /*EL:50*/trim.length();
        final StringBuilder v0 = /*EL:51*/new StringBuilder(length);
        /*SL:52*/for (int v = 0; v < length; ++v) {
            final char a1 = /*EL:53*/trim.charAt(v);
            /*SL:54*/if (a1 < ' ' || a1 == '+' || a1 == '%' || a1 == '=' || a1 == ';') {
                /*SL:55*/v0.append('%');
                /*SL:56*/v0.append(Character.forDigit((char)(a1 >>> 4 & '\u000f'), 16));
                /*SL:57*/v0.append(Character.forDigit((char)(a1 & '\u000f'), 16));
            }
            else {
                /*SL:59*/v0.append(a1);
            }
        }
        /*SL:62*/return v0.toString();
    }
    
    public static JSONObject toJSONObject(final String v0) throws JSONException {
        final JSONObject v = /*EL:83*/new JSONObject();
        final JSONTokener v2 = /*EL:85*/new JSONTokener(v0);
        /*SL:86*/v.put("name", v2.nextTo('='));
        /*SL:87*/v2.next('=');
        /*SL:88*/v.put("value", v2.nextTo(';'));
        /*SL:89*/v2.next();
        /*SL:90*/while (v2.more()) {
            final String v3 = unescape(/*EL:91*/v2.nextTo("=;"));
            final Object v4;
            /*SL:92*/if (v2.next() != '=') {
                /*SL:93*/if (!v3.equals("secure")) {
                    /*SL:96*/throw v2.syntaxError("Missing '=' in cookie parameter.");
                }
                final Object a1 = Boolean.TRUE;
            }
            else {
                /*SL:99*/v4 = unescape(v2.nextTo(';'));
                /*SL:100*/v2.next();
            }
            /*SL:102*/v.put(v3, v4);
        }
        /*SL:104*/return v;
    }
    
    public static String toString(final JSONObject a1) throws JSONException {
        final StringBuilder v1 = /*EL:119*/new StringBuilder();
        /*SL:121*/v1.append(escape(a1.getString("name")));
        /*SL:122*/v1.append("=");
        /*SL:123*/v1.append(escape(a1.getString("value")));
        /*SL:124*/if (a1.has("expires")) {
            /*SL:125*/v1.append(";expires=");
            /*SL:126*/v1.append(a1.getString("expires"));
        }
        /*SL:128*/if (a1.has("domain")) {
            /*SL:129*/v1.append(";domain=");
            /*SL:130*/v1.append(escape(a1.getString("domain")));
        }
        /*SL:132*/if (a1.has("path")) {
            /*SL:133*/v1.append(";path=");
            /*SL:134*/v1.append(escape(a1.getString("path")));
        }
        /*SL:136*/if (a1.optBoolean("secure")) {
            /*SL:137*/v1.append(";secure");
        }
        /*SL:139*/return v1.toString();
    }
    
    public static String unescape(final String v-5) {
        final int length = /*EL:151*/v-5.length();
        final StringBuilder sb = /*EL:152*/new StringBuilder(length);
        /*SL:153*/for (int i = 0; i < length; ++i) {
            char char1 = /*EL:154*/v-5.charAt(i);
            /*SL:155*/if (char1 == '+') {
                /*SL:156*/char1 = ' ';
            }
            else/*SL:157*/ if (char1 == '%' && i + 2 < length) {
                final int a1 = /*EL:158*/JSONTokener.dehexchar(v-5.charAt(i + 1));
                final int v1 = /*EL:159*/JSONTokener.dehexchar(v-5.charAt(i + 2));
                /*SL:160*/if (a1 >= 0 && v1 >= 0) {
                    /*SL:161*/char1 = (char)(a1 * 16 + v1);
                    /*SL:162*/i += 2;
                }
            }
            /*SL:165*/sb.append(char1);
        }
        /*SL:167*/return sb.toString();
    }
}
