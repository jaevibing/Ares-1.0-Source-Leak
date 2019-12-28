package org.json.simple;

import java.util.List;
import java.util.Map;
import java.io.Writer;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.StringReader;
import org.json.simple.parser.JSONParser;
import java.io.Reader;

public class JSONValue
{
    public static Object parse(final Reader v0) {
        try {
            final JSONParser a1 = /*EL:41*/new JSONParser();
            /*SL:42*/return a1.parse(v0);
        }
        catch (Exception v) {
            /*SL:45*/return null;
        }
    }
    
    public static Object parse(final String a1) {
        final StringReader v1 = /*EL:50*/new StringReader(a1);
        /*SL:51*/return parse(v1);
    }
    
    public static Object parseWithException(final Reader a1) throws IOException, ParseException {
        final JSONParser v1 = /*EL:72*/new JSONParser();
        /*SL:73*/return v1.parse(a1);
    }
    
    public static Object parseWithException(final String a1) throws ParseException {
        final JSONParser v1 = /*EL:77*/new JSONParser();
        /*SL:78*/return v1.parse(a1);
    }
    
    public static void writeJSONString(final Object a1, final Writer a2) throws IOException {
        /*SL:96*/if (a1 == null) {
            /*SL:97*/a2.write("null");
            /*SL:98*/return;
        }
        /*SL:101*/if (a1 instanceof String) {
            /*SL:102*/a2.write(34);
            /*SL:103*/a2.write(escape((String)a1));
            /*SL:104*/a2.write(34);
            /*SL:105*/return;
        }
        /*SL:108*/if (a1 instanceof Double) {
            /*SL:109*/if (((Double)a1).isInfinite() || ((Double)a1).isNaN()) {
                /*SL:110*/a2.write("null");
            }
            else {
                /*SL:112*/a2.write(a1.toString());
            }
            /*SL:113*/return;
        }
        /*SL:116*/if (a1 instanceof Float) {
            /*SL:117*/if (((Float)a1).isInfinite() || ((Float)a1).isNaN()) {
                /*SL:118*/a2.write("null");
            }
            else {
                /*SL:120*/a2.write(a1.toString());
            }
            /*SL:121*/return;
        }
        /*SL:124*/if (a1 instanceof Number) {
            /*SL:125*/a2.write(a1.toString());
            /*SL:126*/return;
        }
        /*SL:129*/if (a1 instanceof Boolean) {
            /*SL:130*/a2.write(a1.toString());
            /*SL:131*/return;
        }
        /*SL:134*/if (a1 instanceof JSONStreamAware) {
            /*SL:135*/((JSONStreamAware)a1).writeJSONString(a2);
            /*SL:136*/return;
        }
        /*SL:139*/if (a1 instanceof JSONAware) {
            /*SL:140*/a2.write(((JSONAware)a1).toJSONString());
            /*SL:141*/return;
        }
        /*SL:144*/if (a1 instanceof Map) {
            /*SL:145*/JSONObject.writeJSONString((Map)a1, a2);
            /*SL:146*/return;
        }
        /*SL:149*/if (a1 instanceof List) {
            /*SL:150*/JSONArray.writeJSONString((List)a1, a2);
            /*SL:151*/return;
        }
        /*SL:154*/a2.write(a1.toString());
    }
    
    public static String toJSONString(final Object a1) {
        /*SL:172*/if (a1 == null) {
            /*SL:173*/return "null";
        }
        /*SL:175*/if (a1 instanceof String) {
            /*SL:176*/return "\"" + escape((String)a1) + "\"";
        }
        /*SL:178*/if (a1 instanceof Double) {
            /*SL:179*/if (((Double)a1).isInfinite() || ((Double)a1).isNaN()) {
                /*SL:180*/return "null";
            }
            /*SL:182*/return a1.toString();
        }
        else/*SL:185*/ if (a1 instanceof Float) {
            /*SL:186*/if (((Float)a1).isInfinite() || ((Float)a1).isNaN()) {
                /*SL:187*/return "null";
            }
            /*SL:189*/return a1.toString();
        }
        else {
            /*SL:192*/if (a1 instanceof Number) {
                /*SL:193*/return a1.toString();
            }
            /*SL:195*/if (a1 instanceof Boolean) {
                /*SL:196*/return a1.toString();
            }
            /*SL:198*/if (a1 instanceof JSONAware) {
                /*SL:199*/return ((JSONAware)a1).toJSONString();
            }
            /*SL:201*/if (a1 instanceof Map) {
                /*SL:202*/return JSONObject.toJSONString((Map)a1);
            }
            /*SL:204*/if (a1 instanceof List) {
                /*SL:205*/return JSONArray.toJSONString((List)a1);
            }
            /*SL:207*/return a1.toString();
        }
    }
    
    public static String escape(final String a1) {
        /*SL:216*/if (a1 == null) {
            /*SL:217*/return null;
        }
        final StringBuffer v1 = /*EL:218*/new StringBuffer();
        escape(/*EL:219*/a1, v1);
        /*SL:220*/return v1.toString();
    }
    
    static void escape(final String v-2, final StringBuffer v-1) {
        /*SL:228*/for (int v0 = 0; v0 < v-2.length(); ++v0) {
            final char v = /*EL:229*/v-2.charAt(v0);
            /*SL:230*/switch (v) {
                case '\"': {
                    /*SL:232*/v-1.append("\\\"");
                    /*SL:233*/break;
                }
                case '\\': {
                    /*SL:235*/v-1.append("\\\\");
                    /*SL:236*/break;
                }
                case '\b': {
                    /*SL:238*/v-1.append("\\b");
                    /*SL:239*/break;
                }
                case '\f': {
                    /*SL:241*/v-1.append("\\f");
                    /*SL:242*/break;
                }
                case '\n': {
                    /*SL:244*/v-1.append("\\n");
                    /*SL:245*/break;
                }
                case '\r': {
                    /*SL:247*/v-1.append("\\r");
                    /*SL:248*/break;
                }
                case '\t': {
                    /*SL:250*/v-1.append("\\t");
                    /*SL:251*/break;
                }
                case '/': {
                    /*SL:253*/v-1.append("\\/");
                    /*SL:254*/break;
                }
                default: {
                    /*SL:257*/if ((v >= '\0' && v <= '\u001f') || (v >= '\u007f' && v <= '\u009f') || (v >= '\u2000' && v <= '\u20ff')) {
                        String a2 = /*EL:258*/Integer.toHexString(v);
                        /*SL:259*/v-1.append("\\u");
                        /*SL:260*/for (a2 = 0; a2 < 4 - a2.length(); ++a2) {
                            /*SL:261*/v-1.append('0');
                        }
                        /*SL:263*/v-1.append(a2.toUpperCase());
                        /*SL:264*/break;
                    }
                    /*SL:266*/v-1.append(v);
                    break;
                }
            }
        }
    }
}
