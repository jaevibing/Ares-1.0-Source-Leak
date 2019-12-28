package org.json;

import java.io.StringReader;
import java.io.Reader;
import java.util.Iterator;

public class XML
{
    public static final Character AMP;
    public static final Character APOS;
    public static final Character BANG;
    public static final Character EQ;
    public static final Character GT;
    public static final Character LT;
    public static final Character QUEST;
    public static final Character QUOT;
    public static final Character SLASH;
    public static final String NULL_ATTR = "xsi:nil";
    
    private static Iterable<Integer> codePointIterator(final String a1) {
        /*SL:85*/return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                /*SL:88*/return new Iterator<Integer>() {
                    private int nextIndex = 0;
                    private int length = a1.length();
                    
                    @Override
                    public boolean hasNext() {
                        /*SL:94*/return this.nextIndex < this.length;
                    }
                    
                    @Override
                    public Integer next() {
                        final int v1 = /*EL:99*/a1.codePointAt(this.nextIndex);
                        /*SL:100*/this.nextIndex += Character.charCount(v1);
                        /*SL:101*/return v1;
                    }
                    
                    @Override
                    public void remove() {
                        /*SL:106*/throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    
    public static String escape(final String v1) {
        final StringBuilder v2 = /*EL:129*/new StringBuilder(v1.length());
        /*SL:130*/for (final int a1 : codePointIterator(v1)) {
            /*SL:131*/switch (a1) {
                case 38: {
                    /*SL:133*/v2.append("&amp;");
                    /*SL:134*/continue;
                }
                case 60: {
                    /*SL:136*/v2.append("&lt;");
                    /*SL:137*/continue;
                }
                case 62: {
                    /*SL:139*/v2.append("&gt;");
                    /*SL:140*/continue;
                }
                case 34: {
                    /*SL:142*/v2.append("&quot;");
                    /*SL:143*/continue;
                }
                case 39: {
                    /*SL:145*/v2.append("&apos;");
                    /*SL:146*/continue;
                }
                default: {
                    /*SL:148*/if (mustEscape(a1)) {
                        /*SL:149*/v2.append("&#x");
                        /*SL:150*/v2.append(Integer.toHexString(a1));
                        /*SL:151*/v2.append(';');
                        continue;
                    }
                    /*SL:153*/v2.appendCodePoint(a1);
                    continue;
                }
            }
        }
        /*SL:157*/return v2.toString();
    }
    
    private static boolean mustEscape(final int a1) {
        /*SL:173*/return (Character.isISOControl(a1) && a1 != 9 && a1 != 10 && a1 != 13) || ((a1 < 32 || a1 > 55295) && (a1 < 57344 || a1 > 65533) && (a1 < 65536 || a1 > 1114111));
    }
    
    public static String unescape(final String v-4) {
        final StringBuilder sb = /*EL:194*/new StringBuilder(v-4.length());
        /*SL:195*/for (int i = 0, length = v-4.length(); i < length; ++i) {
            final char v0 = /*EL:196*/v-4.charAt(i);
            /*SL:197*/if (v0 == '&') {
                final int v = /*EL:198*/v-4.indexOf(59, i);
                /*SL:199*/if (v > i) {
                    final String a1 = /*EL:200*/v-4.substring(i + 1, v);
                    /*SL:201*/sb.append(XMLTokener.unescapeEntity(a1));
                    /*SL:203*/i += a1.length() + 1;
                }
                else {
                    /*SL:207*/sb.append(v0);
                }
            }
            else {
                /*SL:211*/sb.append(v0);
            }
        }
        /*SL:214*/return sb.toString();
    }
    
    public static void noSpace(final String a1) throws JSONException {
        int v2 = /*EL:226*/a1.length();
        /*SL:227*/if (v2 == 0) {
            /*SL:228*/throw new JSONException("Empty string.");
        }
        /*SL:230*/for (v2 = 0; v2 < v2; ++v2) {
            /*SL:231*/if (Character.isWhitespace(a1.charAt(v2))) {
                /*SL:232*/throw new JSONException("'" + a1 + "' contains a space character.");
            }
        }
    }
    
    private static boolean parse(final XMLTokener v-6, final JSONObject v-5, final String v-4, final XMLParserConfiguration v-3) throws JSONException {
        JSONObject v0 = /*EL:254*/null;
        Object v = /*EL:269*/v-6.nextToken();
        /*SL:273*/if (v == XML.BANG) {
            char a2 = /*EL:274*/v-6.next();
            /*SL:275*/if (a2 == '-') {
                /*SL:276*/if (v-6.next() == '-') {
                    /*SL:277*/v-6.skipPast("-->");
                    /*SL:278*/return false;
                }
                /*SL:280*/v-6.back();
            }
            else/*SL:281*/ if (a2 == '[') {
                /*SL:282*/v = v-6.nextToken();
                /*SL:283*/if ("CDATA".equals(v) && /*EL:284*/v-6.next() == '[') {
                    /*SL:285*/a2 = v-6.nextCDATA();
                    /*SL:286*/if (a2.length() > 0) {
                        /*SL:287*/v-5.accumulate(v-3.cDataTagName, a2);
                    }
                    /*SL:289*/return false;
                }
                /*SL:292*/throw v-6.syntaxError("Expected 'CDATA['");
            }
            int a3 = /*EL:294*/1;
            /*SL:304*/do {
                v = v-6.nextMeta();
                if (v == null) {
                    throw v-6.syntaxError("Missing '>' after '<!'.");
                }
                if (v == XML.LT) {
                    ++a3;
                }
                else {
                    if (v != XML.GT) {
                        continue;
                    }
                    --a3;
                }
            } while (a3 > 0);
            /*SL:305*/return false;
        }
        /*SL:306*/if (v == XML.QUEST) {
            /*SL:309*/v-6.skipPast("?>");
            /*SL:310*/return false;
        }
        /*SL:311*/if (v == XML.SLASH) {
            /*SL:315*/v = v-6.nextToken();
            /*SL:316*/if (v-4 == null) {
                /*SL:317*/throw v-6.syntaxError("Mismatched close tag " + v);
            }
            /*SL:319*/if (!v.equals(v-4)) {
                /*SL:320*/throw v-6.syntaxError("Mismatched " + v-4 + " and " + v);
            }
            /*SL:322*/if (v-6.nextToken() != XML.GT) {
                /*SL:323*/throw v-6.syntaxError("Misshaped close tag");
            }
            /*SL:325*/return true;
        }
        else {
            /*SL:327*/if (v instanceof Character) {
                /*SL:328*/throw v-6.syntaxError("Misshaped tag");
            }
            final String v2 = /*EL:333*/(String)v;
            /*SL:334*/v = null;
            /*SL:335*/v0 = new JSONObject();
            boolean v3 = /*EL:336*/false;
            while (true) {
                /*SL:338*/if (v == null) {
                    /*SL:339*/v = v-6.nextToken();
                }
                /*SL:342*/if (v instanceof String) {
                    final String a4 = /*EL:343*/(String)v;
                    /*SL:344*/v = v-6.nextToken();
                    /*SL:345*/if (v == XML.EQ) {
                        /*SL:346*/v = v-6.nextToken();
                        /*SL:347*/if (!(v instanceof String)) {
                            /*SL:348*/throw v-6.syntaxError("Missing value");
                        }
                        /*SL:352*/if (v-3.convertNilAttributeToNull && "xsi:nil".equals(a4) && /*EL:353*/Boolean.parseBoolean((String)v)) {
                            /*SL:354*/v3 = true;
                        }
                        else/*SL:355*/ if (!v3) {
                            /*SL:356*/v0.accumulate(a4, /*EL:357*/v-3.keepStrings ? /*EL:358*/((String)v) : stringToValue(/*EL:359*/(String)v));
                        }
                        /*SL:361*/v = null;
                    }
                    else {
                        /*SL:363*/v0.accumulate(a4, "");
                    }
                }
                else/*SL:367*/ if (v == XML.SLASH) {
                    /*SL:369*/if (v-6.nextToken() != XML.GT) {
                        /*SL:370*/throw v-6.syntaxError("Misshaped tag");
                    }
                    /*SL:372*/if (v3) {
                        /*SL:373*/v-5.accumulate(v2, JSONObject.NULL);
                    }
                    else/*SL:374*/ if (v0.length() > 0) {
                        /*SL:375*/v-5.accumulate(v2, v0);
                    }
                    else {
                        /*SL:377*/v-5.accumulate(v2, "");
                    }
                    /*SL:379*/return false;
                }
                else {
                    /*SL:381*/if (v != XML.GT) {
                        /*SL:414*/throw v-6.syntaxError("Misshaped tag");
                    }
                    while (true) {
                        v = v-6.nextContent();
                        if (v == null) {
                            if (v2 != null) {
                                throw v-6.syntaxError("Unclosed tag " + v2);
                            }
                            return false;
                        }
                        else if (v instanceof String) {
                            final String v4 = (String)v;
                            if (v4.length() <= 0) {
                                continue;
                            }
                            v0.accumulate(v-3.cDataTagName, v-3.keepStrings ? v4 : stringToValue(v4));
                        }
                        else {
                            if (v == XML.LT && parse(v-6, v0, v2, v-3)) {
                                if (v0.length() == 0) {
                                    v-5.accumulate(v2, "");
                                }
                                else if (v0.length() == 1 && v0.opt(v-3.cDataTagName) != null) {
                                    v-5.accumulate(v2, v0.opt(v-3.cDataTagName));
                                }
                                else {
                                    v-5.accumulate(v2, v0);
                                }
                                return false;
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    public static Object stringToValue(final String v-1) {
        /*SL:429*/if (v-1.equals("")) {
            /*SL:430*/return v-1;
        }
        /*SL:432*/if (v-1.equalsIgnoreCase("true")) {
            /*SL:433*/return Boolean.TRUE;
        }
        /*SL:435*/if (v-1.equalsIgnoreCase("false")) {
            /*SL:436*/return Boolean.FALSE;
        }
        /*SL:438*/if (v-1.equalsIgnoreCase("null")) {
            /*SL:439*/return JSONObject.NULL;
        }
        final char v0 = /*EL:447*/v-1.charAt(0);
        /*SL:448*/if (v0 < '0' || v0 > '9') {
            if (v0 != '-') {
                return /*EL:470*/v-1;
            }
        }
        try {
            if (v-1.indexOf(46) > -1 || v-1.indexOf(101) > -1 || v-1.indexOf(69) > -1 || "-0".equals(v-1)) {
                final Double a1 = Double.valueOf(v-1);
                if (!a1.isInfinite() && !a1.isNaN()) {
                    return a1;
                }
            }
            else {
                final Long v = Long.valueOf(v-1);
                if (v-1.equals(v.toString())) {
                    if (v == (int)(Object)v) {
                        return (Object)v;
                    }
                    return v;
                }
            }
        }
        catch (Exception ex) {}
        return v-1;
    }
    
    public static JSONObject toJSONObject(final String a1) throws JSONException {
        /*SL:490*/return toJSONObject(a1, XMLParserConfiguration.ORIGINAL);
    }
    
    public static JSONObject toJSONObject(final Reader a1) throws JSONException {
        /*SL:509*/return toJSONObject(a1, XMLParserConfiguration.ORIGINAL);
    }
    
    public static JSONObject toJSONObject(final Reader a1, final boolean a2) throws JSONException {
        /*SL:533*/if (a2) {
            /*SL:534*/return toJSONObject(a1, XMLParserConfiguration.KEEP_STRINGS);
        }
        /*SL:536*/return toJSONObject(a1, XMLParserConfiguration.ORIGINAL);
    }
    
    public static JSONObject toJSONObject(final Reader a1, final XMLParserConfiguration a2) throws JSONException {
        final JSONObject v1 = /*EL:559*/new JSONObject();
        final XMLTokener v2 = /*EL:560*/new XMLTokener(a1);
        /*SL:561*/while (v2.more()) {
            /*SL:562*/v2.skipPast("<");
            /*SL:563*/if (v2.more()) {
                parse(/*EL:564*/v2, v1, null, a2);
            }
        }
        /*SL:567*/return v1;
    }
    
    public static JSONObject toJSONObject(final String a1, final boolean a2) throws JSONException {
        /*SL:592*/return toJSONObject(new StringReader(a1), a2);
    }
    
    public static JSONObject toJSONObject(final String a1, final XMLParserConfiguration a2) throws JSONException {
        /*SL:616*/return toJSONObject(new StringReader(a1), a2);
    }
    
    public static String toString(final Object a1) throws JSONException {
        /*SL:628*/return toString(a1, null, XMLParserConfiguration.ORIGINAL);
    }
    
    public static String toString(final Object a1, final String a2) {
        /*SL:642*/return toString(a1, a2, XMLParserConfiguration.ORIGINAL);
    }
    
    public static String toString(final Object v-3, final String v-2, final XMLParserConfiguration v-1) throws JSONException {
        final StringBuilder v0 = /*EL:659*/new StringBuilder();
        /*SL:664*/if (v-3 instanceof JSONObject) {
            /*SL:667*/if (v-2 != null) {
                /*SL:668*/v0.append('<');
                /*SL:669*/v0.append(v-2);
                /*SL:670*/v0.append('>');
            }
            final JSONObject v = /*EL:675*/(JSONObject)v-3;
            /*SL:676*/for (final String v2 : v.keySet()) {
                Object v3 = /*EL:677*/v.opt(v2);
                /*SL:678*/if (v3 == null) {
                    /*SL:679*/v3 = "";
                }
                else/*SL:680*/ if (v3.getClass().isArray()) {
                    /*SL:681*/v3 = new JSONArray(v3);
                }
                /*SL:685*/if (v2.equals(v-1.cDataTagName)) {
                    /*SL:686*/if (v3 instanceof JSONArray) {
                        final JSONArray v4 = /*EL:687*/(JSONArray)v3;
                        int a3 = /*EL:688*/v4.length();
                        /*SL:690*/for (int a2 = 0; a2 < a3; ++a2) {
                            /*SL:691*/if (a2 > 0) {
                                /*SL:692*/v0.append('\n');
                            }
                            /*SL:694*/a3 = v4.opt(a2);
                            /*SL:695*/v0.append(escape(a3.toString()));
                        }
                    }
                    else {
                        /*SL:698*/v0.append(escape(v3.toString()));
                    }
                }
                else/*SL:703*/ if (v3 instanceof JSONArray) {
                    final JSONArray v4 = /*EL:704*/(JSONArray)v3;
                    /*SL:707*/for (int v5 = v4.length(), v6 = 0; v6 < v5; ++v6) {
                        final Object v7 = /*EL:708*/v4.opt(v6);
                        /*SL:709*/if (v7 instanceof JSONArray) {
                            /*SL:710*/v0.append('<');
                            /*SL:711*/v0.append(v2);
                            /*SL:712*/v0.append('>');
                            /*SL:713*/v0.append(toString(v7, null, v-1));
                            /*SL:714*/v0.append("</");
                            /*SL:715*/v0.append(v2);
                            /*SL:716*/v0.append('>');
                        }
                        else {
                            /*SL:718*/v0.append(toString(v7, v2, v-1));
                        }
                    }
                }
                else/*SL:721*/ if ("".equals(v3)) {
                    /*SL:722*/v0.append('<');
                    /*SL:723*/v0.append(v2);
                    /*SL:724*/v0.append("/>");
                }
                else {
                    /*SL:729*/v0.append(toString(v3, v2, v-1));
                }
            }
            /*SL:732*/if (v-2 != null) {
                /*SL:735*/v0.append("</");
                /*SL:736*/v0.append(v-2);
                /*SL:737*/v0.append('>');
            }
            /*SL:739*/return v0.toString();
        }
        /*SL:743*/if (v-3 != null && (v-3 instanceof JSONArray || v-3.getClass().isArray())) {
            JSONArray v4;
            /*SL:744*/if (v-3.getClass().isArray()) {
                /*SL:745*/v4 = new JSONArray(v-3);
            }
            else {
                /*SL:747*/v4 = (JSONArray)v-3;
            }
            /*SL:751*/for (int v8 = v4.length(), v9 = 0; v9 < v8; ++v9) {
                final Object v3 = /*EL:752*/v4.opt(v9);
                /*SL:756*/v0.append(toString(v3, (v-2 == null) ? "array" : v-2, v-1));
            }
            /*SL:758*/return v0.toString();
        }
        final String v10 = /*EL:761*/(v-3 == null) ? "null" : escape(v-3.toString());
        /*SL:762*/return (v-2 == null) ? ("\"" + v10 + "\"") : /*EL:763*/((v10.length() == 0) ? ("<" + v-2 + "/>") : /*EL:764*/("<" + v-2 + ">" + v10 + "</" + v-2 + ">"));
    }
    
    static {
        AMP = '&';
        APOS = '\'';
        BANG = '!';
        EQ = '=';
        GT = '>';
        LT = '<';
        QUEST = '?';
        QUOT = '\"';
        SLASH = '/';
    }
}
