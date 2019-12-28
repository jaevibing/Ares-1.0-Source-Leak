package org.json;

import java.util.Iterator;

public class JSONML
{
    private static Object parse(final XMLTokener a4, final boolean v1, final JSONArray v2, final boolean v3) throws JSONException {
        String v4 = /*EL:54*/null;
        JSONArray v5 = /*EL:56*/null;
        JSONObject v6 = /*EL:57*/null;
        String v7 = /*EL:59*/null;
        /*SL:68*/while (a4.more()) {
            Object v8 = /*EL:71*/a4.nextContent();
            /*SL:72*/if (v8 == XML.LT) {
                /*SL:73*/v8 = a4.nextToken();
                /*SL:74*/if (v8 instanceof Character) {
                    /*SL:75*/if (v8 == XML.SLASH) {
                        /*SL:79*/v8 = a4.nextToken();
                        /*SL:80*/if (!(v8 instanceof String)) {
                            /*SL:81*/throw new JSONException("Expected a closing name instead of '" + v8 + "'.");
                        }
                        /*SL:85*/if (a4.nextToken() != XML.GT) {
                            /*SL:86*/throw a4.syntaxError("Misshaped close tag");
                        }
                        /*SL:88*/return v8;
                    }
                    else/*SL:89*/ if (v8 == XML.BANG) {
                        final char a5 = /*EL:93*/a4.next();
                        /*SL:94*/if (a5 == '-') {
                            /*SL:95*/if (a4.next() == '-') {
                                /*SL:96*/a4.skipPast("-->");
                            }
                            else {
                                /*SL:98*/a4.back();
                            }
                        }
                        else/*SL:100*/ if (a5 == '[') {
                            /*SL:101*/v8 = a4.nextToken();
                            /*SL:102*/if (!v8.equals("CDATA") || a4.next() != '[') {
                                /*SL:107*/throw a4.syntaxError("Expected 'CDATA['");
                            }
                            if (v2 == null) {
                                continue;
                            }
                            v2.put(a4.nextCDATA());
                        }
                        else {
                            int a6 = /*EL:110*/1;
                            /*SL:120*/do {
                                v8 = a4.nextMeta();
                                if (v8 == null) {
                                    throw a4.syntaxError("Missing '>' after '<!'.");
                                }
                                if (v8 == XML.LT) {
                                    ++a6;
                                }
                                else {
                                    if (v8 != XML.GT) {
                                        continue;
                                    }
                                    --a6;
                                }
                            } while (a6 > 0);
                        }
                    }
                    else {
                        /*SL:122*/if (v8 != XML.QUEST) {
                            /*SL:128*/throw a4.syntaxError("Misshaped tag");
                        }
                        a4.skipPast("?>");
                    }
                }
                else {
                    /*SL:134*/if (!(v8 instanceof String)) {
                        /*SL:135*/throw a4.syntaxError("Bad tagName '" + v8 + "'.");
                    }
                    /*SL:137*/v7 = (String)v8;
                    /*SL:138*/v5 = new JSONArray();
                    /*SL:139*/v6 = new JSONObject();
                    /*SL:140*/if (v1) {
                        /*SL:141*/v5.put(v7);
                        /*SL:142*/if (v2 != null) {
                            /*SL:143*/v2.put(v5);
                        }
                    }
                    else {
                        /*SL:146*/v6.put("tagName", v7);
                        /*SL:147*/if (v2 != null) {
                            /*SL:148*/v2.put(v6);
                        }
                    }
                    /*SL:151*/v8 = null;
                    while (true) {
                        /*SL:153*/if (v8 == null) {
                            /*SL:154*/v8 = a4.nextToken();
                        }
                        /*SL:156*/if (v8 == null) {
                            /*SL:157*/throw a4.syntaxError("Misshaped tag");
                        }
                        /*SL:159*/if (!(v8 instanceof String)) {
                            /*SL:181*/if (v1 && v6.length() > 0) {
                                /*SL:182*/v5.put(v6);
                            }
                            /*SL:187*/if (v8 == XML.SLASH) {
                                /*SL:188*/if (a4.nextToken() != XML.GT) {
                                    /*SL:189*/throw a4.syntaxError("Misshaped tag");
                                }
                                /*SL:191*/if (v2 != null) {
                                    break;
                                }
                                /*SL:192*/if (v1) {
                                    /*SL:193*/return v5;
                                }
                                /*SL:195*/return v6;
                            }
                            else {
                                /*SL:201*/if (v8 != XML.GT) {
                                    /*SL:202*/throw a4.syntaxError("Misshaped tag");
                                }
                                /*SL:204*/v4 = (String)parse(a4, v1, v5, v3);
                                /*SL:205*/if (v4 == null) {
                                    break;
                                }
                                /*SL:206*/if (!v4.equals(v7)) {
                                    /*SL:207*/throw a4.syntaxError("Mismatched '" + v7 + "' and '" + v4 + "'");
                                }
                                /*SL:210*/v7 = null;
                                /*SL:211*/if (!v1 && v5.length() > 0) {
                                    /*SL:212*/v6.put("childNodes", v5);
                                }
                                /*SL:214*/if (v2 != null) {
                                    break;
                                }
                                /*SL:215*/if (v1) {
                                    /*SL:216*/return v5;
                                }
                                /*SL:218*/return v6;
                            }
                        }
                        else {
                            final String a7 = (String)v8;
                            if (!v1 && ("tagName".equals(a7) || "childNode".equals(a7))) {
                                throw a4.syntaxError("Reserved attribute.");
                            }
                            v8 = a4.nextToken();
                            if (v8 == XML.EQ) {
                                v8 = a4.nextToken();
                                if (!(v8 instanceof String)) {
                                    throw a4.syntaxError("Missing value");
                                }
                                v6.accumulate(a7, v3 ? ((String)v8) : XML.stringToValue((String)v8));
                                v8 = null;
                            }
                            else {
                                v6.accumulate(a7, "");
                            }
                        }
                    }
                }
            }
            else {
                /*SL:224*/if (v2 == null) {
                    continue;
                }
                /*SL:225*/v2.put((v8 instanceof String) ? /*EL:226*/(v3 ? XML.unescape((String)v8) : XML.stringToValue((String)v8)) : /*EL:227*/v8);
            }
        }
        throw a4.syntaxError("Bad XML");
    }
    
