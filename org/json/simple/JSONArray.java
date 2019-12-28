package org.json.simple;

import java.io.IOException;
import java.util.Iterator;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;

public class JSONArray extends ArrayList implements List, JSONAware, JSONStreamAware
{
    private static final long serialVersionUID = 3957988303675231981L;
    
    public static void writeJSONString(final List a2, final Writer v1) throws IOException {
        /*SL:32*/if (a2 == null) {
            /*SL:33*/v1.write("null");
            /*SL:34*/return;
        }
        boolean v2 = /*EL:37*/true;
        final Iterator v3 = /*EL:38*/a2.iterator();
        /*SL:40*/v1.write(91);
        /*SL:41*/while (v3.hasNext()) {
            /*SL:42*/if (v2) {
                /*SL:43*/v2 = false;
            }
            else {
                /*SL:45*/v1.write(44);
            }
            final Object a3 = /*EL:47*/v3.next();
            /*SL:48*/if (a3 == null) {
                /*SL:49*/v1.write("null");
            }
            else {
                /*SL:53*/JSONValue.writeJSONString(a3, v1);
            }
        }
        /*SL:55*/v1.write(93);
    }
    
    public void writeJSONString(final Writer a1) throws IOException {
        writeJSONString(/*EL:59*/this, a1);
    }
    
    public static String toJSONString(final List v1) {
        /*SL:72*/if (v1 == null) {
            /*SL:73*/return "null";
        }
        boolean v2 = /*EL:75*/true;
        final StringBuffer v3 = /*EL:76*/new StringBuffer();
        final Iterator v4 = /*EL:77*/v1.iterator();
        /*SL:79*/v3.append('[');
        /*SL:80*/while (v4.hasNext()) {
            /*SL:81*/if (v2) {
                /*SL:82*/v2 = false;
            }
            else {
                /*SL:84*/v3.append(',');
            }
            final Object a1 = /*EL:86*/v4.next();
            /*SL:87*/if (a1 == null) {
                /*SL:88*/v3.append("null");
            }
            else {
                /*SL:91*/v3.append(JSONValue.toJSONString(a1));
            }
        }
        /*SL:93*/v3.append(']');
        /*SL:94*/return v3.toString();
    }
    
    public String toJSONString() {
        /*SL:98*/return toJSONString(this);
    }
    
    public String toString() {
        /*SL:102*/return this.toJSONString();
    }
}
