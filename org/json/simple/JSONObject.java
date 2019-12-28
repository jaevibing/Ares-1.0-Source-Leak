package org.json.simple;

import java.io.IOException;
import java.util.Iterator;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;

public class JSONObject extends HashMap implements Map, JSONAware, JSONStreamAware
{
    private static final long serialVersionUID = -503443796854799292L;
    
    public JSONObject() {
    }
    
    public JSONObject(final Map a1) {
        super(a1);
    }
    
    public static void writeJSONString(final Map a2, final Writer v1) throws IOException {
        /*SL:48*/if (a2 == null) {
            /*SL:49*/v1.write("null");
            /*SL:50*/return;
        }
        boolean v2 = /*EL:53*/true;
        final Iterator v3 = /*EL:54*/a2.entrySet().iterator();
        /*SL:56*/v1.write(123);
        /*SL:57*/while (v3.hasNext()) {
            /*SL:58*/if (v2) {
                /*SL:59*/v2 = false;
            }
            else {
                /*SL:61*/v1.write(44);
            }
            final Entry a3 = /*EL:62*/v3.next();
            /*SL:63*/v1.write(34);
            /*SL:64*/v1.write(escape(String.valueOf(a3.getKey())));
            /*SL:65*/v1.write(34);
            /*SL:66*/v1.write(58);
            /*SL:67*/JSONValue.writeJSONString(a3.getValue(), v1);
        }
        /*SL:69*/v1.write(125);
    }
    
    public void writeJSONString(final Writer a1) throws IOException {
        writeJSONString(/*EL:73*/this, a1);
    }
    
    public static String toJSONString(final Map v1) {
        /*SL:86*/if (v1 == null) {
            /*SL:87*/return "null";
        }
        final StringBuffer v2 = /*EL:89*/new StringBuffer();
        boolean v3 = /*EL:90*/true;
        final Iterator v4 = /*EL:91*/v1.entrySet().iterator();
        /*SL:93*/v2.append('{');
        /*SL:94*/while (v4.hasNext()) {
            /*SL:95*/if (v3) {
                /*SL:96*/v3 = false;
            }
            else {
                /*SL:98*/v2.append(',');
            }
            final Entry a1 = /*EL:100*/v4.next();
            toJSONString(/*EL:101*/String.valueOf(a1.getKey()), a1.getValue(), v2);
        }
        /*SL:103*/v2.append('}');
        /*SL:104*/return v2.toString();
    }
    
    public String toJSONString() {
        /*SL:108*/return toJSONString(this);
    }
    
    private static String toJSONString(final String a1, final Object a2, final StringBuffer a3) {
        /*SL:112*/a3.append('\"');
        /*SL:113*/if (a1 == null) {
            /*SL:114*/a3.append("null");
        }
        else {
            /*SL:116*/JSONValue.escape(a1, a3);
        }
        /*SL:117*/a3.append('\"').append(':');
        /*SL:119*/a3.append(JSONValue.toJSONString(a2));
        /*SL:121*/return a3.toString();
    }
    
    public String toString() {
        /*SL:125*/return this.toJSONString();
    }
    
    public static String toString(final String a1, final Object a2) {
        final StringBuffer v1 = /*EL:129*/new StringBuffer();
        toJSONString(/*EL:130*/a1, a2, v1);
        /*SL:131*/return v1.toString();
    }
    
    public static String escape(final String a1) {
        /*SL:144*/return JSONValue.escape(a1);
    }
}