    public static JSONArray toJSONArray(final String a1) throws JSONException {
        /*SL:247*/return (JSONArray)parse(new XMLTokener(a1), true, null, false);
    }
    
    public static JSONArray toJSONArray(final String a1, final boolean a2) throws JSONException {
        /*SL:269*/return (JSONArray)parse(new XMLTokener(a1), true, null, a2);
    }
    
    public static JSONArray toJSONArray(final XMLTokener a1, final boolean a2) throws JSONException {
        /*SL:291*/return (JSONArray)parse(a1, true, null, a2);
    }
    
    public static JSONArray toJSONArray(final XMLTokener a1) throws JSONException {
        /*SL:308*/return (JSONArray)parse(a1, true, null, false);
    }
    
    public static JSONObject toJSONObject(final String a1) throws JSONException {
        /*SL:326*/return (JSONObject)parse(new XMLTokener(a1), false, null, false);
    }
    
    public static JSONObject toJSONObject(final String a1, final boolean a2) throws JSONException {
        /*SL:346*/return (JSONObject)parse(new XMLTokener(a1), false, null, a2);
    }
    
    public static JSONObject toJSONObject(final XMLTokener a1) throws JSONException {
        /*SL:364*/return (JSONObject)parse(a1, false, null, false);
    }
    
    public static JSONObject toJSONObject(final XMLTokener a1, final boolean a2) throws JSONException {
        /*SL:384*/return (JSONObject)parse(a1, false, null, a2);
    }
    
