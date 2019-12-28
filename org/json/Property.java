package org.json;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Properties;

public class Property
{
    public static JSONObject toJSONObject(final Properties v-1) throws JSONException {
        final JSONObject v0 = /*EL:45*/new JSONObject();
        /*SL:46*/if (v-1 != null && !v-1.isEmpty()) {
            final Enumeration<?> v = /*EL:47*/v-1.propertyNames();
            /*SL:48*/while (v.hasMoreElements()) {
                final String a1 = /*EL:49*/(String)v.nextElement();
                /*SL:50*/v0.put(a1, v-1.getProperty(a1));
            }
        }
        /*SL:53*/return v0;
    }
    
    public static Properties toProperties(final JSONObject v-2) throws JSONException {
        final Properties properties = /*EL:63*/new Properties();
        /*SL:64*/if (v-2 != null) {
            /*SL:66*/for (final String v1 : v-2.keySet()) {
                final Object a1 = /*EL:67*/v-2.opt(v1);
                /*SL:68*/if (!JSONObject.NULL.equals(a1)) {
                    /*SL:69*/((Hashtable<String, String>)properties).put(v1, a1.toString());
                }
            }
        }
        /*SL:73*/return properties;
    }
}
