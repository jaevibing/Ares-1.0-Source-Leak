package org.json;

public class CDL
{
    private static String getValue(final JSONTokener v-1) throws JSONException {
        char v0;
        /*SL:61*/do {
            v0 = v-1.next();
        } while (v0 == ' ' || v0 == '\t');
        /*SL:62*/switch (v0) {
            case '\0': {
                /*SL:64*/return null;
            }
            case '\"':
            case '\'': {
                final char v = /*EL:67*/v0;
                final StringBuffer v2 = /*EL:68*/new StringBuffer();
                while (true) {
                    /*SL:70*/v0 = v-1.next();
                    /*SL:71*/if (v0 == v) {
                        final char a1 = /*EL:73*/v-1.next();
                        /*SL:74*/if (a1 != '\"') {
                            /*SL:76*/if (a1 > '\0') {
                                /*SL:77*/v-1.back();
                            }
                            /*SL:87*/return v2.toString();
                        }
                    }
                    if (v0 == '\0' || v0 == '\n' || v0 == '\r') {
                        throw v-1.syntaxError("Missing close quote '" + v + "'.");
                    }
                    v2.append(v0);
                }
                break;
            }
            case ',': {
                /*SL:89*/v-1.back();
                /*SL:90*/return "";
            }
            default: {
                /*SL:92*/v-1.back();
                /*SL:93*/return v-1.nextTo(',');
            }
        }
    }
    
    public static JSONArray rowToJSONArray(final JSONTokener v-2) throws JSONException {
        final JSONArray jsonArray = /*EL:104*/new JSONArray();
        while (true) {
            final String a1 = getValue(/*EL:106*/v-2);
            char v1 = /*EL:107*/v-2.next();
            /*SL:108*/if (a1 == null || (jsonArray.length() == /*EL:109*/0 && a1.length() == 0 && v1 != ',')) {
                /*SL:110*/return null;
            }
            /*SL:112*/jsonArray.put(a1);
            /*SL:114*/while (v1 != ',') {
                /*SL:117*/if (v1 != ' ') {
                    /*SL:118*/if (v1 == '\n' || v1 == '\r' || v1 == '\0') {
                        /*SL:119*/return jsonArray;
                    }
                    /*SL:121*/throw v-2.syntaxError("Bad character '" + v1 + "' (" + (int)v1 + ").");
                }
                else {
                    /*SL:124*/v1 = v-2.next();
                }
            }
        }
    }
    
    public static JSONObject rowToJSONObject(final JSONArray a1, final JSONTokener a2) throws JSONException {
        final JSONArray v1 = rowToJSONArray(/*EL:141*/a2);
        /*SL:142*/return (v1 != null) ? v1.toJSONObject(a1) : null;
    }
    
    public static String rowToString(final JSONArray v-5) {
        final StringBuilder sb = /*EL:153*/new StringBuilder();
        /*SL:154*/for (int i = 0; i < v-5.length(); ++i) {
            /*SL:155*/if (i > 0) {
                /*SL:156*/sb.append(',');
            }
            final Object opt = /*EL:158*/v-5.opt(i);
            /*SL:159*/if (opt != null) {
                final String string = /*EL:160*/opt.toString();
                /*SL:161*/if (string.length() > 0 && (string.indexOf(44) >= 0 || string.indexOf(10) >= /*EL:162*/0 || string.indexOf(13) >= 0 || string.indexOf(0) >= /*EL:163*/0 || string.charAt(0) == '\"')) {
                    /*SL:164*/sb.append('\"');
                    /*SL:166*/for (int v0 = string.length(), v = 0; v < v0; ++v) {
                        final char a1 = /*EL:167*/string.charAt(v);
                        /*SL:168*/if (a1 >= ' ' && a1 != '\"') {
                            /*SL:169*/sb.append(a1);
                        }
                    }
                    /*SL:172*/sb.append('\"');
                }
                else {
                    /*SL:174*/sb.append(string);
                }
            }
        }
        /*SL:178*/sb.append('\n');
        /*SL:179*/return sb.toString();
    }
    
    public static JSONArray toJSONArray(final String a1) throws JSONException {
        /*SL:190*/return toJSONArray(new JSONTokener(a1));
    }
    
    public static JSONArray toJSONArray(final JSONTokener a1) throws JSONException {
        /*SL:201*/return toJSONArray(rowToJSONArray(a1), a1);
    }
    
    public static JSONArray toJSONArray(final JSONArray a1, final String a2) throws JSONException {
        /*SL:214*/return toJSONArray(a1, new JSONTokener(a2));
    }
    
    public static JSONArray toJSONArray(final JSONArray a2, final JSONTokener v1) throws JSONException {
        /*SL:227*/if (a2 == null || a2.length() == 0) {
            /*SL:228*/return null;
        }
        final JSONArray v2 = /*EL:230*/new JSONArray();
        while (true) {
            final JSONObject a3 = rowToJSONObject(/*EL:232*/a2, v1);
            /*SL:233*/if (a3 == null) {
                break;
            }
            /*SL:236*/v2.put(a3);
        }
        /*SL:238*/if (v2.length() == 0) {
            /*SL:239*/return null;
        }
        /*SL:241*/return v2;
    }
    
    public static String toString(final JSONArray v1) throws JSONException {
        final JSONObject v2 = /*EL:254*/v1.optJSONObject(0);
        /*SL:255*/if (v2 != null) {
            final JSONArray a1 = /*EL:256*/v2.names();
            /*SL:257*/if (a1 != null) {
                /*SL:258*/return rowToString(a1) + toString(a1, v1);
            }
        }
        /*SL:261*/return null;
    }
    
    public static String toString(final JSONArray v1, final JSONArray v2) throws JSONException {
        /*SL:275*/if (v1 == null || v1.length() == 0) {
            /*SL:276*/return null;
        }
        final StringBuffer v3 = /*EL:278*/new StringBuffer();
        /*SL:279*/for (JSONObject a2 = (JSONObject)0; a2 < v2.length(); ++a2) {
            /*SL:280*/a2 = v2.optJSONObject(a2);
            /*SL:281*/if (a2 != null) {
                /*SL:282*/v3.append(rowToString(a2.toJSONArray(v1)));
            }
        }
        /*SL:285*/return v3.toString();
    }
}