    public static String toString(final JSONArray v-7) throws JSONException {
        final StringBuilder sb = /*EL:399*/new StringBuilder();
        String s = /*EL:404*/v-7.getString(0);
        /*SL:405*/XML.noSpace(s);
        /*SL:406*/s = XML.escape(s);
        /*SL:407*/sb.append('<');
        /*SL:408*/sb.append(s);
        Object o = /*EL:410*/v-7.opt(1);
        int i;
        /*SL:411*/if (o instanceof JSONObject) {
            /*SL:412*/i = 2;
            final JSONObject jsonObject = /*EL:413*/(JSONObject)o;
            /*SL:418*/for (final String v1 : jsonObject.keySet()) {
                final Object a1 = /*EL:419*/jsonObject.opt(v1);
                /*SL:420*/XML.noSpace(v1);
                /*SL:421*/if (a1 != null) {
                    /*SL:422*/sb.append(' ');
                    /*SL:423*/sb.append(XML.escape(v1));
                    /*SL:424*/sb.append('=');
                    /*SL:425*/sb.append('\"');
                    /*SL:426*/sb.append(XML.escape(a1.toString()));
                    /*SL:427*/sb.append('\"');
                }
            }
        }
        else {
            /*SL:431*/i = 1;
        }
        final int length = /*EL:436*/v-7.length();
        /*SL:437*/if (i >= length) {
            /*SL:438*/sb.append('/');
            /*SL:439*/sb.append('>');
        }
        else {
            /*SL:441*/sb.append('>');
            /*SL:456*/do {
                o = v-7.get(i);
                ++i;
                if (o != null) {
                    if (o instanceof String) {
                        sb.append(XML.escape(o.toString()));
                    }
                    else if (o instanceof JSONObject) {
                        sb.append(toString((JSONObject)o));
                    }
                    else if (o instanceof JSONArray) {
                        sb.append(toString((JSONArray)o));
                    }
                    else {
                        sb.append(o.toString());
                    }
                }
            } while (i < length);
            /*SL:457*/sb.append('<');
            /*SL:458*/sb.append('/');
            /*SL:459*/sb.append(s);
            /*SL:460*/sb.append('>');
        }
        /*SL:462*/return sb.toString();
    }
    
    public static String toString(final JSONObject v-8) throws JSONException {
        final StringBuilder sb = /*EL:475*/new StringBuilder();
        String s = /*EL:485*/v-8.optString("tagName");
        /*SL:486*/if (s == null) {
            /*SL:487*/return XML.escape(v-8.toString());
        }
        /*SL:489*/XML.noSpace(s);
        /*SL:490*/s = XML.escape(s);
        /*SL:491*/sb.append('<');
        /*SL:492*/sb.append(s);
        /*SL:497*/for (final String v1 : v-8.keySet()) {
            /*SL:498*/if (!"tagName".equals(v1) && !"childNodes".equals(v1)) {
                /*SL:499*/XML.noSpace(v1);
                final Object a1 = /*EL:500*/v-8.opt(v1);
                /*SL:501*/if (a1 == null) {
                    continue;
                }
                /*SL:502*/sb.append(' ');
                /*SL:503*/sb.append(XML.escape(v1));
                /*SL:504*/sb.append('=');
                /*SL:505*/sb.append('\"');
                /*SL:506*/sb.append(XML.escape(a1.toString()));
                /*SL:507*/sb.append('\"');
            }
        }
        final JSONArray optJSONArray = /*EL:514*/v-8.optJSONArray("childNodes");
        /*SL:515*/if (optJSONArray == null) {
            /*SL:516*/sb.append('/');
            /*SL:517*/sb.append('>');
        }
        else {
            /*SL:519*/sb.append('>');
            /*SL:521*/for (int length = optJSONArray.length(), i = 0; i < length; ++i) {
                final Object value = /*EL:522*/optJSONArray.get(i);
                /*SL:523*/if (value != null) {
                    /*SL:524*/if (value instanceof String) {
                        /*SL:525*/sb.append(XML.escape(value.toString()));
                    }
                    else/*SL:526*/ if (value instanceof JSONObject) {
                        /*SL:527*/sb.append(toString((JSONObject)value));
                    }
                    else/*SL:528*/ if (value instanceof JSONArray) {
                        /*SL:529*/sb.append(toString((JSONArray)value));
                    }
                    else {
                        /*SL:531*/sb.append(value.toString());
                    }
                }
            }
            /*SL:535*/sb.append('<');
            /*SL:536*/sb.append('/');
            /*SL:537*/sb.append(s);
            /*SL:538*/sb.append('>');
        }
        /*SL:540*/return sb.toString();
    }
}
